package com.jdf.statemachine_demo.controller;

import com.jdf.statemachine_demo.bean.Form;
import com.jdf.statemachine_demo.bean.Order;
import com.jdf.statemachine_demo.config.form.ComplexFormStateMachineBuilder;
import com.jdf.statemachine_demo.config.form.FormStateMachineBuilder;
import com.jdf.statemachine_demo.config.order.multiple.OrderStateMachineBuilder;
import com.jdf.statemachine_demo.event.ComplexFormEvents;
import com.jdf.statemachine_demo.event.FormEvents;
import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.state.ComplexFormStates;
import com.jdf.statemachine_demo.state.FormStates;
import com.jdf.statemachine_demo.state.OrderStates;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatemachineController {

  Logger logger = LoggerFactory.getLogger(getClass());

  private final BeanFactory beanFactory;

  private final OrderStateMachineBuilder orderStateMachineBuilder;

  private final FormStateMachineBuilder formStateMachineBuilder;

  private final ComplexFormStateMachineBuilder complexFormStateMachineBuilder;

  @Resource
  private StateMachinePersister<OrderStates, OrderEvents, String> orderMemoryStateMachinePersister;

  @Resource
  private StateMachinePersister<FormStates, FormEvents, String> formMemoryStateMachinePersister;

  @Resource
  private StateMachinePersister<FormStates, FormEvents, String> formRedisStateMachinePersister;

  @Resource
  private StateMachinePersister<FormStates, FormEvents, FormStates> formPseudoStateMachinePersister;

  @Autowired
  public StatemachineController(
      BeanFactory beanFactory, OrderStateMachineBuilder orderStateMachineBuilder,
      FormStateMachineBuilder formStateMachineBuilder,
      ComplexFormStateMachineBuilder complexFormStateMachineBuilder) {
    this.beanFactory = beanFactory;
    this.orderStateMachineBuilder = orderStateMachineBuilder;
    this.formStateMachineBuilder = formStateMachineBuilder;
    this.complexFormStateMachineBuilder = complexFormStateMachineBuilder;
  }

  @GetMapping(value = "/test/order")
  public String testMultipleOrderStateMachine() throws Exception {
    final StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineBuilder
        .build(beanFactory);
    stateMachine.start();
    stateMachine.sendEvent(OrderEvents.PAY);
    stateMachine.sendEvent(OrderEvents.RECEIVE);
    return "最终状态：" + stateMachine.getState().getId().toString();
  }

  @GetMapping(value = "/test/form")
  public String testFormStateMachine(@RequestParam String id) throws Exception {
    Form form = Form.builder().id(id).name("TT").age(18).gender("female").build();
    Message<FormEvents> message = MessageBuilder.withPayload(FormEvents.WRITE)
        .setHeader("form", form).build();

    StateMachine<FormStates, FormEvents> stateMachine = formStateMachineBuilder.build(beanFactory);
    stateMachine.start();
    stateMachine.sendEvent(message);
    stateMachine.sendEvent(FormEvents.CONFIRM);
    stateMachine.sendEvent(FormEvents.SUBMIT);
    return "最终状态：" + stateMachine.getState().getId();
  }

  @PostMapping(value = "/test/order")
  public String testPersistOrderStateMachineIntoMemory(@RequestBody Order order) throws Exception {
    StateMachine<OrderStates, OrderEvents> orderStateMachine = orderStateMachineBuilder.build(beanFactory);
    orderStateMachine.start();
    orderStateMachine.sendEvent(OrderEvents.PAY);

    orderMemoryStateMachinePersister.persist(orderStateMachine, order.getId().toString());

    return "order状态机存储成功！";
  }

  @GetMapping(value = "/test/order/{id}")
  public String testRestoreOrderStateMachineFromMemory(@PathVariable Integer id) throws Exception {
    StateMachine<OrderStates, OrderEvents> orderStateMachine = orderStateMachineBuilder
        .build(beanFactory);
    orderMemoryStateMachinePersister.restore(orderStateMachine, id.toString());
    String initStateFromMemory = orderStateMachine.getState().getId().toString();
    orderStateMachine.sendEvent(OrderEvents.RECEIVE);
    return "从内存中获取存储的StateMachine成功，当前状态：" + initStateFromMemory + ", receive后为："
        + orderStateMachine.getState().getId();
  }

//  @PostMapping(value = "/test/form")
//  public String testPersistFormStateMachineIntoMemory(@RequestBody Form form) throws Exception {
//    FormStateMachineBuilder builder = new FormStateMachineBuilder();
//    StateMachine<FormStates, FormEvents> formStateMachine = builder.build(beanFactory);
//    formStateMachine.start();
//    formStateMachine.sendEvent(FormEvents.WRITE);
//    formStateMachine.sendEvent(FormEvents.CONFIRM);
//
//    formMemoryStateMachinePersister.persist(formStateMachine, form.toString());
//
//    return "form状态机存储成功！";
//  }

  @PostMapping(value = "/test/form")
  public String testPersistFormStateMachineIntoRedis(@RequestBody Form form) throws Exception {
    StateMachine<FormStates, FormEvents> formStateMachine = formStateMachineBuilder.build(beanFactory);
    formStateMachine.start();
    formStateMachine.sendEvent(FormEvents.WRITE);
    formStateMachine.sendEvent(FormEvents.CONFIRM);

    formRedisStateMachinePersister.persist(formStateMachine, form.getId());

    return "form状态机存储成功！";
  }

  @GetMapping(value = "/test/form/{id}")
  public String testRestoreFormStateMachineFromMemory(@PathVariable Integer id) throws Exception {
    StateMachine<FormStates, FormEvents> formStateMachine = formStateMachineBuilder
        .build(beanFactory);
    formRedisStateMachinePersister.restore(formStateMachine, id.toString());

    FormStates initStateFromRedis = formStateMachine.getState().getId();
    formStateMachine.sendEvent(FormEvents.SUBMIT);
    return "从内存中获取存储的StateMachine成功，当前状态：" + initStateFromRedis + ", SUBMIT后为："
        + formStateMachine.getState().getId();
  }

  /**
   * 模拟通过从表中读取出的状态，构造状态机
   *
   * @param state 需要从伪持久化类中去除的状态机的状态
   * @return
   */
  @GetMapping("/test/pseudo/form/{state}")
  public String testRestoreFormStateMachineFromPseudoPersis(@PathVariable String state)
      throws Exception {
    StateMachine<FormStates, FormEvents> formStateMachine = formStateMachineBuilder.build(beanFactory);

    FormStates formState = FormStates.BLANK;
    try {
      // 枚举查找区分大小写
      formState = FormStates.valueOf(state.toUpperCase());
    } catch (IllegalArgumentException e) {
      logger.warn(state + "状态不存在，构造默认状态机");
    }
    formPseudoStateMachinePersister.restore(formStateMachine, formState);
    return "从内存中获取存储的StateMachine成功，当前状态：" + formStateMachine.getState().getId();
  }

  /**
   * blank -> full -> confirmed -> success
   * blank -> full -> error -> confirmed -> success
   * blank -> full -> error -> failed
   * 这里的情况包括：
   *  blank -> full -> confirmed -> success
   *  blank -> full -> error返回错误信息给前端
   * @param form
   * @return
   */
  @PostMapping(value = "/test/complex/form")
  public String testComplexFormStateMachine(@RequestBody Form form) throws Exception {
    StateMachine<ComplexFormStates, ComplexFormEvents> stateMachine = complexFormStateMachineBuilder
        .build(beanFactory);
    stateMachine.start();
    stateMachine.sendEvent(ComplexFormEvents.WRITE);

    Message<ComplexFormEvents> message = MessageBuilder.withPayload(ComplexFormEvents.CHECK)
        .setHeader("form", form).build();
    stateMachine.sendEvent(message);

    if (stateMachine.getState().getId() == ComplexFormStates.ERROR_FORM) {
      return "表单内容验证未通过, 请修改后再次提交到下一个put方法的接口";
    }

    stateMachine.sendEvent(ComplexFormEvents.SUBMIT);
    return "表单提交状态：" + stateMachine.getState().getId().toString();
  }


  @Resource
  private StateMachinePersister<ComplexFormStates, ComplexFormEvents, ComplexFormStates> complexFormPseudoStateMachinePersister;

  /**
   * 和上一个接口有关，即返回error修改表单内容后再次提交验证
   * 包括流程：
   *  error -> failed
   *  error -> confirmed -> success
   * @param form
   * @return
   */
  @PutMapping(value = "/test/complex/form")
  public String testComplexFormStateMachineAfterChangeContext(@RequestBody Form form)
      throws Exception {
    StateMachine<ComplexFormStates, ComplexFormEvents> stateMachine = complexFormStateMachineBuilder
        .build(beanFactory);

    // 使用前面的为持久化类将状态机修改成 error_form 状态
    complexFormPseudoStateMachinePersister.restore(stateMachine, ComplexFormStates.ERROR_FORM);

    // 然后再 deal 看看内容修复是否成功
    Message<ComplexFormEvents> message = MessageBuilder.withPayload(ComplexFormEvents.DEAL)
        .setHeader("form", form).build();
    stateMachine.sendEvent(message);

    if (stateMachine.getState().getId() == ComplexFormStates.FAILED_FORM) {
      return "表单验证失败, 流程结束！";
    }

    stateMachine.sendEvent(ComplexFormEvents.SUBMIT);
    return "表单提交状态：" + stateMachine.getState().getId().toString();
  }

}
