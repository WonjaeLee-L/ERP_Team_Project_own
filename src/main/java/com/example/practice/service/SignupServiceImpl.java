package com.example.practice.service;

import com.example.practice.repository.IF_SignupDao;
import com.example.practice.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements IF_SignupService {
    private final IF_SignupDao signupDao;

    @Override
    @Transactional
    public int insertOne(MemberVO member) {
        try {
            return signupDao.insertOne(member);
        } catch (Exception e) {
            // 로그 처리
            throw new RuntimeException("회원가입 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int checkId(String erpId) {
        try {
            return signupDao.checkId(erpId);
        } catch (Exception e) {
            // 로그 처리
            throw new RuntimeException("ID 중복 체크 중 오류가 발생했습니다.", e);
        }
    }
}
