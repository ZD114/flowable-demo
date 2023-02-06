package com.zd.flowable.utils;

import com.google.common.base.CaseFormat;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class JdbcUtility {
    private JdbcUtility() {

    }

    public static SqlParameterSource getSqlParameterSource(Object object) {
        return new BeanPropertyExtSqlParameterSource(object);
    }

    public static SqlParameterSource getSqlParameterSource(Object object, @Nullable String... ignoreProperties) {
        return new BeanPropertyExtSqlParameterSource(object, ignoreProperties);
    }

    public static String getInsertSql(Object object, @Nullable String... ignoreProperties) {
        var sb = new StringBuilder("INSERT INTO `").append(getTableName(object.getClass())).append("` (");

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        var props = beanWrapper.getPropertyDescriptors();
        var ignoreSet = null != ignoreProperties ? Arrays.asList(ignoreProperties).stream()
                .filter(str -> !StringUtils.isEmpty(str)).collect(Collectors.toSet()) : new HashSet<String>(0);
        var names = new ArrayList<String>();

        for (var pd : props) {
            if (!pd.getName().equalsIgnoreCase("class") && beanWrapper.isReadableProperty(pd.getName())
                    && !ignoreSet.contains(pd.getName())) {
                names.add(pd.getName());
            }
        }

        for (var i = 0; i < names.size(); i++) {
            sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, names.get(i)));

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(") VALUES (");

        for (var i = 0; i < names.size(); i++) {
            sb.append(":").append(names.get(i));

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    public static String getInsertSql(String tableName, Object object, @Nullable String... ignoreProperties) {
        var sb = new StringBuilder("INSERT INTO `").append(tableName != "" ? tableName : getTableName(object.getClass())).append("` (");

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        var props = beanWrapper.getPropertyDescriptors();
        var ignoreSet = null != ignoreProperties ? Arrays.asList(ignoreProperties).stream()
                .filter(str -> !StringUtils.isEmpty(str)).collect(Collectors.toSet()) : new HashSet<String>(0);
        var names = new ArrayList<String>();

        for (var pd : props) {
            if (!pd.getName().equalsIgnoreCase("class") && beanWrapper.isReadableProperty(pd.getName())
                    && !ignoreSet.contains(pd.getName())) {
                names.add(pd.getName());
            }
        }

        for (var i = 0; i < names.size(); i++) {
            sb.append("`" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, names.get(i)) + "`");

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(") VALUES (");

        for (var i = 0; i < names.size(); i++) {
            sb.append(":").append(names.get(i));

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    public static String getInsertSql(Object object, boolean filterNUll, @Nullable String... ignoreProperties) {
        var sb = new StringBuilder("INSERT INTO `").append(getTableName(object.getClass())).append("` (");

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        var props = beanWrapper.getPropertyDescriptors();
        var ignoreSet = null != ignoreProperties ? Arrays.asList(ignoreProperties).stream()
                .filter(str -> !StringUtils.isEmpty(str)).collect(Collectors.toSet()) : new HashSet<String>(0);
        var names = new ArrayList<String>();

        for (var pd : props) {
            if (!pd.getName().equalsIgnoreCase("class") && beanWrapper.isReadableProperty(pd.getName())
                    && !ignoreSet.contains(pd.getName())) {
                if (filterNUll) {
                    Method readMethod = pd.getReadMethod();
                    try {
                        Object value = readMethod.invoke(object);
                        if (value == null) {
                            continue;
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
                names.add(pd.getName());
            }
        }

        for (var i = 0; i < names.size(); i++) {
            sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, names.get(i)));

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(") VALUES (");

        for (var i = 0; i < names.size(); i++) {
            sb.append(":").append(names.get(i));

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    public static String getUpdateSql(Object object, @Nullable String... ignoreProperties) {
        var sb = new StringBuilder("UPDATE `").append(getTableName(object.getClass())).append("` SET ");

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        var props = beanWrapper.getPropertyDescriptors();
        var ignoreSet = null != ignoreProperties ? Arrays.asList(ignoreProperties).stream()
                .filter(str -> !StringUtils.isEmpty(str)).collect(Collectors.toSet()) : new HashSet<String>(0);
        var names = new ArrayList<String>();

        for (var pd : props) {
            if (!pd.getName().equalsIgnoreCase("class") && !pd.getName().equalsIgnoreCase("id")
                    && beanWrapper.isReadableProperty(pd.getName()) && !ignoreSet.contains(pd.getName())) {
                names.add(pd.getName());
            }
        }

        for (var i = 0; i < names.size(); i++) {
            var name = names.get(i);

            sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)).append("=").append(":")
                    .append(name);

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(" WHERE id=:id");

        return sb.toString();
    }

    public static String getUpdateSql(Object object, boolean filterNUll, @Nullable String... ignoreProperties) {
        var sb = new StringBuilder("UPDATE `").append(getTableName(object.getClass())).append("` SET ");

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        var props = beanWrapper.getPropertyDescriptors();
        var ignoreSet = null != ignoreProperties ? Arrays.asList(ignoreProperties).stream()
                .filter(str -> !StringUtils.isEmpty(str)).collect(Collectors.toSet()) : new HashSet<String>(0);
        var names = new ArrayList<String>();

        for (var pd : props) {
            if (!pd.getName().equalsIgnoreCase("class") && !pd.getName().equalsIgnoreCase("id")
                    && beanWrapper.isReadableProperty(pd.getName()) && !ignoreSet.contains(pd.getName())) {
                if (filterNUll) {
                    Method readMethod = pd.getReadMethod();
                    try {
                        Object value = readMethod.invoke(object);
                        if (value == null) {
                            continue;
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
                names.add(pd.getName());
            }
        }

        for (var i = 0; i < names.size(); i++) {
            var name = names.get(i);

            sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)).append("=").append(":")
                    .append(name);

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(" WHERE id=:id");

        return sb.toString();
    }

    public static String getUpdateSql(String tableName, Object object, boolean filterNUll, @Nullable String... ignoreProperties) {
        var sb = new StringBuilder("UPDATE `").append(tableName != "" ? tableName : getTableName(object.getClass())).append("` SET ");

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        var props = beanWrapper.getPropertyDescriptors();
        var ignoreSet = null != ignoreProperties ? Arrays.asList(ignoreProperties).stream()
                .filter(str -> !StringUtils.isEmpty(str)).collect(Collectors.toSet()) : new HashSet<String>(0);
        var names = new ArrayList<String>();

        for (var pd : props) {
            if (!pd.getName().equalsIgnoreCase("class") && !pd.getName().equalsIgnoreCase("id")
                    && beanWrapper.isReadableProperty(pd.getName()) && !ignoreSet.contains(pd.getName())) {
                if (filterNUll) {
                    Method readMethod = pd.getReadMethod();
                    try {
                        Object value = readMethod.invoke(object);
                        if (value == null) {
                            continue;
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
                names.add(pd.getName());
            }
        }

        for (var i = 0; i < names.size(); i++) {
            var name = names.get(i);

            sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)).append("=").append(":")
                    .append(name);

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(" WHERE id=:id");

        return sb.toString();
    }

    public static String getTableName(Class<?> cls) {
        var className = cls.getSimpleName();

        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                className.endsWith("Entity") ? className.substring(0, className.length() - "Entity".length())
                        : className);
    }

    public static String getPropertys(Object object, @Nullable String... ignoreProperties) {

        var sb = new StringBuilder();

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        var props = beanWrapper.getPropertyDescriptors();
        var ignoreSet = null != ignoreProperties ? Arrays.asList(ignoreProperties).stream()
                .filter(str -> !StringUtils.isEmpty(str)).collect(Collectors.toSet()) : new HashSet<String>(0);
        var names = new ArrayList<String>();

        for (var pd : props) {
            if (!pd.getName().equalsIgnoreCase("class") && beanWrapper.isReadableProperty(pd.getName())
                    && !ignoreSet.contains(pd.getName())) {
                names.add(pd.getName());
            }
        }

        for (var i = 0; i < names.size(); i++) {
            sb.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, names.get(i)));

            if (i < names.size() - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }
}
