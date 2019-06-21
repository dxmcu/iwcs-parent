package com.wisdom.iwcs.mapper.base;

import com.wisdom.iwcs.common.utils.mapper.LogicDelete.DeleteLogicMapper;
import com.wisdom.iwcs.common.utils.mapper.MyMapperAndIds;
import com.wisdom.iwcs.domain.base.BaseMapBerth;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Generator
 * @date 2019-02-22 11:09:17.
 */
@Repository
public interface BaseMapBerthMapper extends DeleteLogicMapper<BaseMapBerth>, MyMapperAndIds<BaseMapBerth> {
    /**
     * selectPage
     *
     * @param map condition
     * @return list
     */
    List<BaseMapBerth> selectPage(Map map);

    /**
     * 根据地图编号删除地图地码信息
     *
     * @param mapCode
     * @return
     */
    int deleteByMapCode(String mapCode);
}