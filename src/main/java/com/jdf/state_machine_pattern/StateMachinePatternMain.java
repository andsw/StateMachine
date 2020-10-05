package com.jdf.state_machine_pattern;

/**
 * 状态（机）模式示例：订单的提交和接收
 */
public class StateMachinePatternMain {

  public static void main(String[] args) {
    Context context = new Context();
    // 先提交后接收，所以会warning
    context.receive();
    context.pay();
    context.pay();
    context.receive();
    context.receive();
    context.pay();
  }

}

