package com.sptd.eyun.obj;

import com.sptd.eyun.objfather.BaseEntity;



/**
 * 
* @ClassName: Version 
* @Description: 版本控制定义表
* @author 曹文波
* @date 2014年10月11日 上午10:47:49 
*
 */
//@Entity
//@Table(name = "tb_xa_version")
//@ApiModel(value="版本控制定义表")
public class Version extends BaseEntity{
	
//		@ApiModelProperty(value="编号,1.0")
	private String number;
//		@ApiModelProperty(value="版本名称,V 1.0123")
	private String name;
//		@ApiModelProperty(value="适用平台,android")
	private String platform;
//		@ApiModelProperty(value="下载方式,URL")
	private String download;
//		@ApiModelProperty(value="版本更新地址,http://www.163.com")
	private String url;
//		@ApiModelProperty(value="最新,是")
	private Integer isNew;
//		@ApiModelProperty(value="版本描述,测试中")
	private String description;
//		@ApiModelProperty(value="安装吧,/upload/apk/sjm.apk")
	private String apk;
		
	
//		@Column(nullable=true,length=50)
	public String getNumber(){
		return number;
	}
	
	public void setNumber(String number){
		this.number=number;
	}
//		@Column(nullable=true,length=50)
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
//		@Column(nullable=true,length=50)
	public String getPlatform(){
		return platform;
	}
	
	public void setPlatform(String platform){
		this.platform=platform;
	}
//		@Column(nullable=true,length=50)
	public String getDownload(){
		return download;
	}
	
	public void setDownload(String download){
		this.download=download;
	}
//		@Column(nullable=true,length=200)
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url=url;
	}
//		@Column(nullable=true,length=10)
	public Integer getIsNew(){
		return isNew;
	}
	
	public void setIsNew(Integer isNew){
		this.isNew=isNew;
	}
//		@Column(nullable=true,length=500)
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
//		@Column(nullable=true,length=100)
	public String getApk(){
		return apk;
	}
	
	public void setApk(String apk){
		this.apk=apk;
	}
		

}
