package com.example.practice.service;

import com.example.practice.repository.IF_LoginDao;
import com.example.practice.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements IF_LoginService {

    private final IF_LoginDao loginDao;

    @Override
    @Transactional(readOnly = true)
    public MemberVO selectOne(String erpId) {
        return loginDao.selectOne(erpId);
    }

    @Override
    public String findEmail(String erpId) {
        return loginDao.findEmail(erpId);
    }


}