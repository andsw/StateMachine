package com.jdf.statemachine_demo.config.order.single;

import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.state.OrderStates;
import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine(name="orderSingleMachine")
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

  /**
   * 设置订单初始化状态，传递订单所有状态
   * @param states
   * @throws Exception
   */
  @Override
  public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states)
      throws Exception {
    // 建造者模式
    states.withStates().initial(OrderStates.UNPAID).end(OrderStates.DONE)
        .states(EnumSet.allOf(OrderStates.class));
  }

  /**
   *
   * @param transitions
   * @throws Exception
   */
  @Override
  public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions)
      throws Exception {
    transitions.withExternal()
        .source(OrderStates.UNPAID).target(OrderStates.WAITING_FOR_RECEIVE)
        .event(OrderEvents.PAY)
        .and()
        .withExternal().source(OrderStates.WAITING_FOR_RECEIVE).target(OrderStates.DONE)
        .event(OrderEvents.RECEIVE);
  }

}
