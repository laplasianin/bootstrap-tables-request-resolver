package com.laplasianin.bootstraptable.resolver.request;

public class Request {

    final int offset;
    final int limit;
    final String sort;
    final String order;
    final String search;
    final String filterData;

    public Request(int offset, int limit, String sort, String order, String search, String filterData) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
        this.order = order;
        this.search = search;
        this.filterData = filterData;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public String getSort() {
        return sort;
    }

    public String getOrder() {
        return order;
    }

    public String getSearch() {
        return search;
    }

    public String getFilterData() {
        return filterData;
    }
}
