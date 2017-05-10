package com.sptd.eyun.obj;

/**
 * 整个数据返回的类型
 * 
 * @author lanyan
 * 
 * @param <T>
 *            result字段的类型
	 */
public class BaseModel<T> {
	protected String message;// 错误信息
	protected int code;// 错误码
	protected int infCode;// 接口变量
	protected T object;// 结果
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getInfCode() {
		return infCode;
	}
	public void setInfCode(int infCode) {
		this.infCode = infCode;
	}
	public T getObject() {
		return object;
	}
	public void setObject(T object) {
		this.object = object;
	}


}
