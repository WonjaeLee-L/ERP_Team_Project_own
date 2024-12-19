package com.example.practice.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductPageVO {
    private int startNo;
    private int endNo;
    private Integer perPageNum = 10; // 페이지당 게시물 수
    private Integer page = 1;    // 기본 페이지 번호
    private int totalCount;      // 총 게시물 수
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    public int getPerPageNum() {
        return perPageNum;
    }

    public ProductPageVO() {
        this.page = 1;
        this.startNo = 0;
        this.endNo = 0;
        this.perPageNum = 10;
    }


    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcPage();
    }

    public void calcPage() {
        int tempEnd = (int) Math.ceil(page / (double) perPageNum) * perPageNum;
        this.startPage = (tempEnd - perPageNum) + 1;

        if (tempEnd * perPageNum < totalCount) {
            this.endPage = tempEnd;
        } else {
            this.endPage = (int) Math.ceil(totalCount / (double) perPageNum);
        }

        this.prev = startPage > 1;
        this.next = endPage * perPageNum < totalCount;

        this.startNo = (page - 1) * perPageNum + 1;
        this.endNo = Math.min(startNo + perPageNum - 1, totalCount);
    }
}
