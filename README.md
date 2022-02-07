# spring boot 整合spring cloud、seata、nacos和gateway解决分布式事务难题
本人花了整整3天时间踩完所有坑，为你提供现成的源码成果，并提供详细的说明文档。
一直觉得直接上源码是最实在的。

nacos-server、seata-server的部署请直接参考：

https://github.com/nyl1001/onekey-springcloud-docker

## 不同mysql db之间分布式事务问题解决方案示例

product项目

updateProjectById

```
/**
 *  根据 id 更新数据
 * @param product
 */
@GlobalTransactional
@Transactional
public void updateProjectById(Product product){
    int i = productMapper.updateById(product);
    try{
        R r = couponFeignService.updateDbCouponName();
    }catch (Exception e){
        log.info("根据 id 更新数据 的异常信息02：" + e.getMessage());
    }

    int num = 1/0;  // 出现异常 看看两个微服务都回滚不
}
```

## mysql db和redis混合场景下的分布式事务问题解决方案示例

coupon项目

```
@GetMapping("/deduct")
@Transactional
@GlobalTransactional(timeoutMills = 60000 * 2)
public String deductWareHouse(@RequestParam("commodityId") String commodityId,
                              @RequestParam("count") Integer count) throws Exception {
    warehouseService.deductWarehouseFromRedis(commodityId, count);
    couponService.updateDbCouponName();
    throw new RuntimeException();
}
```

## 其他
请在自己的业务测试数据库（对应本项目的默认数据库）导入下列数据库文件完成初始数据导入：

onekey-springboot-seata/onekey-springboot-product/src/main/resources/sql/db.sql




