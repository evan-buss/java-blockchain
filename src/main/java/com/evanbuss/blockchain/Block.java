package com.evanbuss.blockchain;

import com.evanbuss.blockchain.utils.StringUtil;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Block {
  private static long blockCount = 1;

  private long id;
  private long timestamp; // Time block was created
  private String prevHash; // Hash of the previous block
  private String hash; // Hash of this block
  private int magicNumber; // Computed number for POW
  private int minerNum; // Thread number of the first miner to solve the block
  private float mineTime; // Time it took to mine this block
  private int pow; // Number of zeroes to place before a new block

  private ArrayList<String> messages = new ArrayList<>();
  private LinkedBlockingQueue<String> messageQueue;

  Block(String prevHash, int pow, LinkedBlockingQueue<String> messageQueue) {
    this.pow = pow;
    this.id = blockCount++;
    this.prevHash = prevHash;
    this.timestamp = new Date().getTime();
    this.messageQueue = messageQueue;
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
              return newHash + "#" + magicNumber;
            }
          }
          return null;
        };

    ArrayList<Callable<String>> calls = new ArrayList<>();
    // Run our hashing callable in multiple threads and save results in future ArrayList
    for (int i = 0; i < (2 * Runtime.getRuntime().availableProcessors()); i++) {
      calls.add(callable);
    }

    try {
      // Spin up all the hashing threads
      List<Future<String>> futures = HashPool.getInstance().invokeAll(calls);

      // Loop through all the futures and
      for (int i = 0; i < futures.size(); i++) {
        Future<String> future = futures.get(i);
        if (future.get() != null) {
          minerNum = i + 1;
          hash = future.get().split("#")[0];
          magicNumber = Integer.parseInt(future.get().split("#")[1]);
          break;
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    messageQueue.drainTo(messages);
  }

  String getHash() {
    return hash;
  }

  String getPrevHash() {
    return prevHash;
  }

  void setTime(float mineTime) {
    this.mineTime = mineTime;
  }

  @Override
  public String toString() {
    return "Block:\n"
        + "Created By Miner: "
        + minerNum
        + "\nID: "
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
        + hash
        + "\nBlock was generating for "
        + mineTime
        + " seconds"
        + "\nN: "
        + pow
        + "\nMessages:\n\t"
        + String.join("\n\t", messages);
  }
}
