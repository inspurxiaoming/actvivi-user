package com.chengym.activity.common.bean;

import lombok.Data;

import java.util.Set;

/**
 * user Message got from the token
 */
@Data
public class User {

	private String id;//内码、唯一标识
	private String name;//loginName
	private String displayName;//familyName+givenName
	private String email;
	public Set<String> roles;
	private String givenName;
	private String familyName;


}
