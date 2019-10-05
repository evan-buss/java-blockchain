package com.evanbuss.blockchain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BlockChain implements Iterable<Block> {
  private final int pow;
  private List<Block> blockChain = new LinkedList<>();

  BlockChain(int pow) {
    this.pow = pow;
  }

  Block createBlock() {
    Block block;
    if (blockChain.size() == 0) {
      block = new Block("0", pow);
    } else {
      block = new Block(blockChain.get(blockChain.size() - 1).getHash(), pow);
    }
    blockChain.add(block);
    return block;
  }

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

  @Override
  public Iterator<Block> iterator() {
    return blockChain.iterator();
  }
}
