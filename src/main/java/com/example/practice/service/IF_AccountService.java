package com.example.practice.service;

import com.example.practice.vo.DateRangeVO;
import com.example.practice.vo.Pagevo;
import com.example.practice.vo.SlipVO;
import com.example.practice.vo.SliprgVO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface IF_AccountService {
	//서비스 작업을 메서드로 정의 합니다.
	//컨트롤러가 조인 작업을 요청한다. 이때 매개변수로 객체의 주소를 전달.
//	public RevenueVO selectOne(String revenueCode) throws Exception;

	List<SlipVO> selectAll(Pagevo pagevo) throws Exception;

	int totalCountPV() throws Exception;

	void psInsert(SlipVO slipvo) throws Exception;

	List<SlipVO> selectpvNameCmpy(Pagevo pagevo, String searchKeyword, String searchText) throws Exception;
	int countpvNameCmpy(String searchKeyword, String searchText) throws Exception;
	List<SliprgVO> selectrgNameCmpy(Pagevo pagevo, String searchKeyword, String searchText) throws Exception;
	int countrgNameCmpy(String searchKeyword, String searchText) throws Exception;
//	List<SlipVO> selectpvName(Pagevo pagevo, String pvName) throws Exception;
//	List<SlipVO> selectpvCmpy(Pagevo pagevo, String pvCmpy) throws Exception;

	List<SliprgVO> selectAllrgForExcel(String searchKeyword, String searchText) throws Exception;

//	int countpvName(String pvName) throws Exception;
//	int countpvCmpy(String pvCmpy) throws Exception;

	List<SlipVO> selectByType(Pagevo pagevo, String pvslipCode) throws Exception;
	int countByType(String pvslipCode) throws Exception;

	void delpvSlip(int pvCode) throws Exception;
	void rgInsert(SlipVO slipvo) throws Exception;
	List<SliprgVO> selectrgAll(Pagevo pagevo) throws Exception;
	int totalCountRG() throws Exception;
//	List<SliprgVO> selectrgName(Pagevo pagevo, String rgName) throws Exception;
//	int countrgName(String rgName) throws Exception;
//	List<SliprgVO> selectrgCmpy(Pagevo pagevo, String rgCmpy) throws Exception;
//	int countrgCmpy(String rgCmpy) throws Exception;
	List<SliprgVO> selectByrgType(Pagevo pagevo, String rgslipCode);
	int countByrgType(String rgslipCode) throws Exception;

	int productSales(DateRangeVO dvo) throws Exception;

	int serviceSales(DateRangeVO dvo) throws Exception;

	int otherSales(DateRangeVO dvo) throws Exception;

	int interestSales(DateRangeVO dvo) throws Exception;

	int supplyCost(DateRangeVO dvo) throws Exception;

	int shippingCost(DateRangeVO dvo) throws Exception;

	int salaryCost(DateRangeVO dvo) throws Exception;

	int wageCost(DateRangeVO dvo) throws Exception;

	int mechanicalCost(DateRangeVO dvo) throws Exception;

	int inventoryCost(DateRangeVO dvo) throws Exception;

	int marketingCost(DateRangeVO dvo) throws Exception;

	int printCost(DateRangeVO dvo) throws Exception;

	int sellingCost(DateRangeVO dvo) throws Exception;

	int maintainCost(DateRangeVO dvo) throws Exception;

	int otherCost(DateRangeVO dvo) throws Exception;

	int depreciationCost(DateRangeVO dvo) throws Exception;

	int stermDebt(DateRangeVO dvo) throws Exception;

	int ltermDebt(DateRangeVO dvo) throws Exception;

	int payableCost(DateRangeVO dvo) throws Exception;

	int payableWage(DateRangeVO dvo) throws Exception;

//	int SCAL(DateRangeVO dvo) throws Exception;

	int sumupVAT(DateRangeVO dvo) throws Exception;

	int grossProfit(DateRangeVO dvo) throws Exception;

	int operatingIncome(DateRangeVO dvo) throws Exception;

	int netIncome(DateRangeVO dvo) throws Exception;

	List<SliprgVO> selectByDate(DateRangeVO dvo) throws Exception;

	public String getEmailBySessionId(HttpSession session);

	String selectName(String erpId);
}
