/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.system.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhengxinacc.common.config.BaseBean;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月23日 上午10:51:56
 * @version 1.0
 */
@Document(collection="sys_user")
@Getter
@Setter
@ToString
public class User extends BaseBean implements UserDetails {
	/**
	 * @author eko.zhan at 2017年12月23日 下午3:23:39
	 */
	private static final long serialVersionUID = 110895296317295676L;
	
	@Id
	private String id;
	private String username;
	private String password;
	@JSONField(serialize=false)
	private String salt;
	private UserInfo userInfo;
//	@DBRef
//	private List<Role> roles; //role 中已经引用了 user，如果多对多双向引用会导致死循环，由于role量少，所以采用直接记录
	@Transient
	private List<GrantedAuthority> authorities;

	@JsonIgnore
	@JSONField(serialize=false)
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	@JsonIgnore
	@JSONField(serialize=false)
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}
	
	@JsonIgnore
	@JSONField(serialize=false)
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@JsonIgnore
	@JSONField(serialize=false)
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@JsonIgnore
	@JSONField(serialize=false)
	@Override
	public boolean isEnabled() {
		return false;
	}

}
