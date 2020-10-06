package com.jdf.statemachine_demo.config.order.single;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@WithStateMachine(name = "orderSingleMachine")
public class OrderSingleActionConfig {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 当前状态UNPAID
   */
  @OnTransition(target = "UNPAID")
  public void create() {
    logger.info("---[ -> unpaid]订单创建，待支付---");
  }

  /**
   * UNPAID->WAITING_FOR_RECEIVE 执行的动作
   */
  @OnTransition(source = "UNPAID", target = "WAITING_FOR_RECEIVE")
  public void pay() {
    logger.info("---[unpaid -> waitForReceive]用户完成支付，待收货---");
  }

  /**
   * WAITING_FOR_RECEIVE->DONE 执行的动作
   */
  @OnTransition(source = "WAITING_FOR_RECEIVE", target = "DONE")
  public void receive() {
    logger.info("---[waitForReceive -> DONE]用户已收货，订单完成---");
  }

}

