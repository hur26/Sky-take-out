package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api("套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    SetmealService setmealService ;

    @PostMapping
    @ApiOperation("新建套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐信息");
        setmealService.save(setmealDTO);
        return Result.success() ;
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询");
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        setmealService.delete(ids);
        return Result.success();
    }


    @GetMapping("/{id}")
    @ApiOperation("根据id查找套餐")
    public Result<SetmealVO> getById(@PathVariable  Long id ){
        log.info("根据id查找套餐");
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO) ;
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result update (@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐");
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("修改套餐状态");
        setmealService.startOrStop(status,id);
        return Result.success();
    }

}
