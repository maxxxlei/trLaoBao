package com.seeyon.apps.goldwind.trlaobao.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.seeyon.apps.collaboration.po.ColSummary;
import com.seeyon.apps.goldwind.trlaobao.manager.LbManager;
import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.content.mainbody.MainbodyType;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.form.bean.FormDataMasterBean;
import com.seeyon.ctp.form.bean.FormDataSubBean;
import com.seeyon.ctp.form.service.FormService;
import com.seeyon.ctp.workflow.event.AbstractWorkflowEvent;
import com.seeyon.ctp.workflow.event.WorkflowEventData;
import com.seeyon.ctp.workflow.event.WorkflowEventResult;

public class UpdateSizeFlowEvent extends AbstractWorkflowEvent{

	private static Logger LOGGER = Logger.getLogger(UpdateSizeFlowEvent.class);
	private LbManager lbManager;
	
	public void setLbManager(LbManager lbManager) {
		this.lbManager = lbManager;
	}

	@Override
	public String getId() {
		return "updateSizeFlowEvent";
	}

	@Override
	public String getLabel() {
		return "劳保用品尺码更新";
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
		
		List<UpdateVo> listVo=new ArrayList<UpdateVo>();
		String userCode=null;
		// getFieldValue 获取主表数据
		Object userCodeObject = masterBean.getFieldValue(AppContext.getSystemProperty("trlaobao.updateFormMain.userCode"));
		if (userCodeObject != null) {
			LOGGER.info("人员编码 userCodeObject："+userCodeObject.toString());
			userCode=userCodeObject.toString();
			
			//注释掉的部分是如果这个字段（例：field0001）里面存储的是人员的ID，就通过下面的部门取出人员的属性
//			V3xOrgMember v3=orgManager.getMemberById(Long.parseLong(userName.toString()));
//			LOGGER.info("发起者姓名："+v3.getName());
//			LOGGER.info("发起者创建时间："+v3.getCreateTime());
//			LOGGER.info("发起者编码："+v3.getCode());
		}
		
		// getSubData 获取重复表数据
		List<FormDataSubBean> subList = masterBean.getSubData(AppContext.getSystemProperty("trlaobao.updateFormSon.tableName"));
		for (FormDataSubBean subBean : subList) {
			UpdateVo updateVo=new UpdateVo();
			Object lbCode = subBean.getFieldValue(AppContext.getSystemProperty("trlaobao.updateFormSon.lbCode"));
			Object lbSize = subBean.getFieldValue(AppContext.getSystemProperty("trlaobao.updateFormSon.lbSize"));
			
			if (userCode!=null) {
				LOGGER.info("人员编码："+userCode);
				updateVo.setUserCode(userCode);
			}
			if (lbCode != null) {
				LOGGER.info("劳保编码	" + lbCode.toString());
				updateVo.setLbCode( lbCode.toString());
			}
			if (lbSize != null) {
				LOGGER.info("劳保更新尺寸	" + lbSize.toString());
				updateVo.setLbSize( lbSize.toString());
			}
			listVo.add(updateVo);
		}
		lbManager.updateSize(listVo);
	}

	
}
