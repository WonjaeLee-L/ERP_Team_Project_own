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
    public String product(@ModelAttribute ProductVO productVO, @RequestParam(value = "file") MultipartFile file, InputStream inputStream, HttpServletResponse response) throws Exception {
        try {
//        if (file.isEmpty()) {
//            System.out.println("null");
//        }else {
//            System.out.println("체크");
//        }
            String saveFileName = productFile.saveFile(file);
            productVO.setProduct_img(saveFileName);
            productservice.insertProduct(productVO);
            System.out.println(saveFileName + "savefileName");
            System.out.println(productVO.getProduct_img() + "product_img");
            String sub = saveFileName;
            if (saveFileName.length() > 70) {
                sub = saveFileName.substring(69);
            }
            System.out.println(sub + "sub");
            Long subLong = Long.parseLong(sub);
            inputStream = new FileInputStream(new File(productVO.getProduct_img()));

            response.setContentType("image/jpeg");
            showImage(subLong, response, inputStream);

//            return "redirect:productlistview";

        } catch (NumberFormatException e) {

        }
        return "redirect:productlistview";
    }

    @ResponseBody
    @GetMapping("/productImg/{product_img}")
    public UrlResource showImage(@PathVariable("product_img") Long img, HttpServletResponse response, InputStream inputStream) throws IOException {

//        img = URLDecoder.decode(img, "UTF-8");
        response.setContentType("image/jpeg");
        InputStream i = new ByteArrayInputStream("product_img".getBytes());
        IOUtils.copy(i, response.getOutputStream());

        return new UrlResource("file:" + "product_img");
//        return new UrlResource("file:"+ img);
    }

//    @GetMapping

//    @GetMapping("/{product_img}")
//    public ResponseEntity<byte[]> imgview(@PathVariable("product_img") Long img) throws Exception {
//        InputStream ii = new FileInputStream(""+img);
//        byte[] imageByteArray = IOUtils.copy(i)
//        ii.close();
//        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
//    }
//@GetMapping("/{product_img}")
//public void renderImageFromDB(@PathVariable("product_img") Long im, HttpServletResponse response)
//        throws IOException {
//
//    log.debug("renderImageFromDB in IamgeController");
//
//
//
//        byte[] byteArray = new byte[recipeCommand.getImage().length];
//
//        int index = 0;
//        for (byte b : recipeCommand.getImage()) {
//            byteArray[index++] = b;
//
//
//        response.setContentType("image/jpeg");
//        InputStream is = new ByteArrayInputStream(byteArray);
//        IOUtils.copy(is, response.getOutputStream());
//    }
//}
//    @GetMapping("/{product_img}")
//    public void imgview(@PathVariable("product_img") Long img, HttpServletResponse response) throws IOException {
//
//    }

    //    @GetMapping("'/display?filename='+${data_product.product_img}")
//    @ResponseBody
//    public Resource viewimg(@PathVariable("data_product.product_img") long product_img, Model model) throws Exception {
//        FileEntity file = productFile.
//    }
//    @GetMapping("/display")
//    public ResponseEntity<Resource> viewimage(@RequestParam("filename") String filename) throws Exception {
//        String uploadDir = "C:/Data/aa/projectSample-master/src/main/resources/static/productImg/";
//        uploadDir = URLEncoder.encode(uploadDir, "UTF-8");
//        FileSystemResource resource = new FileSystemResource(uploadDir + filename);
//        if(!resource.exists())
//            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
//        HttpHeaders header = new HttpHeaders();
//        Path filePath = null;
//        try{
//            filePath = Paths.get(uploadDir + filename);
//            header.add("Content-type", Files.probeContentType(filePath));
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<Resource>((Resource) resource, header, HttpStatus.OK);
//    }

    //     물품 전체 리스트
    @ResponseBody
    @GetMapping("/productlistview")
    public ModelAndView product(@ModelAttribute ProductPageVO productPageVO) throws Exception {
        if (productPageVO.getPage() == null) {
            productPageVO.setPage(1);
        }
        productPageVO.setTotalCount(productservice.totalproductcount());

        List<ProductVO> productlist = productservice.selectAll(productPageVO);
        //        System.out.println(productVO.getProduct_img());
//            model.addAttribute("productlist", productlist);
//            model.addAttribute("productPageVO", productPageVO);
        ModelAndView mv = new ModelAndView();
        mv.addObject("productPageVO", productPageVO);
        mv.addObject("productlist", productlist);

        mv.setViewName("MainProduct");

        return mv;
    }


//    @GetMapping("/productlistview")
//    public String product(Model model, @ModelAttribute ProductPageVO productPageVO, @RequestPart(value = "file")MultipartFile file) throws Exception {
//        if(productPageVO.getPage()==null) {
//            productPageVO.setPage(1);
//        }
//        productPageVO.setTotalCount(productservice.totalproductcount());
//
//        List<ProductVO> productlist = productservice.selectAll(productPageVO);

    /// /        System.out.println(productVO.getProduct_img());
//        model.addAttribute("productlist", productlist);
//        model.addAttribute("productPageVO", productPageVO);
//
//        return "MainProduct";
//    }

    // 물품 검색, 이름 다중 검색
    @GetMapping("/productoneview")
    public String productOneView(@RequestParam("search1") String product_name, Model model, @ModelAttribute ProductVO productVO) throws Exception {
        List<ProductVO> productVOS = productservice.selectProduct(product_name);
        System.out.println("확인: " + product_name);
        model.addAttribute("productVOS", productVOS);
        // 입력 안하고 검색하면, 전체 리스트 호출
        if (productVOS.isEmpty()) {
            return "redirect:productlistview";
        }
        return "MainProduct";
    }

    // 리스트에서 물품 삭제
    @GetMapping("del")
    public String del(@RequestParam("delcode") String delcode) throws Exception {
        productservice.deleteProduct(delcode);
        return "redirect:productlistview";
    }

//     단일 검색
//    @RequestBody
//    @PostMapping("/productmod")
//    public String productMod(ProductVO productVO, Model model) throws Exception {
//        productVO = productservice.selectOneProduct(productVO.getProduct_name());
//        model.addAttribute("productVOMod", productVO);
//        return "redirect:productlistview";
//    }

//    @PostMapping("/productmod")
//    public String productMod(@RequestParam(value="product_code",required = false) String product_code, Model model) throws Exception {
//        System.out.println(product_code + " 받은 product_code");
//
//        ProductVO productVO = productservice.selectOneProduct(product_code);
//        if (productVO == null) {
//            model.addAttribute("error", "제품 정보를 찾을 수 없습니다.");
//            return "errorPage"; // 에러 페이지로 리다이렉트할 경우
//        }
//        ProductPageVO productPageVO = new ProductPageVO();
//        productPageVO.setStartPage(1); // Or appropriate values
//        productPageVO.setEndPage(10);
//
//        System.out.println(product_code+"받은 product_code");
//
//        System.out.println(productVO.toString());
//        model.addAttribute("productVOMod", productVO);
//        model.addAttribute("productPageVO", productPageVO);
//        return "MainProduct";
//    }

    @PostMapping("/productmod")
    @ResponseBody
    public Map<String, Object> productMod(@RequestParam(value = "category_code", required = false) String category_code) throws Exception {
        Map<String, Object> response = new HashMap<>();

        List<ProductVO> productVO = productservice.selectCategory(category_code);
//        if (productVO == null) {
//            response.put("error", "제품 정보를 찾을 수 없습니다.");
//            return response;
//        }

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
        System.out.println(productVO.toString());
//        System.out.println(objectMapper.writeValueAsString(productVO));
//        objectMapper.writeValueAsString(productVO);
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("productVO", productVO);
//        mv.setViewName("MainProduct");
//        return mv;
//        return "MainProduct";
//        return "modProductInfo";
    }
    // 새로 입력 받은 내용 수정
    @PostMapping("/modProductInfo")
    public String productMod(@ModelAttribute ProductVO productVO) throws Exception {
        productservice.updateProduct(productVO);
        return "redirect:productlistview";
    }

}
