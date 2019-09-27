package com.qingcheng.service.system;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.AdminAndRole;

import java.util.*;

/**
 * admin业务逻辑层
 */
public interface AdminService {


    public List<Admin> findAll();


    public PageResult<Admin> findPage(int page, int size);


    public List<Admin> findList(Map<String,Object> searchMap);


    public PageResult<Admin> findPage(Map<String,Object> searchMap,int page, int size);


    public Admin findById(Integer id);

    public void add(Admin admin);

    /**
     * 参数改为管理员实体类和角色ID集合的组合实体类
     * @param adminAndRole
     */
    public void addAdminRole(AdminAndRole adminAndRole);


    /**
     * 查询ADmin和role ID
     * @param id
     * @return
     */
    public AdminAndRole findByAdminRoleId(Integer id);


    public void update(Admin admin);


    public void delete(Integer id);


    /**
     * 查询name
     * @param name
     * @return
     */
    Admin findByName(String name);
    /**
     * 修改密码
     * @param updatePWDMap
     */
    public void updatePWD(Map<String,Object> updatePWDMap);



}
