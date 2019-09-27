package com.qingcheng.pojo.system;

import java.io.Serializable;
import java.util.List;

/**
 * 管理员实体类和角色ID集合的组合实体类
 */
public class AdminAndRole implements Serializable {


    private Admin admin;

    private List<Integer> roleIdList;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Integer> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Integer> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
