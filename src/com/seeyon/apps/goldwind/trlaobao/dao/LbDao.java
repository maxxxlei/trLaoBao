package com.seeyon.apps.goldwind.trlaobao.dao;

import java.util.List;

import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.exceptions.BusinessException;

public interface LbDao {
	public void updateLines(List<UpdateVo> linesList) throws BusinessException;
	
}