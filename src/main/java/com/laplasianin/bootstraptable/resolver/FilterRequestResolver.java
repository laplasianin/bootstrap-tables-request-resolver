package com.laplasianin.bootstraptable.resolver;

import com.laplasianin.bootstraptable.resolver.request.Request;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static com.laplasianin.bootstraptable.resolver.utils.RequestParser.parse;

public class FilterRequestResolver implements HandlerMethodArgumentResolver {

    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String SORT = "sort";
    private static final String ORDER = "order";
    private static final String SEARCH = "search";
    private static final String FILTER = "filter";

    @Override
    public boolean supportsParameter(MethodParameter param) {
        FilterRequest tableParamAnnotation = param.getParameterAnnotation(FilterRequest.class);
        return tableParamAnnotation != null;
    }

    @Override
    public Object resolveArgument(MethodParameter param, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        FilterRequest tableParamAnnotation = param.getParameterAnnotation(FilterRequest.class);
        if (tableParamAnnotation != null) {
            HttpServletRequest httpRequest = (HttpServletRequest) request.getNativeRequest();

            int offset = Integer.parseInt(httpRequest.getParameter(OFFSET));
            int limit = Integer.parseInt(httpRequest.getParameter(LIMIT));
            String sort = httpRequest.getParameter(SORT);
            String order = httpRequest.getParameter(ORDER);
            String search = httpRequest.getParameter(SEARCH);
            String filterData = httpRequest.getParameter(FILTER);

            final Request tableRequest = new Request(offset, limit, sort, order, search, filterData);

            return parse(tableRequest);
        }

        return WebArgumentResolver.UNRESOLVED;
    }


}
