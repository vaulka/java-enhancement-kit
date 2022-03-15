package com.pongsky.kit.storage.web.aspect.around;

import com.pongsky.kit.storage.annotation.StorageResourceMark;
import com.pongsky.kit.storage.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 云存储自动添加 uri
 *
 * @author pengsenhao
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "storage.is-enable-resource-mark", havingValue = "true", matchIfMissing = true)
public class StorageAspect {

    private final StorageProperties storageProperties;

    @Around("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public Object exec(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        return this.storageResource(method.getAnnotation(StorageResourceMark.class), result);
    }

    /**
     * 云存储资源 uri 拼接
     *
     * @param mark         数据脱敏注解
     * @param originResult 原始 result
     * @return 脱敏后的数据
     * @author pengsenhao
     */
    @SuppressWarnings({"unchecked"})
    private Object storageResource(StorageResourceMark mark, Object originResult) {
        if (this.isBasicDataType(originResult)) {
            return originResult;
        } else if (originResult instanceof String) {
            return mark == null
                    ? originResult
                    : storageProperties.getBaseUri() + originResult;
        } else if (originResult instanceof List) {
            List<Object> list = (List<Object>) originResult;
            List<Object> results = new ArrayList<>(list.size());
            for (Object result : list) {
                results.add(this.storageResource(null, result));
            }
            return results;
        } else if (originResult instanceof Set) {
            Set<Object> set = (Set<Object>) originResult;
            Set<Object> results = new HashSet<>(set.size());
            for (Object result : set) {
                results.add(this.storageResource(null, result));
            }
            return results;
        }
        List<Field> fields = Arrays.stream(originResult.getClass().getDeclaredFields())
                .filter(this::unisBasicFieldDataType)
                .collect(Collectors.toList());
        for (Field field : fields) {
            if (field.getType().toString().equals(String.class.toString())) {
                mark = field.getAnnotation(StorageResourceMark.class);
                if (mark == null) {
                    continue;
                }
                Object result = this.getValue(originResult, field);
                if (result != null) {
                    this.setValue(originResult, field, storageProperties.getBaseUri() + result);
                }
            } else {
                Object result = this.getValue(originResult, field);
                if (result != null) {
                    this.storageResource(null, result);
                }
            }
        }
        return originResult;
    }

    /**
     * 返回值是否是基础数据类型
     * <p>
     * 除了 String 类型
     *
     * @param originResult 返回值
     * @return 返回值是否是基础数据类型
     * @author pengsenhao
     */
    private boolean isBasicDataType(Object originResult) {
        return originResult == null
                || originResult instanceof Enum
                || originResult instanceof Byte
                || originResult instanceof Short
                || originResult instanceof Integer
                || originResult instanceof Long
                || originResult instanceof Float
                || originResult instanceof Double
                || originResult instanceof Character
                || originResult instanceof Boolean
                || originResult instanceof Map
                || originResult instanceof BigInteger
                || originResult instanceof BigDecimal
                || originResult instanceof Date
                || originResult instanceof LocalDate
                || originResult instanceof LocalTime
                || originResult instanceof LocalDateTime;
    }

    /**
     * 字段是否不是基础数据类型 / 常量
     * <p>
     * 除了 String 类型
     *
     * @param field 字段
     * @return 字段是否是基础数据类型 / 常量
     * @author pengsenhao
     */
    private boolean unisBasicFieldDataType(Field field) {
        return field != null
                && !field.toString().contains("final")
                && field.getType().getPackage() != null
                && field.getType().getEnumConstants() == null
                && !field.getType().toString().equals(Byte.class.toString())
                && !field.getType().toString().equals(Short.class.toString())
                && !field.getType().toString().equals(Integer.class.toString())
                && !field.getType().toString().equals(Long.class.toString())
                && !field.getType().toString().equals(Float.class.toString())
                && !field.getType().toString().equals(Double.class.toString())
                && !field.getType().toString().equals(Character.class.toString())
                && !field.getType().toString().equals(Boolean.class.toString())
                && !field.getType().toString().equals(Map.class.toString())
                && !field.getType().toString().equals(BigInteger.class.toString())
                && !field.getType().toString().equals(BigDecimal.class.toString())
                && !field.getType().toString().equals(Date.class.toString())
                && !field.getType().toString().equals(LocalDate.class.toString())
                && !field.getType().toString().equals(LocalTime.class.toString())
                && !field.getType().toString().equals(LocalDateTime.class.toString());
    }

    /**
     * 获取属性值
     *
     * @param obj   obj
     * @param field 字段
     * @return 获取属性值
     * @author pengsenhao
     */
    private Object getValue(Object obj, Field field) {
        field.setAccessible(true);
        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 设置属性值
     *
     * @param obj    obj
     * @param field  字段
     * @param result 值
     * @author pengsenhao
     */
    private void setValue(Object obj, Field field, Object result) {
        field.setAccessible(true);
        try {
            field.set(obj, result);
        } catch (IllegalAccessException e) {
            log.error(e.getLocalizedMessage());
        }
    }

}
