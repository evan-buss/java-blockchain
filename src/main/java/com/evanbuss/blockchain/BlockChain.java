package com.evanbuss.blockchain;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BlockChain implements Iterable<Block> {
  private List<Block> blockChain = new LinkedList<>();

  BlockChain() {}

  void createBlock() {
    if (blockChain.size() == 0) {
      blockChain.add(new Block("0"));
    } else {
      blockChain.add(new Block(blockChain.get(blockChain.size() - 1).getHash()));
    }
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

  @NotNull
  @Override
  public Iterator<Block> iterator() {
    return blockChain.iterator();
  }
}
