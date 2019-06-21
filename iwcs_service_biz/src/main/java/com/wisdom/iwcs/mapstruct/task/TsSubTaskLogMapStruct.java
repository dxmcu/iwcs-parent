package com.wisdom.iwcs.mapstruct.task;


import com.wisdom.iwcs.domain.task.TsSubTaskLog;
import com.wisdom.iwcs.domain.task.dto.TsSubTaskLogDTO;
import com.wisdom.iwcs.mapstruct.EntityMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TsSubTaskLog and its DTO TsSubTaskLogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TsSubTaskLogMapStruct extends EntityMapper<TsSubTaskLogDTO, TsSubTaskLog> {

}
