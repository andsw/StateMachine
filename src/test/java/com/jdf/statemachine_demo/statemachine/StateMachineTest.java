package com.jdf.statemachine_demo.statemachine;

import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.state.OrderStates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StateMachineTest {

  @Autowired
  private StateMachine<OrderStates, OrderEvents> orderSingleMachine;

  @Test
  public void testStateMachine() {
    // 创建流程
    orderSingleMachine.start();
    // 触发PAY事件
    orderSingleMachine.sendEvent(OrderEvents.PAY);
    // 触发RECEIVE事件
    orderSingleMachine.sendEvent(OrderEvents.RECEIVE);
    // 获取最终状态
    System.out.println("最终状态：" + orderSingleMachine.getState().getId());

    orderSingleMachine.stop();
  }

}
