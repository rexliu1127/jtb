package io.grx.wx.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.Query;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import io.grx.modules.tx.service.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.grx.common.exception.RRException;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxUserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rcpt")
public class MemberApiController extends BaseController {
    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxOverdueRecordService txOverdueRecordService;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private LuosimaoService luosimaoService;

    @Autowired
    private TxUserInviteService txUserInviteService;

    @Autowired
    private TxUserRewardService txUserRewardService;

    /**
     * 用户详情页
     *
     * @return
     */
    @RequestMapping(value = "/member/friend/list", method = RequestMethod.GET)
    @ResponseBody
    public R friendPage(Long userId, String userName) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (!user.getUserId().equals(userId)) {
            throw new RRException("Invalid request", 401);
        }

        return R.ok().put("page", txUserService.getFriendList(userId, userName));
    }


    /**
     * 我的借条列表
     */
    @RequestMapping("/member/check_credit")
    @ResponseBody
    public R checkCreidt(String name, String idNo, String captcha){
        if (StringUtils.isBlank(name) || StringUtils.isBlank(idNo)) {
            return R.error("请输入查询对象的姓名和身份证号码");
        }
        List<TxUserEntity> users = txUserService.queryByIdNo(idNo);
        if (users.size() == 0 || !StringUtils.equals(name, users.get(0).getName())) {
            return R.error("姓名或身份证号码不匹配");
        }

        if (luosimaoService.needCaptcha()) {
            if (!luosimaoService.isResponseVerified(captcha)) {
//            if (StringUtils.isEmpty(captcha)) {
                return R.error("人机识别验证失败");
            }
        }

        TxUserEntity user = users.get(0);
        cacheUtils.put(creditCacheKey(ShiroUtils.getUserId()), user.getUserId().toString(),
                60 * 60 * 24);

        return R.ok().put("userId", user.getUserId());
    }

    private String creditCacheKey(Long userId) {
        return "check_credit:" + ShiroUtils.getUserId();
    }

    /**
     * 保存签名
     *
     * @return
     */
    @RequestMapping(value = "/member/sign", method = RequestMethod.POST)
    @ResponseBody
    public R updateUserSign(String path) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (StringUtils.isNotBlank(path)) {
            user.setSignImgPath(path);

            txUserService.update(user);
        }

        return R.ok();
    }


    /**
     * 我的团队列表
     *
     * @return
     */
    @RequestMapping(value = "/member/team_list", method = RequestMethod.GET)
    @ResponseBody
    public R getTeamUserList(int level) {
        TxUserEntity user = ShiroUtils.getTxUser();

        return R.ok()
                .put("list", txUserInviteService.queryUserInviteList(user.getUserId(), level));
    }


    /**
     * 我的奖励列表
     *
     * @return
     */
    @RequestMapping(value = "/member/reward_list", method = RequestMethod.GET)
    @ResponseBody
    public R getUserRewardList(@RequestParam Map<String, Object> params) {
        TxUserEntity user = ShiroUtils.getTxUser();

        Query query = new Query(params);
        query.put("userId", user.getUserId());

        return R.ok()
                .put("page", txUserRewardService.queryListByUser(query));
    }
}
