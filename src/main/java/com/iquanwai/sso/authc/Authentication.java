package com.iquanwai.sso.authc;


import java.io.Serializable;
import java.util.List;

/**
 * @author xcl
 * @version 2020/4/20
 */
public class Authentication implements Serializable {

    private static final long serialVersionUID = -6592138289969667712L;
    //corp_staff.id
    private Integer staffId;

    //唯一id
    private String uuid;

    //账号/手机号
    private String username;

    //显示名称
    private String name;

    //手机号
    private String mobile;

    //权限集合
    private List<String> permissions;

    //角色集合
    private List<String> roles;


    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
