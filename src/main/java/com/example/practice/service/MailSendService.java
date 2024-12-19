package com.example.practice.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailSendService {
    private final JavaMailSender mailSender;
    private final LoginServiceImpl loginService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPdfFromImage(List<String> base64Images) throws MessagingException, IOException {
        // PDF 생성
        File pdfFile = new File("IncomeStatement.pdf");
        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        for (String base64Image : base64Images) {
            // Base64 이미지 데이터에서 헤더 제거
            String base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 이미지를 PDF에 추가
            ImageData imageData = ImageDataFactory.create(imageBytes);
            Image image = new Image(imageData);

            // 이미지 크기를 페이지에 맞게 조정
            float pageWidth = pdf.getDefaultPageSize().getWidth() - 50;
            float scale = pageWidth / image.getImageWidth();
            image.scale(scale, scale);

            document.add(image);

            // 마지막 페이지가 아니면 새 페이지 추가
            if (base64Images.indexOf(base64Image) < base64Images.size() - 1) {
                document.add(new AreaBreak());
            }
        }

        document.close();

        // 현재 인증된 사용자의 erpId 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String erpId = auth.getName();

        // erpId로 사용자 이메일 조회
        String userEmail = loginService.findEmail(erpId);
        if(userEmail == null) {
            throw new RuntimeException("사용자 이메일을 찾을 수 없습니다.");
        }

        // 이메일 전송
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(userEmail);
        helper.setSubject("손익계산서");
        helper.setText("휴먼ERP 손익계산서", true);
        helper.addAttachment("IncomeStatement.pdf", pdfFile);

        mailSender.send(mimeMessage);
        pdfFile.delete();
    }
}