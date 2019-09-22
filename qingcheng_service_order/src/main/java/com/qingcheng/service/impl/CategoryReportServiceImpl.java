package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.dao.CategoryReportMapper;
import com.qingcheng.pojo.order.CategoryReport;
import com.qingcheng.service.order.CategroyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = CategroyReportService.class)
public class CategoryReportServiceImpl implements CategroyReportService {

    @Autowired
    private CategoryReportMapper categoryReportMapper;



    @Override
    public List<CategoryReport> categoryReport(LocalDate date) {
        return categoryReportMapper.categoryReport(date);
    }




    @Override
    @Transactional
    public void createDate() {
        //查询昨天类目
        LocalDate localDate = LocalDate.now().minusDays(1);
        List<CategoryReport> categoryReports = categoryReportMapper.categoryReport(localDate);

        //插入数据
        for (CategoryReport categoryReport : categoryReports) {
            categoryReportMapper.insert(categoryReport);
        }
    }




    @Override
    public List<Map> category1Count(String date1, String date2) {
        return categoryReportMapper.category1Count(date1,date2);
    }
}
