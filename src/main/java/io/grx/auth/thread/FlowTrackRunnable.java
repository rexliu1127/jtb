package io.grx.auth.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserContactService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.AuthUserTokenService;
import io.grx.modules.auth.service.BqsReportService;
import io.grx.modules.auth.service.TjReportService;
import io.grx.modules.auth.service.YxReportService;
import io.grx.modules.flow.service.FlowAllocationRecordService;

/**
 * 用户报告跟踪
 * 
 * @author wanghao
 *
 */
public class FlowTrackRunnable implements Runnable {

	private static final Logger LOG = LoggerFactory
			.getLogger(FlowTrackRunnable.class);

	/**
	 * 只跟踪基本信息
	 */
	public static final Integer SYNC_BASE = 1;

	/**
	 * 只跟踪基本紧急联系人信息
	 */
	public static final Integer SYNC_CONTACT = 2;

	/**
	 * 只跟踪通讯录信息
	 */
	public static final Integer SYNC_CONTACT_LIST = 3;

	/**
	 * 跟踪所有数据|一般是提交的时候操作
	 */
	public static final Integer SYNC_SUBMIT = 4;

	private Long requestId;

	private Integer syncType;

	private FlowAllocationRecordService flowAllocationRecordService;
	
	public FlowTrackRunnable(Long requestId, Integer syncType,FlowAllocationRecordService flowAllocationRecordService) {
		this.requestId = requestId;
		this.syncType = syncType;
		this.flowAllocationRecordService = flowAllocationRecordService;
	}

	@Override
	public void run() {
		if (this.requestId == null) {
			LOG.warn("跟踪报告参数错误,requestId为空");
			return;
		}
		if (SYNC_BASE == this.syncType) {
			LOG.warn("开始跟踪报告，requestId:{}  syncType:", this.requestId, this.syncType);
			this.synchronizeAuthUser();
		} else if (SYNC_CONTACT == this.syncType) {
			LOG.warn("开始跟踪报告，requestId:{}  syncType:", this.requestId, this.syncType);
			this.synchronizeAuthUser();
		} else if (SYNC_CONTACT_LIST == this.syncType) {
			LOG.warn("开始跟踪报告，requestId:{}  syncType:", this.requestId, this.syncType);
			this.synchronizeAuthUserContact();
		} else if (SYNC_SUBMIT == this.syncType) {
			this.synchronizeAuthRequest();
		} else {
			LOG.warn("跟踪报告参数错误,sync_type不合法,当前sync_type:{}", this.syncType);
			return;
		}
	}
	private void synchronizeAuthUser() {
		this.flowAllocationRecordService.updateTrackAuthUser(requestId);
	}
	private void synchronizeAuthUserContact() {
		this.flowAllocationRecordService.updateTrackAuthUser(requestId);
	}
	private void synchronizeAuthRequest() {
		this.flowAllocationRecordService.updateTrackAuthUserRequest(requestId);
	}

}
