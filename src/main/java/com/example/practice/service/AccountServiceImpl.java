package com.example.practice.service;

import com.example.practice.repository.IF_AccountDao;
import com.example.practice.vo.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service    // 해당 클래스를 객체로 만들어라..
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements IF_AccountService{

	private final IF_AccountDao adao;
	private final JavaMailSender emailSender;

	@Override
	public List<SlipVO> selectAll(Pagevo pagevo) throws Exception {
		List<SlipVO> list = adao.selectAll(pagevo);
		return list;
	}

	@Override
	public int totalCountPV() throws Exception {
		return adao.totalCountPV();
	}

	@Override
	public void psInsert(SlipVO slipvo) throws Exception {
		System.out.println("성공1");
        switch (slipvo.getPvslipCode()) {
            case "sales" -> slipvo.setPvslipCode("매출");
            case "cost" -> slipvo.setPvslipCode("비용");
            case "asset" -> slipvo.setPvslipCode("자산");
            case "liability" -> slipvo.setPvslipCode("부채");
        }
		switch (slipvo.getPvPay()) {
			case "cash" -> slipvo.setPvPay("현금");
			case "card" -> slipvo.setPvPay("카드");
			case "bank" -> slipvo.setPvPay("계좌이체");
			case "note" -> slipvo.setPvPay("어음");
		}
		adao.psInsert(slipvo);
		System.out.println("성공");
	}

	@Override
	public List<SlipVO> selectpvNameCmpy(Pagevo pagevo, String searchKeyword, String searchText) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("startNO", pagevo.getStartNo());
		params.put("endNO", pagevo.getEndNo());
		params.put("searchKeyword", searchKeyword);

		// searchKeyword에 따라 적절한 파라미터 이름으로 맵에 추가
		if ("name".equals(searchKeyword)) {
			params.put("pvName", searchText);
		} else if ("cmpy".equals(searchKeyword)) {
			params.put("pvCmpy", searchText);
		}

		return adao.selectpvNameCmpy(params);
	}

	@Override
	public int countpvNameCmpy(String searchKeyword, String searchText) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("searchKeyword", searchKeyword);

		if ("name".equals(searchKeyword)) {
			params.put("pvName", searchText);
		} else if ("cmpy".equals(searchKeyword)) {
			params.put("pvCmpy", searchText);
		}

		return adao.countpvNameCmpy(params);
	}


	@Override
	public List<SliprgVO> selectrgNameCmpy(Pagevo pagevo, String searchKeyword, String searchText) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("startNO", pagevo.getStartNo());
		params.put("endNO", pagevo.getEndNo());
		params.put("searchKeyword", searchKeyword);

		// searchKeyword에 따라 적절한 파라미터 이름으로 맵에 추가
		if ("name".equals(searchKeyword)) {
			params.put("rgName", searchText);
		} else if ("cmpy".equals(searchKeyword)) {
			params.put("rgCmpy", searchText);
		}

		return adao.selectrgNameCmpy(params);
	}

	@Override
	public int countrgNameCmpy(String searchKeyword, String searchText) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("searchKeyword", searchKeyword);

		if ("name".equals(searchKeyword)) {
			params.put("rgName", searchText);
		} else if ("cmpy".equals(searchKeyword)) {
			params.put("rgCmpy", searchText);
		}

		return adao.countrgNameCmpy(params);
	}
	@Override
	public List<SliprgVO> selectAllrgForExcel(String searchKeyword, String searchText) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("searchKeyword", searchKeyword);

		if ("name".equals(searchKeyword)) {
			params.put("rgName", searchText);
		} else if ("cmpy".equals(searchKeyword)) {
			params.put("rgCmpy", searchText);
		}

		return adao.selectAllrgForExcel(params);
	}


//    	@Override
//	public List<SlipVO> selectpvName(Pagevo pagevo, String pvName) throws Exception {
//		Map<String, Object> params = new HashMap<>();
//		params.put("startNO", pagevo.getStartNo());
//		params.put("endNO", pagevo.getEndNo());
//		params.put("pvName", pvName);
//		return adao.selectpvName(params);
//	}
//
//	@Override
//	public List<SlipVO> selectpvCmpy(Pagevo pagevo, String pvCmpy) throws Exception {
//		Map<String, Object> params = new HashMap<>();
//		params.put("startNO", pagevo.getStartNo());
//		params.put("endNO", pagevo.getEndNo());
//		params.put("pvCmpy", pvCmpy);
//		return adao.selectpvCmpy(params);
//	}

//	@Override
//	public int countpvName(String pvName) throws Exception {
//		return adao.countpvName(pvName);
//	}
//
//	@Override
//	public int countpvCmpy(String pvCmpy) throws Exception {
//		return adao.countpvCmpy(pvCmpy);
//	}

	@Override
	public List<SlipVO> selectByType(Pagevo pagevo, String pvslipCode) throws Exception {
        switch (pvslipCode) {
            case "sales" -> pvslipCode = "매출";
            case "cost" -> pvslipCode = "비용";
            case "asset" -> pvslipCode = "자산";
            case "liability" -> pvslipCode = "부채";
        }
		Map<String, Object> params = new HashMap<>();
		params.put("startNO", pagevo.getStartNo());
		params.put("endNO", pagevo.getEndNo());
		params.put("pvslipCode", pvslipCode);
		return adao.selectByType(params);
	}

	@Override
	public int countByType(String pvslipCode) throws Exception {
		switch (pvslipCode) {
			case "sales" -> pvslipCode = "매출";
			case "cost" -> pvslipCode = "비용";
			case "asset" -> pvslipCode = "자산";
			case "liability" -> pvslipCode = "부채";
		}
		return adao.countByType(pvslipCode);
	}

	@Override
	public void delpvSlip(int pvCode) throws Exception {
		adao.delpvSlip(pvCode);
    }
	public void rgInsert(SlipVO slipvo) throws Exception {
		adao.rgInsert(slipvo);
		System.out.println("전표 승인 대기 -> 전표 관리");
	}

	@Override
	public List<SliprgVO> selectrgAll(Pagevo pagevo) throws Exception {
		List<SliprgVO> list = adao.selectrgAll(pagevo);
		return list;
	}

	@Override
	public int totalCountRG() throws Exception {
		return adao.totalCountRG();
	}

//	@Override
//	public List<SliprgVO> selectrgName(Pagevo pagevo, String rgName) throws Exception {
//		Map<String, Object> params = new HashMap<>();
//		params.put("startNO", pagevo.getStartNo());
//		params.put("endNO", pagevo.getEndNo());
//		params.put("rgName", rgName);
//		return adao.selectrgName(params);
//	}
//
//	@Override
//	public int countrgName(String rgName) throws Exception {
//		return adao.countrgName(rgName);
//	}
//
//	@Override
//	public List<SliprgVO> selectrgCmpy(Pagevo pagevo, String rgCmpy) throws Exception {
//		Map<String, Object> params = new HashMap<>();
//		params.put("startNO", pagevo.getStartNo());
//		params.put("endNO", pagevo.getEndNo());
//		params.put("rgCmpy", rgCmpy);
//		return adao.selectrgCmpy(params);
//	}
//
//	@Override
//	public int countrgCmpy(String rgCmpy) throws Exception {
//		return adao.countrgCmpy(rgCmpy);
//	}
	@Override
	public List<SliprgVO> selectByrgType(Pagevo pagevo, String rgslipCode) {
		switch (rgslipCode) {
			case "sales" -> rgslipCode = "매출";
			case "cost" -> rgslipCode = "비용";
			case "asset" -> rgslipCode = "자산";
			case "liability" -> rgslipCode = "부채";
		}
		Map<String, Object> params = new HashMap<>();
		params.put("startNO", pagevo.getStartNo());
		params.put("endNO", pagevo.getEndNo());
		params.put("rgslipCode", rgslipCode);
		return adao.selectByrgType(params);
	}

	@Override
	public int countByrgType(String rgslipCode) {
		switch (rgslipCode) {
			case "sales" -> rgslipCode = "매출";
			case "cost" -> rgslipCode = "비용";
			case "asset" -> rgslipCode = "자산";
			case "liability" -> rgslipCode = "부채";
		}
		return adao.countByrgType(rgslipCode);
	}
//	@Override
//	public int SCAL(DateRangeVO dvo) throws Exception {
//		return adao.SCAL(dvo);
//	}

	@Override
	public int productSales(DateRangeVO dvo) throws Exception {
		return adao.productSales(dvo);
	}

	@Override
	public int serviceSales(DateRangeVO dvo) throws Exception {
		return adao.serviceSales(dvo);
	}

	@Override
	public int otherSales(DateRangeVO dvo) throws Exception {
		return adao.otherSales(dvo);
	}

	@Override
	public int interestSales(DateRangeVO dvo) throws Exception {
		return adao.interestSales(dvo);
	}

	@Override
	public int supplyCost(DateRangeVO dvo) throws Exception {
		return adao.supplyCost(dvo);
	}

	@Override
	public int shippingCost(DateRangeVO dvo) throws Exception {
		return adao.shippingCost(dvo);
	}

	@Override
	public int salaryCost(DateRangeVO dvo) throws Exception {
		return adao.salaryCost(dvo);
	}

	@Override
	public int wageCost(DateRangeVO dvo) throws Exception {
		return adao.wageCost(dvo);
	}

	@Override
	public int mechanicalCost(DateRangeVO dvo) throws Exception {
		return adao.mechanicalCost(dvo);
	}

	@Override
	public int inventoryCost(DateRangeVO dvo) throws Exception {
		return adao.inventoryCost(dvo);
	}

	@Override
	public int marketingCost(DateRangeVO dvo) throws Exception {
		return adao.marketingCost(dvo);
	}

	@Override
	public int printCost(DateRangeVO dvo) throws Exception {
		return adao.printCost(dvo);
	}

	@Override
	public int sellingCost(DateRangeVO dvo) throws Exception {
		return adao.sellingCost(dvo);
	}

	@Override
	public int maintainCost(DateRangeVO dvo) throws Exception {
		return adao.maintainCost(dvo);
	}

	@Override
	public int otherCost(DateRangeVO dvo) throws Exception {
		return adao.otherCost(dvo);
	}

	@Override
	public int depreciationCost(DateRangeVO dvo) throws Exception {
		return adao.depreciationCost(dvo);
	}

	@Override
	public int stermDebt(DateRangeVO dvo) throws Exception {
		return adao.stermDebt(dvo);
	}

	@Override
	public int ltermDebt(DateRangeVO dvo) throws Exception {
		return adao.ltermDebt(dvo);
	}

	@Override
	public int payableCost(DateRangeVO dvo) throws Exception {
		return adao.payableCost(dvo);
	}

	@Override
	public int payableWage(DateRangeVO dvo) throws Exception {
		return adao.payableWage(dvo);
	}

	@Override
	public int sumupVAT(DateRangeVO dvo) throws Exception {
		return adao.sumupVAT(dvo);
	}

	@Override
	public int grossProfit(DateRangeVO dvo) throws Exception {
		return adao.grossProfit(dvo);
	}

	@Override
	public int operatingIncome(DateRangeVO dvo) throws Exception {
		return adao.operatingIncome(dvo);
	}

	@Override
	public int netIncome(DateRangeVO dvo) throws Exception {
		return adao.netIncome(dvo);
	}

	@Override
	public List<SliprgVO> selectByDate(DateRangeVO dvo) throws Exception {
		return adao.selectByDate(dvo);
	}

	@Override
	public String getEmailBySessionId(HttpSession session) {
		String erpId = (String) session.getAttribute("erpId");
		if(erpId == null) {
		return null;
		}
		return adao.selectEmail(erpId);
	}

	@Override
	public String selectName(String erpId) {
		return adao.selectName(erpId);
	}


}
