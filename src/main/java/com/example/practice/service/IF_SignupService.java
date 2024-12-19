package com.example.practice.service;

import com.example.practice.vo.MemberVO;

public interface IF_SignupService {
    public int insertOne(MemberVO member);
    public int checkId(String erpId);
}
