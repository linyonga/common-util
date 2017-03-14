package com.ly.common.utils;


import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.ly.common.utils.annotation.Filter;
import com.ly.common.utils.annotation.Fl;
import com.ly.common.utils.annotation.Index;
import com.ly.common.utils.annotation.PageNum;
import com.ly.common.utils.annotation.PageSize;
import com.ly.common.utils.annotation.Query;
import com.ly.common.utils.annotation.Sort;
import com.ly.common.utils.model.IndexQueryParam;
import com.ly.common.utils.model.IndexReq;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author: yong.lin
 * @date: 2016-10-27  21:05
 */
public class IndexQueryParamBuilder {
    private static final ArrayList<Class<? extends Annotation>> indexAnnotations = Lists.newArrayList(
            Filter.class,
            Fl.class,
            Index.class,
            PageNum.class,
            PageSize.class,
            Query.class,
            Sort.class);
    private static Joiner joiner = Joiner.on(",").skipNulls();

    public static IndexQueryParam build(IndexReq indexReq) {
        IndexQueryParam indexQueryParam = new IndexQueryParam();
        try {
            Class clazz = indexReq.getClass();
            Multimap<Class<? extends Annotation>, Field> annotationMultimap = HashMultimap.create();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                annotationMultimap = fillMultimap(fields, annotationMultimap);
            }
            indexQueryParam = processMultimap(annotationMultimap, indexQueryParam,
                    indexReq);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
        return indexQueryParam;
    }

    private static IndexQueryParam processMultimap(Multimap<Class<? extends Annotation>, Field> annotationMultimap,
                                                   IndexQueryParam indexQueryParam, IndexReq indexReq) throws
            IllegalAccessException {
        for (Class<? extends Annotation> clazz : indexAnnotations) {
            if (clazz.equals(Filter.class)) {
                indexQueryParam = processFilter(annotationMultimap.get(Filter.class), indexReq,
                        indexQueryParam);
            } else if (clazz.equals(Query.class)) {
                indexQueryParam = processQuery(annotationMultimap.get(Query.class), indexReq,
                        indexQueryParam);
            } else if (clazz.equals(Fl.class)) {
                indexQueryParam = processFl(annotationMultimap.get(Fl.class), indexReq,
                        indexQueryParam);
            } else if (clazz.equals(Sort.class)) {
                indexQueryParam = processSort(annotationMultimap.get(Sort.class), indexReq,
                        indexQueryParam);
            } else if (clazz.equals(Index.class)) {
                indexQueryParam = processIndex(annotationMultimap.get(Index.class), indexReq,
                        indexQueryParam);
            } else if (clazz.equals(PageSize.class)) {
                indexQueryParam = processPageSize(annotationMultimap.get(PageSize.class), indexReq,
                        indexQueryParam);
            } else if (clazz.equals(PageNum.class)) {
                indexQueryParam = processPageNum(annotationMultimap.get(PageNum.class), indexReq,
                        indexQueryParam);
            }
        }
        return indexQueryParam;
    }

    private static IndexQueryParam processPageNum(Collection<Field> fields, IndexReq indexReq,
                                                  IndexQueryParam indexQueryParam) throws IllegalAccessException {
        if (fields == null || fields.size() == 0) {
            return indexQueryParam;
        }
        Preconditions.checkArgument(fields.size() == 1, "PageNum最多只有一个");
        Field field = Lists.newArrayList(fields).get(0);
        field.setAccessible(true);
        Object value = field.get(indexReq);
        if (value != null) {
            indexQueryParam.setPageNum(Integer.parseInt(value.toString()));
        }

        return indexQueryParam;
    }

    private static IndexQueryParam processPageSize(Collection<Field> fields, IndexReq indexReq,
                                                   IndexQueryParam indexQueryParam) throws IllegalAccessException {
        if (fields == null || fields.size() == 0) {
            return indexQueryParam;
        }
        Preconditions.checkArgument(fields.size() == 1, "PageSize最多只有一个");
        Field field = Lists.newArrayList(fields).get(0);
        field.setAccessible(true);
        Object value = field.get(indexReq);
        if (value != null) {
            indexQueryParam.setPageSize(Integer.parseInt(value.toString()));
        }

        return indexQueryParam;
    }

    private static IndexQueryParam processIndex(Collection<Field> fields, IndexReq indexReq, IndexQueryParam indexQueryParam)
            throws IllegalAccessException {
        Preconditions.checkArgument(fields != null && fields.size() == 1, "Index注解必须且只有一个");
        Field field = Lists.newArrayList(fields).get(0);
        field.setAccessible(true);
        Object value = field.get(indexReq);
        Preconditions.checkArgument(value != null, "Index注解字段不能为空");
        indexQueryParam.setIndex(value.toString());
        return indexQueryParam;

    }

    private static IndexQueryParam processSort(Collection<Field> fields, IndexReq indexReq,
                                               IndexQueryParam indexQueryParam) throws IllegalAccessException {
        if (fields != null) {
            List<String> sortList = Lists.newArrayList();
            for (Field field : fields) {
                field.setAccessible(true);
                Sort sort = field.getAnnotation(Sort.class);
                Object value = field.get(indexReq);
                if (value != null) {
                    String fieldName = StringUtils.isEmpty(sort.field()) ? field.getName() : sort.field();
                    sortList.add(Sort.SortType.apply(value.toString(), fieldName));
                }
            }

            indexQueryParam.setSort(joiner.join(sortList));
        }
        return indexQueryParam;
    }

    private static IndexQueryParam processFl(Collection<Field> fields, IndexReq indexReq,
                                             IndexQueryParam indexQueryParam) throws IllegalAccessException {
        if (fields != null) {
            List<String> fls = Lists.newArrayList();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(indexReq);
                if (value != null) {
                    fls.add(value.toString());
                }
            }
            indexQueryParam.setFl(joiner.join(fls));
        }
        return indexQueryParam;
    }

    private static IndexQueryParam processQuery(Collection<Field> fields, IndexReq indexReq,
                                                IndexQueryParam indexQueryParam)
            throws IllegalAccessException {
        if (fields != null) {

            for (Field field : fields) {
                field.setAccessible(true);
                Query query = field.getAnnotation(Query.class);
                Object value = field.get(indexReq);
                if (value != null) {
                    indexQueryParam.setQuery(processSymbol(value.toString()));
                    String qf = StringUtils.isEmpty(query.qf()) ? field.getName() : query.qf();
                    indexQueryParam.setQf(qf);
                    break;
                }

            }


        }
        return indexQueryParam;
    }

    @SuppressWarnings("unchecked")
    private static IndexQueryParam processFilter(Collection<Field> fields, IndexReq indexReq,
                                                 IndexQueryParam indexQueryParam) throws IllegalAccessException {
        List<String> filters = Lists.newArrayList();
        if (StringUtils.isNotBlank(indexReq.getCustomFilter())) {
            filters.add(indexReq.getCustomFilter());
        }
        if (fields != null) {
            Map<String, Map<Filter.RangeType, Object>> rangeFilterMap = Maps.newHashMap();

            for (Field field : fields) {
                field.setAccessible(true);
                Filter filter = field.getAnnotation(Filter.class);
                Object value = field.get(indexReq);
                if (value == null) {
                    continue;
                }
                String fieldName = StringUtils.isEmpty(filter.field()) ? field.getName() : filter.field();
                if (filter.filerType() == Filter.FilterType.range) {
                    Map<Filter.RangeType, Object> rangeMap = (Map<Filter.RangeType, Object>)
                            (rangeFilterMap.get(fieldName) == null ? Maps.newHashMap()
                                    : (rangeFilterMap.get(fieldName)));
                    rangeMap.put(filter.rangeType(), value);
                    rangeFilterMap.put(fieldName, rangeMap);
                } else {
                    filters.add(filter.filerType().apply(fieldName, value));
                }
            }
            for (Map.Entry<String, Map<Filter.RangeType, Object>> entry : rangeFilterMap.entrySet()) {
                filters.add(Filter.FilterType.range.apply(entry.getKey(), entry.getValue()));
            }
        }
        indexQueryParam.setFilter(joiner.join(filters));
        return indexQueryParam;
    }

    private static Multimap<Class<? extends Annotation>, Field> fillMultimap(Field[] fields,
                                                                             Multimap<Class<? extends Annotation>,
                                                                                     Field> annotationMultimap) {
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (indexAnnotations.contains(annotation.annotationType())) {
                    annotationMultimap.put(annotation.annotationType(), field);
                }
            }
        }
        return annotationMultimap;
    }

    protected static String processSymbol(String s) {
        if (StringUtils.isNotEmpty(s)) {
            return s.replaceAll(" ", "");
        }
        return s;
    }
}
