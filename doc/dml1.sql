
delete from sys_dept;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('1', '0', '集团公司', '0', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('2', '1', '华东区', '1', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('3', '1', '华南区', '2', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('4', '2', '市场一部', '0', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('5', '2', '市场二部', '1', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('6', '2', '市场三部', '2', '0');

delete from sys_user;

-- 初始数据
INSERT INTO `sys_user` (`user_id`, `merchant_no`,`username`, `password`, `salt`, `email`,
                        `mobile`, `status`, `create_user_id`, `create_time`)
VALUES ('1', '00', 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d',
        'YzcmCZNvbXocrsz9dm8e', 'admin@grx.io', '19900001111', '1', '1', '2017-11-11 11:11:11');


INSERT INTO sequence VALUES ('seq_tx_uuid', '0', '1', CURDATE());


delete from schedule_job;
#
# INSERT INTO `schedule_job` (`bean_name`, `method_name`, `params`, `cron_expression`, `status`, `remark`, `create_time`)
# VALUES ('authTask', 'getSjmhReports', NULL, '0 0/5 * * * ?', '1', '获取认证报告', CURRENT_TIMESTAMP);

INSERT INTO `schedule_job` (`bean_name`, `method_name`, `params`, `cron_expression`, `status`, `remark`, `create_time`)
VALUES ('txTask', 'deleteExpiredTx', NULL, '0 0 * * * ?', '1', '删除过期借条', CURRENT_TIMESTAMP);

INSERT INTO `schedule_job` (`bean_name`, `method_name`, `params`, `cron_expression`, `status`, `remark`, `create_time`)
VALUES ('txTask', 'markOverdueStatus', NULL, '0 0 1-3 * * ?', '1', '逾期状态更新', CURRENT_TIMESTAMP);

INSERT INTO `schedule_job` (`bean_name`, `method_name`, `params`, `cron_expression`, `status`, `remark`, `create_time`)
VALUES ('reportTask', 'newTxStat', NULL, '0 0 7 * * ?', '1', '发送借条报告', CURRENT_TIMESTAMP);

INSERT INTO `schedule_job` (`bean_name`, `method_name`, `params`, `cron_expression`, `status`, `remark`, `create_time`)
VALUES ('cxbAuthTask', 'expireAuthReports', NULL, '0 30 0 * * ?', '1', '标记认证报告过期', CURRENT_TIMESTAMP);


INSERT INTO `schedule_job` (`bean_name`, `method_name`, `params`, `cron_expression`, `status`, `remark`, `create_time`)
VALUES ('txTask', 'patchExtensions', NULL, '0 30 0 * * ?', '1', '处理已支付但未更新展期', CURRENT_TIMESTAMP);

delete from sys_config;

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'AUTH_CONFIG', '1,2,3', 1, null);

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'TRANSACTION_FEE', '0', 1, null);

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'EXTENSION_FEE', '0', 1, null);

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'REPORT_NOTICE_MOBILE', '13631235365', 1, null);

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'FREE_TX_LENDER_NAME', '[]', 1, '如果有最近的认证,这些出借人的借条免费,配合FREE_TX_DEPT_ID及FREE_TX_RECENT_AUTH_DAY使用');
INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'FREE_TX_DEPT_ID', '[]', 1, '可以免借条费的部门');
INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'FREE_TX_RECENT_AUTH_DAY', '1', 1, '最近X天内的认证可以免费认证费');

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'AUTH_TYPE_CONFIG', '[{"authTypeName":"MOBILE","enabled":true,"expiredDay":14},{"authTypeName":"JD","enabled":true,"expiredDay":14},{"authTypeName":"ALIPAY","enabled":true,"expiredDay":14},{"authTypeName":"CREDIT","enabled":true,"expiredDay":14},{"authTypeName":"CREDIT_EMAIL","enabled":true,"expiredDay":14},{"authTypeName":"TAOBAO_CRAWL","enabled":true,"expiredDay":14},{"authTypeName":"INSURE","enabled":true,"expiredDay":14},{"authTypeName":"FUND","enabled":true,"expiredDay":14},{"authTypeName":"JINJIEDAO","enabled":true,"expiredDay":1},{"authTypeName":"WUYOUJIETIAO","enabled":true,"expiredDay":1},{"authTypeName":"MIFANG","enabled":true,"expiredDay":1},{"authTypeName":"JIEDAIBAO","enabled":true,"expiredDay":1}]', 1, '认证报告配置详情');


INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'COMMISSION_RATE_1', '0.22', 1, '二级推广提成费率');

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'COMMISSION_RATE_2', '0.066', 1, '三级级推广提成费率');


INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'WITHDRAWAL_FEE_RATE', '0.001', 1, '提现手续费率');

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'WITHDRAWAL_MIN_FEE', '0', 1, '提现手续下限');

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'WITHDRAWAL_MAX_FEE', '0', 1, '提现手续费上限');

INSERT INTO sys_config (merchant_no, `key`, value, status, remark)
VALUES ('00', 'WITHDRAWAL_MIN_AMOUNT', '100', 1, '最小提现金额');
