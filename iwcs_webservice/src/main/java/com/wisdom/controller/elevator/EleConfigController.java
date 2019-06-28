package com.wisdom.controller.elevator;

import com.wisdom.iwcs.common.utils.GridPageRequest;
import com.wisdom.iwcs.common.utils.GridReturnData;
import com.wisdom.iwcs.common.utils.Result;
import com.wisdom.iwcs.domain.elevator.dto.EleConfigDTO;
import com.wisdom.iwcs.mapstruct.elevator.EleConfigMapStruct;
import com.wisdom.iwcs.service.elevator.IEleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ele_config")
public class EleConfigController {
    @Autowired
    IEleConfigService IEleConfigService;
    @Autowired
    EleConfigMapStruct eleConfigMapStruct;

    /**
     * 根据主键ID删除
     *
     *
     * @param id {@link Integer }
     *
     * @return {@link Result }
     */
    @DeleteMapping(value = "/{id}")
    public Result deleteByPrimaryKey(@PathVariable Integer id) {
        IEleConfigService.deleteByPrimaryKey(id);

        return new Result();
    }

    /**
     * 根据主键ID删除多条记录
     *
     *
     * @param ids {@link List<String> }
     *
     * @return {@link Result }
     */
    @DeleteMapping
    public Result deleteMoreByIds(@RequestBody List<String> ids) {
        IEleConfigService.deleteMore(ids);

        return new Result();
    }

    /**
     * 新增记录
     *
     *
     * @param eleConfigDTO {@link EleConfigDTO }
     *
     * @return {@link Result }
     */
    @PostMapping
    public Result insert(@RequestBody EleConfigDTO eleConfigDTO) {
        IEleConfigService.insert(eleConfigDTO);

        return new Result();
    }

    /**
     * 根据主键查询记录
     *
     *
     * @param id {@link Integer }
     *
     * @return {@link Result }
     */
    @GetMapping(value = "/{id}")
    public Result selectByPrimaryKey(@PathVariable Integer id) {
        EleConfigDTO eleConfigDTO = IEleConfigService.selectByPrimaryKey(id);

        return new Result(eleConfigDTO);
    }

    /**
     * 分页查询记录
     *
     *
     * @param gridPageRequest {@link GridPageRequest }
     *
     * @return {@link Result }
     */
    @PostMapping(value = "/page")
    public Result selectPage(@RequestBody GridPageRequest gridPageRequest) {
        GridReturnData<EleConfigDTO> records = IEleConfigService.selectPage(gridPageRequest);

        return new Result(records);
    }

    /**
     * 更新记录
     *
     *
     * @param eleConfigDTO {@link EleConfigDTO }
     *
     * @return {@link Result }
     */
    @PutMapping
    public Result updateByPrimaryKey(@RequestBody EleConfigDTO eleConfigDTO) {
        IEleConfigService.updateByPrimaryKey(eleConfigDTO);

        return new Result();
    }
}
