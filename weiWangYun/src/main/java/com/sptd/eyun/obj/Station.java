package com.sptd.eyun.obj;



import java.util.List;

import com.sptd.eyun.objfather.BaseEntity;


/**
 * 
 * @ClassName: Station
 * @Description: 电站定义表
 * @author 曹文波
 * @date 2014年10月11日 上午10:47:49
 *
 */
//@Entity
//@Table(name = "tb_xa_station")
//@ApiModel(value = "电站定义表")
public class Station extends BaseEntity {

//	@ApiModelProperty(value = "站名称,站名称")
	private String name;
//	@ApiModelProperty(value = "站类型,站类型")
	private String kind;
 
//	@ApiModelProperty(value = "地理位置,地理位置")
	private String position;
//	@ApiModelProperty(value = "经度")
	private String positionx;
//	@ApiModelProperty(value = "纬度")
	private String positiony;
//	@ApiModelProperty(value = "可再生能源容量,可再生能源容量")
	private String renewableCapacity;
//	@ApiModelProperty(value = "传统能源容量,传统能源容量")
	private String traditionalCapacity;
//	@ApiModelProperty(value = "储能容量,储能容量")
	private String storageCapacity;
//	@ApiModelProperty(value = "负荷容量,负荷容量")
	private String loadingCapacity;
//	@ApiModelProperty(value = "并网日期,并网日期")
	private String gridDate;
//	@ApiModelProperty(value = "详细介绍,详细介绍")
	private String description;
//	@ApiModelProperty(value = "缩略图,缩略图")
	private String picture;
//	@ApiModelProperty(value = "发电总量")
	private String capacity;
//	@ApiModelProperty(value = "发电总功率")
	private String power;
//	@ApiModelProperty(value = "风速")
	private String wind;
//	@ApiModelProperty(value = "温度")
	private String temperature;
//	@ApiModelProperty(value = "天气信息")
	private String weather;
	
//	@ApiModelProperty(value = "所在省")
	private String province;
//	@ApiModelProperty(value = "所在市")
	private String city;
	
//	@ApiModelProperty(value = "GUID")
	private String guid;

//	@ApiModelProperty(value="设备类列表")
	private List<DeviceType> deviceTypeList;
	
	 
	
	
//	@Column(nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@Column(nullable = true, length = 50)
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	 

//	@Column(nullable = true, length = 50)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

//	@Column(nullable = true, length = 50)
	public String getRenewableCapacity() {
		return renewableCapacity;
	}

	public void setRenewableCapacity(String renewableCapacity) {
		this.renewableCapacity = renewableCapacity;
	}

//	@Column(nullable = true, length = 50)
	public String getTraditionalCapacity() {
		return traditionalCapacity;
	}

	public void setTraditionalCapacity(String traditionalCapacity) {
		this.traditionalCapacity = traditionalCapacity;
	}

//	@Column(nullable = true, length = 50)
	public String getStorageCapacity() {
		return storageCapacity;
	}

	public void setStorageCapacity(String storageCapacity) {
		this.storageCapacity = storageCapacity;
	}

//	@Column(nullable = true, length = 50)
	public String getLoadingCapacity() {
		return loadingCapacity;
	}

	public void setLoadingCapacity(String loadingCapacity) {
		this.loadingCapacity = loadingCapacity;
	}

//	@Column(nullable = true, length = 50)
	public String getGridDate() {
		return gridDate;
	}

	public void setGridDate(String gridDate) {
		this.gridDate = gridDate;
	}

//	@Column(nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	@Column(nullable = true, length = 50)
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
//	@Column(nullable = true, length = 20)
	public String getPositionx() {
		return positionx;
	}

	public void setPositionx(String positionx) {
		this.positionx = positionx;
	}
	
//	@Column(nullable = true, length = 20)
	public String getPositiony() {
		return positiony;
	}

	public void setPositiony(String positiony) {
		this.positiony = positiony;
	}
	
//	@Column(nullable = true, length = 50)
	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	
//	@Column(nullable = true, length = 50)
	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}
	
//	@Column(nullable = true, length = 50)
	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}
	
//	@Column(nullable = true, length = 50)
	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
//	@Column(nullable = true, length = 50)
	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}
	
//	@Column(nullable = true, length = 20)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
//	@Column(nullable = true, length = 20)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
//	@Column(nullable = true, length = 50)
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	
//	@OneToMany(cascade= {CascadeType.ALL},fetch=FetchType.LAZY)
//	@JoinColumn(name = "stationId", referencedColumnName = "id", insertable = false, updatable = false)
	public List<DeviceType> getDeviceTypeList() {
		return deviceTypeList;
	} 

	public void setDeviceTypeList(List<DeviceType> deviceTypeList) {
		this.deviceTypeList = deviceTypeList;
	}
	

}
