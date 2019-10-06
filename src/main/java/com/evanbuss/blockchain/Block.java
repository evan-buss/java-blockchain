package com.evanbuss.blockchain;

import com.evanbuss.blockchain.utils.StringUtil;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Block {
  private static long blockCount = 1;

  private long id;
  private long timestamp;
  private String prevHash;
  private String hash;
  private int pow;
  private int magicNumber;

  Block(String prevHash, int pow) {
    this.pow = pow;
    this.id = blockCount++;
    this.prevHash = prevHash;
    this.timestamp = new Date().getTime();
    generateHash();
  }

  private void generateHash() {
    AtomicBoolean found = new AtomicBoolean(false);
    String target = Strings.repeat("0", pow);

    Callable<String> callable =
        () -> {
          while (!found.get()) {
            int magicNumber = Math.abs(ThreadLocalRandom.current().nextInt());
            String data = magicNumber + id + prevHash + timestamp;
            String newHash = StringUtil.applySha256(data);
            if (newHash.substring(0, pow).equals(target)) {
              found.set(true);
              return newHash;
            }
          }
          return null;
        };

    ArrayList<Callable<String>> calls = new ArrayList<>();
    // Run our hashing callable in multiple threads and save results in future ArrayList
    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
      calls.add(callable);
    }

    try {
      // Spin up all the hashing threads
      List<Future<String>> futures = HashPool.getInstance().invokeAll(calls);

      // Loop through all the futures and
      for (int i = 0; i < futures.size(); i++) {
        Future<String> future = futures.get(i);
        if (future.get() != null) {
          System.out.println("Created by miner #" + (i + 1));
          hash = future.get();
          break;
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  String getHash() {
    return hash;
  }

  String getPrevHash() {
    return prevHash;
  }

  @Override
  public String toString() {
    return "Block:\n"
        + "ID: "
        + id
        + "\nTimestamp: "
        + timestamp
        + "\nMagic Number: "
        + magicNumber
        + "\nHash of the Previous Block:\n"
        + "\t"
        + prevHash
        + "\nHash of the Block:\n"
        + "\t"
        + hash;
  }
}
