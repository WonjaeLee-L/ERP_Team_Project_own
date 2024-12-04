package com.example.practice.repository;

import com.example.practice.vo.ProductPageVO;
import com.example.practice.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IF_ProductDao {
    public List<ProductVO> selectAll(ProductPageVO productpageVO) throws Exception;
    public void insertProduct(ProductVO productVO) throws Exception;
    public List<ProductVO> selectProduct(String product_name, String sale_price, String category_code) throws Exception;
    public List<ProductVO> selectCategory(String oneSearch) throws Exception;
    public void deleteProduct(String delcode) throws Exception;
    public void updateProduct(ProductVO productVO) throws Exception;
    public int productCount() throws Exception;
}
