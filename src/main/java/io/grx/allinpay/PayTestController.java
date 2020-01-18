package io.grx.allinpay;

import java.util.Map;

import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.tx.entity.TxUserEntity;

@Controller
public class PayTestController {

    private static final Logger logger = LoggerFactory.getLogger(PayTestController.class);

    /**
     * 测试hello
     *
     * @return
     */
    @RequestMapping(value = "/wx_pay_test.html", method = RequestMethod.GET)
    public String hello(Model model) {
        return "pay_test";
    }


    /**
     * 我的借条列表
     */
    @RequestMapping("/wx/pay_test")
    @ResponseBody
    public R listBorrowedTransaction(){

        TxUserEntity user = ShiroUtils.getTxUser();

        try {
            String orderNo = "2018-t1-" + System.currentTimeMillis();
            String randomNo = UUIDGenerator.getUUID();

            logger.info("orderNo={}, randomNo={}", orderNo, randomNo);

            Map<String, String> response = new SybPayService().pay(1, orderNo,
                    "W02", randomNo, "米仓云服借条服务",
                    user.getWechatId(), "",
                    HttpContextUtils.getRequestBaseUrl() + "/wx_pay_callback",
                    "no_credit");

            logger.info("response: {}", response);

            return R.ok().put("payinfo", response.get("payinfo"));
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

}
