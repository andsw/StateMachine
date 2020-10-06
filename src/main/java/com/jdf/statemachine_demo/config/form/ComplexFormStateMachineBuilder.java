package com.jdf.statemachine_demo.config.form;

import com.jdf.statemachine_demo.event.ComplexFormEvents;
import com.jdf.statemachine_demo.guard.ComplexDeal2FailedGuard;
import com.jdf.statemachine_demo.guard.ComplexFull2ErrorGuard;
import com.jdf.statemachine_demo.state.ComplexFormStates;
import java.util.EnumSet;
import javax.annotation.Resource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;

@Component
public class ComplexFormStateMachineBuilder {

  @Resource(name = "check2Error")
  private Action<ComplexFormStates, ComplexFormEvents> check2Error;

  @Resource(name = "check2Confirmed")
  private Action<ComplexFormStates, ComplexFormEvents> check2Confirmed;

  @Resource(name = "deal2Confirmed")
  private Action<ComplexFormStates, ComplexFormEvents> deal2Confirmed;

  @Resource(name = "check2Confirmed")
  private Action<ComplexFormStates, ComplexFormEvents> deal2Failed;

  public final static String MACHINE_ID = "complexFormStateMachine";

  public StateMachine<ComplexFormStates, ComplexFormEvents> build(BeanFactory beanFactory)
      throws Exception {
    Builder<ComplexFormStates, ComplexFormEvents> builder = StateMachineBuilder.builder();
    builder.configureConfiguration().withConfiguration().beanFactory(beanFactory)
        .machineId(MACHINE_ID);

    builder.configureStates().withStates().initial(ComplexFormStates.BLANK_FORM)
        .choice(ComplexFormStates.CHECK_CHOICE).choice(ComplexFormStates.DEAL_CHOICE).states(
        EnumSet.allOf(ComplexFormStates.class));

    // 具体流程见流程图
    builder.configureTransitions()
        // blank -> full
        .withExternal().source(ComplexFormStates.BLANK_FORM).target(ComplexFormStates.FULL_FORM)
        .event(ComplexFormEvents.WRITE)
        // full -> check_choice
        .and().withExternal().source(ComplexFormStates.FULL_FORM)
        .target(ComplexFormStates.CHECK_CHOICE).event(ComplexFormEvents.CHECK)
        // check_choice -> error/confirmed
        .and().withChoice().source(ComplexFormStates.CHECK_CHOICE)
        .first(ComplexFormStates.ERROR_FORM, new ComplexFull2ErrorGuard(), check2Error)
        .last(ComplexFormStates.CONFIRMED_FORM, check2Confirmed)
        // error -> deal_choice
        .and().withExternal().source(ComplexFormStates.ERROR_FORM)
        .target(ComplexFormStates.DEAL_CHOICE).event(ComplexFormEvents.DEAL)
        // deal_choice -> failed/confirmed
        .and().withChoice().source(ComplexFormStates.DEAL_CHOICE)
        .first(ComplexFormStates.FAILED_FORM, new ComplexDeal2FailedGuard(), deal2Failed)
        .last(ComplexFormStates.CONFIRMED_FORM, deal2Confirmed)
        // confirmed -> success
        .and().withExternal().source(ComplexFormStates.CONFIRMED_FORM)
        .target(ComplexFormStates.SUCCESS_FORM).event(ComplexFormEvents.SUBMIT);

    return builder.build();
  }

}
