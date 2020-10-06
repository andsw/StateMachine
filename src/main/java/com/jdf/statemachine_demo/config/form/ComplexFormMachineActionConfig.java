package com.jdf.statemachine_demo.config.form;

import com.jdf.statemachine_demo.event.ComplexFormEvents;
import com.jdf.statemachine_demo.event.FormEvents;
import com.jdf.statemachine_demo.state.ComplexFormStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@WithStateMachine(name = ComplexFormStateMachineBuilder.MACHINE_ID)
public class ComplexFormMachineActionConfig {

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

  @OnTransition(source = "CONFIRMED", target = "SUCCESS")
  public void submit(Message<FormEvents> message) {
    logger.info("---[confirmed -> success]提交表单成功---");
    System.out.println(message);
  }

  /**
   * 再choice情况下变换状态是不会调用上述的方法的
   * 这里就需要显式使用.action(new Action())的
   * 方式方可调用方法，下同
   * @return
   */
  @Bean(name = "check2Error")
  public Action<ComplexFormStates, ComplexFormEvents> check2Error() {
    return context -> logger.info("---[full -> check -> error]表单填写内容异常---");
  }

  @Bean(name = "check2Confirmed")
  public Action<ComplexFormStates, ComplexFormEvents> check2Confirmed() {
    return context -> logger.info("---[full -> check -> confirmed]表单填写内容合规---");
  }

  @Bean(name = "deal2Confirmed")
  public Action<ComplexFormStates, ComplexFormEvents> deal2Confirmed() {
    return context -> logger.info("---[error -> deal -> confirmed]表单内容修改正确---");
  }

  @Bean(name = "deal2Failed")
  public Action<ComplexFormStates, ComplexFormEvents> deal2Failed() {
    return context -> logger.info("---[error -> deal -> failed]表单内功未修改正确---");
  }
}
