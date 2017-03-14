package com.lianjia.fn.utils.model;

import java.io.Serializable;


public class IndexQueryParam implements Serializable {
    /**
     * 索引名称
     * 必填项 不能为空
     */
    private String index;
    /**
     * 检索关键字
     * 必填项 不能为空
     */
    private String query;
    /**
     * 检索字段，多个字段使用英文逗号（,）分隔，默认多个字段之间使用OR逻辑
     */
    private String qf;
    /**
     * 可以通过此参数获取本次查询需要的字段内容，多个字段使用英文逗号（,）分隔
     * 非 必填项
     * 默认:全部可展示字段
     */
    private String fl;
    /**
     * 页码
     * 非 必填项
     * 默认 1
     */
    private Integer pageNum;
    /**
     * 每页结果个数
     * 非 必填项
     * 默认 10
     */
    private Integer pageSize;
    /**
     * 过滤参数，格式：字段:值，多个过滤参数使用英文逗号（,）分隔
     * 非 必填项
     */
    private String filter;
    /**
     * 排序参数，格式：+field（正序），-field（逆序），多个排序参数使用英文逗号（,）分隔
     * 非 必填项
     */
    private String sort;
    /**
     * 检索策略，业务定制
     * 非 必填项
     */
    private String qp;
    /**
     * 检索规则，可通过控制台配置，
     * 包含fetchFields,filter,sort和查询规则，
     * 如此参数不为空，则其包含的字段参数无效。建议使用配置规则方式
     * 非 必填项
     */
    private String rule;

    /**
     * 高亮
     * 非必填项
     */
    private String hl;
    /**
     * 结果统计
     * 非必填项
     */
    private String facet;

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getQp() {
        return qp;
    }

    public void setQp(String qp) {
        this.qp = qp;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getFacet() {
        return facet;
    }

    public void setFacet(String facet) {
        this.facet = facet;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQf() {
        return qf;
    }

    public void setQf(String qf) {
        this.qf = qf;
    }

    @Override
    public String toString() {
        return "IndexQueryParam{" +
                "index='" + index + '\'' +
                ", query='" + query + '\'' +
                ", qf='" + qf + '\'' +
                ", fl='" + fl + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", filter='" + filter + '\'' +
                ", sort='" + sort + '\'' +
                ", qp='" + qp + '\'' +
                ", rule='" + rule + '\'' +
                ", hl='" + hl + '\'' +
                ", facet='" + facet + '\'' +
                '}';
    }
}
