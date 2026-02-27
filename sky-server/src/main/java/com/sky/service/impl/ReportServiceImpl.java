package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFAnchor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    public TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end){
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double> amountList = new ArrayList<>();
        for(LocalDate localDate : dateList){
            LocalDateTime beginTime = LocalDateTime.of(localDate,LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate,LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double amount = orderMapper.sumByMap(map);
            amount = amount == 0 ? 0.0 : amount;
            amountList.add(amount);
        }

        String dateJoin = org.apache.commons.lang3.StringUtils.join(dateList,",");
        String amountJoin = StringUtils.join(amountList,",");

        return TurnoverReportVO.builder()
                .dateList(dateJoin)
                .turnoverList(amountJoin)
                .build();
    }

    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end){
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalList = new ArrayList<>();
        List<Integer> newList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        for(LocalDate localDate : dateList){
            LocalDateTime beginTime = LocalDateTime.of(localDate,LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate,LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("end",endTime);
            Integer totalNumber = userMapper.countByMap(map);
            map.put("begin",beginTime);
            Integer newNumber = userMapper.countByMap(map);
            totalList.add(totalNumber);
            totalList.add(newNumber);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newList,","))
                .totalUserList(StringUtils.join(totalList,","))
                .build();

    }

    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end){
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderNumbers = new ArrayList<>();
        List<Integer> completedNumbers = new ArrayList<>();
        dateList.add(begin);
        while(!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        for(LocalDate localDate : dateList){
            LocalDateTime beginTime = LocalDateTime.of(localDate,LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate,LocalTime.MAX);
            Integer totalNumber = getOrderCount(beginTime,endTime,null);
            Integer completedNumber = getOrderCount(beginTime,endTime,Orders.COMPLETED);
            orderNumbers.add(totalNumber);
            completedNumbers.add(completedNumber);
        }

        Integer orderNumberCount = orderNumbers.stream().reduce(Integer::sum).get();
        Integer completedNumberCount = completedNumbers.stream().reduce(Integer::sum).get();

        Double rate = 0.0;
        if(orderNumberCount != 0 ){
            rate =  completedNumberCount.doubleValue()/orderNumberCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderNumbers,","))
                .orderCompletionRate(rate)
                .validOrderCountList(StringUtils.join(completedNumbers,","))
                .validOrderCount(completedNumberCount)
                .totalOrderCount(orderNumberCount)
                .build();
    }

    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        Map map = new HashMap<>();
        map.put("begin",beginTime);
        map.put("end",endTime);
        map.put("status", status);
        return orderMapper.CountByMap(map);
    }

    public SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end){
        LocalDateTime beginTime = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);
        List<GoodsSalesDTO> list = orderMapper.getSalesTop10(beginTime,endTime);

        List<String> name = list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());

        List<Integer> number = list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(name,","))
                .numberList(StringUtils.join(number,","))
                .build();
    }

    public void exportBusinessData(HttpServletResponse httpServletResponse) {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = httpServletResponse.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
