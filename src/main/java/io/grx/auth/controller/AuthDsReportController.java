package io.grx.auth.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserReportDsEntity;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserReportDsService;

@Controller
@RequestMapping("/autha/ds/report")
public class AuthDsReportController {

    @Autowired
    private AuthUserReportDsService authUserReportDsService;

    @Autowired
    private AuthRequestService authRequestService;

    /**
     * 贷前报告
     *
     * @return
     */
    @RequestMapping(value = "/{requestId}", method = RequestMethod.GET)
    public String getDsReport(Model model, @PathVariable(value = "requestId") Long requestId) {

        try {
            if (ShiroUtils.getUserEntity() == null) {
                return "redirect:/";
            }

            AuthRequestEntity requestEntity = authRequestService.queryObject(requestId);

            Map<String, Object> report = null;
            if (requestEntity != null) {

                List<AuthUserReportDsEntity> dsEntities = authUserReportDsService.queryByStatuses(
                        requestEntity.getUserId(), VerifyStatus.SUCCESS);

                if (dsEntities.size() > 0) {
                    AuthUserReportDsEntity dsEntity = dsEntities.get(dsEntities.size() - 1);

                    report = new ObjectMapper().readValue(dsEntity.getReportData(),
                            new TypeReference<Map<String, Object>>() {
                            });
                }
            }

            model.addAttribute("report", report);

            return "tongdun/jd_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
