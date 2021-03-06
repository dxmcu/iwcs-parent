package com.wisdom.iwcs.mapper.base;

import com.wisdom.iwcs.common.utils.mapper.LogicDelete.DeleteLogicMapper;
import com.wisdom.iwcs.common.utils.mapper.MyMapperAndIds;
import com.wisdom.iwcs.domain.base.BaseMatPackageSpec;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Generator
 * @date 2019-03-18 11:43:30.
 */
@Repository
public interface BaseMatPackageSpecMapper extends DeleteLogicMapper<BaseMatPackageSpec>, MyMapperAndIds<BaseMatPackageSpec> {
    /**
     * selectPage
     *
     * @param map condition
     * @return list
     */
    List<BaseMatPackageSpec> selectPage(Map map);
}