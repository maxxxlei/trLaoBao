package com.seeyon.apps.goldwind.trlaobao.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kg.commons.utils.CollectionUtils;
import com.seeyon.apps.goldwind.sendflow.manager.SendFlowManager;
import com.seeyon.apps.goldwind.trlaobao.dao.LbDao;
import com.seeyon.apps.goldwind.trlaobao.vo.HistoryVo;
import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.form.modules.serialNumber.SerialNumberManager;
import com.seeyon.ctp.organization.bo.V3xOrgMember;
import com.seeyon.ctp.organization.manager.OrgManager;

public class LbManagerImpl implements LbManager{
	private LbDao lbDao;
	
	public void setLbDao(LbDao lbDao) {
		this.lbDao = lbDao;
	}

	@Override
	public void updateSize(List<UpdateVo> updateVos) throws BusinessException {
		lbDao.updateLines(updateVos);
	}
	
	private static final Logger LOGGER = Logger.getLogger(LbManagerImpl.class);
	private OrgManager orgManager;
	private SendFlowManager sendFlowManager;
	private SerialNumberManager serialnumbermanager = (SerialNumberManager) AppContext.getBean("serialNumberManager");
	
	public void setSendFlowManager(SendFlowManager sendFlowManager) {
		this.sendFlowManager = sendFlowManager;
	}

	public void setOrgManager(OrgManager orgManager) {
		this.orgManager = orgManager;
	}


	@Override
	public void sendFlow(List<HistoryVo> dataList,String serialNumber)
			throws BusinessException {

		 //1.获取到数据,判断是否有数据
		LOGGER.info("=======进入发起流程====");
		if (CollectionUtils.isEmpty(dataList)) {
			LOGGER.info("没有需要发送流程的数据");
			return;
		}
		
		try {
			//3.填充至流程中
			Map<String, Object> formDataMap = new HashMap<String, Object>();
			formDataMap.put("流水号", serialNumber);
			List<Map<String, Object>> sonDataList = new ArrayList<Map<String, Object>>();
			for (HistoryVo historyVo : dataList) {
				Map<String, Object> sonDataMap = new HashMap<String, Object>();
				
				sonDataMap.put("姓名", historyVo.getUserName());
				sonDataMap.put("编号", historyVo.getUserCode());
				sonDataMap.put("劳保物品",historyVo.getLbName());
				sonDataMap.put("编码",historyVo.getLbCode());
				sonDataMap.put("尺码", historyVo.getLbSize());
				sonDataMap.put("数量", historyVo.getLbCount());
				sonDataMap.put("应发日期", historyVo.getLbGrantDate());
				sonDataList.add(sonDataMap);
			}
			formDataMap.put("subForms", sonDataList);
			
			//4.获取发送流程类的数据
			String tokenUrl = AppContext.getSystemProperty("trlaobao.tokenUrl");
			String sendFormUrl = AppContext.getSystemProperty("trlaobao.sendFormUrl");
			String templateCode = AppContext.getSystemProperty("trlaobao.templateCode");
			String subject = AppContext.getSystemProperty("trlaobao.subject");
			LOGGER.info("审批发起流程tokenUrl=" + tokenUrl);
			LOGGER.info("审批发起流程sendFormUrl=" + sendFormUrl);
			LOGGER.info("审批发起流程templateCode=" + templateCode);
			LOGGER.info("审批发起流程subject=" + subject);
			String startMemberId = AppContext.getSystemProperty("trlaobao.startFlowMemberId");
			V3xOrgMember member = orgManager.getMemberById(Long.parseLong(startMemberId));
			if (member == null) {
				LOGGER.error("发起人为空，无法发送！");
				return;
			}
			//5.调用流程方法
			String summaryId = sendFlowManager.saveFormFlow(tokenUrl,sendFormUrl, templateCode, subject, formDataMap,
					member.getLoginName());
			
			LOGGER.info("审批发起流程summaryId===========" + summaryId);
			LOGGER.info("=======结束审批发起流程====");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	

	/**
	 * 获取前端搜索后的的集合
	 */
	@Override
	public void getSearchData(List<HistoryVo> listHistory)
			throws BusinessException {
		// 解析实例
		
		String serialNumber = serialnumbermanager.getSerialNumberValue(Long.parseLong(AppContext.getSystemProperty("trlaobao.serialNumberId")));
		//发起领导审批流程
		sendFlow(listHistory,serialNumber);
		LOGGER.info("=======getSearchData结束审批发起流程====");
		//插入历史底表
		lbDao.insertHistory(listHistory,serialNumber);
		
	}
	
	public void sendFlowJob() throws BusinessException{
		List<HistoryVo> listHistory =new ArrayList<HistoryVo>();
		for (int i = 1; i <= 3; i++) {
			HistoryVo hv=new HistoryVo();
			hv.setCompany("致远互联");
			hv.setDepartment("技术部");
			hv.setLbCode("0001");
			hv.setLbCount("20");
			hv.setLbGrantDate("2017.7.12");
			hv.setLbName("安全帽");
			hv.setLbSize("L");
			hv.setLbType("服装");
			hv.setPost("开发工程师");
			hv.setUserCode("001");
			hv.setSendTime("2017.7.20");
			hv.setUserName("马磊");
			listHistory.add(hv);
		}
		getSearchData(listHistory);
		
	}

	@Override
	public void updateHistoryStatus(String version) throws BusinessException {
		lbDao.updateHistoryStatus(version);
	}
}
