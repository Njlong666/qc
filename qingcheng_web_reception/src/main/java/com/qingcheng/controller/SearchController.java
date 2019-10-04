package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.service.goods.SkuSearchService;
import com.qingcheng.utils.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class SearchController {

    @Reference
    private SkuSearchService skuSearchService;

    @GetMapping("/search")
    public String  serach(Model model,@RequestParam Map<String, String > searchMap) throws Exception {
        //字符集处理
        searchMap = WebUtil.convertCharsetToUTF8(searchMap);
        //远程调用接口
        Map result = skuSearchService.search(searchMap);
        model.addAttribute("result",result);

        //处理url
        StringBuffer url = new StringBuffer("/search.do?");
        for (String  key : searchMap.keySet()){
            url.append("&"+key+"="+searchMap.get(key));
        }
        model.addAttribute("url",url);


        //回传searchMap 前端才能截取过滤条件
        model.addAttribute("searchMap",searchMap);

        return "search";
    }
}
