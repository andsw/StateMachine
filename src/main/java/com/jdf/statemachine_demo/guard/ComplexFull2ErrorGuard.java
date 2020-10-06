package com.jdf.statemachine_demo.guard;

import com.jdf.statemachine_demo.bean.Form;
import com.jdf.statemachine_demo.event.ComplexFormEvents;
import com.jdf.statemachine_demo.state.ComplexFormStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class ComplexFull2ErrorGuard implements Guard<ComplexFormStates, ComplexFormEvents> {

  @Override
  public boolean evaluate(StateContext<ComplexFormStates, ComplexFormEvents> context) {
    Form form = (Form) context.getMessageHeader("form");
    return form.getName() == null;
  }
}
