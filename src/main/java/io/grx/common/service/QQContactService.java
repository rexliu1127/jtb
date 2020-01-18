package io.grx.common.service;

import java.util.Map;

import io.grx.common.utils.R;

public interface QQContactService {

    void prepareLogin(String url);

    String getCaptchaImageBase64();

    R login1(String mobile, String password, String captcha);

    R sendVerifyCode(final String mobile, final String csrfToken);

    R login2(final String mobile, String verifyCode, String csrfToken);

    Map<String, String> downloadContacts(String mobile);
}
