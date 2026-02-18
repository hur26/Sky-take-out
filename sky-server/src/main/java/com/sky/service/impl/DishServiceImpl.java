package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper ;
    @Autowired
    DishFlavorMapper dishFlavorMapper ;
    @Autowired
    SetmealDishMapper setmealDishMapper ;

    @Transactional
    public void saveWithFlavor(DishDTO dishDTO){
        //向菜品表插入一条数据

        Dish dish = new Dish() ;
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId() ;
    //向口味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
        if(flavors != null && flavors.size()>0){
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO){
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page  = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult()) ;
    }

    public void deleteBatch(List<Long> ids) {

        //判断菜品是否处于可出售状态
        for(long id :ids) {
            Dish dish = setmealDishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断菜品是否关联套餐
        for(long id : ids){
            List<Long> setmealId = dishMapper.getSetmealByDishId(id);
            if(setmealId != null || setmealId.size() > 0 ){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }

        //删除菜品表中的数据
//        for(long id : ids){
//            dishMapper.deleteById(id);
//            //删除口味数据
//            dishFlavorMapper.deleteByDishId(id);
//        }

        //采用单条sql批量删除
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }


     public DishVO getByIdWithFlavor(Long id){
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        List<DishFlavor> list = new ArrayList<>();
        list = dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(list);
        return dishVO ;
     }

     public void updateWithFlavor(DishDTO dishDTO){
         Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        List<DishFlavor> list = dishDTO.getFlavors();;
        if(list.size()>0 && list != null){
            list.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(list);
        }
     }

    public List<Dish> list(Long categoryId){
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
       return dishMapper.list(dish);
    }

    public void startOrStop(Integer status,Long id){
        Dish dish = Dish.builder().id(id).status(status).build();
        dishMapper.update(dish);
    }
}
