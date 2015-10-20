package com.laplasianin.bootstraptable.resolver.search;

import java.util.ArrayList;

public class SearchFields extends ArrayList<SearchField> {

    public SearchFields add(String field, SearchType restriction, Object value) {
        this.add(new SearchField(field, restriction, value));
        return this;
    }
}
