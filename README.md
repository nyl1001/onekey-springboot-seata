# spring boot 整合spring cloud、seata、nacos解决分布式事务问题的参考示例
本人花了整整3天时间踩完所有坑，为您提供现成的源码成果，并提供详细的说明文档（持续更新中）。
一直觉得直接上源码才是最实在的。

nacos-server、seata-server的部署请直接参考：

https://github.com/nyl1001/onekey-springcloud-docker

## 1 不同mysql db之间分布式事务问题解决方案示例

product项目


```
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
```

直接访问下列接口进行验证：

```
curl -X POST -H "Content-Type:application/json" -d "{\"id\":1,\"productName\":\"test api\",\"productPrice\":\"100.20\"}" http://localhost:9005/product/productController/updateProductById
```

## 2 mysql db和redis混合场景下的分布式事务问题解决方案示例

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

直接访问下列接口进行验证：

```
curl -X GET http://localhost:9003/coupon/redisController/deduct?commodityId=1&count=2
```

## 3 本示例项目是以nacos作为注册中心和配置中心，对于以zookeeper作为注册中心和配置中心的项目，请直接参考本人另外一个示例项目进行配置操作：
[spring boot + dubbo + zookeeper + seata 整合示例](https://github.com/nyl1001/springboot-dubbo-seata-zk)

本示例是通过 SpringBoot2.1.5 + Dubbo 2.7.3 + Mybatis 3.4.2 + Zookeeper 3.7 +Seata Server 1.4.2 整合来实现Dubbo分布式事务管理，使用Zookeeper作为Dubbo和Seata的注册中心和配置中心,使用 MySQL 数据库和 MyBatis来操作数据。


## 4 其他

请在自己的业务测试数据库（对应本项目的默认数据库）导入下列数据库文件完成测试数据导入：

onekey-springboot-seata/onekey-springboot-seata-product/src/main/resources/sql/db.sql




