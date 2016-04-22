package com.sync;

import java.util.concurrent.LinkedBlockingQueue;

public class SyncMessagePool {
  
  private LinkedBlockingQueue<SyncMessage> messageQueue;
  private static final SyncMessagePool syncMessagePool = new SyncMessagePool();
	
  private SyncMessagePool(){
	  messageQueue = new LinkedBlockingQueue<SyncMessage>();
  }
  
  public static SyncMessagePool getInstance(){
	  return syncMessagePool;
  }
  
  public void addToMessageQueue(SyncMessage syncMessage){
	  try {
		messageQueue.put(syncMessage);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
  }
  
  public SyncMessage getMessageFromQueue(){
	  SyncMessage syncMessage = null; 
	 try {
		 syncMessage =  messageQueue.take();
	 } catch (InterruptedException e) {
		 e.printStackTrace();
	 }
	 return syncMessage;
  }


}
