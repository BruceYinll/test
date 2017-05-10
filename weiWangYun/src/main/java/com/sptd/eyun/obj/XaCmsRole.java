package com.sptd.eyun.obj;



import java.io.Serializable;


/**
 * 系统角色实体.
 */
//@Entity
//@Table(name="tb_ms_cms_role")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XaCmsRole implements Serializable{
	
	private static final long serialVersionUID = 700235310223571456L;

	
	/** 角色Id. */
	private Long roleId;
	
	/** 角色名称. */
	private String roleName;
	
	/** 角色描述 */
	private String roleDesc;
	
	/** 状态. */
	private int status;
	
	//创建人
	private Long userId;
	
	//创建人用户名
	private String userName;
	
	private String createTime;
	
	private String updateTime;
	

//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="roleId")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

//	@Column(name="role_name", nullable=false, length=40)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

//	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

//	@Column(name="role_desc")
	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

//	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
