package com.evanbuss.blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HashPool maintains a FixedThreadPool that is used to mine blocks
 */
class HashPool {

  private static ExecutorService instance =
      Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

  private HashPool() {}

  static ExecutorService getInstance() {
    return instance;
  }

  static void shutdown() {
    if (instance != null) {
      instance.shutdown();
    }
  }
}
