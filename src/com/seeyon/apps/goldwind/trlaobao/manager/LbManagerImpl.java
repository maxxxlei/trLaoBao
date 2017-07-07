package com.seeyon.apps.goldwind.trlaobao.manager;

import java.util.List;

import com.seeyon.apps.goldwind.trlaobao.dao.LbDao;
import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.exceptions.BusinessException;

public class LbManagerImpl implements LbManager{
	private LbDao lbDao;
	
	public void setLbDao(LbDao lbDao) {
		this.lbDao = lbDao;
	}

	@Override
	public void updateSize(List<UpdateVo> updateVos) throws BusinessException {
		lbDao.updateLines(updateVos);
	}

}
