package com.jdf.statemachine_demo.config.order.multiple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@WithStateMachine(name = OrderStateMachineBuilder.MACHINE_ID)
public class OrderMultipleEventConfig {

  private int machineNo = 0;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 当前状态UNPAID
   */
  @OnTransition(target = "UNPAID")
  public void create() {
    logger.info("---第{}个订单创建，待支付---", machineNo);
  }

  /**
   * UNPAID->WAITING_FOR_RECEIVE 执行的动作
   */
  @OnTransition(source = "UNPAID", target = "WAITING_FOR_RECEIVE")
  public void pay() {
    logger.info("---第{}个用户完成支付，待收货---", machineNo);
  }

  /**
   * WAITING_FOR_RECEIVE->DONE 执行的动作
   */
  @OnTransition(source = "WAITING_FOR_RECEIVE", target = "DONE")
  public void receive() {
    logger.info("---第{}个用户已收货，订单完成---", machineNo++);
  }

}
