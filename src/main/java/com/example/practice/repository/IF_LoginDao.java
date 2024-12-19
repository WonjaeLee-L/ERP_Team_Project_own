package com.example.practice.repository;

import com.example.practice.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IF_LoginDao {
    MemberVO selectOne(String erpId);
    String findEmail(String erpId);
}
