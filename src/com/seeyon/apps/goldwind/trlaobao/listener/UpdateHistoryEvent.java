package com.seeyon.apps.goldwind.trlaobao.listener;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.seeyon.apps.collaboration.po.ColSummary;
import com.seeyon.apps.goldwind.trlaobao.manager.LbManager;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.content.mainbody.MainbodyType;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.form.bean.FormDataMasterBean;
import com.seeyon.ctp.form.service.FormService;
import com.seeyon.ctp.workflow.event.AbstractWorkflowEvent;
import com.seeyon.ctp.workflow.event.WorkflowEventData;
import com.seeyon.ctp.workflow.event.WorkflowEventResult;

public class UpdateHistoryEvent extends AbstractWorkflowEvent{

	private static Logger LOGGER = Logger.getLogger(UpdateSizeFlowEvent.class);
	private LbManager lbManager;
	
	public void setLbManager(LbManager lbManager) {
		this.lbManager = lbManager;
	}

	@Override
	public String getId() {
		return "updateHistoryEvent";
	}

	@Override
	public String getLabel() {
		return "历史底表审批状态更新";
	}

	@Override
	public WorkflowEventResult onBeforeStart(WorkflowEventData data) {
		WorkflowEventResult result = new WorkflowEventResult();
		LOGGER.info("进入流程发起前事件");
		return result;
	}

	@Override
	public void onProcessFinished(WorkflowEventData data) {
		LOGGER.info("进入流程结束事件"); 
		ColSummary summary = (ColSummary) data.getSummaryObj();
		if (summary != null && String.valueOf(MainbodyType.FORM.getKey()).equals(summary.getBodyType())) {
			Long masterId = summary.getFormRecordid();
			Long formAppId = summary.getFormAppid();
			
			try {
				//从表单（FormDataMasterBean）中取得数据 
				FormDataMasterBean masterBean = FormService.findDataById(masterId, formAppId);
				//获取主表和重复表数据
				getFormData4Bsk(masterBean);
			} catch (BusinessException e) {
				LOGGER.error(e.getMessage(),e);
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
	}
	
	@Override
	public void onFinishWorkitem(WorkflowEventData data) {
		LOGGER.info("进入流程处理后事件");	
	}
	
	
	/**
	 * 
	 *  获取主表和重复表数据
	 * @param masterBean  表单
	 *
	 * @throws BusinessException
	 */
	public void getFormData4Bsk(FormDataMasterBean masterBean) throws BusinessException {
		LOGGER.info("===进入获取主表和重复表数据方法===");
		String version=null;
		// getFieldValue 获取主表数据
		Object versionObj = masterBean.getFieldValue(AppContext.getSystemProperty("trlaobao.formMain.version"));
		if (versionObj != null) {
			LOGGER.info("版本号version："+versionObj.toString());
			version=versionObj.toString(); 
		}
		lbManager.updateHistoryStatus(version);
	}
}
