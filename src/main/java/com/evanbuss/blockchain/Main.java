package com.evanbuss.blockchain;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {

    Scanner keyboard = new Scanner(System.in);
    System.out.println("Enter the number of zeroes for proof of work: ");

    // int pow = keyboard.nextInt();
    int pow = 8;

    BlockChain blockChain = new BlockChain(pow);

    for (int i = 0; i < 10; i++) {
      long start = System.currentTimeMillis();
      System.out.println(blockChain.createBlock());
      System.out.println(
          "Block was generating for "
              + (System.currentTimeMillis() - start) / 1000F
              + " Seconds\n");
    }

    System.out.println("Blockchain validation:");
    System.out.println(blockChain.validate());
  }
}
