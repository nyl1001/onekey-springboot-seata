create database mall_product;
use mall_product;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `product_name` varchar(8) NOT NULL COMMENT '产品名称',
  `product_price` decimal(10,2) NOT NULL COMMENT '产品价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='产品表';

CREATE TABLE IF NOT EXISTS `undo_log` (
    `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT 'increment id',
    `branch_id`     BIGINT(20)   NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(100) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create datetime',
    `log_modified`  timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'modify datetime',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';

-- alter table undo_log modify column `log_created` timestamp NOT NULL COMMENT '开始时间';
-- alter table undo_log modify column `log_modified` timestamp NOT NULL COMMENT '开始时间';

INSERT INTO mall_product.product (id, product_name, product_price) VALUES (1, '测试产品名称', 100.00);

create database mall_coupon;
use mall_coupon;

CREATE TABLE `coupon_coupon` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `coupon_type` int(4) NOT NULL COMMENT '优惠卷类型[0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券]',
  `coupon_img` varchar(256) NOT NULL COMMENT '优惠券图片',
  `coupon_name` varchar(64) NOT NULL COMMENT '优惠券名称',
  `num` int(10) NOT NULL COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '优惠券金额',
  `per_limit` int(4) NOT NULL COMMENT '每人限领张数',
  `min_point` decimal(10,2) NOT NULL COMMENT '使用门槛',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `use_type` int(4) NOT NULL COMMENT '使用类型[0->全场通用；1->指定分类；2->指定商品]',
  `publish_count` int(10) NOT NULL COMMENT '发行数量',
  `use_count` int(10) NOT NULL COMMENT '已使用数量',
  `receive_count` int(10) NOT NULL COMMENT '领取数量',
  `enable_start_time` datetime NOT NULL COMMENT '可以领取的开始日期',
  `enable_end_time` datetime NOT NULL COMMENT '可以领取的结束日期',
  `code` varchar(64) NOT NULL COMMENT '优惠码',
  `member_level` int(4) NOT NULL COMMENT '可以领取的会员等级[0->不限等级，其他-对应等级]',
  `publish` int(2) NOT NULL COMMENT '发布状态[0-未发布，1-已发布]',
  `note` varchar(256) NOT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='优惠券表';

INSERT INTO mall_coupon.coupon_coupon (id, coupon_type, coupon_img, coupon_name, num, amount, per_limit, min_point, start_time, end_time, use_type, publish_count, use_count, receive_count, enable_start_time, enable_end_time, code, member_level, publish, note) VALUES (1, 1, 'https://tenfei03.cfp.cn/creative/vcg/800/new/VCG211360573823.jpg', '测试优惠券', 2000, 100.00, 2, 300.00, '2022-02-05 13:56:12', '2022-02-12 13:56:15', 0, 1000, 20, 30, '2022-02-05 13:56:52', '2022-04-05 13:56:56', '1341mksfvsffq424', 0, 1, '测试备注');

-- alter table coupon_coupon modify column `start_time` datetime NOT NULL COMMENT '开始时间';
-- alter table coupon_coupon modify column `end_time` datetime NOT NULL COMMENT '开始时间';
-- alter table coupon_coupon modify column `enable_start_time` datetime NOT NULL COMMENT '开始时间';
-- alter table coupon_coupon modify column `enable_end_time` datetime NOT NULL COMMENT '开始时间';

-- CREATE TABLE `undo_log` (
--   `id` bigint(20) NOT NULL AUTO_INCREMENT,
--   `branch_id` bigint(20) NOT NULL,
--   `xid` varchar(100) NOT NULL,
--   `context` varchar(128) NOT NULL,
--   `rollback_info` longblob NOT NULL,
--   `log_status` int(11) NOT NULL,
--   `log_created` datetime NOT NULL,
--   `log_modified` datetime NOT NULL,
--   `ext` varchar(100) DEFAULT NULL,
--   PRIMARY KEY (`id`),
--   UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `undo_log` (
    `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT 'increment id',
    `branch_id`     BIGINT(20)   NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(100) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create datetime',
    `log_modified`  timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'modify datetime',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';


-- alter table undo_log modify column `log_created` timestamp NOT NULL COMMENT '开始时间';
-- alter table undo_log modify column `log_modified` timestamp NOT NULL COMMENT '开始时间';

CREATE USER 'mall'@'%' IDENTIFIED BY 'mall';
GRANT All privileges ON *.* TO 'mall'@'%';
flush privileges;