/**
 * <p>
 * 如果当前的业务方法调用耗时较长, 而真正想用锁的代码块执行业务耗时极短的情况下不建议用该注解,
 * 因为不需要用锁的那些时间片会一直占用锁, 会浪费系统资源[如 1.redis的资源 2.其它与当前业务无关却想占用该锁执行业务的资源]
 * 解决方案: 将用锁的代码块提取到单独的方法 over
 * ==
 * 结合聚合吧当前的业务及架构, 暂时可以不考虑上述资源浪费的情况,大部分情况都是简单的数据操作
 * </p>
 * <p>
 * 二次开发或者增强锁功能,可以参考
 * </p>
 *
 * @see org.springframework.cache.annotation.ProxyCachingConfiguration
 * @see org.springframework.data.redis.cache.RedisCacheConfiguration
 * @see org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator
 * <p>
 */
package org.designer.lock;