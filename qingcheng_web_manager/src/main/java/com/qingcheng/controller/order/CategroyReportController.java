package com.qingcheng.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.pojo.order.CategoryReport;
import com.qingcheng.service.order.CategroyReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categroyReport")
public class CategroyReportController {

    @Reference
    private CategroyReportService categroyReportService;


    /**
     * 昨天的数据统计(商品类目)
     * @return
     */
    @GetMapping("/yesterDay")
    public List<CategoryReport> yesterDay(){
        LocalDate localDate = LocalDate.now().minusDays(1);
       return categroyReportService.categoryReport(localDate);
    }



    @GetMapping("/category1Count")
    public List<Map> category1Count(String date1,String date2){
        return categroyReportService.category1Count(date1,date2);
    }
}
