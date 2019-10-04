package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.service.goods.SkuSearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuSearchServiceImpl implements SkuSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public Map search(Map<String, String> searchMap) {
        //封装查询条件
        SearchRequest searchRequest = new SearchRequest("qcsku");
        searchRequest.types("doc");//设置查询的类型

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();//布尔查询构建器

        //1.1关键字搜索
        MatchQueryBuilder matchQueryBuilder= QueryBuilders.matchQuery("name",searchMap.get("keywords"));
        boolQueryBuilder.must(matchQueryBuilder);

        //1.2商品分类的过滤
        if (searchMap.get("category")!=null){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categoryName",searchMap.get("category"));
            boolQueryBuilder.filter(termQueryBuilder);
        }


        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);


        //聚合条件查询(商品分类)
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("sku_category").field("categoryName");
        searchSourceBuilder.aggregation(termsAggregationBuilder);


        //2.查询封装结果
        Map resultMap = new HashMap();
       try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            SearchHits searchHit = searchResponse.getHits();
            long totalHits = searchHit.getTotalHits();
            System.out.println("记录数:"+totalHits);
            SearchHit[] hits = searchHit.getHits();

            //2.1商品列表
            List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
            for (SearchHit hit:hits){
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                resultList.add(sourceAsMap);
            }
            resultMap.put("rows",resultList);

            //2.2商品分类列表
           Aggregations aggregations = searchResponse.getAggregations();
           Map<String, Aggregation> aggregationsMap = aggregations.getAsMap();

           Terms terms = (Terms) aggregationsMap.get("sku_category");

           List<? extends Terms.Bucket> buckets = terms.getBuckets();
           List<String> categoryList = new ArrayList<String>();
           System.out.println(categoryList);
           for (Terms.Bucket bucket : buckets){
               categoryList.add(bucket.getKeyAsString());
           }

           System.out.println("--------------"+categoryList);
           resultMap.put("categoryList",categoryList);
       } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
