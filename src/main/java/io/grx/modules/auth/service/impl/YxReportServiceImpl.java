package io.grx.modules.auth.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.grx.auth.entity.AuthConfig;
import io.grx.modules.auth.dao.YxReportDao;
import io.grx.modules.auth.entity.YxReportEntity;
import io.grx.modules.auth.enums.YiXiangType;
import io.grx.modules.auth.service.YxReportService;
import io.grx.modules.auth.util.AuthConstants;
import io.grx.modules.sys.service.SysConfigService;


@Service("yxReportService")
public class YxReportServiceImpl implements YxReportService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private YxReportDao yxReportDao;

	@Autowired
	private SysConfigService sysConfigService;

	private static final Map<String, YiXiangType> YX_TYPE_MAP = Arrays.stream(YiXiangType.values())
			.collect(Collectors.toMap(YiXiangType::name,  x -> x));
	
	@Override
	public YxReportEntity queryObject(Long id){
		return yxReportDao.queryObject(id);
	}
	
	@Override
	public List<YxReportEntity> queryList(Map<String, Object> map){
		return yxReportDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return yxReportDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(YxReportEntity yxReport){
		yxReportDao.save(yxReport);
	}
	
	@Override
	@Transactional
	public void update(YxReportEntity yxReport){
		yxReportDao.update(yxReport);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		yxReportDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		yxReportDao.deleteBatch(ids);
	}

	@Override
	public YxReportEntity queryByTaskId(final String taskId) {
		return yxReportDao.queryByTaskId(taskId);
	}

	@Override
	public YxReportEntity queryLatestByUserId(final Long userId, final YiXiangType type) {
		return yxReportDao.queryLatestByUserId(userId, type);
	}

	@Override
	@Transactional
	public void expireReports() {
//		String configStr = sysConfigService.getValue(AuthConstants.AUTH_TYPE_CONFIG);
//		if (StringUtils.isNotBlank(configStr)) {
//			List<AuthConfig> results = new Gson().fromJson(configStr,
//					new TypeToken<ArrayList<AuthConfig>>(){}.getType());
//			for (AuthConfig config : results) {
//				if (config.getEnabled() == null || config.getEnabled()) {
//					YiXiangType type = YX_TYPE_MAP.get(config.getAuthTypeName().toUpperCase());
//					if (type != null) {
//						int total = yxReportDao.expireReports(type, config.getExpiredDay());
//						logger.info("Expire YX report ({}) created before {} days, {} updated",
//								config.getAuthTypeName(), config.getExpiredDay(), total);
//					}
//				}
//			}
//		} else {
//			logger.info("No AUTH_TYPE_CONFIG defined");
//		}
	}

}
