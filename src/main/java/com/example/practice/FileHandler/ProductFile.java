package com.example.practice.FileHandler;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

@Component
public class ProductFile {
    public String saveFile(MultipartFile file) throws Exception, AssertionError {

        String uploadDir = System.getProperty("user.dir") + "/external/productImg/";
        String originName;
        String fileName;

        originName = file.getOriginalFilename();
        String[] parts = originName.split("\\.");
        if (parts.length > 1) {
            fileName = UUID.randomUUID().toString() + "." + parts[parts.length - 1];
        } else {
            fileName = UUID.randomUUID().toString();
        }
        File target = getFile(uploadDir, fileName);
        FileCopyUtils.copy(file.getBytes(), target);

        return "/productImg/" + fileName;
    }

    public File getFile(String uploadDir, String fileName) {
        File target = new File(uploadDir, fileName);

        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        return target;
    }

}
