package com.sync;

import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import com.login.PlayerSocket;
import com.login.SocketManager;

public class SyncService extends Thread{
  private static final SyncService syncService = new SyncService();
  
  private SyncService(){}
  
  public static SyncService getInstance(){
	  return syncService;
  }
  
  @Override
  public void run(){
	  System.out.println("SyncService is running...");
	  
	  SyncMessage syncMessage;
	  Map<Socket, PlayerSocket> socketMap = SocketManager.getInstance().getSocketMap();
	  Iterator<Socket> iterator;
	  Socket socket;
	  SendSocket sendSocket;
	  while(true){
		  syncMessage = SyncMessagePool.getInstance().getMessageFromQueue();
		  if(syncMessage!=null){
			iterator = socketMap.keySet().iterator(); 
			while(iterator.hasNext()){
				socket = iterator.next(); 
				sendSocket = socketMap.get(socket).sendSocket;
				if(syncMessage.socketId!=socket.hashCode()){
				  sendSocket.addToMessageQueue(syncMessage);
				}
			}
		  }
	  }
  }
  
}
