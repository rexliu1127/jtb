package io.grx.modules.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.grx.modules.auth.service.TjReportService;
import io.grx.modules.auth.service.YxReportService;

@Component("cxbAuthTask")
public class CxbAuthTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private YxReportService yxReportService;

    @Autowired
    private TjReportService tjReportService;

    public void expireAuthReports() {
        logger.info("Start task expire auth reports");
        yxReportService.expireReports();
        tjReportService.expireReports();
        logger.info("End task expire auth reports");
    }
}
