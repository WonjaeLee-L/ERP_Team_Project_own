package com.example.practice.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberVO {
	String erpId;
	String erpPass;
	String erpName;
	String erpTel;
	String erpEmail;
	String erpAddress;
	String erpCompanyCode;
	String erpDepartmentCode;
	String erpRole;
}
