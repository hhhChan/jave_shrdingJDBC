package com.can.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.env.PropertyResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Collection;
import java.util.stream.Collectors;

public class CanResolvingCacheResolver extends SimpleCacheResolver {
    private final PropertyResolver propertyResolver;

    protected CanResolvingCacheResolver(CacheManager cacheManager, PropertyResolver propertyResolver) {
        super(cacheManager);
        this.propertyResolver = propertyResolver;
    }

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        MethodBasedEvaluationContext methodBasedEvaluationContext = new MethodBasedEvaluationContext(context.getTarget(), context.getMethod(), context.getArgs(), parameterNameDiscoverer);
        Collection<String> unresolvedCacheNames = super.getCacheNames(context);
        return unresolvedCacheNames.stream()
                .map(unresolvedCacheName -> {
                    ExpressionParser parser = new SpelExpressionParser();
                    Expression expression = parser.parseExpression(unresolvedCacheName);
                    String chacheNames = expression.getValue(methodBasedEvaluationContext).toString();
                    return chacheNames;
                }).collect(Collectors.toList());
    }
}
