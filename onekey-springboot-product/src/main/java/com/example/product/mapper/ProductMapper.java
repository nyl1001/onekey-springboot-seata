package com.example.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.product.entry.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface ProductMapper extends BaseMapper<Product> {


    /**
     * 查询所有的数据
     * @return
     */
    public List<Product> selectProductList();
}
