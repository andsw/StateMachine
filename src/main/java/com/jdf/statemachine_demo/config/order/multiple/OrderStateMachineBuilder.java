package com.jdf.statemachine_demo.config.order.multiple;

import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.state.OrderStates;
import java.util.EnumSet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachineBuilder {

  public static final String MACHINE_ID = "orderMultipleMachine";

  public StateMachine<OrderStates, OrderEvents> build(BeanFactory beanFactory) throws Exception {
    final Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();
    builder.configureConfiguration().withConfiguration().machineId(MACHINE_ID)
        .beanFactory(beanFactory);
    builder.configureStates().withStates()
        .initial(OrderStates.UNPAID).end(OrderStates.DONE)
        .states(EnumSet.allOf(OrderStates.class));
    builder.configureTransitions().withExternal().source(OrderStates.UNPAID)
        .target(OrderStates.WAITING_FOR_RECEIVE).event(OrderEvents.PAY).and().withExternal()
        .source(OrderStates.WAITING_FOR_RECEIVE).target(OrderStates.DONE)
        .event(OrderEvents.RECEIVE);
    return builder.build();
  }

}
