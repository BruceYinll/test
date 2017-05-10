package com.sptd.eyun.objfather;

import java.io.Serializable;

/**
 * 用户类型
 * 
 * @author lanyan
 * 
 */
@SuppressWarnings("serial")
public class User implements Serializable{
	
//ApiModelProperty(value="姓名,张三")
private String name;
	//ApiModelProperty(value="手机号码,13728659999")
private String mobile;
	//ApiModelProperty(value="个人简介,小学生")
private String description;
	//ApiModelProperty(value="注册时间,2015-02-01")
private String registerDate;
	//ApiModelProperty(value="登陆次数,10")
private String loginCount;
	//ApiModelProperty(value="头像,upload/sjm.jpg")
private String headPicture;
	//ApiModelProperty(value="账号,账号")
private String account;
	//ApiModelProperty(value="密码,密码")
private String password;
	

	//Column(nullable=true,length=50)
public String getName(){
	return name;
}

public void setName(String name){
	this.name=name;
}
	//Column(nullable=true,length=50)
public String getMobile(){
	return mobile;
}

public void setMobile(String mobile){
	this.mobile=mobile;
}
	//Column(nullable=true,length=500)
public String getDescription(){
	return description;
}

public void setDescription(String description){
	this.description=description;
}
	//Column(nullable=true,length=50)
public String getRegisterDate(){
	return registerDate;
}

public void setRegisterDate(String registerDate){
	this.registerDate=registerDate;
}
	//Column(nullable=true,length=50)
public String getLoginCount(){
	return loginCount;
}

public void setLoginCount(String loginCount){
	this.loginCount=loginCount;
}
	//Column(nullable=true,length=50)
public String getHeadPicture(){
	return headPicture;
}

public void setHeadPicture(String headPicture){
	this.headPicture=headPicture;
}
	//Column(nullable=true,length=50)
public String getAccount(){
	return account;
}

public void setAccount(String account){
	this.account=account;
}
	//Column(nullable=true,length=50)
public String getPassword(){
	return password;
}

public void setPassword(String password){
	this.password=password;
}
	

}