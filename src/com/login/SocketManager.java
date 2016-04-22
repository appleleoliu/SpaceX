package com.login;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SocketManager {
	private ConcurrentHashMap<Socket, PlayerSocket> socketMap;
	
	private SocketManager(){
		socketMap = new ConcurrentHashMap<Socket, PlayerSocket>();
	}
	
	private static final SocketManager sm = new SocketManager();
	public static SocketManager getInstance(){
		return sm;
	}
	
	public synchronized void removeSocket(Socket socket){
	   socketMap.remove(socket);
	   System.out.println(socket+" removed.");
	   if(socketMap.size()==0){
		   LoginService.getInstance().gameStarted = false;
		   System.out.println("Game ended.");
	   }
	   
	}
	
	public ConcurrentHashMap<Socket, PlayerSocket> getSocketMap(){
		return socketMap;
	}
	
	public void addSocket(Socket socket, PlayerSocket playerSocket){
		socketMap.put(socket, playerSocket);
	}
	
	public int getSocketCount(){
		return socketMap.size();
	}

}
