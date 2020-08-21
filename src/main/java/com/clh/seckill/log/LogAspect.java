package com.clh.seckill.log;

import com.alibaba.fastjson.JSON;
import com.clh.seckill.access.UserContext;
import com.clh.seckill.model.CustomLog;
import com.clh.seckill.model.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author: LongHua
 * @date: 2020/8/20
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.wwj.springboot.service.impl.*.*(..))")'
     */
    @Pointcut("@within(com.clh.seckill.log.AopLog) || @annotation(com.clh.seckill.log.AopLog)")
    public void operationLog() {
    }

    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long time = System.currentTimeMillis();
        try {
            res = joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            return res;
        } finally {
            try {
                //方法执行完成后增加日志
                addOperationLog(joinPoint, res, time, false);
            } catch (Exception e) {
                System.out.println("LogAspect 操作失败：" + e.getMessage());
                addOperationLog(joinPoint, e.getMessage(), time, true);
                e.printStackTrace();
            }
        }
    }

    private void addOperationLog(JoinPoint joinPoint, Object res, long time, boolean hasException) {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        log.debug(request.getRemoteAddr());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HashMap<Object, Object> argsMap = new HashMap<>(4);
        Object[] args = joinPoint.getArgs();

        if (Objects.nonNull(args)) {
            String[] parameterNames = signature.getParameterNames();
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletResponse || args[i] instanceof HttpServletRequest) {
                    continue;
                }
                argsMap.put(parameterNames[i], args[i]);
            }
        }
        CustomLog customLog = new CustomLog();
        customLog.setRunTime(time);
        customLog.setReturnValue(JSON.toJSONString(res));
        customLog.setId(UUID.randomUUID().toString());
        customLog.setArgs(JSON.toJSONString(argsMap));
        customLog.setCreateTime(new Date());
        customLog.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        customLog.setHasException(hasException);
        User user = UserContext.getUser();
        if (user != null) {
            customLog.setUserId(user.getId() + "");
            customLog.setUserName(user.getNickname());
        }
        AopLog annotation = signature.getMethod().getAnnotation(AopLog.class);
        if (annotation != null) {
            customLog.setLevel(annotation.level());
            customLog.setDescribe(getDetail(argsMap, annotation));
            customLog.setOperationType(annotation.operationType().getValue());
            customLog.setOperationUnit(annotation.operationUnit().getValue());
        }
        //TODO 这里保存日志
        System.out.println("记录日志:" + customLog.toString());
        // operationLogService.insert(customLog);
    }

    /**
     * 对当前登录用户和占位符处理
     *
     * @param argMap   方法参数名称
     * @param annotation 注解信息
     * @return 返回处理后的描述
     */
    private String getDetail(Map<Object, Object> argMap, AopLog annotation) {

        StringBuilder detail = new StringBuilder(annotation.detail());
        try {
            detail.append("'").append("#{currentUserName}").append("'=》")
                    .append(annotation.detail());
            for (Map.Entry<Object, Object> entry : argMap.entrySet()) {
                Object k = entry.getKey();
                Object v = entry.getValue();
                detail = new StringBuilder(detail.toString().replace("{{" + k + "}}", JSON.toJSONString(v)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail.toString();
    }

    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        System.out.println("进入方法前执行.....");

    }

    /**
     * 处理完请求，返回内容
     *
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret) {
        System.out.println("方法的返回值 : " + ret);
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing("operationLog()")
    public void throwss(JoinPoint jp) {
        System.out.println("方法异常时执行.....");
    }


    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    @After("operationLog()")
    public void after(JoinPoint jp) {
        System.out.println("方法最后执行.....");
    }
}
