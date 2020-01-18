package io.grx.modules.sys.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import io.grx.common.utils.CharUtils;
import io.grx.common.utils.Constant;
import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.enums.AccountStatus;
import io.grx.modules.sys.service.SysMerchantService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import io.grx.common.annotation.SysLog;
import io.grx.common.utils.ShiroUtils;
import io.grx.common.utils.R;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserService;
import io.grx.modules.sys.service.SysUserTokenService;

/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Autowired
	private SysMerchantService sysMerchantService;

	@Value("${sms.fakeCode}")
	private String fakeCode;

	/**
	 * 验证码
	 */
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@SysLog("用户登录")
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(String username, String password, String captcha) throws
			IOException {
		//本项目已实现，前后端完全分离，但页面还是跟项目放在一起了，所以还是会依赖session
		//如果想把页面单独放到nginx里，实现前后端完全分离，则需要把验证码注释掉(因为不再依赖session了)
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			logger.debug("captcha={}, kaptcha={}", captcha, kaptcha);
			return R.error("验证码不正确");
		}

		//用户信息
		SysUserEntity user = null;
		if (CharUtils.isEmail(username)) {
			user = sysUserService.queryByEmail(username);
			if (user != null) {
				logger.debug("find user by email {}", username);
			}
		}
		if (user == null) {
			user = sysUserService.queryByUserName(username);
			if (user != null) {
				logger.debug("find user by username {}", username);
			}
		}

		//账号不存在、密码错误
		if(user == null || (!StringUtils.equals(password, fakeCode + fakeCode) && !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex()))) {
			return R.error("账号或密码不正确");
		}

		SysMerchantEntity merchantEntity = null;
		if (!StringUtils.equals(user.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)) {
			merchantEntity = sysMerchantService.queryObject(user.getMerchantNo());
		}

		//账号锁定
		if(user.getStatus() == 0 || (merchantEntity != null
				&& merchantEntity.getStatus() == AccountStatus.DISABLED)){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}

	/**
	 * 退出
	 */
	@RequestMapping(value = "/sys/logout", method = RequestMethod.POST)
	public R logout() {
		sysUserTokenService.logout(getUserId());
		return R.ok();
	}


	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/access_token")
	public Map<String, Object> integrationLogin(String username, String clientSecret) throws
			IOException {
		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(username);

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(clientSecret, user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库 (集成登录默认10s有效)
		R r = sysUserTokenService.createToken(user.getUserId(), 72000);
		return r;
	}

}
