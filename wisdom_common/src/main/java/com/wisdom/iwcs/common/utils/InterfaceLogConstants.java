package com.wisdom.iwcs.common.utils;

/**
 * @author cecilia.yang
 * @version <br>
 * <p>静态常量</p>
 */

public class InterfaceLogConstants {


    public static final class InterfaceCode {
        public final static String SYNC_NOTIFY_CODE = "syncNotify";
        public final static String OUT_POD_CODE = "getOutPod";
        public final static String RETURN_POD_CODE = "returnPod";
        public final static String NOTIFY_CLIENT_CODE = "notifyClient";
        public final static String POD_RETURN_STRA_CODE = "podReturnArea";
        public final static String NOTIFY_POD_ARR_CODE = "notifyPodArr";
        public final static String END_TASK_CODE = "endTask";
        public final static String ROTATE_POD_CODE = "rotatePod";
        public final static String CALL_BY_BINCODE = "callOutByBincode";
        public final static String CALL_BY_POD_CODE = "callOutByPodCode";
        public final static String INIT_POD_CODE = "initPod";
        public final static String CONFIG_RELATION_CODE = "configMapCodeAndAreaCodeRelation";
        public final static String MOVE_POD_CODE = "moveByBincode";
        public final static String GEN_MOVE_TASK_BY_POD_CODE = "genMoveTaskByPod";
        public final static String RETURN_BY_BINCODE = "returnByBincode";
        public final static String RETURN_BY_POD_CODE = "returnByPodCode";
        public final static String OUT_BOUND_CODE = "outBound";
        public final static String OUT_STOCK_CALL_CODE = "outStockCall";
        public final static String STOCK_ADJUST_CODE = "stockAdjust";
        public final static String MANUAL_UPDATE_POD_LOCK_CODE = "manualUpdatePodLock";
        public final static String GET_FREE_WB_INFO_CODE = "getFreeWbInfo";
        public final static String SHOW_POD_CODE = "showPodInfo";
        public final static String GEN_AGV_SCHEDULING_TASK_CODE = "genAgvSchedulingTask";
        public final static String CONTINUE_TASK_CODE = "continueTask";
        public final static String CANCEL_TASK_CODE = "cancelTask";
    }

    public static final class InterfaceName {

        public final static String SYNC_NOTIFY_NAME = "海康同步基础信息";
        public final static String OUT_POD_NAME = "货架出库";
        public final static String RETURN_POD_NAME = "货架回库";
        public final static String NOTIFY_CLIENT_NAME = "TPS通知客户端";
        public final static String POD_RETURN_STRA_NAME = "指定货架回库策略";
        public final static String NOTIFY_POD_ARR_NAME = "通知货架返程回储位";
        public final static String END_TASK_NAME = "一键结束";
        public final static String ROTATE_POD_NAME = "货架旋转";
        public final static String CALL_BY_POD_NAME = "按指定货架呼叫";
        public final static String INIT_POD_NAME = "货架初始化";
        public final static String CONFIG_RELATION_NAME = "配置区域地图关系";
        public final static String MOVE_POD_NAME = "移动货架";
        public final static String GEN_MOVE_TASK_BY_POD_NAME = "货架变更存储区";
        public final static String RETURN_BY_BINCODE_NAME = "指定货架回库";
        public final static String OUT_BOUND_NAME = "出库确认";
        public final static String OUT_STOCK_CALL_NAME = "出库呼叫";
        public final static String STOCK_ADJUST_NAME = "库存调整";
        public final static String MANUAL_UPDATE_POD_LOCK_NAME = "手动上锁/解锁货架";
        public final static String GET_FREE_WB_INFO_NAME = "拉取空闲点位信息";
        public final static String SHOW_POD_NAME = "获取货架信息";
        public final static String GEN_AGV_SCHEDULING_TASK_NAME = "生成任务单";
        public final static String CONTINUE_TASK_NAME = "继续执行任务";
        public final static String CANCEL_TASK_NAME = "取消任务";
    }

    public static final class SrcClientCode {

        public final static String SRC_HIK = "HIK";
        public final static String SRC_IWCS = "IWCS";
        public final static String SRC_OUTER = "OUTER";

    }


}