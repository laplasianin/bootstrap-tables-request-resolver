package com.laplasianin.bootstraptable.resolver.sort;

import java.util.ArrayList;

public class SortFields extends ArrayList<SortField> {

    public SortFields add(String field, SortType order) {
        this.add(new SortField(field, order));
        return this;
    }
}
