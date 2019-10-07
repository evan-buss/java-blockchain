package com.evanbuss.blockchain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class BlockChain implements Iterable<Block> {
  private int pow = 1;
  private List<Block> blockChain = new LinkedList<>();
  private LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

  BlockChain() {
    Thread thread = new Thread(this::generateMessages);
    thread.setDaemon(true);
    thread.start();
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
      block = new Block("0", pow, messageQueue);
    } else {
      block = new Block(blockChain.get(blockChain.size() - 1).getHash(), pow, messageQueue);
    }

    float mineTime = (System.currentTimeMillis() - start) / 1000F;
    if (mineTime < 15) {
      pow++;
    } else if (mineTime > 60) {
      pow--;
    }

    block.setTime(mineTime);
    blockChain.add(block);
    return block;
  }

  /**
   * Add random messages to the message queue at random intervals. Designed to be run in a daemon
   * thread
   */
  private void generateMessages() {
    String[] messages =
        new String[]{
            "rito_isimito: PedoBear FURRY PEDO FOX",
            "xFennek: forsenAngry Clap forsenAngry Clap ",
            "Osaka293: widepeepoHappy",
            "Omiyage2: Pog Pool ",
            "Nivelhein: Dora the explorer reference? Pog",
            "PledgeBaam_0: FeelsDankMan WHAT?! 󠀀",
            "Myth0108ia: AYAYA",
            "MustiRaikkonen: cmonBruh DEVS cmonBruh",
            "Mazzo_tv: sodaF1 sodaF2",
            "Snusbot: Looking for a Pleb Free Zone? Join other subscribers on Discord by typing !discord in chat and following the instructions.",
            "TETYYS: xqcJuice ",
            "frankyranky: CoolCat",
            "Dodoluy: monaS",
            "Wajky: FeelsGoodMan",
            "alexnt1: <3",
            "microgold02: monkaS",
            "B_o_r_i_s: SillyChamp",
            "Sliders: :point_up: THIS MaN FUCKED :point_down: THIS MaN IN THE ASS gachiHYPER",
            "xFennek: forsenAngry Clap",
            "Kdeatron: @vali24dm, forsenGFMB forsenE :wave: ",
            "Adio937: forsenAngry forsenGFMB",
            "Cupheadcyphy: :fox_face:",
            "forester587: monkaS",
            "Rus7m: cmonBruh ?",
            "Froggymankyle: :fox_face: the fuck you say",
            "GLHF Pledgesoms: DONT SKIP ThE DiALOGUE LULW",
            "enza_denino_son: cmonBruh",
        };

    while (true) {
      try {
        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(0, 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      int messageIndex = ThreadLocalRandom.current().nextInt(messages.length);
      messageQueue.offer(messages[messageIndex]);
    }
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

  void shutdown() {
    HashPool.shutdown();
  }

  @Override
  public Iterator<Block> iterator() {
    return blockChain.iterator();
  }
}
