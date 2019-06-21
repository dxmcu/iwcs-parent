package com.wisdom.iwcs.mapper.system;

import com.wisdom.iwcs.domain.system.Dictionary;
import com.wisdom.iwcs.domain.system.dto.DictionaryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanglisen on 2016/12/28.
 */
public interface DictionaryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Dictionary record);

    int insertSelective(Dictionary record);

    Dictionary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dictionary record);

    int updateByPrimaryKey(Dictionary record);

    List<Dictionary> selectAll();

    List<DictionaryDto> selectByDictTypePager(Map map);

    List<Dictionary> selectByDictType(@Param("dictType") String dictType);

    // 查询返回Type类型不重复的所有数据
    List<DictionaryDto> selectBaseTypeList(Map map);

    Dictionary selectByDictName(String dictName);
}