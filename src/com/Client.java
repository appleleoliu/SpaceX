package com;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.client.ClientSocket;
import com.login.LoginService;

public class Client {
	private static final int CLIENT_COUNT = 10;
	
  public static void main(String[] args){
	  System.out.println("Client Start");
	  Socket socket = null;
	  try {
		  for(int i=0;i<CLIENT_COUNT;i++){
			  socket = new Socket(LoginService.SERVER_NAME, LoginService.SERVER_PORT);
			  new ClientSocket(socket).start();
		  }
	  } catch (UnknownHostException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  System.out.println("Client End");
  	}
  
}
