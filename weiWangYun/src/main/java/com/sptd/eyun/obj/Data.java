package com.sptd.eyun.obj;

import com.sptd.eyun.objfather.BaseEntity;




/**
 * 
* @ClassName: Data 
* @Description: 数据定义表
* @author 曹文波
* @date 2014年10月11日 上午10:47:49 
*
 */
//@Entity
//@Table(name = "tb_xa_data")
//@ApiModel(value="数据定义表")
public class Data extends BaseEntity{
	
//		@ApiModelProperty(value="参数ID,参数ID")
	private Long parameterId;
//		@ApiModelProperty(value="参数名,参数名")
	private String parameterName;
//		@ApiModelProperty(value="实时数据,实时数据")
	private String data;
//		@ApiModelProperty(value="设备ID,设备ID")
	private Long deviceId;
		
		/** 参数对象 **/
		private Parameter parameter;
	
//		@Column(nullable=false,length=50)
	public Long getParameterId(){
		return parameterId;
	}
	
	public void setParameterId(Long parameterId){
		this.parameterId=parameterId;
	}
//		@Column(nullable=true,length=50)
	public String getParameterName(){
		return parameterName;
	}
	
	public void setParameterName(String parameterName){
		this.parameterName=parameterName;
	}
//		@Column(nullable=false,length=50)
	public String getData(){
		return data;
	}
	
	public void setData(String data){
		this.data=data;
	}
//		@Column(nullable=false,length=50)
	public Long getDeviceId(){
		return deviceId;
	}
	
	public void setDeviceId(Long deviceId){
		this.deviceId=deviceId;
	}
	
//	@ManyToOne(cascade = { CascadeType.REFRESH })
//	@JoinColumn(name = "parameterId", referencedColumnName = "id", insertable = false, updatable = false)
//	@NotFound(action = NotFoundAction.IGNORE)
	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
		
	

}
