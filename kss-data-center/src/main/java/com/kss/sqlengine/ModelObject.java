/**
 * 
 */
package com.kss.sqlengine;

public class ModelObject {
	//增加
	public static final String ACTION_ADD = "ADD";
	//修改
	public static final String ACTION_MOD = "MOD";  
	//删除
	public static final String ACTION_DEL = "DEL";  	
	public static final String OBJECT_STATE_VALID = "A";
	public static final String OBJECT_STATE_INVALID = "X";
	
	private String objActionType;

	public void setObjActionType(String objActionType) {
		this.objActionType = objActionType;
	}

	public String getObjActionType() {
		return objActionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((objActionType == null) ? 0 : objActionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		ModelObject other = (ModelObject) obj;
		if (objActionType == null) {
			if (other.objActionType != null){
				return false;
			}
		} else if (!objActionType.equals(other.objActionType)){
			return false;
		}
		return true;
	}
}
