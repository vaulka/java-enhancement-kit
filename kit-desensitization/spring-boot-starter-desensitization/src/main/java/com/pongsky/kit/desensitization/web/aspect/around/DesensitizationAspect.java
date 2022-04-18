package com.pongsky.kit.desensitization.web.aspect.around;

import com.pongsky.kit.desensitization.annotation.DesensitizationMark;
import com.pongsky.kit.desensitization.handler.DesensitizationHandler;
import com.pongsky.kit.type.parser.enums.ClassType;
import com.pongsky.kit.type.parser.enums.FieldType;
import com.pongsky.kit.type.parser.utils.FieldParserUtils;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.openjdk.jol.vm.VM;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据脱敏
 *
 * @author pengsenhao
 */
@Slf4j
@Aspect
@Component
public class DesensitizationAspect {

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
        return this.desensitization(method.getAnnotation(DesensitizationMark.class), new ArrayList<>(), result);
    }

    /**
     * 数据脱敏
     *
     * @param mark          数据脱敏注解
     * @param originResults 原始 result 列表（防止堆栈溢出）
     * @param originResult  原始 result
     * @return 脱敏后的数据
     * @author pengsenhao
     */
    @SuppressWarnings({"unchecked"})
    private Object desensitization(DesensitizationMark mark, List<Object> originResults, Object originResult)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassType classType = ClassType.getType(originResult);
        if (originResults.contains(VM.current().addressOf(originResult))) {
            // 如果内存地址是常量池，则会导致后续的常量都进不来，但是常量基本都是数值类型，该问题可忽略不计
            // 如果内存地址值存在，则退出递归，防止循环自己导致堆栈溢出
            return originResult;
        }
        // 添加内存地址值
        originResults.add(VM.current().addressOf(originResult));
        DesensitizationHandler handler = this.getHandler(mark);
        switch (classType) {
            case STRING: {
                return mark != null
                        ? handler.exec(originResult.toString())
                        : originResult;
            }
            case ARRAY: {
                Object[] array = (Object[]) originResult;
                Object[] results = new Object[array.length];
                for (int i = 0; i < array.length; i++) {
                    results[i] = this.desensitization(null, originResults, array[i]);
                }
                return results;
            }
            case LIST: {
                List<Object> list = (List<Object>) originResult;
                List<Object> results = new ArrayList<>(list.size());
                for (Object result : list) {
                    results.add(this.desensitization(null, originResults, result));
                }
                return results;
            }
            case SET: {
                Set<Object> set = (Set<Object>) originResult;
                Set<Object> results = new HashSet<>(set.size());
                for (Object result : set) {
                    results.add(this.desensitization(null, originResults, result));
                }
                return results;
            }
            case OBJECT:
                break;
            default:
                return originResult;
        }
        List<Field> fields = FieldParserUtils.getSuperFields(originResult.getClass(), true);
        for (Field field : fields) {
            FieldType fieldType = FieldType.getType(field);
            switch (fieldType) {
                case STRING: {
                    mark = field.getAnnotation(DesensitizationMark.class);
                    if (mark == null) {
                        continue;
                    }
                    handler = this.getHandler(mark);
                    Object result = ReflectUtils.getValue(originResult, field);
                    if (result != null) {
                        ReflectUtils.setValue(originResult, field, handler.exec(result.toString()));
                    }
                    break;
                }
                case ARRAY:
                case LIST:
                case SET:
                case OBJECT: {
                    Object result = ReflectUtils.getValue(originResult, field);
                    if (result != null) {
                        this.desensitization(null, originResults, result);
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return originResult;
    }

    /**
     * 数据脱敏处理器列表
     */
    private static final Map<String, DesensitizationHandler> HANDLERS = new ConcurrentHashMap<>(16);

    /**
     * 获取数据脱敏处理器
     *
     * @param mark 数据脱敏注解
     * @return 数据脱敏处理器
     */
    private DesensitizationHandler getHandler(DesensitizationMark mark) throws
            NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        if (mark == null) {
            return null;
        }
        Class<? extends DesensitizationHandler> type = mark.handler();
        String key = type.getName();
        DesensitizationHandler desensitizationHandler = HANDLERS.get(key);
        if (desensitizationHandler != null) {
            return desensitizationHandler;
        }
        desensitizationHandler = type.getDeclaredConstructor().newInstance();
        HANDLERS.put(key, desensitizationHandler);
        return desensitizationHandler;
    }

}
