package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api("店铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate ;

    public static String Key = "SHOP_STATUS";

    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺状态为:{}",status ==1 ? "营业中" : "打烊");
        redisTemplate.opsForValue().set(Key,status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("查询店铺状态")
     public Result<Integer> getStatus(){
        Integer status =(Integer) redisTemplate.opsForValue().get(Key);
        log.info("当前店铺状态为:{}",status==1 ? "营业中" : "打烊");
        return Result.success(status);
    }
}
