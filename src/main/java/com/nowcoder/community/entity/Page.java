package com.nowcoder.community.entity;

/**
 * 封装分页相关的信息
 */
public class Page {
    // current page
    private int current = 1;
    // max number of posts illustrate in one page
    private int limit = 10;
    // check number of posts in order to check total number of pages
    private int rows;
    // check path for each page (用于复用分页链接）
    private String path;


    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return 当前页的起始行
     */
    public int getOffset(){
        // (current - 1) * limit
        return (current - 1) * limit;
    }

    /**
     *
     * @return total number of pages
     */
    public int getTotal(){
        // rows / limit
        if (rows % limit == 0){
            return rows / limit;
        }
        else{
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     * @return
     */
    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1 : from;

    }

    /**
     * 获取终止页码
     * @return
     */
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
