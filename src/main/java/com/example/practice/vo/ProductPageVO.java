package com.example.practice.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductPageVO {
    private int startNo;
    private int endNo;
    private int perPageNum = 10;
    private Integer page;

    @Setter(AccessLevel.PROTECTED)
    private int totalCount;
    private int endPage;
    private int startPage;
    private boolean prev;
    private boolean next;
    private String searchType;
    private String searchKeyword;

    private void calcPage() {
        // DB쿼리에서 사용... 시작데이터번호 = (jsp클릭한 페이지번호-1)*페이지당 보여지는 개수
        startNo = (this.page - 1) * perPageNum + 1; // 6페이지 일 경우 시작글번호가 50부터..
        // page변수는 현재 jsp에서 클릭한 페이지번호
        int tempEnd = (int) (Math.ceil(page / (double) this.perPageNum) * this.perPageNum);
        // 6/10=0.6 > 1 10
        // 11/10=1.1 > 2 20
        // ceil함수는 천장 함수로 1.1 = 2, 2.1 = 3 으로 출력된다.
        // 반대되는 바닥함수로 floor(), 반올림 함수로 round()가 있다.
        // jsp에서 클릭한 페이지번호를 기준으로 끝 페이지를 계산한다.
        this.startPage = (tempEnd - this.perPageNum) + 1;
        // 6 10 - 10 + 1 >> 1
        // 11 20 - 10 + 1 >> 11
        // 시작 페이지 계산 클릭한page번호 10일때 까지 시작페이지는 1
        if (tempEnd * this.perPageNum > this.totalCount) {
            // 클릭한 page번호로 계산된 게시물수가 실제게시물개수 totalCount 클때
            this.endPage = (int) Math.ceil(this.totalCount / (double) this.perPageNum);
            // this.endNo = startNo + this.perPageNum - 1;
            if (endPage != page) {
                this.endNo = startNo + this.perPageNum - 1;
            } else {
                this.endNo = startNo + this.totalCount % perPageNum - 1;
            }
        } else {
            // 클릭한 page번호로 계산된 게시물수가 실제게시물개수 totalCount 작을때
            this.endPage = tempEnd;
            this.endNo = startNo + this.perPageNum - 1;
            // this.endNo = startNo + this.totalCount % 10 - 1;
        }

        this.prev = this.startPage != 1;// 시작페이지 1보다 크면 무조건 이전 페이지가 있음. true
        this.next = this.endPage * this.perPageNum < this.totalCount;
        // 클릭한 page번호로 계산된 게시물수가 실제 게시물 개수보다 작다면 다음페이지가 있음. true
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcPage();
    }

}
