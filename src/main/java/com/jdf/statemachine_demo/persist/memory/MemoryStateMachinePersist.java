package com.jdf.statemachine_demo.persist.memory;

import java.util.HashMap;
import java.util.Map;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

public class MemoryStateMachinePersist<S, E> implements StateMachinePersist<S, E, String> {

  private final Map<String, StateMachineContext<S, E>> stateMachineMap = new HashMap<>();

  @Override
  public void write(StateMachineContext<S, E> context, String contextObj) {
    stateMachineMap.put(contextObj, context);
  }

  @Override
  public StateMachineContext<S, E> read(String contextObj) {
    return stateMachineMap.get(contextObj);
  }

}
