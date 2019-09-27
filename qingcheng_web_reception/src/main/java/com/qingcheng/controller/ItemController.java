package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qingcheng.pojo.goods.Goods;
import com.qingcheng.pojo.goods.Sku;
import com.qingcheng.pojo.goods.Spu;
import com.qingcheng.service.goods.CategoryService;
import com.qingcheng.service.goods.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Reference
    private SpuService spuService;

    @Value("${pagePath}")
    private String pagePath;

    @Reference
    private CategoryService categoryService;


    //注入模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/createPage")
    public void createPage(String spuId){
        //查询商品信息
        Goods goodsByid = spuService.findGoodsByid(spuId);
        Spu spu = goodsByid.getSpu();//获取spu信息
        List<Sku> skuList = goodsByid.getSkuList();//获取skulieb

        //查询商品分类
        List<String> categoryList = new ArrayList<>();
        categoryList.add(categoryService.findById(spu.getCategory1Id()).getName());//1级分类对象
        categoryList.add(categoryService.findById(spu.getCategory2Id()).getName());//2级分类对象
        categoryList.add(categoryService.findById(spu.getCategory3Id()).getName());//3级分类对象

        //构建sku的地址列表
        Map<String,String> urlMap = new HashMap<>();
        for (Sku sku:skuList){
            if ("1".equals(sku.getStatus())){
                String specJSON = JSON.toJSONString(JSON.parseObject(sku.getSpec()), SerializerFeature.MapSortField);
                urlMap.put(specJSON,sku.getId()+".html");
            }
        }



        //生成sku页面
        for (Sku sku:skuList){
            //创建上下文
            Context context = new Context();
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("spu",spu);
            dataMap.put("sku",sku);
            dataMap.put("categoryList",categoryList);
            dataMap.put("skuImages", sku.getImages().split(","));//sku图片列表
            dataMap.put("spuImages", spu.getImages().split(","));//spu图片列表

            Map paraItems = JSON.parseObject(spu.getParaItems());//spu参数列表
            dataMap.put("paraItems",paraItems);

            Map<String,String> specItems =(Map) JSON.parseObject(sku.getSpec());
            dataMap.put("specItems",specItems);//sku规格列表
            context.setVariables(dataMap);


            Map<String,List> specMap =(Map) JSON.parseObject(spu.getSpecItems());//规格和规格选项
            for (String key:specMap.keySet()){ //循环规格
                List<String> list = specMap.get(key);
                List<Map> mapList = new ArrayList<>();//新的集合


                //循环规格选项
                for (String value:list){
                    Map map = new HashMap();
                    map.put("option",value);//规格选项

                    //如果和当前的sku的规格相同就是选中的
                    if (specItems.get(key).equals(value)){
                        map.put("checked",true);//是否选中
                    }else {
                        map.put("checked",false);//是否选中
                    }
                    Map<String,String> spec = (Map) JSON.parseObject(sku.getSpec());//当前的sku
                    spec.put(key,value);
                    String specJSON = JSON.toJSONString(spec, SerializerFeature.MapSortField);
                    map.put("url",urlMap.get(specJSON));
                    mapList.add(map);
                }
                specMap.put(key,mapList);//用新的集合替换原有的集合
            }

            dataMap.put("specMap",specMap);

            //准备文件
            File dir = new File(pagePath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File dest = new File(dir,sku.getId()+".html");
            //生成页面
            try {
                PrintWriter writer = new PrintWriter(dest,"UTF-8");
                templateEngine.process("item",context,writer);
                System.out.println("生成页面:"+sku.getId()+".html");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
}
