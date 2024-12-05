package com.example.practice.service;

import com.example.practice.repository.IF_MemberDao;
import com.example.practice.repository.IF_ProductDao;
import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IF_ProductService{


    private final IF_ProductDao productDao;

    @Override
    public List<ProductVO> selectAll(ProductPageVO productpageVO) throws Exception {
        return productDao.selectAll(productpageVO);
    }

    @Override
    public void insertProduct(ProductVO productVO) throws Exception {
        productDao.insertProduct(productVO);
    }

    @Override
    public List<ProductVO> selectProduct(String product_name, String sale_price, String category_code) throws Exception {
//        System.out.println("selectProduct product_name: " + product_name);  // Add debug
//        if (product_name == null || product_name.isEmpty()) {
//            System.out.println("product_name이 null이거나 비어 있습니다.");
//
//            return Collections.emptyList();
//        }
//        System.out.println("selectProduct 결과: ");  // 결과 디버그
        List<ProductVO> productVOList = productDao.selectProduct(product_name, sale_price, category_code);
//        System.out.println(productVOList);
        return productVOList;
//        return productDao.selectProduct(product_name);
    }

    @Override
    public List<ProductVO> selectCategory(String oneSearch) throws Exception {

        return productDao.selectCategory(oneSearch);
    }

    @Override
    public void deleteProduct(List<Integer> num) throws Exception {
        productDao.deleteProduct(num);
    }

    @Override
    public void updateProduct(ProductVO productVO) throws Exception {
        productDao.updateProduct(productVO);
    }

    @Override
    public int totalproductcount() throws Exception {
        return productDao.productCount();
    }


}
