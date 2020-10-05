package com.jdf.statemachine_demo.config.form;

import com.jdf.statemachine_demo.event.FormEvents;
import com.jdf.statemachine_demo.state.FormStates;
import java.util.EnumSet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;

@Component
public class FormStateMachineBuilder {

  public final static String MACHINE_ID = "formStateMachine";

  public StateMachine<FormStates, FormEvents> build(BeanFactory beanFactory) throws Exception {
    final Builder<FormStates, FormEvents> builder = StateMachineBuilder.builder();
    builder.configureConfiguration().withConfiguration().machineId(MACHINE_ID)
        .beanFactory(beanFactory);
    builder.configureStates().withStates().initial(FormStates.BLANK).end(FormStates.SUCCESS)
        .states(EnumSet.allOf(FormStates.class));
    builder.configureTransitions().withExternal().source(FormStates.BLANK).target(FormStates.FULL)
        .event(FormEvents.WRITE).and().withExternal().source(FormStates.FULL)
        .target(FormStates.CONFIRMED).event(FormEvents.CONFIRM).and().withExternal()
        .source(FormStates.CONFIRMED).target(FormStates.SUCCESS).event(FormEvents.SUBMIT);
    return builder.build();
  }

}
