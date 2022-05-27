package com.pueeo.common.support.redis.lock;

import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bouncycastle.util.Arrays;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LockAspect {

    @Resource
    private RedisLock redisLock;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(com.pueeo.common.support.redis.lock.Lockable)")
    private void lockPoint(){

    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Lockable lockable = method.getAnnotation(Lockable.class);
        RedisLockKey prefix = lockable.prefix();
        String resource = lockable.resource();
        Object[] args = pjp.getArgs();
        //解析resource的SpEL表达式
        Object[] resouses = parse(resource, method, args);
        String lockKey = prefix.getKey(resouses);
        if(!redisLock.tryLock(lockKey, lockable.expireTime(), lockable.waitTime())) {
            log.error("failed to get lock [{}]", lockKey);
            throw new BusinessException(ResultEnum.FREQUENT_REQUEST);
        }
        try {
            return pjp.proceed();
        } finally {
            redisLock.unlock(lockKey);
        }
    }

    /**
     * 解析SpEL表达式
     *
     * @param resource 表达式
     * @param method 方法
     * @param args 方法参数
     */
    private Object[] parse(String resource, Method method, Object[] args) {
        String[] params = discoverer.getParameterNames(method);
        if (Arrays.isNullOrEmpty(params)){
            return new String[]{};
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i ++) {
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(resource).getValue(context, Object[].class);
    }

    public static void main(String[] args) {
        Object[] values = new Object[]{"92039281", "abcd", new String[]{"abc", "123"}};
        String[] params = new String[]{"uid","tid", "otherparam"};
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i ++) {
            context.setVariable(params[i], values[i]);
        }
        ExpressionParser parser = new SpelExpressionParser();
        Object[] one = parser.parseExpression("#uid").getValue(context, Object[].class);
        Object[] more = parser.parseExpression("new Object[]{#uid,#tid}").getValue(context, Object[].class);
        String key = RedisLockKey.TEST_ONE_PARAM_LOCK.getKey(one);
        String key1 = RedisLockKey.TEST_MORE_PARAM_LOCK.getKey(more);
        System.out.println(key);
        System.out.println(key1);
    }
}
