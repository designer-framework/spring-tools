package org.designer.di.configuration;

import lombok.extern.log4j.Log4j2;
import org.designer.di.FieldMetaInfo;
import org.designer.di.annotation.Factory;
import org.designer.di.annotation.MatchInvoke;
import org.designer.di.handler.InvokeHandler;
import org.designer.di.handler.impl.HandlerContext;
import org.designer.di.strategy.StrategyContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 15:58
 */
@Log4j2
public abstract class AbstractAutoFactoryDi implements MergedBeanDefinitionPostProcessor, InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);

    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);

    private ConfigurableListableBeanFactory beanFactory;

    private String requiredParameterName = "required";

    private boolean requiredParameterValue = true;

    private StrategyContext strategyContext;

    public AbstractAutoFactoryDi() {
        autowiredAnnotationTypes.add(Factory.class);
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
            return pvs;
        } catch (BeanCreationException var7) {
            throw var7;
        } catch (Throwable var8) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", var8);
        }
    }

    private Object getProxyField(FieldMetaInfo fieldMetaInfo) {
        Class<?> targetProxyBeanClass = fieldMetaInfo.getTargetBean().getClass();
        Field diField = (Field) fieldMetaInfo.getInjectedElement().getMember();
        if (!diField.getType().isInterface()) {
            throw new BeanCreationException(diField + "不是一个接口!JDK代理失败");
        }
        Class<?>[] interfaces = diField.getType().getInterfaces();
        List<Class<?>> classList = Arrays.stream(interfaces)
                .collect(Collectors.toList());
        classList.add(diField.getType());
        return Proxy.newProxyInstance(
                targetProxyBeanClass.getClassLoader()
                , classList.toArray(new Class<?>[0])
                , getInvokeHandler(fieldMetaInfo).getInvocationHandler(fieldMetaInfo)
        );
    }

    protected void setProxyField(FieldMetaInfo fieldMetaInfo, Object proxyField) throws IllegalAccessException {
        proxyField(fieldMetaInfo, proxyField);
        proxyMethod(fieldMetaInfo, proxyField);
    }

    protected void proxyMethod(FieldMetaInfo fieldMetaInfo, Object proxyField) throws IllegalAccessException {
        InjectionMetadata.InjectedElement injectedElement = fieldMetaInfo.getInjectedElement();
        Field field = (Field) injectedElement.getMember();
        ReflectionUtils.makeAccessible(field);
        field.set(fieldMetaInfo.getTargetBean(), proxyField);
    }

    protected void proxyField(FieldMetaInfo fieldMetaInfo, Object proxyField) throws IllegalAccessException {
        InjectionMetadata.InjectedElement injectedElement = fieldMetaInfo.getInjectedElement();
        Field field = (Field) injectedElement.getMember();
        ReflectionUtils.makeAccessible(field);
        field.set(fieldMetaInfo.getTargetBean(), proxyField);
    }

    protected InvokeHandler getInvokeHandler(FieldMetaInfo fieldMetaInfo) {
        Optional<InvokeHandler> handler = beanFactory.getBean(HandlerContext.class).getHandler(fieldMetaInfo);
        if (handler.isPresent()) {
            return handler.get();
        } else {
            throw new BeanCreationException("getHandler: " + fieldMetaInfo);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, (PropertyValues) null);
        metadata.checkConfigMembers(beanDefinition);
    }

    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, @Nullable PropertyValues pvs) {
        String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
        InjectionMetadata metadata = injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (injectionMetadataCache) {
                metadata = injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }

                    metadata = buildAutowiringMetadata(clazz);
                    injectionMetadataCache.put(cacheKey, metadata);
                }
            }
        }

        return metadata;
    }

    @Nullable
    private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
        if (ao.getAnnotations().length > 0) {
            Iterator var2 = autowiredAnnotationTypes.iterator();

            while (var2.hasNext()) {
                Class<? extends Annotation> type = (Class) var2.next();
                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
                if (attributes != null) {
                    return attributes;
                }
            }
        }

        return null;
    }

    protected boolean determineRequiredStatus(AnnotationAttributes ann) {
        return !ann.containsKey(requiredParameterName) || requiredParameterValue == ann.getBoolean(requiredParameterName);
    }

    private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        Class targetClass = clazz;
        do {
            List<InjectionMetadata.InjectedElement> currElements = new ArrayList<>();
            ReflectionUtils.doWithLocalFields(targetClass, (field) -> {
                AnnotationAttributes ann = findAutowiredAnnotation(field);
                if (ann != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (log.isWarnEnabled()) {
                            log.warn("Autowired annotation is not supported on static fields: " + field);
                        }

                        return;
                    }

                    boolean required = determineRequiredStatus(ann);
                    currElements.add(new AutowiredFieldElement(field, required));
                }

            });
            ReflectionUtils.doWithLocalMethods(targetClass, (method) -> {
                Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                if (BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
                    AnnotationAttributes ann = findAutowiredAnnotation(bridgedMethod);
                    if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                        if (Modifier.isStatic(method.getModifiers())) {
                            if (log.isWarnEnabled()) {
                                log.warn("Autowired annotation is not supported on static methods: " + method);
                            }

                            return;
                        }

                        if (method.getParameterCount() == 0 && log.isWarnEnabled()) {
                            log.warn("Autowired annotation should only be used on methods with parameters: " + method);
                        }

                        boolean required = determineRequiredStatus(ann);
                        PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                        currElements.add(new AutowiredMethodElement(method, required, pd));
                    }

                }
            });
            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(clazz, elements);
    }

    @Nullable
    private Object resolvedCachedArgument(@Nullable String beanName, @Nullable Object cachedArgument) {
        if (cachedArgument instanceof DependencyDescriptor) {
            DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
            Assert.state(beanFactory != null, "No BeanFactory available");
            return beanFactory.resolveDependency(descriptor, beanName, (Set) null, (TypeConverter) null);
        } else {
            return cachedArgument;
        }
    }

    private void registerDependentBeans(@Nullable String beanName, Set<String> autowiredBeanNames) {
        if (beanName != null) {
            Iterator var3 = autowiredBeanNames.iterator();

            while (var3.hasNext()) {
                String autowiredBeanName = (String) var3.next();
                if (beanFactory != null && beanFactory.containsBean(autowiredBeanName)) {
                    beanFactory.registerDependentBean(autowiredBeanName, beanName);
                }

                if (log.isDebugEnabled()) {
                    log.debug("Autowiring by type from bean name '" + beanName + "' to bean named '" + autowiredBeanName + "'");
                }
            }
        }

    }

    private static class ShortcutDependencyDescriptor extends DependencyDescriptor {
        private static final long serialVersionUID = 3049535669369858053L;
        private final String shortcut;
        private final Class<?> requiredType;

        public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType) {
            super(original);
            this.shortcut = shortcut;
            this.requiredType = requiredType;
        }

        @Override
        public Object resolveShortcut(BeanFactory beanFactory) {
            return beanFactory.getBean(shortcut, requiredType);
        }
    }

    class AutowiredMethodElement extends org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement {
        private final boolean required;
        private volatile boolean cached = false;
        @Nullable
        private volatile Object[] cachedMethodArguments;

        public AutowiredMethodElement(Method method, boolean required, @Nullable PropertyDescriptor pd) {
            super(method, pd);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            if (!checkPropertySkipping(pvs)) {
                Method method = (Method) member;
                Object[] arguments;
                if (cached) {
                    arguments = resolveCachedArguments(beanName);
                } else {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    arguments = new Object[paramTypes.length];
                    DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
                    Set<String> autowiredBeans = new LinkedHashSet(paramTypes.length);
                    Assert.state(beanFactory != null, "No BeanFactory available");
                    TypeConverter typeConverter = beanFactory.getTypeConverter();

                    for (int i = 0; i < arguments.length; ++i) {
                        MethodParameter methodParam = new MethodParameter(method, i);
                        DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, required);
                        currDesc.setContainingClass(bean.getClass());
                        descriptors[i] = currDesc;

                        try {
                            Object arg = beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
                            if (arg == null && !required) {
                                arguments = null;
                                break;
                            }

                            arguments[i] = arg;
                        } catch (BeansException var18) {
                            throw new UnsatisfiedDependencyException((String) null, beanName, new InjectionPoint(methodParam), var18);
                        }
                    }

                    synchronized (this) {
                        if (!cached) {
                            if (arguments != null) {
                                Object[] cachedMethodArguments = new Object[paramTypes.length];
                                System.arraycopy(descriptors, 0, cachedMethodArguments, 0, arguments.length);
                                registerDependentBeans(beanName, autowiredBeans);
                                if (autowiredBeans.size() == paramTypes.length) {
                                    Iterator<String> it = autowiredBeans.iterator();

                                    for (int ix = 0; ix < paramTypes.length; ++ix) {
                                        String autowiredBeanName = (String) it.next();
                                        if (beanFactory.containsBean(autowiredBeanName) && beanFactory.isTypeMatch(autowiredBeanName, paramTypes[ix])) {
                                            cachedMethodArguments[ix] = new ShortcutDependencyDescriptor(descriptors[ix], autowiredBeanName, paramTypes[ix]);
                                        }
                                    }
                                }

                                this.cachedMethodArguments = cachedMethodArguments;
                            } else {
                                cachedMethodArguments = null;
                            }

                            cached = true;
                        }
                    }
                }

                if (arguments != null) {
                    try {
                        ReflectionUtils.makeAccessible(method);
                        method.invoke(bean, arguments);
                    } catch (InvocationTargetException var16) {
                        throw var16.getTargetException();
                    }
                }

            }
        }

        @Nullable
        private Object[] resolveCachedArguments(@Nullable String beanName) {
            Object[] cachedMethodArguments = this.cachedMethodArguments;
            if (cachedMethodArguments == null) {
                return null;
            } else {
                Object[] arguments = new Object[cachedMethodArguments.length];

                for (int i = 0; i < arguments.length; ++i) {
                    arguments[i] = resolvedCachedArgument(beanName, cachedMethodArguments[i]);
                }

                return arguments;
            }
        }
    }

    class AutowiredFieldElement extends InjectionMetadata.InjectedElement {
        private final boolean required;
        private volatile boolean cached = false;
        @Nullable
        private volatile Object cachedFieldValue;

        public AutowiredFieldElement(Field field, boolean required) {
            super(field, (PropertyDescriptor) null);
            this.required = required;
        }

        private Object resolveDependency(Object bean, Field field, String beanName, Set<String> autowiredBeanNames) {
            Factory factory = field.getAnnotation(Factory.class);
            Class<?> type = field.getType();
            Method method = matchVerifyMethod(type, factory.matchInvoke());
            //
            FieldMetaInfo fieldMetaInfo = FieldMetaInfo.builder()
                    .injectedElement(this)
                    .factory(factory)
                    .targetBean(bean)
                    .validMethod(method)
                    .build();
            Object proxyField = getProxyField(fieldMetaInfo);
            try {
                setProxyField(fieldMetaInfo, proxyField);
                return proxyField;
            } catch (IllegalAccessException e) {
                throw new BeanInitializationException(field.getDeclaringClass().getName() + "注入字段[" + field.getName() + "] 失败！", e);
            }
        }


        private Method matchVerifyMethod(Class<?> fieldType, MatchInvoke matchInvoke) {
            List<Method> list = new ArrayList<>();
            ReflectionUtils.doWithMethods(fieldType, list::add, method -> {
                if (!matchInvoke.method().equals(method.getName())) {
                    return false;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<?>[] invokeVerifyClasses = matchInvoke.argsType();
                boolean predict = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (parameterTypes.length != invokeVerifyClasses.length) {
                        predict = false;
                        break;
                    }
                    Class<?> runtimeParameterType = parameterTypes[i];
                    Class<?> invokeVerifyClass = invokeVerifyClasses[i];
                    if (!runtimeParameterType.isAssignableFrom(invokeVerifyClass)) {
                        predict = false;
                        break;
                    }
                }
                if (!predict) {
                    log.debug("参数列表不匹配: " + Arrays.toString(parameterTypes) + " match " + Arrays.toString(invokeVerifyClasses));
                }
                return predict;
            });
            if (matchInvoke.enable()) {
                if (list.size() == 1) {
                    return list.get(0);
                }
                throw new BeanInitializationException("无法确定调用哪个方法进行参数校验!" + fieldType.getName() + " _match_ " + matchInvoke);
            }
            return null;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            Field field = (Field) member;
            Object value;
            if (cached) {
                value = resolvedCachedArgument(beanName, cachedFieldValue);
            } else {
                DependencyDescriptor desc = new DependencyDescriptor(field, required);
                desc.setContainingClass(bean.getClass());
                Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
                Assert.state(beanFactory != null, "No BeanFactory available");
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                try {
                    value = resolveDependency(bean, field, beanName, autowiredBeanNames);
                } catch (BeansException var12) {
                    throw new UnsatisfiedDependencyException((String) null, beanName, new InjectionPoint(field), var12);
                }

                synchronized (this) {
                    if (!cached) {
                        if (value == null && !required) {
                            cachedFieldValue = null;
                        } else {
                            cachedFieldValue = desc;
                            registerDependentBeans(beanName, autowiredBeanNames);
                            if (autowiredBeanNames.size() == 1) {
                                String autowiredBeanName = (String) autowiredBeanNames.iterator().next();
                                if (beanFactory.containsBean(autowiredBeanName) && beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                                    cachedFieldValue = new ShortcutDependencyDescriptor(desc, autowiredBeanName, field.getType());
                                }
                            }
                        }

                        cached = true;
                    }
                }
            }

            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                field.set(bean, value);
            }

        }
    }

}
