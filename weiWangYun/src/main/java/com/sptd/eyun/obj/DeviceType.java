package com.sptd.eyun.obj;



import java.util.List;

import com.sptd.eyun.objfather.BaseEntity;


/**
 * 
* @ClassName: DeviceType 
* @Description: 设备类定义表
* @author 曹文波
* @date 2014年10月11日 上午10:47:49 
*
 */
//@Entity
//@Table(name = "tb_xa_devicetype")
//@ApiModel(value="设备类定义表")
public class DeviceType extends BaseEntity{
	
//		@ApiModelProperty(value="类名称,光伏发电类")
	private String name;
//		@ApiModelProperty(value="类描述,类描述")
	private String description;
//		@ApiModelProperty(value="电站ID,1")
	private Long stationId;
//		@ApiModelProperty(value="设备列表")
		private List<Device> deviceList;
		
//		@ApiModelProperty(value="参数列表")
		private List<Parameter> parameterList;
	
//		@Column(nullable=true,length=50)
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
//		@Column(nullable=true,length=500)
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
//		@Column(nullable=true,length=50)
	public Long getStationId(){
		return stationId;
	}
	
	public void setStationId(Long stationId){
		this.stationId=stationId;
	}
	
//	@Transient
	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}
	
//	 @Transient
	public List<Parameter> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<Parameter> parameterList) {
		this.parameterList = parameterList;
	}
		
	

}
