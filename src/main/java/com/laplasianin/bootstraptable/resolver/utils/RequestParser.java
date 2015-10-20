package com.laplasianin.bootstraptable.resolver.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplasianin.bootstraptable.resolver.*;
import com.laplasianin.bootstraptable.resolver.request.Request;
import com.laplasianin.bootstraptable.resolver.search.SearchFields;
import com.laplasianin.bootstraptable.resolver.search.SearchType;
import com.laplasianin.bootstraptable.resolver.sort.SortFields;
import com.laplasianin.bootstraptable.resolver.sort.SortType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Iterator;

public class RequestParser {

    public static Object parse(Request tableRequest) throws IOException {
        SearchFields searchFields = new SearchFields();
        parseSearches(tableRequest.getSearch(), searchFields);
        parseSearches(tableRequest.getFilterData(), searchFields);
        SortFields sortFields = parseSorts(tableRequest.getSort(), tableRequest.getOrder());

        final Filter filter = new Filter();
        filter.setLimit(tableRequest.getLimit());
        filter.setOffset(tableRequest.getOffset());
        filter.setSortFields(sortFields);
        filter.setSearchFields(searchFields);
        return filter;
    }

    private static SortFields parseSorts(String sort, String order) {
        SortFields sortFields = new SortFields();
        if (StringUtils.isNotEmpty(sort) && StringUtils.isNotEmpty(order)) {
            sortFields.add(sort, SortType.valueOfCaseInsensitive(order));
        }
        return sortFields;
    }

    private static void parseSearches(String search, SearchFields searchFields) throws IOException {

        if (StringUtils.isEmpty(search)) {
            return;
        }

        JsonNode json;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.readTree(search);
        } catch (Exception e) {
            searchFields.add("_ALL_", SearchType.LIKE, search);
            return;
        }

        if (!json.fieldNames().hasNext()) {
            searchFields.add("_ALL_", SearchType.LIKE, search);
            return;
        }

        Iterator<String> fields = json.fieldNames();
        while (fields.hasNext()) {
            String field = fields.next();

            JsonNode searchNodes = json.findValue(field);
            Iterator<String> searchesNames = searchNodes.fieldNames();
            while (searchesNames.hasNext()) {
                SearchType searchType = null;
                Object searchValue = null;
                String searchTypeString = searchesNames.next();
                switch (searchTypeString) {
                    case "lte":
                        searchType = SearchType.LESS_THAN_OR_EQUAL_TO;
                        break;
                    case "gte":
                        searchType = SearchType.GREATER_THAN_OR_EQUAL_TO;
                        break;
                    case "eq":
                        searchType = SearchType.EQUAL;
                        break;
                    case "cnt":
                        searchType = SearchType.LIKE_CASE_INSENSITIVE;
                        break;
                    case "in":
                        searchType = SearchType.IN;
                        break;
                    case "neq":
                        searchType = SearchType.NOT_EQUAL;
                        break;
                    case "nn":
                        searchType = SearchType.NOT_NULL;
                        break;
                    case "null":
                        searchType = SearchType.NULL;
                        break;
                }

                JsonNode value = searchNodes.findValue(searchTypeString);
                if (!"_values".equals(searchTypeString)) {
                    try {
                        searchValue = value.textValue();
                    } catch (NumberFormatException e) {}
                } else {
                    if (value.isArray()) {
                        for (JsonNode jsonNode : value) {
                            if (jsonNode.isTextual()) {
                                String val = jsonNode.textValue();
                                switch (val) {
                                    case "ept":
                                        searchFields.add(field, SearchType.NULL, "ept");
                                        break;
                                    case "in":
                                        searchFields.add(field, SearchType.IN, val);
                                        break;
                                    case "nept":
                                        searchFields.add(field, SearchType.NOT_NULL, "nept");
                                        break;
                                    default:
                                        searchFields.add(field, SearchType.LIKE_CASE_INSENSITIVE, val);
                                        break;
                                }
                            }
                        }
                    } else {
                        String val = value.textValue();
                        switch (val) {
                            case "in":
                                searchFields.add(field, SearchType.IN, val);
                                break;
                            default:
                                searchFields.add(field, SearchType.LIKE_CASE_INSENSITIVE, val);
                                break;
                        }
                    }
                }

                if (StringUtils.isNotEmpty(field) && searchType != null && searchValue != null) {
                    searchFields.add(field, searchType, searchValue);
                }
            }
        }
    }
}
