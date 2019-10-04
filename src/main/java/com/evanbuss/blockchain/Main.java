package com.evanbuss.blockchain;

public class Main {
  public static void main(String[] args) {
    BlockChain blockChain = new BlockChain();

    for (int i = 0; i < 10; i++) {
      blockChain.createBlock();
    }

    for (Block b : blockChain) {
      System.out.println(b);
      System.out.println();
    }

    System.out.println("Blockchain validation:");
    System.out.println(blockChain.validate());
  }
}
