package com.sptd.eyun.obj;

import com.sptd.eyun.objfather.BaseEntity;


/**
 * 
* @ClassName: Parameter 
* @Description: 参数定义表
* @author 曹文波
* @date 2014年10月11日 上午10:47:49 
*
 */
//@Entity
//@Table(name = "tb_xa_parameter")
//@ApiModel(value="参数定义表")
public class Parameter extends BaseEntity{
	
//		@ApiModelProperty(value="参数名称,参数名称")
	private String name;
//		@ApiModelProperty(value="设备类ID,设备类ID")
	private Long deviceTypeId;
//		@ApiModelProperty(value="数据长度,数据长度")
	private Integer length;
//		@ApiModelProperty(value="单位,单位")
	private String unit;
//		@ApiModelProperty(value="存储周期,存储周期(0：不保存，1：每日，2：每周，3：每月，4：最新)")
	private Integer storageCycle;
//		@ApiModelProperty(value="数据,用于存数据表数据")
		private String data;
	
//		@Column(nullable=false,length=50)
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
		 
//		@Column(nullable=true,length=50)
	public Integer getLength(){
		return length;
	}
	
	public void setLength(Integer length){
		this.length=length;
	}
//		@Column(nullable=true,length=50)
	public String getUnit(){
		return unit;
	}
	
	public void setUnit(String unit){
		this.unit=unit;
	}
//		@Column(nullable=true,length=50)
	public Integer getStorageCycle(){
		return storageCycle;
	}
	
	public void setStorageCycle(Integer storageCycle){
		this.storageCycle=storageCycle;
	}
	
//	@Column(nullable=true,length=50)
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	
//	@Column(nullable=true,length=50)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
		
	

}
