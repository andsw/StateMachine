package com.jdf.statemachine_demo.controller;

import com.jdf.statemachine_demo.bean.Form;
import com.jdf.statemachine_demo.bean.Order;
import com.jdf.statemachine_demo.config.form.FormStateMachineBuilder;
import com.jdf.statemachine_demo.config.order.multiple.OrderStateMachineBuilder;
import com.jdf.statemachine_demo.event.FormEvents;
import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.state.FormStates;
import com.jdf.statemachine_demo.state.OrderStates;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatemachineController {

  private final BeanFactory beanFactory;

  private final OrderStateMachineBuilder orderStateMachineBuilder;

  private final FormStateMachineBuilder formStateMachineBuilder;

  private final StateMachinePersister<OrderStates, OrderEvents, String> orderStateMachinePersist;

  private final StateMachinePersister<FormStates, FormEvents, String> formStateMachinePersist;

  @Autowired
  public StatemachineController(
      BeanFactory beanFactory, OrderStateMachineBuilder orderStateMachineBuilder,
      FormStateMachineBuilder formStateMachineBuilder,
      StateMachinePersister<OrderStates, OrderEvents, String> orderStateMachinePersist,
      StateMachinePersister<FormStates, FormEvents, String> formStateMachinePersist) {
    this.beanFactory = beanFactory;
    this.orderStateMachineBuilder = orderStateMachineBuilder;
    this.formStateMachineBuilder = formStateMachineBuilder;
    this.orderStateMachinePersist = orderStateMachinePersist;
    this.formStateMachinePersist = formStateMachinePersist;
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
    OrderStateMachineBuilder builder = new OrderStateMachineBuilder();
    StateMachine<OrderStates, OrderEvents> orderStateMachine = builder.build(beanFactory);
    orderStateMachine.start();
    orderStateMachine.sendEvent(OrderEvents.PAY);

    orderStateMachinePersist.persist(orderStateMachine, order.getId().toString());

    return "order状态机存储成功！";
  }

  @GetMapping(value = "/test/order/{id}")
  public String testRestoreOrderStateMachineFromMemory(@PathVariable Integer id) throws Exception {
    StateMachine<OrderStates, OrderEvents> orderStateMachine = orderStateMachineBuilder.build(beanFactory);
    orderStateMachinePersist.restore(orderStateMachine, id.toString());
    String initStateFromMemory = orderStateMachine.getState().getId().toString();
    orderStateMachine.sendEvent(OrderEvents.RECEIVE);
    return "从内存中获取存储的StateMachine成功，当前状态：" + initStateFromMemory + ", receive后为："
        + orderStateMachine.getState().getId();
  }

  @PostMapping(value = "/test/form")
  public String testPersistFormStateMachineIntoMemory(@RequestBody Form form) throws Exception {
    FormStateMachineBuilder builder = new FormStateMachineBuilder();
    StateMachine<FormStates, FormEvents> formStateMachine = builder.build(beanFactory);
    formStateMachine.start();
    formStateMachine.sendEvent(FormEvents.WRITE);
    formStateMachine.sendEvent(FormEvents.CONFIRM);

    formStateMachinePersist.persist(formStateMachine, form.toString());

    return "form状态机存储成功！";
  }

}
