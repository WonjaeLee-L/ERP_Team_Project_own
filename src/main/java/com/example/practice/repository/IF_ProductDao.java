package com.example.practice.repository;

import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IF_ProductDao {
    public List<ProductVO> selectAllProduct(ProductPageVO productpageVO) throws Exception;
    public List<ProductVO> selectProduct(Map<String, Object> params) throws Exception;
    public List<ProductVO> selectCategory(Map<String, Object> params) throws Exception;
    public void insertProduct(ProductVO productVO) throws Exception;
    public void deleteProduct(List<Integer> num) throws Exception;
    public void updateProduct(ProductVO productVO) throws Exception;
    public int productCount() throws Exception;
    public int countSearchProduct(ProductVO productVO) throws Exception;
    ProductVO selectProductByNum(int num) throws Exception;
}
