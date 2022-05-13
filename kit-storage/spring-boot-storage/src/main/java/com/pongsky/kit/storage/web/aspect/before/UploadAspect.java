package com.pongsky.kit.storage.web.aspect.before;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * 检验文件上传
 * <p>
 * 参数列表必须以 MultipartFile 开头
 *
 * @author pengsenhao
 */
@Aspect
public class UploadAspect {

    @Before("(@annotation(org.springframework.stereotype.Controller) " +
            "|| @annotation(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "&& args(org.springframework.web.multipart.MultipartFile,..)) ")
    public void exec(JoinPoint point) {
        long count = Arrays.stream(((MethodSignature) point.getSignature()).getMethod().getParameters())
                .filter(p -> p.isAnnotationPresent(RequestParam.class)
                        && p.getType().toString().equals(MultipartFile.class.toString()))
                .count();
        if (count == 0) {
            return;
        }
        for (Object arg : point.getArgs()) {
            if (arg instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) arg;
                if (file.isEmpty() || file.getSize() == 0 || StringUtils.isBlank(file.getOriginalFilename())) {
                    throw new RuntimeException("请选择文件进行上传");
                }
            }
        }
    }

}
