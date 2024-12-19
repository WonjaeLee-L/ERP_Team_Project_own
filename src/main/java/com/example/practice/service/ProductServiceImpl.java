package com.example.practice.service;

import com.example.practice.repository.IF_ProductDao;
import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IF_ProductService{


    private final IF_ProductDao productDao;

    @Override
    public List<ProductVO> selectAllProduct(ProductPageVO productpageVO) throws Exception {
        return productDao.selectAllProduct(productpageVO);
    }

    @Override
    public void insertProduct(ProductVO productVO) throws Exception {
        productDao.insertProduct(productVO);
    }

    @Override
    public List<ProductVO> searchProduct(Map<String, Object> params) throws Exception {
        List<ProductVO> productVOS = productDao.selectProduct(params);
        return productVOS;
    }

    @Override
    public int countSearchProduct(ProductVO productVO) throws Exception {
        return productDao.countSearchProduct(productVO);
    }

    @Override
    public List<ProductVO> selectCategory(Map<String, Object> params) throws Exception {
        return productDao.selectCategory(params);
    }

    @Override
    public void deleteProduct(List<Integer> num) throws Exception {
        productDao.deleteProduct(num);
    }

    @Override
    @Transactional
    public void updateProduct(ProductVO productVO) throws Exception {
        productDao.updateProduct(productVO);
    }

    @Override
    public int totalproductcount() throws Exception {
        return productDao.productCount();
    }

    @Override
    public ProductVO getProductByNum(int num) throws Exception {
        return productDao.selectProductByNum(num);
    }

}
