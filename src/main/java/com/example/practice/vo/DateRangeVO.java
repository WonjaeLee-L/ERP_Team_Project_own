package com.example.practice.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;


@Getter
@Setter
@ToString
public class DateRangeVO {
    private Date startDate;
    private Date endDate;
}
