package com.sptd.eyun.obj;

import com.sptd.eyun.objfather.BaseEntity;



/**
 * 
 * @ClassName: UserStation
 * @Description: 用户电站定义表
 * @author 曹文波
 * @date 2014年10月11日 上午10:47:49
 *
 */
//@Entity
//@Table(name = "tb_xa_userstation")
//@ApiModel(value = "用户电站定义表")
public class UserStation extends BaseEntity {

//	@ApiModelProperty(value = "用户ID,用户ID")
	private Long userId;
//	@ApiModelProperty(value = "电站ID,电站ID")
	private Long stationId;
 
	/** 电站对象 **/
	private Station station;

//	@Column(nullable = false, length = 50)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

//	@Column(nullable = false, length = 50)
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

//	@ManyToOne(cascade = { CascadeType.REFRESH })
//	@JoinColumn(name = "stationId", referencedColumnName = "id", insertable = false, updatable = false)
//	@NotFound(action = NotFoundAction.IGNORE)
	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

}
