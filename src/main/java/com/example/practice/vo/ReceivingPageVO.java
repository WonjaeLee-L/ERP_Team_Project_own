package com.example.practice.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReceivingPageVO {
    private int startNo;
    private int endNo;
    private int perPageNum=10;
    private Integer page;
    private int totalCount;
    private int endPage;
    private int startPage;
    private boolean prev;
    private boolean next;
    // 검색용 변수 2개 추가
    private String searchType;
    private String searchKeyword;

    private void calcPage() {
        // totalCount가 0일 때의 처리 추가
        if (totalCount == 0) {
            this.startNo = 0;
            this.endNo = 0;
            this.startPage = 1;
            this.endPage = 1;
            this.prev = false;
            this.next = false;
            return;
        }

        startNo = (this.page - 1) * perPageNum + 1;

        int totalPageCount = (int) Math.ceil((double) totalCount / perPageNum);
        int tempEnd = (int) Math.ceil((double) page / perPageNum) * perPageNum;

        this.startPage = (tempEnd - perPageNum) + 1;
        this.endPage = Math.min(tempEnd, totalPageCount);

        this.endNo = startNo + perPageNum - 1;
        if (this.endNo > totalCount) {
            this.endNo = totalCount;
        }

        this.prev = this.startPage > 1;
        this.next = this.endPage < totalPageCount;
    }


    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcPage();
    }


}
