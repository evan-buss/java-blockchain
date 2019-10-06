package com.evanbuss.blockchain;

public class Main {
  public static void main(String[] args) {

    BlockChain blockChain = new BlockChain();

    for (int i = 0; i < 5; i++) {
      long start = System.currentTimeMillis();
      System.out.println(blockChain.createBlock());
      System.out.println(
          "Block was generating for " + (System.currentTimeMillis() - start) / 1000F + " Seconds");
      System.out.println("N: " + blockChain.getPow() + "\n");
    }

    System.out.println("Blockchain validation:");
    System.out.println(blockChain.validate());
  }
}
