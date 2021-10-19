package org.designer.lock.interceptor;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @description:
 * @author: Designer
 * @date : 2021/9/19 19:13
 */
public abstract class LockedExpressionEvaluator {

    private final SpelExpressionParser parser;

    private final ParameterNameDiscoverer parameterNameDiscoverer;

    protected LockedExpressionEvaluator(SpelExpressionParser parser) {
        parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        Assert.notNull(parser, "SpelExpressionParser must not be null");
        this.parser = parser;
    }

    protected ParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    protected SpelExpressionParser getParser() {
        return parser;
    }

    protected Expression getExpression(Map<ExpressionKey, Expression> cache, AnnotatedElementKey elementKey, String expression) {
        return cache.computeIfAbsent(
                createKey(elementKey, expression)
                , key -> getParser().parseExpression(expression)
        );
    }

    private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
        return new ExpressionKey(elementKey, expression);
    }

    protected static class ExpressionKey implements Comparable<ExpressionKey> {
        private final AnnotatedElementKey element;
        private final String expression;

        protected ExpressionKey(AnnotatedElementKey element, String expression) {
            Assert.notNull(element, "AnnotatedElementKey must not be null");
            Assert.notNull(expression, "Expression must not be null");
            this.element = element;
            this.expression = expression;
        }

        @Override
        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof ExpressionKey)) {
                return false;
            } else {
                ExpressionKey otherKey = (ExpressionKey) other;
                return element.equals(otherKey.element) && ObjectUtils.nullSafeEquals(expression, otherKey.expression);
            }
        }

        @Override
        public int hashCode() {
            return element.hashCode() * 29 + expression.hashCode();
        }

        @Override
        public String toString() {
            return element + " with expression \"" + expression + "\"";
        }

        @Override
        public int compareTo(ExpressionKey other) {
            int result = element.toString().compareTo(other.element.toString());
            if (result == 0) {
                result = expression.compareTo(other.expression);
            }

            return result;
        }
    }

}
