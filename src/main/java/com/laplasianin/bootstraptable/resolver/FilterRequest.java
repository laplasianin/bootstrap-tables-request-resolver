package com.laplasianin.bootstraptable.resolver;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterRequest {
}
