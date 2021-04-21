package org.designer.di.configuration;


import org.designer.di.handler.impl.HandlerContext;
import org.designer.di.strategy.StrategyContext;
import org.springframework.context.annotation.Import;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/15 18:28
 */
@Import(value = {HandlerContext.class, StrategyContext.class})
public class AutoFactoryFactoryDiConfiguration extends AbstractAutoFactoryDi {

}
