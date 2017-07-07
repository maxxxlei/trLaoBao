package com.seeyon.apps.goldwind.trlaobao.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.util.JDBCAgent;

public class LbDaoImpl implements LbDao{
	private static Logger LOGGER = Logger.getLogger(LbDaoImpl.class);
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
	
}
