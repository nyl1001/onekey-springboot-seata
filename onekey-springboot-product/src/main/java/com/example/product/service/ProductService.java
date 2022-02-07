package com.example.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entry.PageUtil;
import com.example.product.entry.Product;
import com.github.pagehelper.PageInfo;


public interface ProductService extends IService<Product> {

    /**
     * 批量新增数据
     */
    public void  insertBatchProject();

    /**
     * 查询所有的数据
     * @return
     */
    public PageInfo<Product> selectProductList(PageUtil pageUtil);

    /**
     *  根据 id 更新数据
     * @param project
     */
    public void updateProjectById(Product project);
}
