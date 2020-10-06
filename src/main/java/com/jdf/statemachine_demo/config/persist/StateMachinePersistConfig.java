package com.jdf.statemachine_demo.config.persist;

import com.jdf.statemachine_demo.event.ComplexFormEvents;
import com.jdf.statemachine_demo.event.FormEvents;
import com.jdf.statemachine_demo.event.OrderEvents;
import com.jdf.statemachine_demo.persist.memory.MemoryStateMachinePersist;
import com.jdf.statemachine_demo.persist.pseudo.PseudoStateMachinePersist;
import com.jdf.statemachine_demo.state.ComplexFormStates;
import com.jdf.statemachine_demo.state.FormStates;
import com.jdf.statemachine_demo.state.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

@Configuration
public class StateMachinePersistConfig {

  private final RedisConnectionFactory redisConnectionFactory;

  @Autowired
  public StateMachinePersistConfig(
      RedisConnectionFactory redisConnectionFactory) {
    this.redisConnectionFactory = redisConnectionFactory;
  }

  /**
   * 注意区分 persist 和 persister
   *
   * @return
   */
  @Bean(name = "orderMemoryStateMachinePersister")
  public StateMachinePersister<OrderStates, OrderEvents, String> orderMemoryStateMachinePersister() {
    return new DefaultStateMachinePersister<>(new MemoryStateMachinePersist<>());
  }

  @Bean(name = "formMemoryStateMachinePersister")
  public StateMachinePersister<FormStates, FormEvents, String> formMemoryStateMachinePersister() {
    return new DefaultStateMachinePersister<>(new MemoryStateMachinePersist<>());
  }

  @Bean(name = "orderRedisStateMachinePersister")
  public StateMachinePersister<OrderStates, OrderEvents, String> orderRedisStateMachinePersister() {
    RedisStateMachineContextRepository<OrderStates, OrderEvents> repo = new RedisStateMachineContextRepository<>(
        redisConnectionFactory);
    return new RedisStateMachinePersister<>(new RepositoryStateMachinePersist<>(repo));
  }

  @Bean(name = "formRedisStateMachinePersister")
  public StateMachinePersister<FormStates, FormEvents, String> formRedisStateMachinePersister() {
    RedisStateMachineContextRepository<FormStates, FormEvents> repo = new RedisStateMachineContextRepository<>(
        redisConnectionFactory);
    return new RedisStateMachinePersister<>(new RepositoryStateMachinePersist<>(repo));
  }

  @Bean(name = "orderPseudoStateMachinePersister")
  public StateMachinePersister<OrderStates, OrderEvents, OrderStates> orderPseudoStateMachinePersister() {
    return new DefaultStateMachinePersister<>(new PseudoStateMachinePersist<>());
  }

  @Bean(name = "formPseudoStateMachinePersister")
  public StateMachinePersister<FormStates, FormEvents, FormStates> formPseudoStateMachinePersister() {
    return new DefaultStateMachinePersister<>(new PseudoStateMachinePersist<>());
  }

  @Bean(name = "complexFormPseudoStateMachinePersister")
  public StateMachinePersister<ComplexFormStates, ComplexFormEvents, ComplexFormStates> complexFormPseudoStateMachinePersister() {
    return new DefaultStateMachinePersister<>(new PseudoStateMachinePersist<>());
  }

}
