package com.example.practice.controller;

import com.example.practice.FileHandler.ProductFile;
import com.example.practice.service.IF_ProductService;
import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;
import com.example.practice.vo.SlipVO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductFile productFile;
    private final IF_ProductService productservice;
    // 12/12 16:43
    // add product_1
    @ResponseBody
    @PostMapping("/product")
    public ResponseEntity<String> getValid(@Valid @ModelAttribute ProductVO productVO, BindingResult result, @RequestParam(value = "file") MultipartFile file) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            addProduct(productVO, file);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // add product_2
    private void addProduct(ProductVO productVO, MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String saveFileName = productFile.saveFile(file);
            productVO.setProduct_img(saveFileName);
        } else {
            if (productVO.getProduct_img() == null || productVO.getProduct_img().isEmpty()) {
                productVO.setProduct_img("/productImg/default/defaultImage.png");
            }
        }
        productservice.insertProduct(productVO);
    }

    @PostMapping("/modProductInfo")
    @ResponseBody
    public ResponseEntity<?> modProduct(@Valid @ModelAttribute ProductVO productVO,
                                        BindingResult result,
                                        @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        // 기존 데이터 조회
        ProductVO existingProduct = productservice.getProductByNum(productVO.getNum());  // 이 메서드 추가 필요

        // 새 이미지가 없으면 기존 이미지 경로 사용
        if (file == null || file.isEmpty()) {
            productVO.setProduct_img(existingProduct.getProduct_img());
        }

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            modImage(productVO, file);
            productservice.updateProduct(productVO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("productImg", productVO.getProduct_img());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error occurred");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private void modImage(ProductVO productVO, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            // 새로운 이미지가 업로드된 경우에만 이미지 변경
            deleteImage(productVO);
            String absolutePath = productFile.saveFile(file);
            if (absolutePath != null) {
                String relativePath = "/productImg/" + new File(absolutePath).getName();
                productVO.setProduct_img(relativePath);
            }
        }
    }

    // mod product_3
    private void deleteImage(ProductVO productVO) {
        String existingImgPath = productVO.getProduct_img();
        if (existingImgPath != null && !existingImgPath.equals("/productImg/default/defaultImage.png")) {
            File existingFile = new File("C:/Data/final/ERP_Springboot_TeamProject/src/main/resources" + existingImgPath);
            if (existingFile.exists()) {
                existingFile.delete();
            }
        }
    }

    // product list
    @GetMapping("/productList")
    public ModelAndView allProduct(
            @RequestParam(value = "page", defaultValue = "1") int page) throws Exception {

        ProductPageVO productPageVO = new ProductPageVO();
        productPageVO.setPage(page);
        productPageVO.setTotalCount(productservice.totalproductcount());
        List<ProductVO> productlist = productservice.selectAllProduct(productPageVO);
        productlist.forEach(product -> {
            if (product.getProduct_img() == null || product.getProduct_img().isEmpty()) {
                product.setProduct_img("/productImg/default/defaultImage.png");
            }
        });
        ModelAndView mv = new ModelAndView();
        mv.addObject("productPageVO", productPageVO);
        mv.addObject("productlist", productlist);
        mv.setViewName("manageProduct");
        return mv;
    }

    // search product(product name, product price, category code)
    @GetMapping("/searchProduct")
    public String searchProduct(
            @RequestParam(value = "search-name", required = false) String productName,
            @RequestParam(value = "search-price", required = false) String productPrice,
            @RequestParam(value = "search-category", required = false) String categoryCode,
            @ModelAttribute ProductPageVO productPageVO,
            Model model) throws Exception {

        if (productPageVO.getPage() == null || productPageVO.getPage() <= 0) {
            productPageVO.setPage(1);
        }
        if (productPageVO.getPerPageNum() <= 0) {
            productPageVO.setPerPageNum(10);
        }

        productPageVO.setTotalCount(0);
        productPageVO.setStartNo((productPageVO.getPage() - 1) * productPageVO.getPerPageNum());

        ProductVO productVO = new ProductVO();
        if (productName != null && !productName.trim().isEmpty()) {
            productVO.setProduct_name(productName.trim());
        }
        if (productPrice != null && !productPrice.trim().isEmpty()) {
            try {
                productVO.setSale_price(Integer.parseInt(productPrice.trim()));
            } catch (NumberFormatException e) {
                model.addAttribute("message", "가격은 숫자로만 입력해주세요.");
                return "manageProduct";
            }
        }
        if (categoryCode != null && !categoryCode.trim().isEmpty()) {
            productVO.setCategory_code(categoryCode.trim());
        }
        int totalCount = productservice.countSearchProduct(productVO);
        productPageVO.setTotalCount(totalCount);
        productPageVO.calcPage();

        Map<String, Object> params = new HashMap<>();
        params.put("productVO", productVO);
        params.put("productPageVO", productPageVO);
        List<ProductVO> productList = productservice.searchProduct(params);

        if (productList == null || productList.isEmpty()) {
            model.addAttribute("message", "검색 결과가 없습니다.");
            model.addAttribute("productlist", Collections.emptyList());
        }

        setDefaultImage(productList);
        model.addAttribute("productlist", productList);
        model.addAttribute("productPageVO", productPageVO);

//        model.addAttribute("searchName", productName);
//        model.addAttribute("searchPrice", productPrice);
//        model.addAttribute("searchCategory", categoryCode);

        return "manageProduct";
    }


    private void setDefaultImage(List<ProductVO> productList) {
        for (ProductVO product : productList) {
            if (product.getProduct_img() == null || product.getProduct_img().isEmpty()) {
                product.setProduct_img("productImg/default/defaultImage.png");
            }
        }
    }

    // select product(category)
    @PostMapping("/selectcategory")
    @ResponseBody
    public Map<String, Object> selectCategory(@RequestParam(value = "category_code", required = false) String category_code, @RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        Map<String, Object> response = new HashMap<>();
        ProductPageVO productPageVO = new ProductPageVO();
        productPageVO.setPage(page);
        productPageVO.setPerPageNum(size);
        productPageVO.setTotalCount(productservice.totalproductcount());
        int startNo = (page - 1) * size;
        int endNo = startNo + size;
        productPageVO.setStartNo(startNo);
        productPageVO.setEndNo(endNo);

        Map<String, Object> params = new HashMap<>();
        params.put("category_code", category_code);
        params.put("productPageVO", productPageVO);

        List<ProductVO> productVO = productservice.selectCategory(params);
        for (ProductVO product : productVO) {
            if (product.getProduct_img() == null || product.getProduct_img().isEmpty()) {
                product.setProduct_img("productImg/default/defaultImage.png");
            }
        }
        response.put("productVOMod", productVO);
        response.put("productPageVO", productPageVO);
        return response;
    }

    // delete product(list & modal)
    @PostMapping("/productdel")
    public ResponseEntity<String> deleteProduct(@RequestBody Map<String, List<Integer>> payload) throws Exception {
        List<Integer> num = payload.get("num");
        System.out.println("controller in: " + num);
        if (num == null || num.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request: 'num' list is empty or null.");
        }
        productservice.deleteProduct(num);
        return ResponseEntity.ok("Del");
    }

    // excel download
    @RequestMapping(value = "/excelDown.do")
    public void downloadExcel(HttpServletResponse response, ProductPageVO productPageVO) throws Exception {

        if (productPageVO.getPage() == null) {
            productPageVO.setPage(1);
        }
        productPageVO.setTotalCount(productservice.totalproductcount());

        List<ProductVO> list = productservice.selectAllProduct(productPageVO);

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("상품");
        Row row = null;
        Cell cell = null;
        int rowNo = 0;


        CellStyle headStyle = wb.createCellStyle();
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BRIGHT_GREEN.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);

        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue("등록 번호");
        cell = row.createCell(1);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상품 이름");
        cell = row.createCell(2);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상품 가격");

        for (ProductVO vo : list) {
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getNum());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getProduct_name());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getPrice());
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        wb.write(response.getOutputStream());
        wb.close();
    }
}