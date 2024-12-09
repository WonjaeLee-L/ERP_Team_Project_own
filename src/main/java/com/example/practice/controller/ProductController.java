package com.example.practice.controller;

import com.example.practice.FileHandler.ProductFile;
import com.example.practice.service.IF_ProductService;
import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductController {


    private final ProductFile productFile;
    private final IF_ProductService productservice;

    // 메서드명
    // 물품 등록_1
    @PostMapping("/product")
    public String getValid(@Valid @ModelAttribute ProductVO productVO, BindingResult result, @RequestParam(value = "file") MultipartFile file) throws Exception {
        if (result.hasErrors()) {
            return "MainProduct";
        }

        try {
            handleFileAndPersistProduct(productVO, file);
        } catch (Exception e) {
            return "ErrorPage";
        }
        return "redirect:productList";
    }

    // 물품 등록_2
    private void handleFileAndPersistProduct(ProductVO productVO, MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String saveFileName = productFile.saveFile(file);
            productVO.setProduct_img(saveFileName);
        } else {
            if (productVO.getProduct_img() == null || productVO.getProduct_img().isEmpty()) {
                productVO.setProduct_img("/static/productImg/default/defaultImage.png");
            }
        }
        productservice.insertProduct(productVO);
    }

    // 물품 수정_1
    @PostMapping("/modProductInfo")
    public String productMod(@Valid @ModelAttribute ProductVO productVO, BindingResult result,
                             @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if (result.hasErrors()) {
            return "MainProduct";
        }
        handleProductImageUpdate(productVO, file);
        productservice.updateProduct(productVO);
        return "redirect:productList";
    }

    // 물품 수정_2
    private void handleProductImageUpdate(ProductVO productVO, MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            deleteExistingProductImage(productVO);
            String absolutePath = productFile.saveFile(file);
            if (absolutePath != null) {
                String relativePath = "/static/productImg/" + new File(absolutePath).getName();
                productVO.setProduct_img(relativePath);
            }
        }
    }

    // 물품 수정_3
    private void deleteExistingProductImage(ProductVO productVO) {
        String existingImgPath = productVO.getProduct_img();
        if (existingImgPath != null && !existingImgPath.equals("/static/productImg/default/defaultImage.png")) {
            File existingFile = new File("C:/Data/aa/projectSample-master/src/main/resources" + existingImgPath);
            if (existingFile.exists()) {
                existingFile.delete();
            }
        }
    }

    // product list
    @ResponseBody
    @GetMapping("/productList")
    public ModelAndView product(@ModelAttribute ProductPageVO productPageVO) throws Exception {
        if (productPageVO.getPage() == null) {
            productPageVO.setPage(1);
        }
        productPageVO.setTotalCount(productservice.totalproductcount());
        List<ProductVO> productlist = productservice.selectAllProduct(productPageVO);
        for (ProductVO product : productlist) {
            if (product.getProduct_img() == null || product.getProduct_img().isEmpty()) {
                product.setProduct_img("/static/productImg/default/defaultImage.png");
            }
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("productPageVO", productPageVO);
        mv.addObject("productlist", productlist);
        mv.setViewName("MainProduct");
        return mv;
    }

//     search product
    @GetMapping("/searchProduct")
    public String searchProduct(@RequestParam("search-name") String product_name, @RequestParam("search-price") String product_price, @RequestParam("search-category") String category_code, @ModelAttribute ProductPageVO productPageVO, Model model) throws Exception {
        if (productPageVO.getPage() == null) {
            productPageVO.setPage(1);
        }
        productPageVO.setTotalCount(productservice.totalproductcount());
        ProductVO productVO = new ProductVO();
        if (product_name != null) {
            productVO.setProduct_name(product_name);
        } else {
            productVO.setProduct_name("");
        }
        if (!product_price.isEmpty()) {
            productVO.setSale_price(Integer.parseInt(product_price));
        } else {
            productVO.setSale_price(0);
        }
        if (category_code != null) {
            productVO.setCategory_code(category_code);
        } else {
            productVO.setCategory_code("");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("productVO", productVO);
        params.put("productPageVO", productPageVO);

        List<ProductVO> productVOS = productservice.searchProduct(params);
        if (productVOS.isEmpty()) {
            return "redirect:productList";
        }
        model.addAttribute("productlist", productVOS);
        model.addAttribute("productPageVO", productPageVO);
        return "MainProduct";
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
                product.setProduct_img("/static/productImg/default/defaultImage.png");
            }
        }
        response.put("productVOMod", productVO);
        response.put("productPageVO", productPageVO);
        return response;
    }

    // delete product(list & modal)
    @PostMapping("/productdel")
    public ResponseEntity<String> del(@RequestBody Map<String, List<Integer>> payload) throws Exception {
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
    public void excelDown(HttpServletResponse response, ProductPageVO productPageVO) throws Exception {

        // 게시판 목록조회
        if (productPageVO.getPage() == null) {
            productPageVO.setPage(1);
        }
        productPageVO.setTotalCount(productservice.totalproductcount());

        List<ProductVO> list = productservice.selectAllProduct(productPageVO);

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("게시판");
        Row row = null;
        Cell cell = null;
        int rowNo = 0;


        CellStyle headStyle = wb.createCellStyle();
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
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