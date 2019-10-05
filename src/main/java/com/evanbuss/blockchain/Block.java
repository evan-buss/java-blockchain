package com.evanbuss.blockchain;

import com.evanbuss.blockchain.utils.StringUtil;
import com.google.common.base.Strings;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

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

    String target = Strings.repeat("0", pow);
    String data;

    do {
      magicNumber = Math.abs(ThreadLocalRandom.current().nextInt());
      data = magicNumber + id + prevHash + timestamp;
      hash = StringUtil.applySha256(data);
    } while (!hash.substring(0, pow).equals(target));
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
        + prevHash
        + "\nHash of the Block:\n"
        + hash;
  }
}
