package com.hqyj.pojo;

import java.io.Serializable;

//描述表userinfo的信息
public class UserInfo extends MyPage implements Serializable {
    //描述userId列的
    private int userId;
    //描述userName列的
    private String userName;
    //描述userPwd列的
    private String userPwd;

    //描述修改密码的旧密码
    private String oldPwd;

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    private  String js;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    //描述修改密码的新密码
    private String newPwd;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    //盐值
    private String salt;

    public String getCondition() {
        if(condition==null){
            return null;
        }
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConValue() {
        if(conValue==null){
            return "";
        }
        return conValue;
    }

    public void setConValue(String conValue) {
        this.conValue = conValue;
    }

    //描述查询条件的的列
    private String condition;
    //描述查询条件
    private String conValue;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
}
