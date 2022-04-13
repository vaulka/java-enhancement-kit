package com.pongsky.kit.storage.web.aspect.around;

import com.pongsky.kit.storage.annotation.StorageResourceMark;
import com.pongsky.kit.storage.config.StorageProperties;
import com.pongsky.kit.type.parser.enums.ClassType;
import com.pongsky.kit.type.parser.enums.FieldType;
import com.pongsky.kit.type.parser.utils.FieldParserUtils;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return this.storageResource(method.getAnnotation(StorageResourceMark.class), new ArrayList<>(), result);
    }

    /**
     * 云存储资源 uri 拼接
     *
     * @param mark          数据脱敏注解
     * @param originResults 原始 result 列表（防止堆栈溢出）
     * @param originResult  原始 result
     * @return 脱敏后的数据
     * @author pengsenhao
     */
    @SuppressWarnings({"unchecked"})
    private Object storageResource(StorageResourceMark mark, List<Object> originResults, Object originResult) {
        ClassType classType = ClassType.getType(originResult);
        if (classType == ClassType.OBJECT
                && originResults.contains(originResult)) {
            // 如果存在，则退出递归，防止循环自己导致堆栈溢出
            return originResult;
        }
        switch (classType) {
            case STRING: {
                return mark != null
                        ? storageProperties.getBaseUri() + originResult
                        : originResult;
            }
            case ARRAY: {
                Object[] array = (Object[]) originResult;
                Object[] results = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    results[i] = this.storageResource(null, originResults, array[i]);
                }
                return results;
            }
            case LIST: {
                List<Object> list = (List<Object>) originResult;
                List<Object> results = new ArrayList<>(list.size());
                for (Object result : list) {
                    results.add(this.storageResource(null, originResults, result));
                }
                return results;
            }
            case SET: {
                Set<Object> set = (Set<Object>) originResult;
                Set<Object> results = new HashSet<>(set.size());
                for (Object result : set) {
                    results.add(this.storageResource(null, originResults, result));
                }
                return results;
            }
            case OBJECT:
                break;
            default:
                return originResult;
        }
        List<Field> fields = FieldParserUtils.getSuperFields(originResult.getClass());
        for (Field field : fields) {
            FieldType fieldType = FieldType.getType(field);
            switch (fieldType) {
                case STRING: {
                    mark = field.getAnnotation(StorageResourceMark.class);
                    if (mark == null) {
                        break;
                    }
                    Object result = ReflectUtils.getValue(originResult, field);
                    if (result != null) {
                        ReflectUtils.setValue(originResult, field, storageProperties.getBaseUri() + result);
                    }
                    break;
                }
                case ARRAY:
                case LIST:
                case SET:
                case OBJECT: {
                    Object result = ReflectUtils.getValue(originResult, field);
                    if (result != null) {
                        this.storageResource(null, originResults, result);
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return originResult;
    }

}
