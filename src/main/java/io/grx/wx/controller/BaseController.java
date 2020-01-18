package io.grx.wx.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grx.modules.tx.entity.TxUserEntity;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final static String TX_TEMPLATE_PATH = "rcpt/";

    protected TxUserEntity getUser(HttpServletRequest request) {
        return (TxUserEntity) request.getAttribute("TX_USER");
    }
}
