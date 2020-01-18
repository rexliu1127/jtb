package io.grx.modules.auth.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserReportDsEntity;
import io.grx.modules.auth.entity.JxlReportEntity;
import io.grx.modules.auth.enums.AuthVendorType;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserContactService;
import io.grx.modules.auth.service.AuthUserReportDsService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.JxlReportService;

@Controller
@RequestMapping("/tongdun")
public class TongdunReportController {

    @Autowired
    private JxlReportService jxlReportService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthUserContactService authUserContactService;

    @Autowired
    private AuthUserReportDsService authUserReportDsService;


    /**
     * 贷前报告
     *
     * @return
     */
    @RequestMapping(value = "/report/{token}", method = RequestMethod.GET)
    public String getReport(Model model, @PathVariable(value = "token") String token) {

        try {
            if (ShiroUtils.getUserEntity() == null) {
                return "redirect:/";
            }

            JxlReportEntity reportEntity = jxlReportService.queryByToken(token);


            if (reportEntity == null) {
                return "redirect:/";
            }
            Map<String, Object> report = new ObjectMapper().readValue(reportEntity.getReportData(),
                    new TypeReference<Map<String, Object>>() {});

            model.addAttribute("report", report);

            AuthRequestEntity requestEntity = authRequestService.queryObject(reportEntity.getRequestId());
            model.addAttribute("request", requestEntity);

            model.addAttribute("user", authUserService.queryObject(requestEntity.getUserId()));

            Map<String, Object> jdReport = null;
            if (requestEntity != null) {

                List<AuthUserReportDsEntity> dsEntities = authUserReportDsService.queryByStatuses(
                        requestEntity.getUserId(), VerifyStatus.SUCCESS);

                if (dsEntities.size() > 0) {
                    AuthUserReportDsEntity dsEntity = dsEntities.get(dsEntities.size() - 1);

                    jdReport = new ObjectMapper().readValue(dsEntity.getReportData(),
                            new TypeReference<Map<String, Object>>() {
                            });
                }
            }

            model.addAttribute("jdReport", jdReport);


            return "tongdun/report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 贷前报告
     *
     * @return
     */
    @RequestMapping(value = "/mobile_report/{token}", method = RequestMethod.GET)
    public String getMobileReport2(Model model, @PathVariable(value = "token") String token) {

        try {
            if (ShiroUtils.getUserEntity() == null) {
                return "redirect:/";
            }

            JxlReportEntity reportEntity = jxlReportService.queryByToken(token);

            if (reportEntity == null) {
                return "redirect:/";
            }

            AuthRequestEntity authRequestEntity = authRequestService.queryByToken(token);

            model.addAttribute("request", authRequestEntity);

            model.addAttribute("user", authUserService.queryObject(authRequestEntity.getUserId()));


            Map<String, String> contact = authUserContactService.getContacts(authRequestEntity.getUserId());
            model.addAttribute("contactMap", contact);

            Map<String, Object> report = new ObjectMapper().readValue(reportEntity.getMobileData(),
                    new TypeReference<Map<String, Object>>() {
                    });

            if (authRequestEntity.getVendorType() == AuthVendorType.JXL) {
                model.addAttribute("report", report.get("report_data"));

                return "jxl/report";
            } else {
                model.addAttribute("report", report);
                return "tongdun/mobile_report2";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 报告联系人详情
     *
     * @return
     */
    @RequestMapping(value = "/mobile_contact_detail/{token}", method = RequestMethod.GET)
    @ResponseBody
    public R getMobileContactDetail(@PathVariable(value = "token") String token) {

        try {
            JxlReportEntity reportEntity = jxlReportService.queryByToken(token);

            if (reportEntity == null) {
                return R.error("invalid request");
            }
            Map<String, Object> report = new ObjectMapper().readValue(reportEntity.getMobileData(),
                    new TypeReference<Map<String, Object>>() {});

            AuthRequestEntity authRequestEntity = authRequestService.queryByToken(token);

            Map<String, String> contactMap = authUserContactService.getContacts(authRequestEntity.getUserId());

            if (authRequestEntity.getVendorType() == AuthVendorType.JXL) {
                return R.ok().put("allContactDetail", MapUtils.getObject(MapUtils.getMap(report, "report_data"),
                        "contact_list"));
            } else {
                Collection<Map<String, Object>> contactDetails = (Collection<Map<String, Object>>) MapUtils.getObject
                        (report, "all_contact_detail");
                for (Map<String, Object> contactDetail : contactDetails) {
                    String contactNumber = MapUtils.getString(contactDetail, "contact_number");
                    String contactName = MapUtils.getString(contactDetail, "contact_name");
                    if (StringUtils.isBlank(contactName)) {
                        String contactNameInContactMap = contactMap.get(contactNumber);
                        contactDetail.put("contact_name", contactNameInContactMap);
                    }
                }
                return R.ok().put("allContactDetail", report.get("all_contact_detail"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 报告联系人详情
     *
     * @return
     */
    @RequestMapping(value = "/mobile_contact_detail_by_id/{idNo}", method = RequestMethod.GET)
    @ResponseBody
    public R getMobileContactDetailByIdNo(@PathVariable(value = "idNo") String idNo) {
        try {
            AuthRequestEntity requestEntity =  authRequestService.queryLatestByIdNo(idNo, null);
            Object result = null;
            if (requestEntity != null) {
                JxlReportEntity reportEntity = jxlReportService.queryByToken(requestEntity.getVerifyToken());

                if (reportEntity != null) {
                    Map<String, Object> report = new ObjectMapper().readValue(reportEntity.getMobileData(),
                            new TypeReference<Map<String, Object>>() {});

                    if (requestEntity.getVendorType() == AuthVendorType.JXL) {
                        return R.ok().put("allContactDetail", MapUtils.getObject(MapUtils.getMap(report, "report_data"),
                                "contact_list"));
                    } else {
                        return R.ok().put("allContactDetail", report.get("all_contact_detail"));
                    }
                }
            }

            return R.ok();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 贷前报告
     *
     * @return
     */
    @RequestMapping(value = "/mobile/{token}", method = RequestMethod.GET)
    public String getMobileReport(Model model, @PathVariable(value = "token") String token) {

        try {
            if (ShiroUtils.getUserEntity() == null) {
                return "redirect:/";
            }

            JxlReportEntity reportEntity = jxlReportService.queryByToken(token);


            if (reportEntity == null) {
                return "redirect:/";
            }
            Map<String, Object> report = new ObjectMapper().readValue(reportEntity.getRawData(),
                    new TypeReference<Map<String, Object>>() {});

            enrichMobileReport(report);
            model.addAttribute("report", report);

            AuthRequestEntity requestEntity = authRequestService.queryObject(reportEntity.getRequestId());
            model.addAttribute("request", requestEntity);

            model.addAttribute("user", authUserService.queryObject(requestEntity.getUserId()));

            return "tongdun/mobile_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void enrichMobileReport(Map<String, Object> report) {
        Map<String, Object> data = MapUtils.getMap(report, "data");
        String userIdNo = MapUtils.getString(data, "identity_code");
        String userRealName = MapUtils.getString(data, "real_name");

        Map<String, Object> taskData = MapUtils.getMap(data, "task_data");
        Map<String, Object> baseInfo = MapUtils.getMap(taskData, "base_info");

        String mobileIdNo = MapUtils.getString(baseInfo, "cert_num");
        String mobileUserRealName = MapUtils.getString(baseInfo, "user_name");

        boolean idNoMatched = StringUtils.equals(StringUtils.substring(userIdNo, 0, 4),
                StringUtils.substring(mobileIdNo, 0, 4))
                && StringUtils.equals(StringUtils.substring(userIdNo, StringUtils.length(userIdNo) - 4,
                        StringUtils.length(userIdNo)),
                StringUtils.substring(mobileIdNo, StringUtils.length(mobileIdNo) - 4,
                        StringUtils.length(mobileIdNo)));
        boolean userNameMatched = StringUtils.equalsIgnoreCase(userRealName, mobileUserRealName);
        data.put("real_name_matched", userNameMatched);
        data.put("identity_code_matched", idNoMatched);
    }


}
