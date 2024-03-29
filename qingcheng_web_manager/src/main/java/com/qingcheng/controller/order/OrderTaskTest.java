package com.qingcheng.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.service.order.CategroyReportService;
import com.qingcheng.service.order.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderTaskTest {

    @Reference
    private OrderService orderService;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void orderOutTime(){
        System.out.println("每俩分钟间隔执行一次!!!"+new Date());
//        orderService.orderTimeOutLogic();
    }





    @Reference
    private CategroyReportService categroyReportService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void createCategotyDate(){
        System.out.println("生成类目统计数据++++++++");
        categroyReportService.createDate();
    }
}
