package com.example.product.controller;


import com.example.common.utils.R;
import com.example.common.entry.PageUtil;
import com.example.product.entry.Product;
import com.example.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品 控制器
 *  用来验证 分页组件的
 */
@RestController
@RequestMapping("/productController")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     *  批量新增数据
     * @return
     */
    @RequestMapping("/insertBatchProduct")
    public R insertBatchProduct(){
       productService.insertBatchProduct();
       return R.ok();
    }

    /**
     *  查询所有的数据
     * @return
     */
    @RequestMapping("/selectProductList")
    public R selectProductList(@RequestBody PageUtil pageUtil){
        PageInfo<Product> productPageInfo = productService.selectProductList(pageUtil);
        return R.ok(productPageInfo);
    }

    /**
     *  根据 id 更新数据
     * @param product
     * @return
     */
    @PostMapping("/updateProductById")
    public R updateProductById(@RequestBody Product product){
        try {
            productService.updateProductById(product);
        } catch (Exception ex){
            return R.error(ex.getMessage());
        }
        return R.ok();
    }

}
