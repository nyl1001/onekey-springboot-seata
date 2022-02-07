package com.example.coupon.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface WarehouseService {

    /**
     * 扣减库存操作tcc
     *
     * @param commodityId 商品id
     * @param count       待扣减的库存量
     * @return 操作结果
     */
    @TwoPhaseBusinessAction(name = "deductWarehouseFromRedis", commitMethod = "doDeductFromRedis", rollbackMethod = "cancelDeductFromRedis")
    String deductWarehouseFromRedis(
            @BusinessActionContextParameter(paramName = "commodityId") String commodityId,
            @BusinessActionContextParameter(paramName = "count") Integer count) throws Exception;


    /**
     * 确认扣减库存
     *
     * @param context 分布式事物context
     * @return true/false
     */
    boolean doDeductFromRedis(BusinessActionContext context);

    /**
     * 取消扣减库存
     *
     * @param context 分布式事物context
     * @return true/false
     */
    boolean cancelDeductFromRedis(BusinessActionContext context) throws Exception;
}
