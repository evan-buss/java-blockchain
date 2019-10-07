package com.evanbuss.blockchain;

public class Main {
  public static void main(String[] args) {

    BlockChain blockChain = new BlockChain();

    for (int i = 0; i < 7; i++) {
      System.out.println(blockChain.createBlock());
      System.out.println();
    }

    System.out.println("Blockchain validation:");
    System.out.println(blockChain.validate());

    blockChain.shutdown();
  }
}
