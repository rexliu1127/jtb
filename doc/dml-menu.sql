delete from sys_menu;

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (1, 0, '系统管理', null, null, 0, 'fa fa-cog', 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (2, 1, '管理员列表', 'modules/sys/user.html', null, 1, 'fa fa-user', 1);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (3, 1, '角色管理', 'modules/sys/role.html', null, 1, 'fa fa-user-secret', 2);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (4, 1, '菜单管理', 'modules/sys/menu.html', null, 1, 'fa fa-th-list', 3);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (5, 1, 'SQL监控', 'druid/sql.html', null, 1, 'fa fa-bug', 4);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (6, 1, '定时任务', 'modules/job/schedule.html', null, 1, 'fa fa-tasks', 5);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (7, 6, '查看', null, 'sys:schedule:list,sys:schedule:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (8, 6, '新增', null, 'sys:schedule:save', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (9, 6, '修改', null, 'sys:schedule:update', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (10, 6, '删除', null, 'sys:schedule:delete', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (11, 6, '暂停', null, 'sys:schedule:pause', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (12, 6, '恢复', null, 'sys:schedule:resume', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (13, 6, '立即执行', null, 'sys:schedule:run', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (14, 6, '日志列表', null, 'sys:schedule:log', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (15, 2, '查看', null, 'sys:user:list,sys:user:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (16, 2, '新增', null, 'sys:user:save,sys:role:select', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (17, 2, '修改', null, 'sys:user:update,sys:role:select', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (18, 2, '删除', null, 'sys:user:delete', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (19, 3, '查看', null, 'sys:role:list,sys:role:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (20, 3, '新增', null, 'sys:role:save,sys:menu:list', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (21, 3, '修改', null, 'sys:role:update,sys:menu:list', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (22, 3, '删除', null, 'sys:role:delete', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (23, 4, '查看', null, 'sys:menu:list,sys:menu:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (24, 4, '新增', null, 'sys:menu:save,sys:menu:select', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (25, 4, '修改', null, 'sys:menu:update,sys:menu:select', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (26, 4, '删除', null, 'sys:menu:delete', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (27, 1, '参数管理', 'modules/sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'fa fa-sun-o', 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (29, 1, '系统日志', 'modules/sys/log.html', 'sys:log:list', 1, 'fa fa-file-text-o', 7);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (30, 1, '文件上传', 'modules/oss/oss.html', 'sys:oss:all', 1, 'fa fa-file-image-o', 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (31, 0, '运营管理', null, null, 0, 'fa fa-bars', 2);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (32, 31, '渠道列表', 'modules/opt/channel.html', null, 1, 'fa fa-cubes', 1);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (33, 32, '查看', null, 'opt:channel:list,opt:channel:info', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (34, 32, '新增', null, 'opt:channel:save', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (35, 32, '修改', null, 'opt:channel:update', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (36, 32, '删除', null, 'opt:channel:delete', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (37, 31, '渠道帐号列表', 'modules/sys/channel_user.html', null, 1, 'fa fa-user-o', 2);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (38, 37, '查看', null, 'sys:channel_user:list,sys:channel_user:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (39, 37, '修改', null, 'sys:channel_user:update', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (40, 37, '新增', null, 'sys:channel_user:save', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (41, 37, '删除', null, 'sys:channel_user:delete', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (42, 0, '借条管理', null, null, 0, 'fa fa-credit-card', 1);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (43, 42, '借条列表', 'modules/tx/tx_base.html', 'tx:txbase:list', 1, 'fa fa-money', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (44, 1, '认证配置', 'modules/opt/auth_config.html', 'opt:auth_config:save', 1, 'fa fa-certificate', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (45, 50, '申请单列表', 'modules/auth/request.html', null, 1, 'fa fa-list-ol', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (46, 45, '查看', null, 'auth:request:list,auth:request:info', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (47, 45, '分配', null, 'auth:request:allocate', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (48, 45, '处理', null, 'auth:request:process', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (49, 45, '导出', null, 'auth:request:export', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (50, 0, '申请单管理', null, null, 0, 'fa fa-list-alt', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (51, 31, '渠道订单列表', 'modules/opt/order_list.html', 'opt:channel:order_list', 1, 'fa fa-list-alt', 3);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (52, 0, '统计报表', null, null, 0, 'fa fa-bar-chart', 3);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (53, 52, '申请单转化率', 'modules/stat/request_stat.html', 'stat:authuser', 1, 'fa fa-line-chart', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (54, 1, '部门管理', 'modules/sys/dept.html', null, 1, 'fa fa-file-code-o', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (55, 54, '查看', null, 'sys:dept:list,sys:dept:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (56, 54, '新增', null, 'sys:dept:save,sys:dept:select', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (57, 54, '修改', null, 'sys:dept:update,sys:dept:select', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (58, 54, '删除', null, 'sys:dept:delete', 2, null, 0);

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (59, 42, '借条申诉', 'modules/tx/txcomplain.html', null, 1, 'fa fa-file-code-o', 1);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (60, 59, '查看', null, 'tx:txcomplain:list,tx:txcomplain:info', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (61, 59, '处理申诉', null, 'tx:txcomplain:update', 2, null, 6);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (62, 42, '借条用户管理', 'modules/tx/tx_user.html', null, 1, 'fa fa-file-code-o', 2);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (63, 62, '查看', null, 'tx:user:list', 2, null, 6);

INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (64, '1', '公司管理', 'modules/sys/sysmerchant.html', NULL, '1', 'fa fa-file-code-o', '6');

INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
  SELECT 65, 64, '查看', null, 'sys:sysmerchant:list,sys:sysmerchant:info', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
  SELECT 66, 64, '修改', null, 'sys:sysmerchant:update', '2', null, '6';

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (67, 43, '销账', null,
                                                                                           'tx:txbase:update', 2, null,0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (68, 43, '刪除', null,
                                                                                           'tx:txbase:delete', 2, null,2);

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (69, 52, '凭证用户佣金', 'modules/stat/tx_user_reward_stat2.html', 'stat:tx_user_reward:list', 1, 'fa fa-line-chart', 1);

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (71, 0, '财务管理', null, null, 0, 'fa fa-bank', 5);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (72, 71, '个人用户提现', 'modules/fin/tx_user_withdrawal.html', '', 1, 'fa fa-line-chart', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (73, 72, '查看', null, 'tx:tx_user_withdrawal:list,tx:tx_user_withdrawal:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (74, 72, '审核', null, 'tx:tx_user_withdrawal:update', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (75, 72, '删除', null, 'tx:tx_user_withdrawal:delete', 2, null, 0);

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (76, 52, '渠道统计', 'modules/stat/channel_stat.html', 'opt:channel:stat', 1, 'fa fa-line-chart', 2);

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (77, 0, '客户管理', null, null, 0, 'fa fa-users', 4);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (78, 77, '客户管理', 'modules/auth/auth_user.html', '', 1, 'fa fa-line-chart', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (79, 78, '查看', null, 'auth:user:list,auth:user:info', 2, null, 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES (80, 78, '导出', null, 'auth:user:export', 2, null, 0);

-- 菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES (81, 31, '客服人员', 'modules/auth/staff.html', NULL, '1', 'fa fa-file-code-o', '3');

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 82, 81, '查看', null, 'auth:staff:list,auth:staff:info', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 83, 81, '新增', null, 'auth:staff:save', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 84, 81, '修改', null, 'auth:staff:update', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 85, 81, '删除', null, 'auth:staff:delete', '2', null, '6';

-- 菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES ('86', '42', '出借人管理', 'modules/tx/txlender.html', NULL, '1', 'fa fa-file-code-o', '6');

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT '87', '86', '查看', null, 'auth:txlender:list,auth:txlender:info', '2', null, '0';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT '88', '86', '新增', null, 'auth:txlender:save', '2', null, '1';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT '89', '86',  '修改', null, 'auth:txlender:update', '2', null, '2';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT '90', '86',  '删除', null, 'auth:txlender:delete', '2', null, '3';

-- 菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES (91, '71', '费用统计', 'modules/sys/sysfun.html', NULL, '1', 'fa fa-file-code-o', '1');

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 93, 91, '查看', null, 'sys:sysfun:list,sys:sysfun:info', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 94, 91, '新增', null, 'sys:sysfun:save', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 95, 91, '修改', null, 'sys:sysfun:update', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 96, 91, '删除', null, 'sys:sysfun:delete', '2', null, '6';

-- 菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES (97, '71', '充值明细', 'modules/sys/sysfunrecord.html', NULL, '1', 'fa fa-file-code-o', '2');

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 98, 97, '查看', null, 'sys:sysfunrecord:list,sys:sysfunrecord:info', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 99, 97, '新增', null, 'sys:sysfunrecord:save', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 100, 97, '修改', null, 'sys:sysfunrecord:update', '2', null, '6';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 101, 97, '删除', null, 'sys:sysfunrecord:delete', '2', null, '6';

-- 菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES (102, '71', '费用明细', 'modules/sys/sysfundetails.html', NULL, '1', 'fa fa-file-code-o', '3');


-- 菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES (105, '1', '公告管理', 'modules/sys/sys_notice.html', 'sys:sys_notice:list', '1', null, '13');

-- 菜单对应按钮SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 106, 105, '查看', null, 'sys:sys_notice:list,sys:sys_notice:info', '2', null, '0';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 107, 105, '新增', null, 'sys:sys_notice:save', '2', null, '1';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 108, 105, '修改', null, 'sys:sys_notice:update', '2', null, '2';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 109, 105, '删除', null, 'sys:sys_notice:delete', '2', null, '3';

-- 菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    VALUES (110, '0', '首页', NULL, NULL, '0', 'fa fa-list-alt', '0');

-- 菜单对应二级菜单SQL
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 111, 110, '用户登录跟踪', 'modules/sys/merchantlog.html', 'sys:log:merchantLogList', '1', NULL, '0';
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
    SELECT 112, 110, '系统公告', 'modules/sys/merchantNotice.html', 'sys:sys_notice:merchantList', '1', NULL, '1';


delete from sys_role;
INSERT INTO sys_role (role_id, role_name, remark, create_user_id, create_time) VALUES (2, '管理员', null, 1, '2017-11-23 09:00:16');
INSERT INTO sys_role (role_id, role_name, remark, create_user_id, create_time) VALUES (3, '审核员', '', 1, '2017-12-15 18:24:45');
INSERT INTO sys_role (role_id, role_name, remark, create_user_id, create_time) VALUES (4, '分配员', null, 1, '2017-12-25 01:24:39');
INSERT INTO sys_role (role_id, role_name, remark, create_user_id, create_time) VALUES (5, '客服', null, 1, '2017-12-25 01:25:22');
INSERT INTO sys_role (role_id, role_name, remark, create_user_id, create_time) VALUES (6, '数据隐私', null, 1, '2017-12-25 01:25:41');

delete from sys_role_menu;
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (19, 2, 50);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (20, 2, 45);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (21, 2, 46);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (22, 2, 47);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (23, 2, 48);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (24, 2, 49);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (25, 2, 42);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (26, 2, 43);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (27, 2, 31);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (28, 2, 32);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (29, 2, 33);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (30, 2, 34);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (31, 2, 35);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (32, 2, 36);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (33, 2, 37);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (34, 2, 38);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (35, 2, 39);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (36, 2, 40);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (37, 2, 41);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (38, 2, 1);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (39, 2, 44);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (40, 2, 2);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (41, 2, 15);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (42, 2, 16);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (43, 2, 17);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (44, 2, 18);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (45, 2, 3);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (46, 2, 19);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (47, 2, 20);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (48, 2, 21);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (49, 2, 22);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (50, 2, 4);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (51, 2, 23);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (52, 2, 24);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (53, 2, 25);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (54, 2, 26);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (55, 2, 27);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (56, 2, 29);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (57, 3, 50);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (58, 3, 45);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (59, 3, 46);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (60, 3, 48);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (61, 4, 50);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (62, 4, 45);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (63, 4, 46);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (64, 4, 47);