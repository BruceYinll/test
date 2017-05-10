package com.sptd.eyun.obj;



import java.util.List;

import com.sptd.eyun.objfather.BaseEntity;



/**
 * 
* @ClassName: Device 
* @Description: 设备定义表
* @author 曹文波
* @date 2014年10月11日 上午10:47:49 
*
 */
//@Entity
//@Table(name = "tb_xa_device")
//@ApiModel(value="设备定义表")
public class Device extends BaseEntity{
	
//		@ApiModelProperty(value="设备名称,设备名称")
	private String name;
//		@ApiModelProperty(value="电站ID,电站ID")
	private Long stationId;
//		@ApiModelProperty(value="设备类ID,设备类ID")
	private Long deviceTypeId;
//		@ApiModelProperty(value="设备描述,设备描述")
	private String description;
		
//		@ApiModelProperty(value="数据列表")
		private List<Data> dataList;
		
//		@ApiModelProperty(value="参数列表")
		private List<Parameter> parameterList;
	
//		@Column(nullable=true,length=50)
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
//		@Column(nullable=false,length=50)
	public Long getStationId(){
		return stationId;
	}
	
	public void setStationId(Long stationId){
		this.stationId=stationId;
	}
//		@Column(nullable=false,length=50)
	public Long getDeviceTypeId(){
		return deviceTypeId;
	}
	
	public void setDeviceTypeId(Long deviceTypeId){
		this.deviceTypeId=deviceTypeId;
	}
//		@Column(nullable=true,length=500)
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
//	 @Transient
	public List<Data> getDataList() {
		return dataList;
	}

	public void setDataList(List<Data> dataList) {
		this.dataList = dataList;
	}
	
//	@Transient
	public List<Parameter> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<Parameter> parameterList) {
		this.parameterList = parameterList;
	}
	
	
		

}
