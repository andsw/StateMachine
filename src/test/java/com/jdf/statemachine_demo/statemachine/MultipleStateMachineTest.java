package com.jdf.statemachine_demo.statemachine;

import com.jdf.statemachine_demo.config.order.multiple.OrderStateMachineBuilder;
import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.state.OrderStates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MultipleStateMachineTest {

  @Autowired
  private BeanFactory beanFactory;

  @Autowired
  private OrderStateMachineBuilder orderStateMachineBuilder;

  @Test
  public void testMultipleStateMachine() throws Exception {
    final StateMachine<OrderStates, OrderEvents> orderStateMachine = orderStateMachineBuilder
        .build(beanFactory);
    orderStateMachine.start();
    orderStateMachine.sendEvent(OrderEvents.PAY);
    orderStateMachine.sendEvent(OrderEvents.RECEIVE);
  }

}
