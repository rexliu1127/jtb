package io.grx.modules.auth.controller;


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
import io.grx.modules.auth.entity.JxlReportEntity;
import io.grx.modules.auth.service.JxlReportService;

@Controller
@RequestMapping("/jxl")
public class JxlReportController {

    @Autowired
    private JxlReportService jxlReportService;


    /**
     * 认证报告
     *
     * @return
     */
    @RequestMapping(value = "/report/{token}", method = RequestMethod.GET)
    public String applyTransactionPage(Model model, @PathVariable(value = "token") String token) {

        try {
            if (ShiroUtils.getUserEntity() == null) {
                return "redirect:/";
            }

            JxlReportEntity reportEntity = jxlReportService.queryByToken(token);

            if (reportEntity == null) {
                return "redirect:/";
            }
            Map<String, Object> report = new ObjectMapper().readValue(reportEntity.getMobileData(),
                    new TypeReference<Map<String, Object>>() {});

            model.addAttribute("report", report.get("report_data"));

            return "jxl/report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
