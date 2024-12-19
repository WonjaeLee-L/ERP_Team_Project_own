package com.example.practice.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ProductVO {

    int num;
    @Pattern(regexp = "[0-9]{4}", message = "숫자(4자리) 입력")
    String product_code;
    String product_name;
    @Min(value = 0, message = "0 이상의 숫자만 입력 가능합니다.")
    @Max(value = 9999999, message = "7자리 이하의 숫자만 입력 가능합니다.")
    int sale_price;
    @Min(value = 0, message = "0 이상의 숫자만 입력 가능합니다.")
    @Max(value = 9999999, message = "7자리 이하의 숫자만 입력 가능합니다.")
    int price;
    @Pattern(regexp = "[0-9]{4}", message = "숫자(4자리) 입력")
    String category_code;
    String product_img;
    String product_explain;
    String company_code;
}
