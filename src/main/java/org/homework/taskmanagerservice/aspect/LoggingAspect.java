package org.homework.taskmanagerservice.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.homework.taskmanagerservice.dto.response.TaskResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j(topic = "TaskManagerService")
public class LoggingAspect {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void serviceLayer() {
    }

    @Before(value = "@within(org.springframework.web.bind.annotation.RestController)")
    public void loggingControllers(JoinPoint joinPoint) {
        String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        log.info("Controller: {} Called method {} with a path {}", controllerName, methodName, path);
    }

    @Before("serviceLayer()")
    public void loggingServices(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String params = Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(","));
        if (params.isEmpty()) {
            params = "without params";
        }
        log.info("Service: {} Called method {} with params - {}", className, methodName, params);
    }

    @AfterReturning(value = "serviceLayer()", returning = "task")
    public void loggingServicesReturn(JoinPoint joinPoint, TaskResponse task) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("{} from {} returns: {}", methodName, className, task);
    }

    @AfterReturning(value = "serviceLayer()", returning = "tasks")
    public void loggingServicesReturnList(JoinPoint joinPoint, List<TaskResponse> tasks) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        if (tasks.isEmpty()) {
            log.info("{} from {} returns null", methodName, className);
        } else {
            log.info("{} from {} returns list of objects. Examples:", methodName, className);
            tasks.stream().limit(10).forEach(task -> log.info(task.toString()));
        }
    }

    @Around("serviceLayer()")
    public Object loggingServicesAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("AROUND before - invoked method {} in class {}", methodName, className);

        try {
            Object result = joinPoint.proceed();
            log.info("AROUND after returning - invoked method {} in class {}", methodName, className);
            return result;
        } catch (Throwable ex) {
            log.warn("AROUND after throwing - invoked method {} in class {}", methodName, className);
            throw ex;
        }
    }

    @After(value = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler) " +
            "&& execution(public * org.homework.taskmanagerservice.controller.advice.*.*(..))")
    public void loggingControllerAdvice(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        StringBuilder exception = new StringBuilder();
        StringBuilder message = new StringBuilder();
        Optional<Throwable> exceptionsOptional = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof Throwable)
                .map(arg -> (Throwable) arg)
                .findFirst();
        if (exceptionsOptional.isPresent()) {
            exception.append(exceptionsOptional.get().getClass().getName());
            message.append(exceptionsOptional.get().getMessage());
        }
        log.warn("Exception intercepted {}, message: {}, controller: {}, method: {}",
                exception, message, className, methodName);
    }

    @AfterThrowing(value = "serviceLayer()", throwing = "ex")
    public void loggingServicesThrowing(JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        log.warn("After throwing advice: Exception intercepted in {}, with throwing - {}", className, ex.getMessage());
    }
}
