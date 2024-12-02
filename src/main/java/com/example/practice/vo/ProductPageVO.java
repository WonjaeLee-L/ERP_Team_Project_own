package com.example.practice.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class ProductPageVO {
    private int startNo; // page의 시작 글 번호
    private int endNo; // page의 마지막 글 번호
    private int perPageNum = 10; // 한 page당 게시글 수
    private Integer page;
    // Integer(String을 저장하지만 int로 인식) : 웹에서 받은 페이지 정보(String)가 없으면 null인데 int는 null을
    // 저장할 수 없다. 오류방지
    // 현재 page

    @Setter(AccessLevel.PROTECTED)
    private int totalCount; // 전체 게시글 수


    private int endPage; // 페이지 그룹의 마지막 페이지 번호
    private int startPage; // 페이지 그룹의 첫 페이지 번호(13페이지라면 11페이지)
    private boolean prev; // 이전
    private boolean next; // 다음
    // 검색용 변수 2개 추가 >> paging을 할 때, 검색한 것을 가지고 페이지를 만들도록. 페이지 번호를 넘길 때, 검색어도 같이 넘겨야
    // 그 뒤 페이지도 제대로 된 paging이 된다.
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
                this.endNo = startNo + this.totalCount % 10 - 1;
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
