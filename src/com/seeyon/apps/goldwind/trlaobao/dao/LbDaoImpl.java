package com.seeyon.apps.goldwind.trlaobao.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.seeyon.apps.goldwind.trlaobao.vo.HistoryVo;
import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.util.JDBCAgent;
import com.seeyon.ctp.util.UUIDLong;

public class LbDaoImpl implements LbDao{
	private static Logger LOGGER = Logger.getLogger(LbDaoImpl.class);
	/**
	 * 
	 * 更新劳保物品尺寸
	 */
	@Override
	public void updateLines(List<UpdateVo> linesList) throws BusinessException {
		String maintenanceFormMainUserCode =AppContext.getSystemProperty("trlaobao.maintenanceFormMain.userCode");
		String maintenanceFormMainTableName=AppContext.getSystemProperty("trlaobao.maintenanceFormMain.tableName");
		String maintenanceFormSonLbCode=AppContext.getSystemProperty("trlaobao.maintenanceFormSon.lbCode");
		String maintenanceFormSonLbSize=AppContext.getSystemProperty("trlaobao.maintenanceFormSon.lbSize");
		String maintenanceFormSonTableName=AppContext.getSystemProperty("trlaobao.maintenanceFormSon.tableName");
		
		JDBCAgent jdbc = new JDBCAgent(false);		
		try {
			List<String> sqls=new ArrayList<String>();
			for (UpdateVo vo : linesList) {
				String sql = "update "+maintenanceFormSonTableName+" s set s."+maintenanceFormSonLbSize+" = '"+vo.getLbSize()+"' where s.formmain_id = ( select id from "+maintenanceFormMainTableName+" m where m."+maintenanceFormMainUserCode+" = '"+vo.getUserCode()+"' ) and s."+maintenanceFormSonLbCode+" = '"+vo.getLbCode()+"' ";
				LOGGER.info("******更新维护表sql =" + sql);
				sqls.add(sql);
			}
			jdbc.executeBatch(sqls);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BusinessException(e);
		} finally {
			jdbc.close();
		}
	}

	
	/**
	 * 插入劳保历史记录底表
	 * 
	 */
	@Override
	public void insertHistory(List<HistoryVo> listHistory, String serialNumber)
			throws BusinessException {
		LOGGER.info("=======intoHistory插入历史表底表====");
		// 主表信息
		String formTable = AppContext.getSystemProperty("trlaobao.historyFormMain.formMainTable");
		String company=AppContext.getSystemProperty("trlaobao.historyFormMain.company");
		String department=AppContext.getSystemProperty("trlaobao.historyFormMain.department");
    	String post=AppContext.getSystemProperty("trlaobao.historyFormMain.post");
    	String userName=AppContext.getSystemProperty("trlaobao.historyFormMain.userName");
    	String sendDate=AppContext.getSystemProperty("trlaobao.historyFormMain.sendDate");;
    	String lbType=AppContext.getSystemProperty("trlaobao.historyFormMain.lbType");
    	String userCode=AppContext.getSystemProperty("trlaobao.historyFormMain.userCode");
    	String approve=AppContext.getSystemProperty("trlaobao.historyFormMain.approve");
    	String version=AppContext.getSystemProperty("trlaobao.historyFormMain.version");
    	
    	//重复表信息
    	String sonTable=AppContext.getSystemProperty("trlaobao.historyFormSon.formSonTable");
    	String lbNumber=AppContext.getSystemProperty("trlaobao.historyFormSon.lbNumber");
    	String lbName=AppContext.getSystemProperty("trlaobao.historyFormSon.lbName");
    	String lbCode=AppContext.getSystemProperty("trlaobao.historyFormSon.lbCode");
    	String lbSize=AppContext.getSystemProperty("trlaobao.historyFormSon.lbSize");
    	String lbQuantity=AppContext.getSystemProperty("trlaobao.historyFormSon.lbQuantity");
    	JDBCAgent jdbc = new JDBCAgent(false);		
		try {
			List<String> sqls=new ArrayList<String>();
			for (HistoryVo historyVo : listHistory) {
				long mainId = UUIDLong.longUUID();
				long sonId = UUIDLong.longUUID();
				String mainSql = "insert into "+formTable+"(id,"+company+","+department+","+post+","+userName+","
						+ ""+sendDate+","+lbType+","+userCode+","+approve+","+version+") "+
									"	values('"+mainId+"','"+historyVo.getCompany()+"','"+historyVo.getDepartment()+"','"+historyVo.getPost()+"','"+historyVo.getUserName()+"','"+historyVo.getSendTime()+"','"+historyVo.getLbType()+"','"+historyVo.getUserCode()+"','审批中','"+serialNumber+"');";
				LOGGER.info("******插入历史底表sql =" + mainSql);
				
				sqls.add(mainSql);
				String sonSql="insert into "+sonTable+"(id,formmain_id,"+lbNumber+","+lbName+","+lbCode+","+lbSize+","+lbQuantity+")"+
						"values('"+sonId+"','"+mainId+"','1','"+historyVo.getLbName()+"','"+historyVo.getLbCode()+"','"+historyVo.getLbSize()+"','"+historyVo.getLbCount()+"');";
				
				LOGGER.info("******插入历史底表重复表sql =" + sonSql);
				sqls.add(sonSql);
			}
			jdbc.executeBatch(sqls);
			
			LOGGER.info("******插入历史底表结束*************");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BusinessException(e);
		} finally {
			jdbc.close();
		}
	}


	@Override
	public void updateHistoryStatus(String version) throws BusinessException {
		
		LOGGER.info("========进入修改历史表状态DAO方法========");
		String formTable = AppContext.getSystemProperty("trlaobao.historyFormMain.formMainTable");
    	String sendDate=AppContext.getSystemProperty("trlaobao.historyFormMain.sendDate");;
    	String approve=AppContext.getSystemProperty("trlaobao.historyFormMain.approve");
    	String versionField=AppContext.getSystemProperty("trlaobao.historyFormMain.version");
		
    	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String date = sdf.format(new Date());
    	LOGGER.info("******领导审批结束日期**  ==="+date);
		String sql = "update "+formTable+" set "+approve+" ='审批通过',"+sendDate+"='"+date+"' where "+versionField+" = '"+version+"';";
		LOGGER.info("******领导审批结束sql**  ==="+sql);
		JDBCAgent jdbc = new JDBCAgent(false);		
		try {
			jdbc.execute(sql);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new BusinessException(e);
		} finally {
			jdbc.close();
		}
	}
}
