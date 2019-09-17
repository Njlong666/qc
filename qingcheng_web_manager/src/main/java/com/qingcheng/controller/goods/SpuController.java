package com.qingcheng.controller.goods;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.PageResult;
import com.qingcheng.entity.Result;
import com.qingcheng.pojo.goods.Goods;
import com.qingcheng.pojo.goods.Spu;
import com.qingcheng.service.goods.SpuService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/spu")
public class SpuController {

    @Reference
    private SpuService spuService;

    @GetMapping("/findAll")
    public List<Spu> findAll(){
        return spuService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Spu> findPage(int page, int size){
        return spuService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Spu> findList(@RequestBody Map<String,Object> searchMap){
        return spuService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Spu> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  spuService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Spu findById(String id){
        return spuService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Spu spu){
        spuService.add(spu);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Spu spu){
        spuService.update(spu);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(String id){
        spuService.delete(id);
        return new Result();
    }


    /**'
     * 添加商品信息
     * @param goods
     * @return
     */
    @PostMapping("/save")
    public Result save( @RequestBody Goods goods){
        spuService.saveGoods(goods);
        return new Result();
    }


    /**
     * 根据id查询sku列表
     * @param id
     * @return
     */
    @GetMapping("/findGoodsById")
    public Goods findGoodsById(String id){
       return spuService.findGoodsByid(id);
    }

    /**
     * 商品上架
     * @param map
     * @return
     */
    @PostMapping
    public Result audid(@RequestBody Map<String ,String > map){
        spuService.audit(map.get("id"),map.get("status"),map.get("message"));
        return new Result();
    }

    /**
     * 商品下架
     * @param id
     * @return
     */
    @GetMapping("/pull")
    public Result pull(String id){
        spuService.pull(id);
        return new Result();
    }

    /**
     *商品上架
     * @param id
     * @return
     */
    @GetMapping("/put")
    public Result put(String id){
        spuService.put(id);
        return new Result();
    }

    /**
     * 批量上架
     * @param ids
     * @return
     */
    @GetMapping("/putMany")
    public Result putMany(String[] ids){

        int conut = spuService.putMany(ids);

        return new Result(0,"上架"+conut+"个商品");
    }
}
