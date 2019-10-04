package com.evanbuss.blockchain;

import com.evanbuss.blockchain.utils.StringUtil;

import java.util.Date;

public class Block {
  private static long blockCount = 1;
  private long id;
  private long timestamp;
  private String prevHash;
  private String hash;

  Block(String prevHash) {
    this.id = blockCount++;
    this.prevHash = prevHash;
    this.timestamp = new Date().getTime();
    generateHash();
  }

  private void generateHash() {
    String data = id + prevHash + timestamp;
    this.hash = StringUtil.applySha256(data);
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
        + "\nHash of the Previous Block:\n"
        + prevHash
        + "\nHash of the Block:\n"
        + hash;
  }
}
