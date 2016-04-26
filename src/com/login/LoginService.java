package com.login;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class LoginService extends Thread{
	public static final String SERVER_NAME = "localhost";
	public static final int SERVER_PORT = 8000;
	
	private LoginService(){}
	
	private static final LoginService loginService = new LoginService();
	public static LoginService getInstance(){
		return loginService;
	}

	private int maxPlayerNumber = 0;
	public int getMaxPlayerNumber(){
		return maxPlayerNumber;
	}
	public void setMaxPlayerNumber(int maxPlayerNumber){
	    this.maxPlayerNumber = maxPlayerNumber;	
	}

    private boolean gameStarted = false;
	public boolean getGameStarted(){
      return gameStarted;
	}
	public void setGameStarted(boolean gameStarted){
		this.gameStarted = gameStarted;
	}
	
	@Override
	public void run(){
		System.out.println("LoginService is running...");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(SERVER_PORT); 
			System.out.println(serverSocket);
			Socket socket;
			int count;
			while (true) {  
				if(!gameStarted){
					count = SocketManager.getInstance().getSocketCount();
					System.out.println("Waiting for "+(maxPlayerNumber-count)+" players to join the game...");	
				}
				
				socket = serverSocket.accept();
				
				if(!gameStarted){
					count = SocketManager.getInstance().getSocketCount();
					addPlayerSocket(socket, count+"");
				}else{
					socket.close();
					System.out.println(socket.getLocalAddress()+":"+socket.getPort()+" is refused!");
					
				}
				
			}  
          
		} catch (Exception e) {  
			e.printStackTrace();  
		}finally{
			if(serverSocket!=null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		}		 
		System.out.println("LoginService stopped.");	
	}
	
	private void addPlayerSocket(Socket socket, String playerId){
		BufferedInputStream	bis = null;
		int roleId = -1;
		try{
			bis = new BufferedInputStream(socket.getInputStream());
			byte[] buffer = new byte[1024];
			int length = bis.read(buffer);
			byte[] content = Arrays.copyOf(buffer, length);
				
			JSONObject jsonObject = new JSONObject(new String(content));
			System.out.println(jsonObject.toString());
			roleId = jsonObject.getInt("role_id");
		}catch(Exception e){
			System.out.println(e);
			try {
				if(bis!=null){
					bis.close();
				}
				if(socket!=null){
					socket.close();
				}
			} catch (IOException e1) {
				System.out.println(e1);
			}
		} 
		
		if(roleId>-1){
			PlayerSocket playerSocket = new PlayerSocket(socket, playerId, roleId);
			SocketManager.getInstance().addSocket(socket, playerSocket);
			System.out.println(socket.getLocalAddress()+":"+socket.getPort()+" "+playerSocket.toString()+" joined the game!");
		
			if(SocketManager.getInstance().getSocketCount()==maxPlayerNumber){
				startGame();		
			}
		}
	}
	
	private void startGame(){
		System.out.println("start game()...");
		BufferedOutputStream bos = null;
		
		Map<Socket, PlayerSocket> socketMap = SocketManager.getInstance().getSocketMap();
		Iterator<Socket> iterator = socketMap.keySet().iterator();
		Socket socket;
		PlayerSocket playerSocket;
			
		Iterator<Socket> innerIterator;
		Socket innerSocket;
		PlayerSocket innerPlayerSocket;
		StringBuffer sb;
		JSONObject jsonObject;
			
		while(iterator.hasNext()){
			socket = iterator.next();
				
			try{
				//generate the begin information
				sb = new StringBuffer("{'Data':[");
				innerIterator = socketMap.keySet().iterator();
				int i=0;
				while(innerIterator.hasNext()){
					innerSocket = innerIterator.next();
					if(innerSocket!=socket){
						i++;
						innerPlayerSocket = socketMap.get(innerSocket);
						if(i>1){sb.append(",");}
						sb.append("{'player_id':'"+innerPlayerSocket.playerId+"','role_id':"+innerPlayerSocket.roleId+"}");	
					}
				}
				sb.append("]}");
				jsonObject = new JSONObject(sb.toString());
				System.out.println(jsonObject.toString());
				
				//send the begin information
				bos = new BufferedOutputStream(socket.getOutputStream());
				bos.write((jsonObject.toString()+"\0").getBytes());
				bos.flush();
					
				//start the recv & send socket
				playerSocket = socketMap.get(socket);
				playerSocket.recvSocket.start();
				playerSocket.sendSocket.start(); 
			}catch(Exception e){
				 System.out.println(e);
				 SocketManager.getInstance().removeSocket(socket);
				 try{
					 if(bos!=null){
						 bos.close();	
					 }
					 if(socket!=null){
						 socket.close();
					 }
				 }catch(IOException e1){
					 System.out.println(e1);
				 }
			}
		}
		
		if(SocketManager.getInstance().getSocketCount()>0){
			gameStarted = true;
			System.out.println("Game started!");	
		}
	}
	
}
