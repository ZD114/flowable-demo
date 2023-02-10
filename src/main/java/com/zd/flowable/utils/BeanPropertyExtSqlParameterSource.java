package com.zd.flowable.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanPropertyExtSqlParameterSource extends AbstractSqlParameterSource {
    private final Set<String> ignoreProperties = new HashSet<>();

    private final BeanWrapper beanWrapper;

    @Nullable
    private String[] propertyNames;

    public BeanPropertyExtSqlParameterSource(Object object) {
        this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
    }

    public BeanPropertyExtSqlParameterSource(Object object, @Nullable String... ignoreProperties) {
        this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);

        var ignoreSet = Arrays.asList(ignoreProperties).stream().filter(str -> !StringUtils.hasLength(str))
                .collect(Collectors.toSet());

        if (!ignoreSet.isEmpty()) {
            this.ignoreProperties.addAll(ignoreSet);
        }
    }

    @Override
    public boolean hasValue(String paramName) {
        return this.beanWrapper.isReadableProperty(paramName);
    }

    @Override
    @Nullable
    public Object getValue(String paramName) {
        try {
            return this.beanWrapper.getPropertyValue(paramName);
        } catch (NotReadablePropertyException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Override
    public int getSqlType(String paramName) {
        var sqlType = super.getSqlType(paramName);

        if (sqlType != TYPE_UNKNOWN) {
            return sqlType;
        }

        var propType = this.beanWrapper.getPropertyType(paramName);

        return StatementCreatorUtils.javaTypeToSqlParameterType(propType);
    }

    @Override
    @NonNull
    public String[] getParameterNames() {
        return getReadablePropertyNames();
    }

    public String[] getReadablePropertyNames() {
        if (this.propertyNames == null) {
            var names = new ArrayList<String>();
            var props = this.beanWrapper.getPropertyDescriptors();

            for (var pd : props) {
                if (this.beanWrapper.isReadableProperty(pd.getName()) && !ignoreProperties.contains(pd.getName())) {
                    names.add(pd.getName());
                }
            }

            this.propertyNames = StringUtils.toStringArray(names);
        }

        return this.propertyNames;
    }

}
