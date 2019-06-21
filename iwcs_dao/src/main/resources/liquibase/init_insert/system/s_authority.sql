INSERT INTO `s_authority` VALUES (1, 'M_SALES', NULL, '销售管理', 'list', '销售管理', 0, 1, 0);
INSERT INTO `s_authority` VALUES (2, 'M_COMMERCE', NULL, '商务管理', 'list', '商务管理', 0, 1, 0);
INSERT INTO `s_authority` VALUES (3, 'M_OPERATING', NULL, '操作管理', 'list', '操作管理', 0, 1, 0);
INSERT INTO `s_authority` VALUES (4, 'M_CLIENT', NULL, '客户管理', 'list', '客户管理', 0, 1, 0);
INSERT INTO `s_authority` VALUES (5, 'M_FINANCE', NULL, '财务管理', 'list', '财务管理', 0, 1, 0);
INSERT INTO `s_authority` VALUES (6, 'M_BASIC_CODE', NULL, '基础代码', 'list', '基础代码', 0, 1, 0);
INSERT INTO `s_authority` VALUES (7, 'M_BOOKING', NULL, '订舱管理', 'list', '订舱管理', 1, 1, 0);
INSERT INTO `s_authority` VALUES (8, 'M_BUSINESS_BOOKING', NULL, '商务订舱', 'list', '商务订舱', 2, 1, 0);
INSERT INTO `s_authority` VALUES (9, 'M_BUSINESS_STATISTICS', NULL, '商务统计', 'list', '商务统计', 2, 1, 0);
INSERT INTO `s_authority` VALUES (10, 'M_BOOKING_OPERATION', NULL, '订舱操作', 'list', '订舱操作', 3, 1, 0);
INSERT INTO `s_authority` VALUES (11, 'M_BILLING_OPERATION', NULL, '计费操作', 'list', '计费操作', 3, 1, 0);
INSERT INTO `s_authority` VALUES (12, 'M_CHARGE_APPLICATION', NULL, '收费申请', 'list', '收费申请', 3, 1, 0);
INSERT INTO `s_authority` VALUES (13, 'M_PAYMENT_APPLICATION', NULL, '付费申请', 'list', '付费申请', 3, 1, 0);
INSERT INTO `s_authority` VALUES (14, 'M_PLACEMENT_APPLICATION', NULL, '放单申请', 'list', '放单申请', 3, 1, 0);
INSERT INTO `s_authority` VALUES (15, 'M_CUSTOMER _INFORMATION', NULL, '客户信息', 'list', '客户信息', 4, 1, 0);
INSERT INTO `s_authority` VALUES (16, 'M_CUSTOMER_APPLICATION', NULL, '客户申请', 'list', '客户申请', 4, 1, 0);
INSERT INTO `s_authority` VALUES (17, 'M_AGREEMENT_CUSTOMERS', NULL, '协议客户', 'list', '协议客户', 4, 1, 0);
INSERT INTO `s_authority` VALUES (18, 'M_BILLING_MANAGEMENT', NULL, '账单管理', 'list', '账单管理', 5, 1, 0);
INSERT INTO `s_authority` VALUES (19, 'M_PAYMENT_MANAGEMENT', NULL, '收付费管理', 'list', '收付费管理', 5, 1, 0);
INSERT INTO `s_authority` VALUES (20, 'M_REAL PAYMENT', NULL, '实收实付管理', 'list', '实收实付管理', 5, 1, 0);
INSERT INTO `s_authority` VALUES (21, 'M_INVOICE', NULL, '发票管理', 'list', '发票管理', 5, 1, 0);
INSERT INTO `s_authority` VALUES (22, 'M_SHIP_TIME_CHART', NULL, '船期表', 'list', '船期表', 6, 1, 0);
INSERT INTO `s_authority` VALUES (23, 'M_BUSINESS_CODE', NULL, '业务代码', 'list', '业务代码', 6, 1, 0);
INSERT INTO `s_authority` VALUES (24, 'M_FOREIGN_EXCHANGE_RATE', NULL, '外币汇率', 'list', '外币汇率', 6, 1, 0);
INSERT INTO `s_authority` VALUES (25, 'M_FINANCIAL_CODE', NULL, '财务代码', 'list', '财务代码', 6, 1, 0);
INSERT INTO `s_authority` VALUES (26, 'M_GROUP_QUERY', NULL, '分组查询', 'list', '分组查询', 6, 1, 0);
INSERT INTO `s_authority` VALUES (27, 'M_RATE_SETTING', NULL, '费率设置', 'list', '费率设置', 6, 1, 0);
INSERT INTO `s_authority` VALUES (28, 'M_TYPE_SETTING', NULL, '费种设置', 'list', '费种设置', 6, 1, 0);
INSERT INTO `s_authority` VALUES (29, 'M_FEE_CONSOLIDATION', NULL, '费种合并', 'list', '费种合并', 6, 1, 0);
INSERT INTO `s_authority` VALUES (30, 'M_EXPENSE_PLAN', NULL, '费种方案', 'list', '费种方案', 6, 1, 0);
INSERT INTO `s_authority` VALUES (31, 'M_EDI_SETTING', NULL, 'EDI设置', 'list', 'EDI设置', 6, 1, 0);
INSERT INTO `s_authority` VALUES (32, 'M_SYSTEM', NULL, '系统设置', 'list', '系统设置', 0, 1, 0);
INSERT INTO `s_authority` VALUES (36, 'own_space', NULL, '个人信息', 'list', '个人信息', 32, 1, 0);
INSERT INTO `s_authority` VALUES (37, 'M_COMPANY_DEPARTMENT', NULL, '公司部门', 'list', '公司部门', 32, 1, 0);
INSERT INTO `s_authority` VALUES (38, 'M_GROUP', NULL, '小组管理', 'list', '小组管理', 32, 1, 0);
INSERT INTO `s_authority` VALUES (39, 'M_ACCOUNT_OPENING', NULL, '账户开通', 'list', '账户开通', 32, 1, 0);
INSERT INTO `s_authority` VALUES (40, 'M_MANAGEMENT', NULL, '角色管理', 'list', '角色管理', 32, 1, 0);
INSERT INTO `s_authority` VALUES (236, 'B_CRM_UPDATE', 0, '修改客户信息', NULL, NULL, 15, 2, 1000);
INSERT INTO `s_authority` VALUES (237, 'SeaShipSave', 0, '下货纸保存', NULL, NULL, 10, 2, 1000);
INSERT INTO `s_authority` VALUES (238, 'M_APPROVE_MANAGE', 0, '审批中心', NULL, '审批中心', 0, 1, 0);
INSERT INTO `s_authority` VALUES (239, 'M_ORDER_APPROVE', 0, '账单审批', NULL, '账单审批', 238, 1, 0);
INSERT INTO `s_authority` VALUES (240, 'SALES_APPROVE', 0, '销售审核', NULL, NULL, 239, 2, 1000);
INSERT INTO `s_authority` VALUES (241, 'FINANCE_APPROVE', 0, '财务审核', NULL, NULL, 239, 2, 1000);
INSERT INTO `s_authority` VALUES (242, 'MANAGER_APPROVE', 0, '经理审核', NULL, NULL, 239, 2, 1000);
INSERT INTO `s_authority` VALUES (243, 'approve_setting', 0, '审批设置', NULL, NULL, 238, 1, 1000);
INSERT INTO `s_authority` VALUES (244, 'approve_update', 0, '审批维护', NULL, NULL, 238, 1, 1000);
INSERT INTO `s_authority` VALUES (245, 'release_order_approve', 0, '放单审批', NULL, NULL, 238, 1, 1000);
INSERT INTO `s_authority` VALUES (246, 'pay_order_approve', 0, '付费单审批', NULL, NULL, 238, 1, 1000);
INSERT INTO `s_authority` VALUES (247, 'SEA_EXPORT', 0, '海运出口', NULL, '菜单管理', 7, 2, 1000);
INSERT INTO `s_authority` VALUES (248, 'SEA_IMPORT', 0, '海运进口', NULL, '菜单管理', 7, 2, 1000);
INSERT INTO `s_authority` VALUES (249, 'AIR_EXPORT', 0, '空运出口', NULL, '菜单管理', 7, 2, 1000);
INSERT INTO `s_authority` VALUES (250, 'AIR_IMPORT', 0, '空运进口', NULL, '菜单管理', 7, 2, 1000);
INSERT INTO `s_authority` VALUES (251, 'BUSINESS_SEA_EXPORT', 0, '海运出口', NULL, '菜单管理', 8, 2, 1000);
INSERT INTO `s_authority` VALUES (252, 'BUSINESS_SEA_IMPORT', 0, '海运进口', NULL, '菜单管理', 8, 2, 1000);
INSERT INTO `s_authority` VALUES (253, 'BUSINESS_AIR_EXPORT', 0, '空运出口', NULL, '菜单管理', 8, 2, 1000);
INSERT INTO `s_authority` VALUES (254, 'BUSINESS_AIR_IMPORT', 0, '空运进口', NULL, '菜单管理', 8, 2, 1000);
INSERT INTO `s_authority` VALUES (255, 'OPER_SEA_EXPORT', 0, '海运出口', NULL, '菜单管理', 10, 2, 1000);
INSERT INTO `s_authority` VALUES (256, 'OPER_SEA_IMPORT', 0, '海运进口', NULL, '菜单管理', 10, 2, 1000);
INSERT INTO `s_authority` VALUES (257, 'OPER_AIR_EXPORT', 0, '空运出口', NULL, '菜单管理', 10, 2, 1000);
INSERT INTO `s_authority` VALUES (258, 'OPER_AIR_IMPORT', 0, '空运进口', NULL, '菜单管理', 10, 2, 1000);
INSERT INTO `s_authority` VALUES (259, 'financial-order-transfer', 0, '转账管理', NULL, '转账管理', 5, 1, 0);
INSERT INTO `s_authority` VALUES (260, 'statistics', NULL, '查询统计', NULL, '查询统计', 0, 1, 0);
INSERT INTO `s_authority` VALUES (261, 'profit-statistics', NULL, '利润统计', NULL, '利润统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (262, 'profit-detail-statistics', NULL, '利润详情统计', NULL, '利润详情统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (263, 'arrears-statistics', NULL, '欠费统计', NULL, '欠费统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (264, 'arrears-detail-statistics', NULL, '欠费详情统计', NULL, '欠费详情统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (265, 'unpaid-statistics', NULL, '未付统计', NULL, '未付统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (266, 'unpaid-detail-statistics', NULL, '未付详情统计', NULL, '未付详情统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (267, 'payment-statistics', NULL, '收付统计', NULL, '收付统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (268, 'payment-detail-statistics', NULL, '收付详情统计', NULL, '收付详情统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (269, 'container-count-statistics', NULL, '箱量统计', NULL, '箱量统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (270, 'container-count-detail-statistics', NULL, '箱量详情统计', NULL, '箱量详情统计', 260, 1, 0);
INSERT INTO `s_authority` VALUES (271, 'checking-account', NULL, '对账', NULL, '对账', 260, 1, 0);