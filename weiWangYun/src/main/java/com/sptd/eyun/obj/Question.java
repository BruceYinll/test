package com.sptd.eyun.obj;

import java.io.File;
import java.util.List;

import com.sptd.eyun.objfather.BaseEntity;

/**
 * 
 * @ClassName: Question
 * @Description: 运维定义表
 * @author 曹文波
 * @date 2014年10月11日 上午10:47:49
 *
 */
// @Entity
// @Table(name = "tb_xa_question")
// @ApiModel(value="运维定义表")
public class Question extends BaseEntity {

	// @ApiModelProperty(value="标题,标题")
	private String name;
	// @ApiModelProperty(value="描述,描述")
	private String description;
	// @ApiModelProperty(value="电站ID,3")
	private Long stationId;
	// @ApiModelProperty(value="设备ID,2")
	private Long deviceId;
	// @ApiModelProperty(value="设备类ID,1")
	private Long deviceTypeId;
	// @ApiModelProperty(value="电站名称,闵行区电站")
	private String stationName;
	// @ApiModelProperty(value="设备类名称,风类")
	private String deviceTypeName;
	// @ApiModelProperty(value="设备名称,hp 200打印机")
	private String deviceName;
	// @ApiModelProperty(value="状态,已经解决")
	private Integer state;
	// @ApiModelProperty(value="是否发布,发布")
	private Integer publish;
	private String video;

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
	private File videoFile;

	public File getVideoFile() {
		return videoFile;
	}

	public void setVideoFile(File videoFile) {
		this.videoFile = videoFile;
	}
	// @ApiModelProperty(value="关联图片")
	private String photos;

	/** 电站对象 **/
	private Station station;

	/** 设备类对象 **/
	private DeviceType deviceType;

	/** 设备对象 **/
	private Device device;

	/** 用户对象 **/
	private UserObj user;

	/** 图片对象 **/
	private List<Picture> pictureList;

	// @Column(nullable=false,length=50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// @Column(nullable=true,length=50)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// @Column(nullable=true,length=50)
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	// @Column(nullable=true,length=50)
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	// @Column(nullable=true,length=50)
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	// @Column(nullable=true,length=50)
	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	// @Column(nullable=true,length=50)
	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	// @Column(nullable=true,length=50)
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	// @Column(nullable=true,length=50)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	// @Column(nullable=true,length=50)
	public Integer getPublish() {
		return publish;
	}

	public void setPublish(Integer publish) {
		this.publish = publish;
	}

	// @Column(nullable=true,length=100)
	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	// @ManyToOne(cascade = { CascadeType.REFRESH })
	// @JoinColumn(name = "stationId", referencedColumnName = "id", insertable =
	// false, updatable = false)
	// @NotFound(action = NotFoundAction.IGNORE)
	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	// @ManyToOne(cascade = { CascadeType.REFRESH })
	// @JoinColumn(name = "deviceTypeId", referencedColumnName = "id",
	// insertable = false, updatable = false)
	// @NotFound(action = NotFoundAction.IGNORE)
	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	// @ManyToOne(cascade = { CascadeType.REFRESH })
	// @JoinColumn(name = "deviceId", referencedColumnName = "id", insertable =
	// false, updatable = false)
	// @NotFound(action = NotFoundAction.IGNORE)
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	// @ManyToOne(cascade = { CascadeType.REFRESH })
	// @JoinColumn(name = "createUser", referencedColumnName = "userId",
	// insertable = false, updatable = false)
	// @NotFound(action = NotFoundAction.IGNORE)
	public UserObj getUser() {
		return user;
	}

	public void setUser(UserObj user) {
		this.user = user;
	}

	// @Transient
	public List<Picture> getPictureList() {
		return pictureList;
	}

	public void setPictureList(List<Picture> pictureList) {
		this.pictureList = pictureList;
	}

}
