package io.grx.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiniu.util.StringUtils;
import io.grx.common.annotation.DataFilter;
import io.grx.modules.sys.dao.SysDeptDao;
import io.grx.modules.sys.entity.SysDeptEntity;
import io.grx.modules.sys.service.SysDeptService;



@Service("sysDeptService")
public class SysDeptServiceImpl implements SysDeptService {
	@Autowired
	private SysDeptDao sysDeptDao;
	
	@Override
	public SysDeptEntity queryObject(Long deptId){
		return sysDeptDao.queryObject(deptId);
	}
	
	@Override
	@DataFilter(tableAlias = "d", user = false)
	public List<SysDeptEntity> queryList(Map<String, Object> map){
		return sysDeptDao.queryList(map);
	}

	@Override
	@Transactional
	public void save(SysDeptEntity sysDept){
		sysDeptDao.save(sysDept);
	}
	
	@Override
    @Transactional
	public void update(SysDeptEntity sysDept){
		sysDeptDao.update(sysDept);
	}
	
	@Override
    @Transactional
	public void delete(Long deptId){
		sysDeptDao.delete(deptId);
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return sysDeptDao.queryDetpIdList(parentId);
	}

	@Override
	public String getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		//添加本部门
		deptIdList.add(deptId);

		String deptFilter = StringUtils.join(deptIdList, ",");
		return deptFilter;
	}

	/**
	 * 查询子部门ID列表
	 *
	 * @param parentIds 上级部门ID列表
	 */
	@Override
	public List<Long> queryDetpIdList(final List<Long> parentIds) {
		List<Long> results = new ArrayList<>();
		for (Long parentId : parentIds) {
			results.add(parentId);
			results.addAll(queryDetpIdList(parentId));
		}
		return null;
	}

	/**
	 * 递归
	 */
	private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}
}
