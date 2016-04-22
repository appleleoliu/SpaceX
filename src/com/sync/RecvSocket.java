package com.sync;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import com.login.SocketManager;

public class RecvSocket extends Thread{
	private Socket socket;
	
	public RecvSocket(Socket socket){
		this.socket = socket;
	}
  
	@Override
	public void run(){
      System.out.println(this.toString()+" is running...");
	  BufferedInputStream bis = null;
	  try {
		  bis = new BufferedInputStream(socket.getInputStream());
		  byte[] content;
		  int length;
		  byte[] buffer = new byte[1024];
		  SyncMessage syncMessage;
		  while((length=bis.read(buffer))!=-1){
			  content = Arrays.copyOf(buffer, length);
			  syncMessage = new SyncMessage(socket.hashCode(), new Timestamp(new Date().getTime()), content);
			  SyncMessagePool.getInstance().addToMessageQueue(syncMessage);
		  }
		
	  } catch (IOException e) {
		System.out.println(socket+" disconnected");
	  } finally{
		SocketManager.getInstance().removeSocket(socket);
		if(bis != null){
			try {
				bis.close();
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
		return "RecvSocket "+this.socket.getLocalPort()+" "+Thread.currentThread().getName();
	}
}
