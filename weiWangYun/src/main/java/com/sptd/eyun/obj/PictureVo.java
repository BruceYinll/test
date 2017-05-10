package com.sptd.eyun.obj;

/**
 * @ClassName: PictureVo
 * @Description:图片 Vo
 * @author hchen
 * @date 2014年8月13日 下午1:40:23
 *
 */
// @ApiModel(value = "图片Vo对象")
public class PictureVo {

	// @ApiModelProperty(value = "主键，自动增长")
	private Long tid;

	// @ApiModelProperty(value = "创建时间")
	private String createTime;
	// @ApiModelProperty(value = "文件种类，1表示图片，2表示PDF")
	private Integer fileType;
	// @ApiModelProperty(value = "图片类型,1：处方图片")
	private Integer type;
	// @ApiModelProperty(value = "关联内容的ID,如果是处方图片，则该ID为处方的ID")
	private Long contentId;
	// @ApiModelProperty(value = "图片相对路径,图片相对路径")
	private String url;
	// @ApiModelProperty(value = "图片缩略图路径")
	private String iconUrl;
	// @ApiModelProperty(value = "上传文件名")
	private String fileName;
	// @ApiModelProperty(value = "显示文件名")
	private String showName;
	// @ApiModelProperty(value = "文件大小")
	private Long size;
	// @ApiModelProperty(value = "首页显示")
	private Integer isShow;

	public PictureVo(Long tid, Integer type, Long contentId, String url) {
		this.tid = tid;
		this.type = type;
		this.contentId = contentId;
		this.url = url;
	}

	public PictureVo() {
		super();
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

}
