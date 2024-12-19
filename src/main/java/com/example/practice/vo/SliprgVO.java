package com.example.practice.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class SliprgVO {
    int rgCode = 0;
    String rgslipCode = null;
    String rgName = null;
    Date rgDate = null;
    String rgslipName = null;
    String rgCmpy = null;
    int rgPrice = 0;
    int rgFee = 0;
    String rgPay = null;
    String rgComment = null;

}
