package com.sptd.eyun.obj;

import com.sptd.eyun.objfather.BaseEntity;



/**
 * 
 * @ClassName: Picture
 * @Description: 图片定义表
 * @author 曹文波
 * @date 2014年10月11日 上午10:47:49
 *
 */
//@Entity
//@Table(name = "tb_xa_picture")
//@ApiModel(value = "图片定义表")
public class Picture extends BaseEntity {

//	@ApiModelProperty(value = "文件种类，1表示图片，2表示PDF")
	private Integer fileType;
//	@ApiModelProperty(value = "文件类型，具体见常量类中的说明")
	private Integer type;
//	@ApiModelProperty(value = "关联内容的ID")
	private Long contentId;
//	@ApiModelProperty(value = "图片相对路径,图片相对路径")
	private String url;
//	@ApiModelProperty(value = "图片缩略图路径")
	private String iconUrl;
//	@ApiModelProperty(value = "上传文件名")
	private String fileName;
//	@ApiModelProperty(value = "显示文件名")
	private String showName;
//	@ApiModelProperty(value = "文件大小")
	private Long size;
//	@ApiModelProperty(value = "首页显示")
	private Integer isShow;
	
	public void setType(Integer type) {
		this.type = type;
	}

//	@Column(nullable = false)
	public Integer getType() {
		return type;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

//	@Column(nullable = true)
	public Long getContentId() {
		return contentId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

//	@Column(nullable = false, length = 200)
	public String getUrl() {
		return url;
	}

//	@Column(nullable = true, length = 200)
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

//	@Column(nullable = true, length = 200)
	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

//	@Column(nullable = true, length = 200)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
//	@Column(nullable = true, length = 200)
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

//	@Column(nullable = true, length = 20)
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
