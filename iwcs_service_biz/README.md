# iwcs_biz_service


iwcs管理系统--业务流程模块



#### 模块作用
    业务流程扶负责调用多个业务core来满足上层业务需求及流程
    在业务流程模块（service）中可以复用core里面的操作。但是业务流程间(service)不可服用，core中是一个个最基本的业务单元，可复用。

#### 模块package分包原则
- 按照业务分包，减少业务依赖，若业务独立性较大，可拆分出独立子模块
    

