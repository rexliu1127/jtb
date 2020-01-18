-- 菜单
drop table if EXISTS  `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) COMMENT '菜单名称',
  `url` varchar(200) COMMENT '菜单URL',
  `perms` varchar(500) COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) COMMENT '菜单图标',
  `order_num` int COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单管理';


-- 部门
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL DEFAULT '00' COMMENT '商家编号',
  `parent_id` bigint COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) COMMENT '部门名称',
  `order_num` int COMMENT '排序',
  `del_flag` tinyint DEFAULT 0 COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门管理';

-- 系统用户
drop table if EXISTS  `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL DEFAULT '00' COMMENT '商家编号',
  `dept_id` bigint(20) COMMENT '部门ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `name` varchar(50) COMMENT '用户名姓名',
  `password` varchar(100) COMMENT '密码',
  `salt` varchar(20) COMMENT '盐',
  `email` varchar(100) COMMENT '邮箱',
  `mobile` varchar(100) COMMENT '手机号',
  `status` tinyint COMMENT '状态  0：禁用   1：正常',
  `is_channel_user` bit NOT NULL DEFAULT 0 COMMENT '是否渠道用户  0：否   1：是',
  `create_user_id` bigint(20) COMMENT '创建者ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  index(dept_id),
  UNIQUE INDEX (`username`, `merchant_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户';

drop table if EXISTS  `sys_user_token`;
-- 系统用户Token
CREATE TABLE `sys_user_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '系统用户ID',
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  index (user_id),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户Token';

-- 角色
drop table if EXISTS  `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL DEFAULT '00' COMMENT '商家编号',
  `role_name` varchar(100) COMMENT '角色名称',
  `remark` varchar(100) COMMENT '备注',
  `create_user_id` bigint(20) COMMENT '创建者ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

-- 用户与角色对应关系
drop table if EXISTS  `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint COMMENT '用户ID',
  `role_id` bigint COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- 角色与菜单对应关系
drop table if EXISTS  `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint COMMENT '角色ID',
  `menu_id` bigint COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';


-- 系统配置信息
drop table if EXISTS  `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL DEFAULT '00' COMMENT '商家编号',
  `key` varchar(50) COMMENT 'key',
  `value` varchar(2000) COMMENT 'value',
  `status` tinyint DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`merchant_no`, `key`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='系统配置信息表';

-- 系统日志
drop table if EXISTS  `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL DEFAULT '00' COMMENT '商家编号',
  `username` varchar(50) COMMENT '用户名',
  `operation` varchar(50) COMMENT '用户操作',
  `method` varchar(200) COMMENT '请求方法',
  `params` varchar(5000) COMMENT '请求参数',
  `time` bigint NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) COMMENT 'IP地址',
  `create_date` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='系统日志';


-- Tx
-- 渠道
drop table if EXISTS  `channel`;
CREATE TABLE `channel` (
  `channel_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `name` varchar(50) COMMENT '渠道名称',
  `product_name` VARCHAR(50) COMMENT '产品名称',
  `dept_id` bigint(20) COMMENT '部门ID',
  `channel_key` varchar(200) COMMENT '渠道key',
  `image_path` varchar(1000) COMMENT '欢迎页背景图片url',
  `owner_user_id` bigint(20) COMMENT '负责人ID',
  `auditor_user_id` bigint(20) COMMENT '审核者ID',
  `create_user_id` bigint(20) COMMENT '创建者ID',
  `create_time` datetime COMMENT '创建时间',
  `status` tinyint DEFAULT 1 COMMENT '状态  0：禁用   1：正常',
  PRIMARY KEY (`channel_id`),
  index(channel_key),
  index(dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渠道';


-- 交易用户
drop table if EXISTS  `tx_user`;
CREATE TABLE `tx_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `wechat_id` varchar(100) COMMENT '微信openId',
  `union_id` varchar(100) COMMENT '微信unionId',
  `nick_name` varchar(50) COMMENT '微信昵称',
  `head_img_url` varchar(1000) COMMENT '微信头像url',
  `mobile` varchar(100) NOT NULL COMMENT '手机号',
  `name` varchar(20) COMMENT '真实姓名',
  `name_pinyin` varchar(400) COMMENT '真实姓名拼音首字母',
  `id_no` varchar(20) COMMENT '身份证号',
  `bank_account` varchar(100) COMMENT '银行帐号',
  `bank_name` varchar(100) COMMENT '银行名称',
  `sign_img_path` varchar(400) COMMENT '签名图片路径',
  `create_user_id` bigint(20) COMMENT '创建者ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX (`wechat_id`),
  UNIQUE INDEX (`union_id`, `merchant_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易用户';


-- 交易用户
drop table if EXISTS  `tx_user_relation`;
CREATE TABLE `tx_user_relation` (
  `relation_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `friend_user_id` BIGINT NOT NULL COMMENT '朋友ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`relation_id`),
  INDEX (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易用户关系';



-- 交易用户
drop table if EXISTS  `tx_user_password`;
CREATE TABLE `tx_user_password` (
  `user_id` bigint NOT NULL,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `password` varchar(100) NOT NULL COMMENT '交易密码',
  `salt` varchar(100) COMMENT '盐',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易用户密码';


-- 用户余额
drop table if EXISTS  `tx_user_balance`;
CREATE TABLE `tx_user_balance` (
  `user_id` bigint NOT NULL,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `balance` DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '用户余额',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户余额';


-- 用户余额记录
drop table if EXISTS  `tx_user_balance_log`;
CREATE TABLE `tx_user_balance_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `tx_id` bigint COMMENT '借条ID',
  `extension_id` bigint COMMENT '展期ID',
  `merchant_no` varchar(50) COMMENT '商家编号',
  `income` DECIMAL(10,2) COMMENT '余额收入',
  `expense` DECIMAL(10,2) COMMENT '余额支出',
  `balance` DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '用户余额',
  PRIMARY KEY (`id`),
  index (`user_id`),
  index (`tx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户余额记录';


-- 用户邀请记录
drop table if EXISTS  `tx_user_invite`;
CREATE TABLE `tx_user_invite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `inviter_user_id` bigint COMMENT '邀请人用户ID',
  `inviter_agent_no` varchar(100) COMMENT '邀请人渠道商ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`),
  index (`user_id`),
  index (`inviter_user_id`),
  index (`inviter_agent_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户邀请记录';


-- 用户奖励
drop table if EXISTS  `tx_user_reward`;
CREATE TABLE `tx_user_reward` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `invitee_user_id` bigint NOT NULL COMMENT '被邀请人用户ID',
  `tx_id` bigint COMMENT '借条ID',
  `extension_id` bigint COMMENT '展期ID',
  `level` tinyint COMMENT '推广级别',
  `reward` DECIMAL(10,2) COMMENT '奖励',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`),
  index (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户奖励';


-- 用户提现记录
drop table if EXISTS  `tx_user_withdraw`;
CREATE TABLE `tx_user_withdraw` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `amount` DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '提现金额(含手续费)',
  `fee_amount` DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '提现手续费',
  `status` TINYINT NOT NULL COMMENT '提现状态',
  `create_time` datetime COMMENT '提交时间',
  `approval_user_id` bigint COMMENT '审核人用户ID',
  `approval_time` datetime COMMENT '审核时间',
  `complete_time` datetime COMMENT '放款时间',
  `actual_amount` int COMMENT '到账金额',
  `bank_account` varchar(100) COMMENT '银行帐号',
  `bank_name` varchar(100) COMMENT '银行名称',
  `tranx_id` varchar(100) COMMENT '提现SN',
  `tranx_code` varchar(100) COMMENT '提现状态代码',
  `tranx_message` varchar(100) COMMENT '提现状态信息',
   PRIMARY KEY (`id`),
   index(`user_id`),
   index(`approval_user_id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户提现记录';



drop table IF EXISTS tx_base;

-- 交易基本资料
drop table if EXISTS  `tx_base`;
CREATE TABLE `tx_base` (
  `tx_id` bigint NOT NULL AUTO_INCREMENT,
  `tx_uuid` VARCHAR(40) NOT NULL COMMENT '交易全局ID',
  `merchant_no` varchar(50) COMMENT '商家编号',
  `amount` int NOT NULL COMMENT '借款金额',
  `begin_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '还款日期',
  `rate` int NOT NULL COMMENT '利率*100',
  `interest` DECIMAL(18,2) NOT NULL COMMENT '预计利息',
  `fee_amount` DECIMAL(10,2) COMMENT '费用',
  `remark` varchar(1000) COMMENT '费用',
  `usage_type` TINYINT COMMENT '借款用途类型',
  `usage_remark` varchar(1000) COMMENT '借款用途补充说明',
  `borrower_name` varchar(20) COMMENT '借款人姓名',
  `lender_name` varchar(20) COMMENT '出借人姓名',
  `borrower_user_id` bigint(20) COMMENT '借款人ID',
  `lender_user_id` bigint(20) COMMENT '出借人ID',
  `borrower_sign_img_path` varchar(400) COMMENT '借款人签名path',
  `lender_sign_img_path` varchar(400) COMMENT '出借人签名path',
  `create_user_id` bigint(20) COMMENT '创建者ID',
  `status` TINYINT NOT NULL COMMENT '状态',
  `overdue_date` date COMMENT '逾期日期',
  `repay_date` date COMMENT '实际还款日期',
  `outstanding_amount` int DEFAULT 0 COMMENT '未还金额',
  `outstanding_interest` DECIMAL(18,2) COMMENT '剩余利息',
  -- `total_amount` DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '总还款金额',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `deleted` bit NOT NULL DEFAULT 0 COMMENT '是否删除  0：否   1：是',
  PRIMARY KEY (`tx_id`),
  UNIQUE INDEX (`tx_uuid`),
  INDEX (`end_date`),
  INDEX (`borrower_user_id`),
  INDEX (`lender_user_id`),
  INDEX (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易基本资料';


-- 还款记录
drop table if EXISTS  `tx_repayment`;
CREATE TABLE `tx_repayment` (
  `repayment_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `tx_id` bigint NOT NULL COMMENT '借条ID',
  `repayment_type` tinyint NOT NULL COMMENT '还款方式',
  -- `repay_amount` DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '还款金额',
  `status` TINYINT NOT NULL COMMENT '状态',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`repayment_id`),
  INDEX (`tx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='还款记录';

-- 还款记录
drop table if EXISTS  `tx_extension`;
CREATE TABLE `tx_extension` (
  `extension_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `tx_id` bigint NOT NULL COMMENT '借条ID',
  `old_rate` int COMMENT '原利率',
  `rate` int NOT NULL COMMENT '展期利率',
  `extend_amount` int NOT NULL COMMENT '展期金额',
  -- `extension_amount` DECIMAL(18,2) COMMENT '展期金额',
  `fee_amount` DECIMAL(10,2) COMMENT '费用',
  `old_end_date` date COMMENT '原到期日期',
  `new_end_date` date NOT NULL COMMENT '新到期日期',
  `status` TINYINT NOT NULL COMMENT '状态',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`extension_id`),
  INDEX (`tx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='展期记录';

-- 还款计划
drop table if EXISTS  `tx_repay_plan`;
CREATE TABLE `tx_repay_plan` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) COMMENT '商家编号',
  `tx_id` bigint NOT NULL COMMENT '借条ID',
  `begin_date` date NOT NULL COMMENT '到期日期',
  `end_date` date NOT NULL COMMENT '到期日期',
  `planned_amount` int NOT NULL COMMENT '借款金额',
  `planned_interest` DECIMAL(18,2) NOT NULL COMMENT '预计利息',
  `actual_amount` int COMMENT '还款金额',
  `actual_interest` DECIMAL(18,2) COMMENT '还款利息',
  `repay_date` date COMMENT '还期日期',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`plan_id`),
  INDEX (`tx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='还款计划';


drop table if exists sequence;
create table sequence (
  seq_name        VARCHAR(50) NOT NULL, -- 序列名称
  current_val     INT         NOT NULL, -- 当前值
  increment_val   INT         NOT NULL    DEFAULT 1, -- 步长(跨度)
  last_date       date,
  PRIMARY KEY (seq_name));

create function currval(v_seq_name VARCHAR(50))
  returns integer
  begin
    declare value integer;
    set value = 0;
    select current_val into value from sequence where seq_name = v_seq_name;
    return value;
  end;

create function nextval(v_seq_name VARCHAR(50))
  returns integer
  begin
    update sequence set last_date = CURDATE(), current_val = 0 where seq_name = v_seq_name
                                                                     and (last_date is null or last_date < CURDATE());
    update sequence set current_val = current_val + increment_val  where seq_name = v_seq_name;
    return currval(v_seq_name);
  end;


-- 用户与渠道对应关系
drop table if EXISTS sys_user_channel;
CREATE TABLE `sys_user_channel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint COMMENT '用户ID',
  `channel_id` bigint COMMENT '渠道ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渠道用户与渠道对应关系';

-- 认证用户
drop table if EXISTS auth_user;
CREATE TABLE `auth_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `channel_id` bigint(20) COMMENT '渠道ID',
  `mobile` varchar(50) NOT NULL COMMENT '菜单名称',
  `name` varchar(50) COMMENT '用户名称',
  `wechat_id` varchar(50) COMMENT '微信ID',
  `head_image_url` varchar(400) COMMENT '头像url',
  `id_no` varchar(50) COMMENT '身份证号码',
  `qq_no` varchar(50) COMMENT 'QQ号码',
  `wechat_no` varchar(50) COMMENT '微信号码',
  `company_name` varchar(100) COMMENT '公司名称',
  `company_addr` varchar(400) COMMENT '公司单位地址',
  `company_tel` varchar(100) COMMENT '公司联系方式',
  `company_job` varchar(100) COMMENT '公司职位',
  `salary` varchar(100) COMMENT '薪水',
  `sesame_points` varchar(20) COMMENT '芝麻分',
  `mobile_pass` varchar(400) COMMENT '手机服务密码',
  `contact1_type` TINYINT COMMENT '联系人1类型',
  `contact1_name` varchar(50) COMMENT '联系人1名字',
  `contact1_mobile` varchar(50) COMMENT '联系人1电话',
  `contact2_type` TINYINT COMMENT '联系人2类型',
  `contact2_name` varchar(50) COMMENT '联系人2名字',
  `contact2_mobile` varchar(50) COMMENT '联系人2电话',
  `contact3_type` TINYINT COMMENT '联系人3类型',
  `contact3_name` varchar(50) COMMENT '联系人3名字',
  `contact3_mobile` varchar(50) COMMENT '联系人3电话',
  `id_url_1` varchar(400) COMMENT '身份证正面url',
  `id_url_2` varchar(400) COMMENT '身份证反面url',
  `id_url_3` varchar(400) COMMENT '手持身份证url',
  `create_time` datetime COMMENT '创建时间',
  `machine_id` varchar(400) COMMENT 'app机器ID',
  `auth_status` bit NOT NULL DEFAULT 0 COMMENT '认证结果  0：失败   1：通过',
  `auth_report_url` varchar(1000) COMMENT '认证报告url',
  `auth_task_id` varchar(400) COMMENT '认证ID',
  PRIMARY KEY (`user_id`),
  index(id_no),
  index(machine_id),
  index(auth_task_id)
  UNIQUE KEY (`merchant_no`, `mobile`, `channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='认证用户';


-- 认证用户Token
drop table if EXISTS auth_user_token;
CREATE TABLE `auth_user_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='认证用户Token';


-- 申请单
drop table if EXISTS auth_request;
CREATE TABLE `auth_request` (
  `request_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `request_uuid` varchar(50) NOT NULL COMMENT '请求UUID',
  `user_id` bigint(20) NOT NULL COMMENT '申请用户ID',
  `dept_id` bigint(20) COMMENT '部门ID',
  `processor_id` bigint(20) COMMENT '处理系统用户ID',
  `assignee_id` bigint(20) COMMENT '客服系统用户ID',
  `channel_id` bigint(20) COMMENT '渠道ID',
  `name` varchar(50) COMMENT '用户名称',
  `id_no` varchar(50) COMMENT '身份证号码',
  `qq_no` varchar(50) COMMENT 'QQ号码',
  `gps_addr` varchar(400) COMMENT 'GPS地址信息',
  `mobile_pass` varchar(400) COMMENT '手机服务密码',
  `contact1_type` TINYINT COMMENT '联系人1类型',
  `contact1_name` varchar(50) COMMENT '联系人1名字',
  `contact1_mobile` varchar(50) COMMENT '联系人1电话',
  `contact2_type` TINYINT COMMENT '联系人2类型',
  `contact2_name` varchar(50) COMMENT '联系人2名字',
  `contact2_mobile` varchar(50) COMMENT '联系人2电话',
  `contact1_call_count` int COMMENT '联系人1最近3月通话次数',
  `contact2_call_count` int COMMENT '联系人2最近3月通话次数',
  `id_url_1` varchar(400) COMMENT '身份证正面url',
  `id_url_2` varchar(400) COMMENT '身份证反面url',
  `id_url_3` varchar(400) COMMENT '手持身份证url',
  `status` TINYINT COMMENT '状态',
  `verify_token` varchar(50) COMMENT '认证token',
  `verify_status` TINYINT COMMENT '状态',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `phone_checked` bit COMMENT '运营商三要素是否匹配  0：否   1：是  null:未知',
  `id_no_matched` bit NOT NULL DEFAULT 0 COMMENT '身份证是否匹配运营商信息  0：否   1：是',
  `name_matched` bit NOT NULL DEFAULT 0 COMMENT '身份证是否匹配运营商信息  0：否   1：是',
  `vendor_type` TINYINT COMMENT '第三方认证商: 0-同盾数据魔盒, 1-聚信力',
  `wechat_no` varchar(50) COMMENT '微信号码',
  `company_name` varchar(100) COMMENT '公司名称',
  `company_addr` varchar(400) COMMENT '公司单位地址',
  `company_tel` varchar(100) COMMENT '公司联系方式',
  `contact3_type` TINYINT COMMENT '联系人3类型',
  `contact3_name` varchar(50) COMMENT '联系人3名字',
  `contact3_mobile` varchar(50) COMMENT '联系人3电话',
  PRIMARY KEY (`request_id`),
  index(dept_id),
  index(user_id),
  index(id_no),
  index(verify_token),
  index(channel_id),
  index(verify_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请单';

-- 申请单历史
drop table if exists auth_request_history;
CREATE TABLE `auth_request_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `request_id` bigint(20) NOT NULL COMMENT '申请单ID',
  `processor_id` bigint(20) COMMENT '处理系统用户ID',
  `status` TINYINT COMMENT '处理状态',
  `user_remark` varchar(400) COMMENT '审核备注',
  `admin_remark` varchar(400) COMMENT '跟踪备注',
  `create_user_id` bigint(20) COMMENT '操作人ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请单历史';


-- 借条用户Token
drop table if EXISTS tx_user_token;
CREATE TABLE `tx_user_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借条用户Token';


-- 用户认证报告(聚信立)
drop table if EXISTS jxl_report;
CREATE TABLE `jxl_report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(100) COMMENT 'token',
  `request_id` bigint(20) NOT NULL COMMENT '申请单ID',
  `report_data` LONGTEXT COMMENT '用户报告内容(json)',
  `raw_data` LONGTEXT COMMENT '用户移动运营商原始数据(json)',
  `mobile_data` LONGTEXT COMMENT '用户移动运营商原始数据(json)',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  index(token),
  index(request_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户认证报告(聚信立)';

create TABLE `tx_daily_report` (
  `report_date` varchar(30) NOT NULL COMMENT '报告日期',
  `new_user_count` int NOT NULL COMMENT '新增用户数',
  `new_user_tx_bount` int NOT NULL COMMENT '新增用户借条数',
  `old_user_tx_bount` int NOT NULL COMMENT '旧用户借条数',
  `total_tx_bount` int NOT NULL COMMENT '总借条数',
  `total_ex_count` int NOT NULL DEFAULT 0 COMMENT '总展期数',
  PRIMARY KEY (`report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日借条统计';

-- 支付记录
drop table if exists pay_record;
CREATE TABLE `pay_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `tx_id` bigint(20) COMMENT '借条ID',
  `pay_user_id` bigint(20) COMMENT '支付用户ID',
  `extension_id` bigint(20) COMMENT '借条ID',
  `order_no` VARCHAR(100) COMMENT '系统订单号',
  `amount` INT NOT NULL COMMENT '支付金额',
  `account_id` varchar(100) COMMENT '支付人openId',
  `trx_id` varchar(100) COMMENT '收银宝平台的交易流水号',
  `out_trx_id` varchar(100) COMMENT '收银宝平台的交易流水号',
  `chnl_trx_id` varchar(100) COMMENT '例如微信,支付宝平台的交易单号',
  `request_trx_status` varchar(100) COMMENT '下单状态',
  `pay_trx_status` varchar(100) COMMENT '支付状态',
  `fin_time` varchar(100) COMMENT '下单时间',
  `pay_time` varchar(100) COMMENT '完成时间',
  `error_message` varchar(100) COMMENT '错误信息',
  `status` TINYINT COMMENT '状态',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`),
  index(pay_user_id),
  index(trx_id),
  index(tx_id),
  index(extension_id),
  index(create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付记录';


-- 逾期记录
drop table if exists tx_overdue_record;
CREATE TABLE `tx_overdue_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `tx_id` bigint(20) NOT NULL COMMENT '借条ID',
  `overdue_date` date NOT NULL COMMENT '逾期开始日期',
  `overdue_end_date` date COMMENT '逾期结束日期',
  `overdueDays` int COMMENT '逾期天数',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`),
  index(tx_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='逾期记录';



-- 逾期记录
drop table if exists auth_missing_record;
CREATE TABLE `auth_missing_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `verify_token` varchar(100) NOT NULL COMMENT 'token',
  `mobile` varchar(100) NOT NULL COMMENT 'mobile',
  `status` int COMMENT '状态',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='丢失记录';

-- 支付扫描记录
drop table if exists pay_scan_record;
CREATE TABLE `pay_scan_record` (
  `pay_id` varchar(100) NOT NULL,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `order_no` VARCHAR(100) COMMENT '系统订单号',
  `pay_type` VARCHAR(100) COMMENT '订单类型',
  `order_id` BIGINT COMMENT '借条或展期ID',
  `pay_user_id` BIGINT COMMENT '订单用户ID',
  `status` TINYINT COMMENT '状态',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付扫描记录';


-- 通讯录
drop table if EXISTS auth_user_contact;
CREATE TABLE `auth_user_contact` (
  `user_id` bigint NOT NULL,
  `contact` LONGTEXT COMMENT '联系人内容',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通讯录';


-- APP机器通讯录
drop table if EXISTS auth_machine_contact;
CREATE TABLE `auth_machine_contact` (
  `machine_id` varchar(400) NOT NULL,
  `contact` LONGTEXT COMMENT '联系人内容',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`machine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='APP机器通讯录';


-- 电商认证报告
drop table if EXISTS auth_user_report_ds;
CREATE TABLE `auth_user_report_ds` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `task_id` varchar(50) COMMENT '认证TASK ID',
  `ds_type` tinyint COMMENT '电商类型  0：京东   1：苏宁',
  `name` varchar(50) COMMENT '用户名称',
  `id_no` varchar(50) COMMENT '身份证号码',
  `mobile` varchar(50) COMMENT '手机号',
  `report_data` LONGTEXT COMMENT '联系人内容',
  `verify_status` TINYINT COMMENT '状态',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`),
  index(task_id),
  index(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='电商认证';

drop table if EXISTS tj_report;
CREATE TABLE `tj_report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `task_id` varchar(50) COMMENT '认证TASK ID',
  `search_id` varchar(50) COMMENT '认证Search ID',
  `tianji_type` tinyint COMMENT '报告类型',
  `tianji_state` tinyint COMMENT '报告状态',
  `name` varchar(50) COMMENT '用户名称',
  `id_no` varchar(50) COMMENT '身份证号码',
  `mobile` varchar(50) COMMENT '手机号',
  `report_json_path` varchar(50) COMMENT '报告路径json',
  `report_html_path` varchar(50) COMMENT '报告路径html',
  `verify_status` TINYINT COMMENT '状态',
  `is_expired` bit NOT NULL DEFAULT 0 COMMENT '是否过期  0：否   1：是',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`),
  index(task_id),
  index(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='天机认证';



drop table if EXISTS yx_report;

CREATE TABLE `yx_report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `user_id` bigint COMMENT '用户ID',
  `task_id` varchar(50) COMMENT '认证TASK ID',
  `search_id` varchar(50) COMMENT '认证Search ID',
  `yi_xiang_type` tinyint COMMENT '报告类型',
  `name` varchar(50) COMMENT '用户名称',
  `id_no` varchar(50) COMMENT '身份证号码',
  `mobile` varchar(50) COMMENT '手机号',
  `report_json_path` varchar(400) COMMENT '报告路径json',
  `report_html_path` varchar(400) COMMENT '报告路径html',
  `verify_status` TINYINT COMMENT '状态',
  `is_expired` bit NOT NULL DEFAULT 0 COMMENT '是否过期  0：否   1：是',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '列新时间',
  PRIMARY KEY (`id`),
  index(task_id),
  index(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='亿象认证';


-- 文件上传
CREATE TABLE `sys_oss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) COMMENT 'URL地址',
  `create_date` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COMMENT='文件上传';


-- 申请单
drop table if EXISTS auth_recommend;
CREATE TABLE `auth_recommend` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `mobile` varchar(50) NOT NULL COMMENT '用户手机',
  `from_user_id` bigint(20) NOT NULL COMMENT '源用户ID',
  `to_user_id` bigint(20) NOT NULL COMMENT '目标用户ID',
  `create_user_id` bigint(20) COMMENT '操作人用户ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请单推荐记录';


-- 客服人员
drop table if EXISTS auth_staff;
CREATE TABLE `auth_staff` (
  `staff_id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `staff_type` TINYINT NOT NULL COMMENT '客服类型: 0-微信 1-QQ',
  `staff_no` varchar(50) NOT NULL COMMENT '客服号码',
  `staff_barcode` MEDIUMTEXT COMMENT '客服二维码图片',
  `processor_id` bigint(20) COMMENT '关联系统用户ID',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`staff_id`),
  index (`staff_no`),
  index (`processor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客服人员';

alter table sys_merchant add `logo` MEDIUMTEXT COMMENT '公司logo';

-- 通讯录助手
drop table if EXISTS contact_user;
CREATE TABLE `contact_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `mobile` varchar(50) NOT NULL COMMENT '商家编号',
  `contact` LONGTEXT COMMENT '联系人内容',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通讯录助手';


drop table if EXISTS auth_credit;
CREATE TABLE `auth_credit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `task_id` varchar(50) COMMENT '认证TASK ID',
  `verify_status` TINYINT COMMENT '状态',
  `is_expired` bit NOT NULL DEFAULT 0 COMMENT '是否过期  0：否   1：是',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`),
  index(task_id),
  index(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='认证记录';

drop table if EXISTS sys_channel_user;
CREATE TABLE `sys_channel_user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`channel_id` BIGINT (20) NOT NULL COMMENT '渠道ID',
	`user_id` BIGINT (20) NOT NULL COMMENT '系统用户ID',
	PRIMARY KEY (`id`),
	index (`channel_id`),
	index (`user_id`)
) COMMENT='渠道与审核人员对应关系';


DROP TABLE IF EXISTS `tx_lender`;
CREATE TABLE `tx_lender` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mobile` varchar(50) DEFAULT NULL COMMENT '出借人手机号码',
  `name` varchar(60) DEFAULT NULL COMMENT '出借人姓名',
  `merchant_no` varchar(50) DEFAULT NULL COMMENT '商户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态(0无效，1有效)',
  PRIMARY KEY (`id`),
  index(mobile)
) COMMENT='出借人';

DROP TABLE IF EXISTS `sys_fun`;

CREATE TABLE `sys_fun` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `merchant_no` varchar(50) NOT NULL COMMENT '商家编号',
  `total_amount` decimal(16,4) DEFAULT NULL COMMENT '总额',
  `remaining_sum` decimal(16,4) DEFAULT NULL COMMENT '可用余额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `isrist` tinyint(1) DEFAULT NULL COMMENT '是否有风险',
  `merchant_name` varchar(50) DEFAULT NULL COMMENT '商户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='账户总览表';


DROP TABLE IF EXISTS `sys_fun_record`;

CREATE TABLE `sys_fun_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `fun_amount` decimal(16,4) DEFAULT NULL COMMENT '充值金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `merchant_no` varchar(50) DEFAULT NULL COMMENT '商家编号',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `merchant_name` varchar(50) DEFAULT NULL COMMENT '商户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值记录表';




DROP TABLE IF EXISTS `sys_fun_details`;

CREATE TABLE `sys_fun_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '关联申请用户id',
  `borrower_phone` varchar(60) DEFAULT NULL COMMENT '借款人手机',
  `fun_type` bigint(20) DEFAULT NULL COMMENT '关联费用类型',
  `amount` decimal(16,4) DEFAULT NULL COMMENT '费用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `merchant_name` varchar(50) DEFAULT NULL COMMENT '商户名称',
  `merchant_no` varchar(50) DEFAULT NULL COMMENT '商家编号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `task_id` varchar(100) DEFAULT NULL COMMENT '交易编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户费用明细表';


DROP TABLE IF EXISTS `sys_notice`;

CREATE TABLE `sys_notice`(
  `notice_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_title` VARCHAR(100) COMMENT '标题',
  `notice_status` tinyint(1) COMMENT '是否发布0-不发布，1-发布',
  `notice_content` text COMMENT '内容',
  `create_time` DATETIME COMMENT '创建时间',
  `create_by` BIGINT(20) COMMENT '发布人',
  PRIMARY KEY (`notice_id`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci
COMMENT='公告表';