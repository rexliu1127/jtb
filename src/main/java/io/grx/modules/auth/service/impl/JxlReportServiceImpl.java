package io.grx.modules.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.auth.dao.JxlReportDao;
import io.grx.modules.auth.entity.JxlReportEntity;
import io.grx.modules.auth.service.JxlReportService;



@Service("jxlReportService")
public class JxlReportServiceImpl implements JxlReportService {
	@Autowired
	private JxlReportDao jxlReportDao;
	
	@Override
	public JxlReportEntity queryObject(Long id){
		return jxlReportDao.queryObject(id);
	}

	@Override
	public List<JxlReportEntity> queryList(Map<String, Object> map){
		return jxlReportDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return jxlReportDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(JxlReportEntity jxlReport){
		jxlReportDao.save(jxlReport);
	}
	
	@Override
	@Transactional
	public void update(JxlReportEntity jxlReport){
		jxlReportDao.update(jxlReport);
	}

	@Override
	@Transactional
	public void delete(Long id){
		jxlReportDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		jxlReportDao.deleteBatch(ids);
	}

    @Override
    public JxlReportEntity queryByToken(final String token) {
        return jxlReportDao.queryByToken(token);
    }

}
