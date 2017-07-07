package com.seeyon.apps.goldwind.trlaobao.vo;

/**
 * 劳改更新时记录需要
 * @author ML
 *
 */
public class UpdateVo {
	/**
	 * 人员编码
	 */
	private String userCode;
	/**
	 * 劳改编码
	 */
	private String lbCode;
	/**
	 * 劳改尺寸
	 */
	private String lbSize;
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getLbCode() {
		return lbCode;
	}
	public void setLbCode(String lbCode) {
		this.lbCode = lbCode;
	}
	public String getLbSize() {
		return lbSize;
	}
	public void setLbSize(String lbSize) {
		this.lbSize = lbSize;
	}
	@Override
	public String toString() {
		return "UpdateVo [userCode=" + userCode + ", lbCode=" + lbCode
				+ ", lbSize=" + lbSize + "]";
	}
	
	
	
}
