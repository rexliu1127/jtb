package io.grx.modules.job.task;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.auth.service.JuxinliService;
import io.grx.auth.service.ShujuMoheMobileService;
import io.grx.auth.service.ShujumoheDsService;
import io.grx.auth.service.TongDunService;
import io.grx.common.service.PhoneCheckService;
import io.grx.modules.auth.entity.AuthMissingRecordEntity;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.AuthUserReportDsEntity;
import io.grx.modules.auth.entity.JxlReportEntity;
import io.grx.modules.auth.enums.AuthVendorType;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthMissingRecordService;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserReportDsService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.JxlReportService;

/**
 * 获取认证报告任务
 */
@Component("authTask")
public class AuthTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private JuxinliService juxinliService;

    @Autowired
    private JxlReportService jxlReportService;

    @Autowired
    private ShujuMoheMobileService shujuMoheMobileService;

    @Autowired
    private TongDunService tongDunService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private PhoneCheckService phoneCheckService;

    @Autowired
    private AuthMissingRecordService authMissingRecordService;

    @Autowired
    private AuthUserReportDsService authUserReportDsService;

    @Autowired
    private ShujumoheDsService shujumoheDsService;

    public void getJxlReports(){
        logger.info("Start to get pending reports.");

        List<AuthRequestEntity> pendingRequests = authRequestService.queryPendingVerificationList();
        if (CollectionUtils.isEmpty(pendingRequests)) {
            logger.info("No pending request.");
            return;
        }

        logger.info("Going to get JXL report for {} requests", pendingRequests.size());
        for (AuthRequestEntity request : pendingRequests) {
            String token = request.getVerifyToken();
            if (juxinliService.isJobCompleted(token)) {
                logger.info("{} is completed", token);

                String reportData = juxinliService.getReportDataByToken(token);
                String rawData = juxinliService.getRawDataByToken(token);

                JxlReportEntity reportEntity = new JxlReportEntity();
                reportEntity.setToken(token);
                reportEntity.setRequestId(request.getRequestId());
                reportEntity.setReportData(reportData);
                reportEntity.setRawData(rawData);
                reportEntity.setUpdateTime(new Date());

                jxlReportService.save(reportEntity);

                request.setVerifyStatus(VerifyStatus.SUCCESS);
                authRequestService.update(request);
            } else {
                logger.info("{} is not completed", token);
            }
        }
        logger.info("Task completed");
    }


    public void getSjmhReports(){
        logger.info("Start to get pending reports (SJMH).");

        List<AuthRequestEntity> pendingRequests = authRequestService.queryPendingVerificationList();

        logger.info("Going to get SJMH report for {} requests", pendingRequests.size());
        for (AuthRequestEntity request : pendingRequests) {

            try {
                String taskId = request.getVerifyToken();
                logger.info("going to process request: {}", taskId);
                JxlReportEntity reportEntity = jxlReportService.queryByToken(taskId);
                if (reportEntity == null) {
                    reportEntity = new JxlReportEntity();
                    reportEntity.setToken(taskId);
                    reportEntity.setRequestId(request.getRequestId());
                }

                if (request.getVendorType() == AuthVendorType.SJMH) {
                    if (StringUtils.isBlank(reportEntity.getRawData())) {
                        String taskDetail = shujuMoheMobileService.queryTaskResult(taskId);

                        try {
                            Map<String, Object> responseMap = new ObjectMapper().readValue(taskDetail,
                                    new TypeReference<Map<String, Object>>() {
                                    });
                            int resultCode = MapUtils.getIntValue(responseMap, "code", -1);
                            if (resultCode == 137 || resultCode == 2007) {
                                // mobile report not ready yet
                                continue;
                            }
                            if (resultCode == 0) {
                                reportEntity.setRawData(taskDetail);
                            } else {
                                // mobile report is failed
                                request.setVerifyStatus(VerifyStatus.FAILED);
                                authRequestService.update(request);

                                continue;
                            }
                        } catch (Throwable t) {
                            logger.error("Failed to process mobile raw data", t);
                        }
                    }

                    if (StringUtils.isBlank(reportEntity.getMobileData())) {
                        AuthUserEntity userEntity = authUserService.queryObject(request.getUserId());
                        String mobileData = shujuMoheMobileService.queryReportResult(taskId, userEntity);

                        if (StringUtils.isNotBlank(mobileData)) {
                            reportEntity.setMobileData(mobileData);

                            updateRequestData(request, mobileData);
                            authRequestService.update(request);
                        }
                    }

                } else if (request.getVendorType() == AuthVendorType.JXL) {
                    String token = request.getVerifyToken();
                    if (juxinliService.isJobCompleted(token)) {
                        logger.info("{} is completed", token);

                        if (StringUtils.isBlank(reportEntity.getMobileData())) {
                            String reportData = juxinliService.getReportDataByToken(token);
                            reportEntity.setMobileData(reportData);
                        }

                        if (StringUtils.isBlank(reportEntity.getRawData())) {
                            String rawData = juxinliService.getRawDataByToken(token);
                            reportEntity.setRawData(rawData);
                        }
                    } else {
                        logger.info("{} is not completed", token);
                        continue;
                    }
                }

                if (StringUtils.isBlank(reportEntity.getReportData())) {
                    String report = tongDunService.getBodyGuardReport(request);
                    reportEntity.setReportData(report);
                }

                AuthUserEntity user = authUserService.queryObject(request.getUserId());
                if (request.getPhoneChecked() == null) {
                    request.setPhoneChecked(phoneCheckService.isPhoneVerified(request.getName(), request.getIdNo(),
                            user.getMobile()));
                }

                reportEntity.setUpdateTime(new Date());

                if (reportEntity.getId() == null) {
                    jxlReportService.save(reportEntity);
                } else {
                    jxlReportService.update(reportEntity);
                }

                if (StringUtils.isNotBlank(reportEntity.getReportData())
                        && StringUtils.isNotBlank(reportEntity.getRawData())
                        && StringUtils.isNotBlank(reportEntity.getMobileData())) {
                    request.setVerifyStatus(VerifyStatus.SUCCESS);
                    authRequestService.update(request);
                }
            } catch (Throwable t) {
                logger.error("Failed to process auth request {}", request.getVerifyToken());
            }
        }

        getDsReports();
        logger.info("Tasks completed");
    }

    private void updateRequestData(AuthRequestEntity requestEntity, String mobileData) {
        if (StringUtils.isBlank(requestEntity.getContact1Mobile())
                && StringUtils.isNotBlank(requestEntity.getContact2Mobile())) {
            return;
        }

        try {
            Map<String, Object> responseMap = new ObjectMapper().readValue(mobileData,
                    new TypeReference<Map<String, Object>>() {
                    });

            Collection<Map<String, Object>> allContactData = (Collection<Map<String, Object>>) MapUtils.getObject
                    (responseMap, "all_contact_detail");

            if (allContactData != null) {
                for (Map<String, Object> record : allContactData) {
                    String mobile = MapUtils.getString(record, "contact_number");
                    int count = MapUtils.getInteger(record, "call_count_3month", 0);
//                    logger.info("mobile={}, count={}", mobile, count);
                    if (StringUtils.equals(mobile, requestEntity.getContact1Mobile())) {
                        requestEntity.setContact1CallCount(count);
                    } else if (StringUtils.equals(mobile, requestEntity.getContact2Mobile())) {
                        requestEntity.setContact2CallCount(count);
                    }
                }
            }

            if (requestEntity.getContact1CallCount() == null) {
                requestEntity.setContact1CallCount(0);
            }

            if (requestEntity.getContact2CallCount() == null) {
                requestEntity.setContact2CallCount(0);
            }
        } catch (Exception e) {
            logger.error("updateRequestData()", e);
        }
    }

    public void fixMissingRecords() {
        List<AuthMissingRecordEntity> records = authMissingRecordService.queryMissingRecords();

        if (CollectionUtils.isEmpty(records)) {
            logger.info("No missing records by now");
            return;
        }

        for (AuthMissingRecordEntity r : records) {
            String mobile = r.getMobile();
            String token = r.getVerifyToken();
            Date createTime = r.getCreateTime();

            List<AuthUserEntity> users = authUserService.queryAllChannelUsersByMobile(mobile);

            if (CollectionUtils.isEmpty(users)) {
                logger.warn("no found user by mobile: {}", mobile);
                continue;
            }
            if (users.size() > 1) {
                logger.warn("more than 1 user for mobile: {}, ingore token: {}", mobile, token);
                return;
            }

            if (StringUtils.isNotBlank(shujuMoheMobileService.queryTaskResult(token))) {
                AuthUserEntity user = users.get(0);

                authRequestService.createAuthRequest(token, user, createTime);

                r.setStatus(1);
                authMissingRecordService.update(r);
            } else {
                logger.warn("cannot get task result by token: {}", token);
            }


        }
    }

    public void checkAuthResult() {
        List<AuthRequestEntity> pendingAuthRequests = authRequestService.queryPendingVerifyRequests();

        if (CollectionUtils.isEmpty(pendingAuthRequests)) {
            logger.info("No pending records by now");
            return;
        }

        for (AuthRequestEntity r : pendingAuthRequests) {
            if (r.getCreateTime().getTime() > System.currentTimeMillis() - 60 * 1000) {
                continue;
            }

            int resultCode = shujuMoheMobileService.queryTaskResultCode(r.getVerifyToken());
            if (resultCode == 0 || resultCode == 137) {
                // 已成功
                r.setVerifyStatus(VerifyStatus.SUBMITTED);
                authRequestService.update(r);
            } else if (resultCode == 2007) {
                logger.info("{} is still in progress, continue", r.getVerifyToken());
                continue;
            } else {
                if (r.getCreateTime().getTime() < System.currentTimeMillis() - 60 * 30 * 1000) {
                    r.setVerifyStatus(VerifyStatus.FAILED);
                    authRequestService.update(r);

                    logger.info("Update task to failed after 30 mins: {}", r.getVerifyToken());
                }
            }
            logger.info("Checked {}, code={}", r.getVerifyToken(), resultCode);
        }
    }

    private void getDsReports() {
        logger.info("Going to check DS reports");

        List<AuthUserReportDsEntity> pendingDsReports = authUserReportDsService.queryByStatuses(
                null, VerifyStatus.SUBMITTED);
        logger.info("Submitted DS reports: {}", pendingDsReports.size());
        for (AuthUserReportDsEntity dsEntity : pendingDsReports) {
            try {
                String taskDetail = shujumoheDsService.queryTaskResult(dsEntity.getTaskId());
                Map<String, Object> responseMap = new ObjectMapper().readValue(taskDetail,
                        new TypeReference<Map<String, Object>>() {
                        });

                int resultCode = MapUtils.getIntValue(responseMap, "code", -1);
//                if (resultCode == 137 || resultCode == 2007) {
//                    // mobile report not ready yet
//                    continue;
//                }
                if (resultCode == 0) {
                    dsEntity.setReportData(taskDetail);
                    dsEntity.setVerifyStatus(VerifyStatus.SUCCESS);
                    authUserReportDsService.update(dsEntity);
                    logger.info("Update {} DS report to success", dsEntity.getTaskId());
                } else {
                    // mobile report is failed
                    dsEntity.setVerifyStatus(VerifyStatus.FAILED);
                    authUserReportDsService.update(dsEntity);
                    logger.info("Update {} DS report to failed", dsEntity.getTaskId());

                }
            } catch (Throwable t) {
                logger.error("Failed to process mobile raw data", t);
            }
        }

        pendingDsReports = authUserReportDsService.queryByStatuses(
                null, VerifyStatus.PROCESSING);
        logger.info("Processing DS reports: {}", pendingDsReports.size());
        for (AuthUserReportDsEntity dsEntity : pendingDsReports) {
            try {
                if (dsEntity.getCreateTime().getTime() > System.currentTimeMillis() - 300 * 1000) {
                    continue;
                }

                String taskDetail = shujumoheDsService.queryTaskResult(dsEntity.getTaskId());
                Map<String, Object> responseMap = new ObjectMapper().readValue(taskDetail,
                        new TypeReference<Map<String, Object>>() {
                        });

                int resultCode = MapUtils.getIntValue(responseMap, "code", -1);
//                if (resultCode == 137 || resultCode == 2007) {
//                    // mobile report not ready yet
//                    continue;
//                }
                if (resultCode == 0) {
                    dsEntity.setReportData(taskDetail);
                    dsEntity.setVerifyStatus(VerifyStatus.SUCCESS);
                    authUserReportDsService.update(dsEntity);
                    logger.info("Update {} DS report to success", dsEntity.getTaskId());
                } else {
                    // mobile report is failed
                    dsEntity.setVerifyStatus(VerifyStatus.FAILED);
                    authUserReportDsService.update(dsEntity);
                    logger.info("Update {} DS report to failed", dsEntity.getTaskId());

                }
            } catch (Throwable t) {
                logger.error("Failed to process mobile raw data", t);
            }
        }

    }
}
