package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService ;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        String key = "dish_" + dishDTO.getCategoryId() ;
        CleanCache(key);
        dishService.saveWithFlavor(dishDTO);
        return Result.success() ;

    }


    @GetMapping("/page")
    @ApiOperation("菜品分页管理")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询");
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @Transactional
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids) ;
        String key = "dish_*" ;
        CleanCache(key);
        return Result.success();


    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id ){
        log.info("根据id查找菜品");
        DishVO dish =dishService.getByIdWithFlavor(id);
        return Result.success(dish) ;
    }

    @PutMapping
    @ApiOperation("更新菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        dishService.updateWithFlavor(dishDTO);
        String key = "dish_*" ;
        CleanCache(key);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据分类查询菜品");
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品状态接口")
    public Result startOrStop( @PathVariable Integer status, Long id){
        log.info("修改菜品状态");
        String key = "dish_*" ;
        CleanCache(key);
        dishService.startOrStop(status,id);
        return Result.success();
    }

    //清理缓存数据
    private void CleanCache(String key){
        Set set = redisTemplate.keys(key);
        redisTemplate.delete(key);
    }

}
