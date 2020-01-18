package io.grx.modules.tx.controller;


import io.grx.modules.tx.entity.TxComplainResultEntity;
import io.grx.modules.tx.service.TxComplainResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxUserService;

import java.util.List;

@RequestMapping("/tx/txbase")
@Controller
public class TxController {

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxComplainResultService txComplainResultService;

    /**
     * 认证登录页
     *
     * @return
     */
    @RequestMapping(value = "/{txId}", method = RequestMethod.GET)
    public String loadTx(@PathVariable(value = "txId") Long txId, Model model) {
        TxBaseEntity tx = txBaseService.queryObject(txId);
        if (tx == null) {
            tx = new TxBaseEntity();
        }
        if (tx.getBorrowerUserId() != null) {
            model.addAttribute("borrower", txUserService.queryObject(tx.getBorrowerUserId()));
        }
        if (tx.getLenderUserId() != null) {
            model.addAttribute("lender", txUserService.queryObject(tx.getLenderUserId()));
        }
        model.addAttribute("tx", tx);
        return "tx/tx_detail";
    }

    /**
     * 认证登录页
     *
     * @return
     */
    @RequestMapping(value = "/complain_result/{complainId}", method = RequestMethod.GET)
    public String complainResult(@PathVariable(value = "complainId") Long complainId, Model model) {

        List<TxComplainResultEntity> resultEntityList = txComplainResultService.queryByComplainId(complainId);
        model.addAttribute("list", resultEntityList);
        return "tx/complain_result";
    }
}
