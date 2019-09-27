package com.qingcheng.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingcheng.dao.AdminMapper;
import com.qingcheng.dao.AdminRoleMapper;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.AdminAndRole;
import com.qingcheng.pojo.system.AdminRole;
import com.qingcheng.service.system.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Admin> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Admin> admins = (Page<Admin>) adminMapper.selectAll();
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Admin> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return adminMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Admin> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Admin> admins = (Page<Admin>) adminMapper.selectByExample(example);
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Admin findById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }


    /**
     * 新增
     * @param admin
     */
    public void add(Admin admin) {
        adminMapper.insert(admin);
    }

    /**
     * 新增
     * @param adminAndRole 组合实体类
     */
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    public void addAdminRole(AdminAndRole adminAndRole) {
        Admin admin = new Admin();
        String passwordPlain;
        //设置新密码
        if ((passwordPlain = admin.getPassword())!=null ){
            //并且加盐
            String gensalt = BCrypt.gensalt();
            String hashpw = BCrypt.hashpw(passwordPlain, gensalt);
            admin.setPassword(hashpw);
        }
        //存完再取id 保证取出的id不为空
        if (admin.getId()==null){
            adminMapper.insert(admin);
        }else {
            adminMapper.updateByPrimaryKeySelective(admin);
        }
        Integer id = admin.getId();
        //删除表中原有角色信息
        Example example = new Example(AdminRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId", id);
        adminRoleMapper.deleteByExample(example);
        //保存角色信息
        List<Integer> roleIdList = adminAndRole.getRoleIdList();
        AdminRole adminRole  = new AdminRole();
        adminRole.setAdminId(id);
        for (Integer roleId : roleIdList) {
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }
    }

    /**
     *
     * 查询id
     * @param id
     * @return
     */
    public AdminAndRole findByAdminRoleId(Integer id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        admin.setPassword(null);
        Example example = new Example(AdminRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId",id);

        //根据条件查出adminRoleList的id集合遍历
        List<AdminRole> adminRoleList = adminRoleMapper.selectByExample(example);
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (AdminRole adminRole : adminRoleList) {
            //存储进arrayList集合
            arrayList.add(adminRole.getRoleId());
        }
        AdminAndRole adminAndRole = new AdminAndRole();
        adminAndRole.setAdmin(admin);
        adminAndRole.setRoleIdList(arrayList);

        return adminAndRole;
    }

    /**
     * 修改
     * @param admin
     */
    public void update(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询name
     * @param name
     * @return
     */
    public Admin findByName(String name) {
        Admin admin = adminMapper.selectByPrimaryKey(name);
        return admin;
    }

    /**
     * 修改密码
     * @param updatePWDMap
     */
    public void updatePWD(Map<String, Object> updatePWDMap) {
        String  loginName = (String) updatePWDMap.get("loginName");
        String firstPassword = (String)updatePWDMap.get("firstPassword");
        String newPassword =(String) updatePWDMap.get("newPassword");
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loginName",loginName);
        List<Admin> adminList = adminMapper.selectByExample(example);
        //判断用户是否激活
        if (adminList.isEmpty()) {
            throw new RuntimeException("该用户未激活!");
        }
        //校验密码是否正确
        boolean checkpw = BCrypt.checkpw(firstPassword, adminList.get(0).getPassword());
        if (!checkpw){
            throw new RuntimeException("密码输入错误!");
        }
        //设置新密码 并使用BCrypt加盐
        Admin admin = new Admin();
        admin.setId(adminList.get(0).getId());
        admin.setPassword(BCrypt.hashpw(newPassword,BCrypt.gensalt()));
        adminMapper.updateByPrimaryKeySelective(admin); //  updateByPrimaryKeySelective 记住忽略空密码
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 用户名
            if(searchMap.get("loginName")!=null && !"".equals(searchMap.get("loginName"))){
                criteria.andEqualTo("loginName",searchMap.get("loginName"));
            }
            // 密码
            if(searchMap.get("password")!=null && !"".equals(searchMap.get("password"))){
                criteria.andEqualTo("password",searchMap.get("password"));
            }
            // 状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
            }

            // id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }

        }
        return example;
    }

}
