package com.example.practice.vo;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ProductVO {

    int num;
    @Pattern(regexp = "[0-9]{4}", message = "숫자(4자리) 입력")
    String product_code;
    String product_name;
    @Pattern(regexp = "[0-9]{0,7}", message = "숫자(7자리 이하) 입력")
    int sale_price;
    @Pattern(regexp = "[0-9]{0,7}", message = "숫자(7자리 이하) 입력")
    int price;
    @Pattern(regexp = "[0-9]{4}", message = "숫자(4자리) 입력")
    String category_code;
    String product_img;
    String product_explain;
    @Pattern(regexp = "[0-9]{4}", message = "숫자(4자리) 입력")
    String company_code;


//    public String getThumbnail() {
//        return URLEncoder.encode(product_img, StandardCharsets.UTF_8);
//    }

}
