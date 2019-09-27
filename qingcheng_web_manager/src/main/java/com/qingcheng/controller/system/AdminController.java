package com.qingcheng.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.PageResult;
import com.qingcheng.entity.Result;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.AdminAndRole;
import com.qingcheng.pojo.system.AdminRole;
import com.qingcheng.service.system.AdminService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Reference
    private AdminService adminService;

    @GetMapping("/findAll")
    public List<Admin> findAll(){
        return adminService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Admin> findPage(int page, int size){
        return adminService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Admin> findList(@RequestBody Map<String,Object> searchMap){
        return adminService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Admin> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  adminService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Admin findById(Integer id){
        return adminService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Admin admin){
        adminService.update(admin);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        adminService.delete(id);
        return new Result();
    }


    /**
     * 新增
     * @param adminAndRole
     * @return
     */
    @PostMapping("/addAdminRole")
    public Result addAdminRole(@RequestBody AdminAndRole adminAndRole){
        adminService.addAdminRole(adminAndRole);
        return new Result();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/findByAdminRoleId")
    public Result findByAdminRoleId(Integer id){
        adminService.findByAdminRoleId(id);
        return new Result();
    }


    /**
     * 修改密码
     * @param updatePWDMap
     * @return
     */
    @PostMapping("/updatePWD")
    public Result updatePWD(@RequestBody Map<String, Object> updatePWDMap){
        //获取当前登录用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        updatePWDMap.put("loginName",name);
        adminService.updatePWD(updatePWDMap);
        return new Result();
    }

}
