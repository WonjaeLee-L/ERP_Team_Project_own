package com.example.practice.service;

import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IF_ProductService {
    public List<ProductVO> selectAllProduct(ProductPageVO productpageVO) throws Exception;

    public void insertProduct(ProductVO productVO) throws Exception;

    public List<ProductVO> searchProduct(Map<String, Object> params) throws Exception;

    public int countSearchProduct(ProductVO productVO) throws Exception;

    public List<ProductVO> selectCategory(Map<String, Object> params) throws Exception;

    public void deleteProduct(List<Integer> num) throws Exception;

    public void updateProduct(ProductVO productVO) throws Exception;

    public int totalproductcount() throws Exception;

    ProductVO getProductByNum(int num) throws Exception;
}
