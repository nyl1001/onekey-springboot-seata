package com.example.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entry.PageUtil;
import com.example.common.utils.R;
import com.example.product.entry.Product;
import com.example.product.feign.CouponFeignService;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CouponFeignService couponFeignService;



    /**
     * 批量新增数据  saveBatch()
     * Collection<T> entityList, int batchSize
     */
    public void  insertBatchProduct(){
        Collection<Product> entityList = new ArrayList<Product>();
        String productName = "基本商品";

        for(int i = 0; i <100; i++){
            Product product = new Product();
            product.setProductName(productName + i);
            BigDecimal pPrice = new BigDecimal("" + i);
            product.setProductPrice(pPrice);
            entityList.add(product);
        }
        boolean b = this.saveBatch(entityList);
        log.info("批量插入：" + b);
    }

    /**
     * 查询所有的数据
     * @return
     */
    @GlobalTransactional
    @Transactional
    public PageInfo<Product> selectProductList(PageUtil pageUtil){
        int pageNo = pageUtil.getPageNo();
        int pageSize = pageUtil.getPageSize();
        PageHelper.startPage(pageNo,pageSize);
//        List<Product> lists = productMapper.selectProductList();

        Wrapper<Product> queryWrapper = new QueryWrapper();
        List<Product> lists = productMapper.selectList(queryWrapper);

        PageInfo<Product> pageInfo = new PageInfo<>(lists);
        return pageInfo;
    }


    /**
     *  根据 id 更新数据
     * @param product
     */
    @GlobalTransactional
    @Transactional
    public void updateProductById(Product product){
        int i = productMapper.updateById(product);
        try{
            R r = couponFeignService.updateDbCouponName();
        }catch (Exception e){
            log.info("根据 id 更新数据 的异常信息02：" + e.getMessage());
        }

        int num = 1/0;  // 出现异常 看看两个微服务都回滚不
    }

}
