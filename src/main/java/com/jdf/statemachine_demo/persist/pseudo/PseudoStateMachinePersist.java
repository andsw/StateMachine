package com.jdf.statemachine_demo.persist.pseudo;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;

/**
 * 伪状态机存储器，通过将
 * @param <S>
 * @param <E>
 */
public class PseudoStateMachinePersist<S, E> implements StateMachinePersist<S, E, S> {

  @Override
  public void write(StateMachineContext<S, E> stateMachineContext, S state) {
    System.out.println("---状态机存储，未作任何事---");
  }

  @Override
  public StateMachineContext<S, E> read(S state) {
    return new DefaultStateMachineContext<>(state, null, null,
        null);
  }
}
