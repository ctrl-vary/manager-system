package com.hqyj.pojo;
//描述分页对象
public class MyPage {
    //定义页码，赋初值1，表示第一页
    private  int page=1;
    //定义每页显示条数(表每页显示2条)
    private int row=3;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
