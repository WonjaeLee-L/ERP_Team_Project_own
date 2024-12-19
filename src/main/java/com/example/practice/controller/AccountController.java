package com.example.practice.controller;

import com.example.practice.service.IF_AccountService;
import com.example.practice.service.MailSendService;
import com.example.practice.vo.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final IF_AccountService accountService;
    private final MailSendService mailSendService;

    @PostMapping("/pSlip")
    public ResponseEntity<String> psInsert(@ModelAttribute SlipVO slipvo) throws Exception {
        accountService.psInsert(slipvo);
        return ResponseEntity.ok("successful");
    }

    @GetMapping("/slistView")
    public ModelAndView showSlipList(Model model, @ModelAttribute Pagevo pagevo) throws Exception {
        if (pagevo.getPage() == null) {
            pagevo.setPage(1);
        }

        pagevo.setTotalCount(accountService.totalCountPV());

        List<SlipVO> slipList = accountService.selectAll(pagevo);  // 전표 목록 가져오기
        ModelAndView mv = new ModelAndView("sliplist");  // sliplist.html 파일을 반환
        model.addAttribute("slips", slipList);  // 전표 목록을 "slips"라는 이름으로 모델에 추가

        return mv;
    }

    @GetMapping("/sliprgList")
    public ModelAndView showrgList(HttpSession session, Model model, @ModelAttribute Pagevo pagevo) throws Exception {
        session.setAttribute("searchKeyword", null);
        session.setAttribute("rgtext", null);
        session.setAttribute("page", null);
        if (pagevo.getPage() == null) {
            pagevo.setPage(1);
        }
        pagevo.setTotalCount(accountService.totalCountRG());

        List<SliprgVO> sliprgList = accountService.selectrgAll(pagevo);
        ModelAndView mv = new ModelAndView("sliprglist");
        model.addAttribute("slips", sliprgList);
        return mv;
    }

    @GetMapping("/incomeStatement")
    public ModelAndView showIncomeStatement(HttpSession session) throws Exception {
        String erpId = (String) session.getAttribute("erpId");
        String erpEmail = accountService.getEmailBySessionId(session);

        ModelAndView mv = new ModelAndView("incomeStatement");

        // 세션 데이터가 있는 경우 뷰에 추가
//        if (erpId != null) {
//            mv.addObject("erpId", erpId);
//            mv.addObject("erpEmail", erpEmail);
//            System.out.println("erpId: " + erpId);
//            System.out.println(erpEmail);
//        } else {
//            // 세션 정보가 없으면 로그인 페이지로 리다이렉트
//            mv.setViewName("redirect:/login");
//        }

        return mv;
    }

//    @GetMapping("/search")
//    public ModelAndView search(@RequestParam String searchKeyword,
//                               @RequestParam String pvtext,
//                               @RequestParam(defaultValue = "1") Integer page,
//                               Model model) throws Exception {
//
//        Pagevo pagevo = new Pagevo();
//        pagevo.setPage(page);
//
//        List<SlipVO> slipList;
//        ModelAndView mv = new ModelAndView("sliplist");
//
//        if(searchKeyword.equals("name")) {
//            pagevo.setTotalCount(accountService.countpvName(pvtext));
//            slipList = accountService.selectpvNameCmpy(pagevo, pvtext);
//        } else if(searchKeyword.equals("cmpy")) {
//            pagevo.setTotalCount(accountService.countpvCmpy(pvtext));
//            slipList = accountService.selectpvNameCmpy(pagevo, pvtext);
//        } else {
//            pagevo.setTotalCount(accountService.totalCountPV());
//            slipList = accountService.selectAll(pagevo);
//        }
//
//        model.addAttribute("slips", slipList);
//        model.addAttribute("pagevo", pagevo);
//
//        model.addAttribute("searchKeyword", searchKeyword);
//        model.addAttribute("pvtext", pvtext);
//        return mv;
//    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam String searchKeyword,
                               @RequestParam String pvtext,
                               @RequestParam(defaultValue = "1") Integer page,
                               Model model) throws Exception {

        Pagevo pagevo = new Pagevo();
        pagevo.setPage(page);
        List<SlipVO> slipList;
        ModelAndView mv = new ModelAndView("sliplist");

        if (searchKeyword.equals("name") || searchKeyword.equals("cmpy")) {
            pagevo.setTotalCount(accountService.countpvNameCmpy(searchKeyword, pvtext));
            slipList = accountService.selectpvNameCmpy(pagevo, searchKeyword, pvtext);
        } else {
            pagevo.setTotalCount(accountService.totalCountPV());
            slipList = accountService.selectAll(pagevo);
        }

        model.addAttribute("slips", slipList);
        model.addAttribute("pagevo", pagevo);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("pvtext", pvtext);
        return mv;
    }

    @GetMapping("/slips")
    public ModelAndView filterByType(@RequestParam String pvslipCode,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     Model model) throws Exception {
        Pagevo pagevo = new Pagevo();
        pagevo.setPage(page);

        List<SlipVO> slipList;
        ModelAndView mv = new ModelAndView("sliplist");

        if (pvslipCode != null && !pvslipCode.isEmpty()) {
            pagevo.setTotalCount(accountService.countByType(pvslipCode));
            slipList = accountService.selectByType(pagevo, pvslipCode);
        } else {
            pagevo.setTotalCount(accountService.totalCountPV());
            slipList = accountService.selectAll(pagevo);
        }

        model.addAttribute("slips", slipList);
        model.addAttribute("pagevo", pagevo);
        model.addAttribute("pvslipCode", pvslipCode);
        return mv;
    }

    @PostMapping(value = "delpvSlip")
    public ResponseEntity<String> delpvSlip(@RequestParam("pvCode") int pvCode, HttpServletResponse response) throws Exception {
        accountService.delpvSlip(pvCode);
        response.sendRedirect("slistView");
        return ResponseEntity.ok("Delete successful");
    }

    @PostMapping(value = "rgInsert")
    public ResponseEntity<String> rgInsert(@ModelAttribute SlipVO slipvo, @RequestParam("pvCode") int pvCode, HttpServletResponse response) throws Exception {
        accountService.rgInsert(slipvo);
        accountService.delpvSlip(pvCode);
        response.sendRedirect("slistView");
        return ResponseEntity.ok("Insert successful");
    }

    @GetMapping("/searchrg")
    public ModelAndView searchrg(
            @RequestParam String searchKeyword,
            @RequestParam String rgtext,
            @RequestParam(defaultValue = "1") Integer page,
            HttpSession session,
            Model model) throws Exception {

        Pagevo pagevo = new Pagevo();
        pagevo.setPage(page);
        List<SliprgVO> slipList;
        ModelAndView mv = new ModelAndView("sliprglist");

        if (searchKeyword.equals("name") || searchKeyword.equals("cmpy")) {
            pagevo.setTotalCount(accountService.countrgNameCmpy(searchKeyword, rgtext));
            slipList = accountService.selectrgNameCmpy(pagevo, searchKeyword, rgtext);
        } else {
            pagevo.setTotalCount(accountService.totalCountRG());
            slipList = accountService.selectrgAll(pagevo);
        }

        // 엑셀 다운로드를 위한 세션 정보 유지
        session.setAttribute("searchKeyword", searchKeyword);
        session.setAttribute("rgtext", rgtext);
        session.setAttribute("page", page);

        model.addAttribute("slips", slipList);
        model.addAttribute("pagevo", pagevo);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("rgtext", rgtext);

        return mv;
    }

    @GetMapping("/rgslips")
    public ModelAndView filterByrgType(
            @RequestParam String typeSelect,
            @RequestParam(defaultValue = "1") Integer page,
            Model model) throws Exception {

        Pagevo pagevo = new Pagevo();
        pagevo.setPage(page);
        List<SliprgVO> sliprgList;
        ModelAndView mv = new ModelAndView("sliprglist");

        if (typeSelect == null || typeSelect.isEmpty() || typeSelect.equals("selectAll")) {
            pagevo.setTotalCount(accountService.totalCountRG());
            sliprgList = accountService.selectrgAll(pagevo);
        } else {
            pagevo.setTotalCount(accountService.countByrgType(typeSelect));
            sliprgList = accountService.selectByrgType(pagevo, typeSelect);
        }

        model.addAttribute("slips", sliprgList);
        model.addAttribute("pagevo", pagevo);
        model.addAttribute("typeSelect", typeSelect);

        return mv;
    }

    @GetMapping("/getincomevalues")
    public ResponseEntity<Map<String, Object>> getIncomeStatement(
            @RequestParam(value = "startDate", defaultValue = "0000-01-01") String startDate,
            @RequestParam(value = "endDate", defaultValue = "9999-12-31") String endDate,
            HttpSession session) throws Exception {

        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        DateRangeVO dvo = new DateRangeVO();
        dvo.setStartDate(Date.valueOf(startLocalDate));
        dvo.setEndDate(Date.valueOf(endLocalDate));

        Map<String, Object> result = new HashMap<>();
        result.put("productSales", accountService.productSales(dvo));
        result.put("serviceSales", accountService.serviceSales(dvo));
        result.put("otherSales", accountService.otherSales(dvo));
        result.put("interestSales", accountService.interestSales(dvo));
        result.put("supplyCost", accountService.supplyCost(dvo));
        result.put("shippingCost", accountService.shippingCost(dvo));
        result.put("salaryCost", accountService.salaryCost(dvo));
        result.put("wageCost", accountService.wageCost(dvo));
        result.put("mechanicalCost", accountService.mechanicalCost(dvo));
        result.put("inventoryCost", accountService.inventoryCost(dvo));
        result.put("grossProfit", accountService.grossProfit(dvo));
        result.put("marketingCost", accountService.marketingCost(dvo));
        result.put("printCost", accountService.printCost(dvo));
        result.put("sellingCost", accountService.sellingCost(dvo));
        result.put("maintainCost", accountService.maintainCost(dvo));
        result.put("otherCost", accountService.otherCost(dvo));
        result.put("depreciationCost", accountService.depreciationCost(dvo));
        result.put("operatingIncome", accountService.operatingIncome(dvo));
        result.put("stermDebt", accountService.stermDebt(dvo));
        result.put("ltermDebt", accountService.ltermDebt(dvo));
        result.put("payableCost", accountService.payableCost(dvo));
        result.put("payableWage", accountService.payableWage(dvo));
        result.put("sumupVAT", accountService.sumupVAT(dvo));
        result.put("netIncome", accountService.netIncome(dvo));

        session.setAttribute("startDate", startDate);
        session.setAttribute("endDate", endDate);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/exceldownload")
    public void excelDownload(HttpSession session, HttpServletResponse response) throws Exception {
        String searchKeyword = (String) session.getAttribute("searchKeyword");
        String rgtext = (String) session.getAttribute("rgtext");
        Integer page = (Integer) session.getAttribute("page");

        Pagevo pagevo = new Pagevo();
        if (searchKeyword == null && rgtext == null && page == null) {
            pagevo.setPage(1);
            pagevo.setTotalCount(accountService.totalCountRG());
        } else {
            pagevo.setPage(page);
            pagevo.setTotalCount(accountService.countrgNameCmpy(searchKeyword, rgtext));
        }

        // sliprgList 초기화. 삼항 연산자로 코드 최적화 해보기
        // 삼항연산자란?          (조건) ? (참일 때 실행) : (거짓일 때 실행)
        List<SliprgVO> sliprgList = (searchKeyword == null && rgtext == null && page == null)
                ? accountService.selectrgAll(pagevo)
                : accountService.selectrgNameCmpy(pagevo, searchKeyword, rgtext);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Search Results");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"전표 코드", "전표 타입", "작성자 이름", "전표 등록 날짜", "계정과목", "거래처명",
                "금액", "부가세", "결제 수단", "적요"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 데이터 행 생성. 첫번 째 열은 헤더로 써야하니 rowIdx를 1로 지정해보자
        int rowIdx = 1;
        for (SliprgVO slip : sliprgList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(slip.getRgCode());
            row.createCell(1).setCellValue(slip.getRgslipCode());
            row.createCell(2).setCellValue(slip.getRgName());
            row.createCell(3).setCellValue(slip.getRgDate());
            row.createCell(4).setCellValue(slip.getRgslipName());
            row.createCell(5).setCellValue(slip.getRgCmpy());
            row.createCell(6).setCellValue(slip.getRgPrice());
            row.createCell(7).setCellValue(slip.getRgFee());
            row.createCell(8).setCellValue(slip.getRgPay());
            row.createCell(9).setCellValue(slip.getRgComment());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=SearchResults.xlsx");


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1024);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @GetMapping("/allexceldownload")
    public void allcxelDownload(HttpSession session, HttpServletResponse response) throws Exception {
        String searchKeyword = (String) session.getAttribute("searchKeyword");
        String rgtext = (String) session.getAttribute("rgtext");

        List<SliprgVO> sliprgList = (searchKeyword == null && rgtext == null)
                ? Collections.emptyList()
                : accountService.selectAllrgForExcel(searchKeyword, rgtext);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("AllList");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"전표 코드", "전표 타입", "작성자 이름", "전표 등록 날짜", "계정과목", "거래처명",
                "금액", "부가세", "결제 수단", "적요"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIdx = 1;
        for (SliprgVO slip : sliprgList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(slip.getRgCode());
            row.createCell(1).setCellValue(slip.getRgslipCode());
            row.createCell(2).setCellValue(slip.getRgName());
            row.createCell(3).setCellValue(slip.getRgDate());
            row.createCell(4).setCellValue(slip.getRgslipName());
            row.createCell(5).setCellValue(slip.getRgCmpy());
            row.createCell(6).setCellValue(slip.getRgPrice());
            row.createCell(7).setCellValue(slip.getRgFee());
            row.createCell(8).setCellValue(slip.getRgPay());
            row.createCell(9).setCellValue(slip.getRgComment());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=AllList.xlsx");

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1024);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/incomedownload")
    public void incomedownload(HttpSession session, HttpServletResponse response) throws Exception {
        String startDate;
        String endDate;

        if (session.getAttribute("startDate") != null) {
            startDate = (String) session.getAttribute("startDate");
        } else {
            startDate = "0000-01-01";
        }
        if (session.getAttribute("endDate") != null) {
            endDate = (String) session.getAttribute("endDate");
        } else {
            endDate = "0000-01-01";
        }

        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        DateRangeVO dvo = new DateRangeVO();
        dvo.setStartDate(Date.valueOf(startLocalDate));
        dvo.setEndDate(Date.valueOf(endLocalDate));

        List<SliprgVO> incomeStatementList = accountService.selectByDate(dvo);
        System.out.println(accountService.selectByDate(dvo));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Income Statement Results");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"전표 코드", "전표 타입", "작성자 이름", "전표 등록 날짜", "계정과목", "거래처명",
                "금액", "부가세", "결제 수단", "적요"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        int rowIdx = 1;
        for (SliprgVO slip : incomeStatementList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(slip.getRgCode());
            row.createCell(1).setCellValue(slip.getRgslipCode());
            row.createCell(2).setCellValue(slip.getRgName());
            row.createCell(3).setCellValue(slip.getRgDate());
            row.createCell(4).setCellValue(slip.getRgslipName());
            row.createCell(5).setCellValue(slip.getRgCmpy());
            row.createCell(6).setCellValue(slip.getRgPrice());
            row.createCell(7).setCellValue(slip.getRgFee());
            row.createCell(8).setCellValue(slip.getRgPay());
            row.createCell(9).setCellValue(slip.getRgComment());
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=IncomeStatement.xlsx");
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1024);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestBody Map<String, List<String>> request) {
        try {
            mailSendService.sendPdfFromImage(request.get("images"));
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
//        session.invalidate(); // 세션의 모든 데이터 삭제
//        System.out.println("세션 정보 삭제 완료");
        return "redirect:/";
    }

    @GetMapping("/getName")
    public String getName(@RequestParam("erpId") String erpId) {
        return accountService.selectName(erpId);
    }


}
