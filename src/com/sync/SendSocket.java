package com.sync;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import com.login.SocketManager;

public class SendSocket extends Thread{
	
   private Socket socket;
   private LinkedBlockingQueue<SyncMessage> messageQueue;
	
   public SendSocket(Socket socket){
     this.socket = socket;
     messageQueue = new LinkedBlockingQueue<SyncMessage>();
   }
   
   public void addToMessageQueue(SyncMessage syncMessage){
	   	try {
	   		messageQueue.put(syncMessage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
   }

   @Override
   public void run(){
	 System.out.println(this.toString()+" is running...");
	 BufferedOutputStream bos = null;
     try {
    	bos = new BufferedOutputStream(socket.getOutputStream());
    	SyncMessage syncMessage;
    	while(socket.isConnected()){
    		syncMessage = messageQueue.take();//blocking method
    		if(syncMessage!=null){
    			bos.write(syncMessage.content);
    			bos.flush();
    			//System.out.println(this.toString()+": "+syncMessage.toString());
    		}
    	}
		
	} catch (IOException e) {
		System.out.println(socket+" disconnected");
	} catch (InterruptedException e) {
		e.printStackTrace();
	} finally{
		SocketManager.getInstance().removeSocket(socket);
		if(bos!=null){
		  try {
			bos.close();
		  } catch (IOException e) {
			e.printStackTrace();
		  }	
		}
		if(socket!=null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
   }
	
   @Override
   public String toString(){
	   return "SendSocket "+this.socket.getPort();
   }
}
