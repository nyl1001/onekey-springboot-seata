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
@RequestMapping("/projectController")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService projectService;

    /**
     *  批量新增数据
     * @return
     */
    @RequestMapping("/insertBatchProject")
    public R insertBatchProject(){
       projectService.insertBatchProject();
       return R.ok();
    }

    /**
     *  查询所有的数据
     * @return
     */
    @RequestMapping("/selectProductList")
    public R selectProductList(@RequestBody PageUtil pageUtil){
        PageInfo<Product> projectPageInfo = projectService.selectProductList(pageUtil);
        return R.ok(projectPageInfo);
    }

    /**
     *  根据 id 更新数据
     * @param project
     * @return
     */
    @PostMapping("/updateProjectById")
    public R updateProjectById(@RequestBody Product project){
        try {
            projectService.updateProjectById(project);
        } catch (Exception ex){
            return R.error(ex.getMessage());
        }
        return R.ok();
    }

}
