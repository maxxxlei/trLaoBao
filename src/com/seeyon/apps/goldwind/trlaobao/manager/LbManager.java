package com.seeyon.apps.goldwind.trlaobao.manager;

import java.util.List;

import com.seeyon.apps.goldwind.trlaobao.vo.UpdateVo;
import com.seeyon.ctp.common.exceptions.BusinessException;

public interface LbManager {
	public void updateSize(List<UpdateVo> updateVos) throws BusinessException;
}
