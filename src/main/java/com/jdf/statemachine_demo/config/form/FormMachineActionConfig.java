package com.jdf.statemachine_demo.config.form;

import com.jdf.statemachine_demo.event.FormEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@WithStateMachine(name = FormStateMachineBuilder.MACHINE_ID)
public class FormMachineActionConfig {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @OnTransition(target = "BLANK")
  public void create() {
    logger.info("---[ -> blank]创建空白表单---");
  }

  @OnTransition(source = "BLANK", target = "FULL")
  public void write(Message<FormEvents> message) {
    logger.info("---[blank -> full]填写空白表单---");
    System.out.println(message);
  }

  @OnTransition(source = "FULL", target = "CONFIRMED")
  public void confirm(Message<FormEvents> message) {
    logger.info("---[full -> confirmed]验证表单---");
    System.out.println(message);
  }

  @OnTransition(source = "CONFIRMED", target = "SUCCESS")
  public void submit(Message<FormEvents> message) {
    logger.info("---[confirmed -> success]提交表单---");
    System.out.println(message);
  }
}
