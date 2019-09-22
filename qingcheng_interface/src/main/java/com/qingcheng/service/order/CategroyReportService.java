package com.qingcheng.service.order;

import com.qingcheng.pojo.order.CategoryReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CategroyReportService {

    /**
     * 查询昨天数据类目
     * @param date
     * @return
     */
    public List<CategoryReport> categoryReport(LocalDate date);

    /**
     * 生成类目
     */
    public void createDate();


    public List<Map> category1Count(String date1,String date2);
}
