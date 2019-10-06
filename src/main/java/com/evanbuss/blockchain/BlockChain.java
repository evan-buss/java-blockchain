package com.evanbuss.blockchain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BlockChain implements Iterable<Block> {
  private int pow = 1;
  private List<Block> blockChain = new LinkedList<>();

  BlockChain() {
  }

  /**
   * Create a new block.
   *
   * <p>The block is "mined" by submitting it to a thread pool using all available processors. Once
   * a valid hash has been found, the rest of the miners stop working. The Proof of Work is adjusted
   * between each block creation to have a POW time of between 15 seconds and 60 seconds.
   *
   * @return The newly created block added to the block chain
   */
  Block createBlock() {
    Block block;
    long start = System.currentTimeMillis();

    if (blockChain.size() == 0) {
      block = new Block("0", pow);
    } else {
      block = new Block(blockChain.get(blockChain.size() - 1).getHash(), pow);
    }

    float mineTime = (System.currentTimeMillis() - start) / 1000F;
    if (mineTime < 15) {
      pow++;
    } else if (mineTime > 60) {
      pow--;
    }

    blockChain.add(block);
    return block;
  }

  // Loop through the BlockChain and validate each block
  // Each block MUST contain a valid previous block
  boolean validate() {
    String prevHash = "0";
    for (Block b : blockChain) {
      if (!prevHash.equals(b.getPrevHash())) {
        System.out.println(b.getHash() + " != " + prevHash);
        return false;
      }
      prevHash = b.getHash();
    }
    return true;
  }

  public int getPow() {
    return pow;
  }

  @Override
  public Iterator<Block> iterator() {
    return blockChain.iterator();
  }
}
