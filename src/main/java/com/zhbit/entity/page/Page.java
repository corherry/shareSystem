package com.zhbit.entity.page;

import java.util.List;

public class Page<T> {

    private int pageNum;

    private int pageSize;

    private int totalRow;

    private List<T> resultList;

    public Page(int pageNum, int pageSize, int totalRow, List<T> resultList) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRow = totalRow;
        this.resultList = resultList;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}
