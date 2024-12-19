package com.example.practice.service;

import com.example.practice.vo.MemberVO;

public interface IF_LoginService {
    MemberVO selectOne(String erpId);
    String findEmail(String erpId);
}
