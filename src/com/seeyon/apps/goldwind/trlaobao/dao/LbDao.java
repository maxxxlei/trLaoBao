package com.seeyon.apps.goldwind.trlaobao.dao;

import java.util.List;
import java.util.Map;

import com.seeyon.apps.goldwind.trlaobao.vo.HistoryVo;
import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.util.FlipInfo;

public interface LbDao {
	
	/**
	 * 更新劳保物品尺寸
	 * @param linesList
	 * @throws BusinessException
	 */
	public void updateLines(List<UpdateVo> linesList) throws BusinessException;

	/**
	 * 插入历史底表
	 * @param listHistory 数据
	 * @param serialNumber 流水号（在历史表中充当版本号）
	 * @throws BusinessException
	 */
	public void insertHistory(List<HistoryVo> listHistory, String serialNumber) throws BusinessException;

	/**
	 * 修改历史底表审批状态
	 * @param version 版本号
	 * @throws BusinessException
	 */
	public void updateHistoryStatus(String version) throws BusinessException;
	
	/**
	 * 
	 * 获取劳保用品数据
	 * @param flipInfo
	 * @param query
	 * @return
	 * @throws BusinessException
	 */
	public FlipInfo getLaoBaoList(FlipInfo flipInfo, Map<String, String> query) throws BusinessException;
}
