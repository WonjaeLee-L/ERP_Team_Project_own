package com.example.practice.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pagevo {
	private int startNo;
	private int endNo;
	private int perPageNum=10;
	private Integer page; // Integer 웹에서 받은 페이지 정보(String)가 없으면 null인데 int는 null을 저장할 수 없다. 오류방지
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
		calcPage();// totalCount 전제게시물개수가 있어야지 페이지계산을 할 수 있기 때문에
	}


}
