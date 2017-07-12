package com.seeyon.apps.goldwind.trlaobao.manager;

import java.util.List;

import com.seeyon.apps.goldwind.trlaobao.vo.HistoryVo;
import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.exceptions.BusinessException;

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
	public void sendFlowJob() throws BusinessException;


	public void updateHistoryStatus(String version)throws BusinessException;
}
