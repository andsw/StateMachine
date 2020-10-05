package com.jdf.statemachine_demo.config.persist;

import com.jdf.statemachine_demo.event.FormEvents;
import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.persist.memory.MemoryStateMachinePersist;
import com.jdf.statemachine_demo.state.FormStates;
import com.jdf.statemachine_demo.state.OrderStates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@Configuration
public class StateMachinePersistConfig {

  /**
   * 注意区分 persist 和 persister
   * @return
   */
  @Bean
  public StateMachinePersister<OrderStates, OrderEvents, String> orderMemoryStateMachinePersist() {
    return new DefaultStateMachinePersister<>(new MemoryStateMachinePersist<>());
  }

  @Bean
  public StateMachinePersister<FormStates, FormEvents, String> FormMemoryStateMachinePersist() {
    return new DefaultStateMachinePersister<>(new MemoryStateMachinePersist<>());
  }

}
