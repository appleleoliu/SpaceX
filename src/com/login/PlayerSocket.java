package com.login;

import java.net.Socket;

import com.sync.RecvSocket;
import com.sync.SendSocket;

public class PlayerSocket {
	public String playerId;
	public int roleId;
	public RecvSocket recvSocket;
	public SendSocket sendSocket;
	
	public PlayerSocket(Socket socket, String playerId, int roleId){
		this.playerId = playerId;
		this.roleId = roleId;
		this.recvSocket = new RecvSocket(socket);
		this.sendSocket = new SendSocket(socket);
	}
	
	@Override
	public String toString(){
	  return "playerId: "+playerId+" roleId: "+roleId;	
	}
  
}
