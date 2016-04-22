package com.sync;

import java.sql.Timestamp;

public class SyncMessage {
  public int socketId;
  public Timestamp time;
  public byte[] content;
  
  public SyncMessage(){}
  
  public SyncMessage(int socketId, Timestamp time, byte[] content){
	  this.socketId = socketId;
	  this.time = time;
	  this.content = content;
  }
  
  @Override
  public String toString(){
	return time+" "+new String(content);  
  }

}
