package io.grx.modules.job.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Connector;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.stereotype.Component;

import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.DateUtils;
import io.grx.modules.pay.service.PayRecordService;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.tx.service.TxDailyReportService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.wx.utils.WechatUtils;

/**
 * 报告任务
 */
@Component("reportTask")
public class ReportTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private EmbeddedWebApplicationContext appContext;

    @Value("${server.externalUrl}")
    private String externalUrl;

    @Value("${server.context-path}")
    private String contextPath;

    @Value("${wechat.confirmOrderTemplateId}")
    private String confirmOrderTemplateId;

    private String baseUrl;

    @Autowired
    private TxDailyReportService txDailyReportService;

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private CacheUtils cacheUtils;

    public void newTxStat() throws Exception {
        try {
            txDailyReportService.generateLastDaySum();
        } catch (Exception e) {
            logger.warn("Generate daily tx report failed", e);
        }

        logger.info("Going to send Tx report");
        String mobiles = sysConfigService.getValue("REPORT_NOTICE_MOBILE");
        if (StringUtils.isNotBlank(mobiles)) {
            for (String mobile : StringUtils.split(mobiles, ",")) {
                sendNewTxStatNotice(mobile);
                logger.info("Sent Tx report to {}", mobile);
            }
        }
        logger.info("End sending tx report");
    }

    private void sendNewTxStatNotice(String wechatId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("first", "最新运营报告已更新!");
            params.put("keyword1", DateUtils.formateDate(new Date()));
            params.put("keyword2", "借条报告");
            params.put("keyword3", "");
            params.put("remark", "查看详情");

            wechatUtils.sendTemplateMessage(wechatId, confirmOrderTemplateId,
                    getBaseUrl() + "/wx/tx/txdailyreport/page?limit=30&page=1", params);
        } catch (Exception e) {
            logger.warn("Failed to send wechat notice", e);
        }
    }

    private String getBaseUrl() throws Exception {
        if (StringUtils.isBlank(baseUrl)) {
            if (StringUtils.isNotBlank(externalUrl)) {
                baseUrl = externalUrl + contextPath;
            } else {
                baseUrl = getTomcatUrl();
            }
        }

        return baseUrl;
    }

    public void incomeReport() {
        Date now = new Date();
        String dateStr = DateUtils.formateDate(now);
        Date todayStart = DateUtils.parseDate(dateStr);

        long lastDayAccSum = NumberUtils.toLong(cacheUtils.get(dateStr + ":last_day_acc_sum"), -1);
        if (lastDayAccSum == -1) {
            lastDayAccSum = payRecordService.sumPaidAmount(null, todayStart);
            cacheUtils.put(dateStr + ":last_day_acc_sum", String.valueOf(lastDayAccSum), 60 * 60 * 24);
        }

        long lastDaySum = NumberUtils.toLong(cacheUtils.get(dateStr + ":last_day_sum"), -1);
        if (lastDaySum == -1) {
            Date lastDayStart = Date.from(LocalDate.now().plusDays(-1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
            lastDaySum = payRecordService.sumPaidAmount(lastDayStart, todayStart);
            cacheUtils.put(dateStr + ":last_day_sum", String.valueOf(lastDaySum), 60 * 60 * 24);
        }

        long todaySum = payRecordService.sumPaidAmount(todayStart, null);

        logger.info("Going to send income report");
        String mobiles = sysConfigService.getValue("REPORT_NOTICE_MOBILE");
        if (StringUtils.isNotBlank(mobiles)) {
            for (String mobile : StringUtils.split(mobiles, ",")) {
                sendIncomeNotice(mobile, todaySum, lastDaySum,
                        lastDayAccSum + todaySum);
                logger.info("Sent income report to {}", mobile);
            }
        }
        logger.info("End sending income report");
    }

    private void sendIncomeNotice(String wechatId, long todaySum, long lastDaySum, long totalSum) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("first", "请查看最新营收!");
            params.put("keyword1", DateUtils.formateDateTime(new Date()));
            params.put("keyword2", "营收报告");
            params.put("keyword3", "");

            params.put("remark", String.format("今日营收: %1$s元, 昨日营收: %2$s元, 累计营收: %3$s万元",
                    String.valueOf(todaySum / 100.0), String.valueOf(lastDaySum/ 100.0),
                    new BigDecimal(totalSum).divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP)));

            wechatUtils.sendTemplateMessage(wechatId, confirmOrderTemplateId,
                    getBaseUrl() + "/wx/main", params);
        } catch (Exception e) {
            logger.warn("Failed to send wechat notice", e);
        }
    }


    private String getTomcatUrl() throws UnknownHostException {
        Connector connector = ((TomcatEmbeddedServletContainer) appContext.getEmbeddedServletContainer()).getTomcat().getConnector();
        String scheme = connector.getScheme();
        String ip = InetAddress.getLocalHost().getHostAddress();
        int port = connector.getPort();
        String contextPath = appContext.getServletContext().getContextPath();
        return scheme + "://" + ip + ":" + port + contextPath;
    }
}
