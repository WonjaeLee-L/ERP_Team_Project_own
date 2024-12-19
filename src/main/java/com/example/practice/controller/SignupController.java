package com.example.practice.controller;

import com.example.practice.service.IF_SignupService;
import com.example.practice.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final IF_SignupService signupService;
    private final PasswordEncoder passwordEncoder;  // PasswordEncoder 주입

    @PostMapping("/loginSignup")
    public ResponseEntity<Map<String, Object>> signup(@ModelAttribute MemberVO member) {
        Map<String, Object> result = new HashMap<>();

        try {
            String encodedPassword = passwordEncoder.encode(member.getErpPass());
            member.setErpPass(encodedPassword);

            member.setErpRole("user");

            int insertResult = signupService.insertOne(member);

            if (insertResult > 0) {
                result.put("status", "success");
                result.put("message", "회원가입이 완료되었습니다.");
                return ResponseEntity.ok(result);
            } else {
                result.put("status", "fail");
                result.put("message", "회원가입에 실패했습니다.");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @GetMapping("/loginCheckId")
    public ResponseEntity<Map<String, Object>> checkId(@RequestParam String id) {
        Map<String, Object> result = new HashMap<>();

        try {
            int count = signupService.checkId(id);

            result.put("status", "success");
            result.put("code", count);
            result.put("message", count > 0 ? "이미 사용중인 아이디입니다." : "사용 가능한 아이디입니다.");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
}