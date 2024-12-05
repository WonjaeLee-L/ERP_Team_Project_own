package com.example.practice.controller;

import com.example.practice.FileHandler.ProductFile;
import com.example.practice.service.IF_ProductService;
import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.Banner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductController {


    private final ProductFile productFile;
    private final IF_ProductService productservice;

    @RequestMapping("/aa")
    public String dd() throws Exception {
        return "list";
    }

    // 물품 등록 화면 이동
    @RequestMapping("/storage")
    public String storage() throws Exception {
        return "storage";
    }

    // 물품 등록 ok
    //, required = false
    @PostMapping("/product")
    public String product(@ModelAttribute ProductVO productVO, @RequestParam(value = "file") MultipartFile file) throws Exception {

        try {
            if (!file.isEmpty()) {
                String saveFileName = productFile.saveFile(file);
                productVO.setProduct_img(saveFileName);
            } else {
                if (productVO.getProduct_img() == null || productVO.getProduct_img().isEmpty()) {
                    productVO.setProduct_img("/static/productImg/default/defaultImage.png");
                }
            }
            productservice.insertProduct(productVO);
        } catch (NumberFormatException e) {
            // 오류 처리
        }
        return "redirect:productlistview";
    }


    //     물품 전체 리스트
    @ResponseBody
    @GetMapping("/productlistview")
    public ModelAndView product(@ModelAttribute ProductPageVO productPageVO) throws Exception {
        if (productPageVO.getPage() == null) {
            productPageVO.setPage(1);
        }
        productPageVO.setTotalCount(productservice.totalproductcount());

        List<ProductVO> productlist = productservice.selectAll(productPageVO);

        // 디버깅 및 기본 이미지 설정
        for (ProductVO product : productlist) {
            System.out.println("Product Img: " + product.getProduct_img());
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




    // 물품 검색, 이름 다중 검색
    @GetMapping("/productoneview")
    public String productOneView(@RequestParam("search") String product_name, @RequestParam("search1") String product_price, @RequestParam("search2") String category_code, @ModelAttribute ProductVO productVO, Model model) throws Exception {
        List<ProductVO> productVOS = productservice.selectProduct(product_name, product_price, category_code);
        System.out.println("확인: " + product_name);
        model.addAttribute("productlist", productVOS);
        // 입력 안하고 검색하면, 전체 리스트 호출
        if (productVOS.isEmpty()) {
            return "redirect:productlistview";
        }


        for (ProductVO productVO1 : productVOS) {
            System.out.println(productVO1.toString());
        };

        return "MainProduct";
    }

    // 리스트에서 물품 삭제
    @PostMapping("/productdel")
    public String del(@RequestParam("num[]") List<Integer> num) throws Exception {
        System.out.println("controller in"+num);
        productservice.deleteProduct(num);
        System.out.println("controller out : check");
        return "redirect:productlistview";
    }


    @PostMapping("/productmod")
    @ResponseBody
    public Map<String, Object> productMod(@RequestParam(value = "category_code", required = false) String category_code) throws Exception {
        Map<String, Object> response = new HashMap<>();

        List<ProductVO> productVO = productservice.selectCategory(category_code);


        ProductPageVO productPageVO = new ProductPageVO();
        productPageVO.setStartPage(1);
        productPageVO.setEndPage(2);

        response.put("productVOMod", productVO);
        response.put("productPageVO", productPageVO);
        return response;
    }


    @ResponseBody
    @PostMapping(value = "/productlistview1", consumes = "multipart/form-data")
    public void productModalView(@RequestParam("num") int num, @RequestParam("product_code") String product_code, @RequestParam("product_name") String product_name, @RequestParam("sale_price") int sale_price, @RequestParam("price") int price, @RequestParam("category_code") String category_code, @RequestParam("product_explain") String product_explain, @RequestParam("company_code") String company_code) throws Exception {
//        @RequestParam("file") MultipartFile file,
//        String filePath = productFile.saveFile(file);
        ProductVO productVO = new ProductVO();
        productVO.setNum(num);
        productVO.setProduct_code(product_code);
        productVO.setProduct_name(product_name);
        productVO.setSale_price(sale_price);
        productVO.setPrice(price);
        productVO.setCategory_code(category_code);
//        productVO.setProduct_img(filePath);
//        productVO.setProduct_img(product_img);
        productVO.setProduct_explain(product_explain);
        productVO.setCompany_code(company_code);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(productVO);
//        System.out.println(productVO.toString());

    }



        @PostMapping("/modProductInfo")
        public String productMod(@ModelAttribute ProductVO productVO, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        System.out.println(productVO.toString());


            if (file != null && !file.isEmpty()) {
                // 기존 절대 경로 삭제, 중복 저장 방지 로직 추가
                String existingImgPath = productVO.getProduct_img();
                if (existingImgPath != null && !existingImgPath.equals("/static/productImg/default/defaultImage.png")) {
                    // 파일 삭제 로직 추가
                    File existingFile = new File("C:/Data/aa/projectSample-master/src/main/resources" + existingImgPath);
                    if (existingFile.exists()) {
                        existingFile.delete();
                    }
                }

                String absolutePath = productFile.saveFile(file);
                if (absolutePath != null) {
                    String relativePath = "/static/productImg/" + new File(absolutePath).getName();
                    productVO.setProduct_img(relativePath);
                }
            }
            productservice.updateProduct(productVO);
            return "redirect:productlistview";
        }}




