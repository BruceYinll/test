package com.sptd.eyun.obj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sptd.eyun.objfather.BaseEntity;

/**
 * 
 * @ClassName: Reply
 * @Description: 回复定义表
 * @author 曹文波
 * @date 2014年10月11日 上午10:47:49
 *
 */
// @Entity
// @Table(name = "tb_xa_reply")
// @ApiModel(value="回复定义表")
public class Reply extends BaseEntity {

	// @ApiModelProperty(value="回复内容,回复内容")
	private String content;
	// @ApiModelProperty(value="维护ID,维护ID(对问题的回复)")
	private Long questionId;
	// @ApiModelProperty(value="回复ID,回复ID（对回复的回复）")
	private Long replyId;

	// @ApiModelProperty(value="关联视频")
	private String video;

	// @ApiModelProperty(value="关联图片")
	private String photos;

	/** 用户对象 **/
	private UserObj user;

	/** 图片对象 **/
	private List<Picture> pictureList = new ArrayList<Picture>();

	// private List<Reply> replyes = new ArrayList<Reply>();
	private Reply reply;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}

	public String getVideo() {
		return video;
	}

	private File videoFile;

	public File getVideoFile() {
		return videoFile;
	}

	public void setVideoFile(File videoFile) {
		this.videoFile = videoFile;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public UserObj getUser() {
		return user;
	}

	public void setUser(UserObj user) {
		this.user = user;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	public List<Picture> getPictureList() {
		return pictureList;
	}

	public void setPictureList(List<Picture> pictureList) {
		this.pictureList = pictureList;
	}

}
