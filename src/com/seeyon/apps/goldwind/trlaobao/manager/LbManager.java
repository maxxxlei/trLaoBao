package com.seeyon.apps.goldwind.trlaobao.manager;

import java.util.List;
import java.util.Map;

import com.seeyon.apps.goldwind.trlaobao.vo.HistoryVo;
import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.util.FlipInfo;

public interface LbManager {
	/**
	 * 修改劳保物品尺寸方法
	 * @param updateVos 
	 * @throws BusinessException
	 */
	public void updateSize(List<UpdateVo> updateVos) throws BusinessException;
	
	
	/**
	 * 发送流程
	 * @throws BusinessException
	 */
	public void sendFlow(List<HistoryVo> dataList,String serialNumber) throws BusinessException;
	
	/**
	 * 
	 * 获取数据
	 * @throws BusinessException
	 */
	public void getSearchData(List<HistoryVo> listHistory) throws BusinessException;
	
	/**
	 * 定时器，定时触发发起领导审批流程（测试使用）
	 * @throws BusinessException
	 */
	public void sendFlowJob() throws BusinessException;


	/**
	 * 修改历史表底表审批状态
	 * @param version  版本号（根据版本号批量修改）
	 * @throws BusinessException
	 */
	public void updateHistoryStatus(String version)throws BusinessException;
	
	
	/**
	 * 获取前台劳保用品列表数据
	 * @param flipInfo 
	 * @param query 查询条件
	 * @return
	 * @throws BusinessException
	 */
	public FlipInfo getLaoBaoList(FlipInfo flipInfo, Map<String, String> query) throws BusinessException;
	
}
