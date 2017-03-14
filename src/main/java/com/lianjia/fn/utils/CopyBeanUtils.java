package com.lianjia.fn.utils;


import com.google.common.base.Function;
import com.lianjia.fn.utils.annotation.FieldInfo;
import jodd.bean.BeanUtil;
import jodd.bean.BeanUtilBean;
import jodd.bean.BeanVisitorImplBase;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static jodd.util.StringPool.LEFT_SQ_BRACKET;
import static jodd.util.StringPool.RIGHT_SQ_BRACKET;

/**
 * @author: yong.lin
 * @date: 2016-10-21  12:34
 */
public class CopyBeanUtils extends BeanVisitorImplBase<CopyBeanUtils> {


    protected Object destination;
    protected boolean forced;
    protected boolean declaredTarget;
    protected boolean isTargetMap;
    protected Function<String, Object> expressionFunction;
    protected Map<String, String> fromToFieldMap = new HashMap<String, String>();
    protected Map<String, String> expressionMap = new HashMap<String, String>();

    // ---------------------------------------------------------------- ctor

    /**
     * Creates new BeanCopyUtils process between the source and the destination.
     * Both source and destination can be a POJO object or a <code>Map</code>.
     */
    public CopyBeanUtils(Object source, Object destination) {
        this.source = source;
        this.destination = destination;
    }

    private void buildFromToFieldMap(Object destination) {
        Class<?> clazz = destination.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                FieldInfo fieldInfo = field.getAnnotation(FieldInfo.class);
                if (fieldInfo != null) {
                    if (StringUtils.isNotEmpty(fieldInfo.from())) {
                        fromToFieldMap.put(fieldInfo.from(), field.getName());
                    } else if (StringUtils.isNotEmpty(fieldInfo.expression())) {
                        expressionMap.put(field.getName(), fieldInfo.expression());
                    }

                }
            }
        }
    }

    private CopyBeanUtils(Object source) {
        this.source = source;
    }

    /**
     * Simple static factory for <code>BeanCopyUtils</code>.
     *
     * @see #CopyBeanUtils(Object, Object)
     */
    public static CopyBeanUtils beans(Object source, Object destination) {
        return new CopyBeanUtils(source, destination);
    }

    /**
     * Creates <copy>BeanCopyUtils</copy> with given POJO bean as a source.
     */
    public static CopyBeanUtils fromBean(Object source) {
        return new CopyBeanUtils(source);
    }

    /**
     * Creates <copy>BeanCopyUtils</copy> with given <code>Map</code> as a source.
     */
    public static CopyBeanUtils fromMap(Map<?, ?> source) {
        CopyBeanUtils beanCopy = new CopyBeanUtils(source);

        beanCopy.isSourceMap = true;

        return beanCopy;
    }

    /**
     * Defines source, detects a map.
     */
    public static CopyBeanUtils from(Object source) {
        CopyBeanUtils beanCopy = new CopyBeanUtils(source);

        beanCopy.isSourceMap = source instanceof Map;

        return beanCopy;
    }

    // ---------------------------------------------------------------- destination

    /**
     * Defines destination bean.
     */
    public CopyBeanUtils toBean(Object destination) {
        this.destination = destination;
        return this;
    }

    /**
     * Defines destination map.
     */
    public CopyBeanUtils toMap(Map<?, ?> destination) {
        this.destination = destination;

        isTargetMap = true;

        return this;
    }

    /**
     * Defines destination, detects a map.
     */
    public CopyBeanUtils to(Object destination) {
        this.destination = destination;

        this.isTargetMap = destination instanceof Map;

        return this;
    }

    public CopyBeanUtils function(Function<String, Object> function) {
        this.expressionFunction = function;
        return this;
    }

    // ---------------------------------------------------------------- properties

    /**
     * Defines if all properties should be copied (when set to <code>true</code>)
     * or only public (when set to <code>false</code>, default).
     */
    public CopyBeanUtils declared(boolean declared) {
        this.declared = declared;
        this.declaredTarget = declared;
        return this;
    }

    /**
     * Fine-tuning of the declared behaviour.
     */
    public CopyBeanUtils declared(boolean declaredSource, boolean declaredTarget) {
        this.declared = declaredSource;
        this.declaredTarget = declaredTarget;
        return this;
    }

    public CopyBeanUtils forced(boolean forced) {
        this.forced = forced;
        return this;
    }

    // ---------------------------------------------------------------- visitor

    protected BeanUtil beanUtil;

    /**
     * Performs the copying.
     */
    public void copy() {
        buildFromToFieldMap(this.destination);
        beanUtil = new BeanUtilBean().declared(declared).forced(forced).silent(true);
        visit();
        if (MapUtils.isNotEmpty(expressionMap)) {
            for (Map.Entry<String, String> entry : expressionMap.entrySet()) {
                Object value = expressionFunction.apply(entry.getValue());
                beanUtil.setProperty(destination, entry.getKey(), value);
            }
        }
    }

    /**
     * Copies single property to the destination.
     * Exceptions are ignored, so copying continues if
     * destination does not have some of the sources properties.
     */
    @Override
    protected boolean visitProperty(String name, Object value) {
        if (isTargetMap) {
            name = LEFT_SQ_BRACKET + name + RIGHT_SQ_BRACKET;
        }
        if (StringUtils.isNotEmpty(fromToFieldMap.get(name))) {
            name = fromToFieldMap.get(name);
        }
        beanUtil.setProperty(destination, name, value);

        return true;
    }
}
