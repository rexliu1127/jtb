package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   灯塔-胡萝卜获取借贷信息
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthHuLuoboVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//是否命中网贷黑名单
	private String hit;
	private String note;

	//四大平台是否存在逾期
	private boolean overdueFlag;

	//四大借条平台申请数,最大四个
	private Integer appAllCount;

	//借贷宝
	private AuthJieDaiBaoVO   jiedaibaoVo;

    //是否命中无忧黑名单
	private String wuYouHit;
	//无忧描述】
	private String wuYouNote;
	//无忧详情
	private List<AuthWuYouVO>  wuyouList;

	//是否命中米房黑名单
	private String miFangHit;
	//米房描述
	private String miFangNote;
	//米房逾期列表
	private List<AuthMiFangVO>  mifangList;

	//今借到
	private  AuthJinJieDaoVO  jinJieDaoVO;

	public String getHit() {
		return hit;
	}

	public void setHit(String hit) {
		this.hit = hit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public AuthJieDaiBaoVO getJiedaibaoVo() {
		return jiedaibaoVo;
	}

	public void setJiedaibaoVo(AuthJieDaiBaoVO jiedaibaoVo) {
		this.jiedaibaoVo = jiedaibaoVo;
	}

	public String getWuYouHit() {
		return wuYouHit;
	}

	public void setWuYouHit(String wuYouHit) {
		this.wuYouHit = wuYouHit;
	}

	public String getWuYouNote() {
		return wuYouNote;
	}

	public void setWuYouNote(String wuYouNote) {
		this.wuYouNote = wuYouNote;
	}

	public List<AuthWuYouVO> getWuyouList() {
		return wuyouList;
	}

	public void setWuyouList(List<AuthWuYouVO> wuyouList) {
		this.wuyouList = wuyouList;
	}

	public String getMiFangHit() {
		return miFangHit;
	}

	public void setMiFangHit(String miFangHit) {
		this.miFangHit = miFangHit;
	}

	public String getMiFangNote() {
		return miFangNote;
	}

	public void setMiFangNote(String miFangNote) {
		this.miFangNote = miFangNote;
	}

	public List<AuthMiFangVO> getMifangList() {
		return mifangList;
	}

	public void setMifangList(List<AuthMiFangVO> mifangList) {
		this.mifangList = mifangList;
	}

	public AuthJinJieDaoVO getJinJieDaoVO() {
		return jinJieDaoVO;
	}

	public void setJinJieDaoVO(AuthJinJieDaoVO jinJieDaoVO) {
		this.jinJieDaoVO = jinJieDaoVO;
	}

	public boolean isOverdueFlag() {
		return overdueFlag;
	}

	public void setOverdueFlag(boolean overdueFlag) {
		this.overdueFlag = overdueFlag;
	}

	public Integer getAppAllCount() {
		return appAllCount;
	}

	public void setAppAllCount(Integer appAllCount) {
		this.appAllCount = appAllCount;
	}
}
