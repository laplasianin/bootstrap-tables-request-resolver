This resolver allows user transform server-side request from bootstrap table plugin (https://github.com/wenzhixin/bootstrap-table/) to pojo.
Uses spring and javax-servlet and provide implementation of HandlerMethodArgumentResolver spring class.

<b>Usage</b>

<b>Configuration</b>
Xml config:<br>
In your `<mvc:annotation-driven>` attribute add new resolver and point it to `com.laplasianin.bootstraptable.resolver.FilterRequestResolver`

example:
```
<mvc:annotation-driven>
        <mvc:argument-resolvers>
            <bean class="com.crm.guard.filter.FilterRequestResolver"/>
        </mvc:argument-resolvers>
</mvc:annotation-driven>
```

and that's it, spring will do the rest

<b>Usage</b><br>
in your controller add request param with type `com.laplasianin.bootstraptable.resolver.Filter` and mark with 
annotation `@FilterRequest` and resolver will fill it

example:

`public WebResultTable<Client> dataTable(@FilterRequest Filter filter) {}`

Filter contains SearchFields and SortFields

Each sort field has `String field` and `SortType order` (ASC and DESC)<br>
Each search field has `String field`, `Object value` and `SearchType restriction`. 


Supported restrictions: NULL, NOT_NULL,
    EQUAL, NOT_EQUAL,
    GREATER_THAN, GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN, LESS_THAN_OR_EQUAL_TO,
    LIKE, LIKE_CASE_INSENSITIVE,
    IN, NOT_IN


<b>Usage 2</b><br>
Other way to use is just call util method `RequestParser.parse(Request tableRequest)`.<br>
In this case you need to populate Request with folowing parameters: offset, limit, sort, order, search, filterData. Check bootstrap table documentation for further explains of this attributes.
