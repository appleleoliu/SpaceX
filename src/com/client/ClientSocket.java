package com.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientSocket extends Thread{
	  private Socket socket;
	  public ClientSocket(Socket socket){
		  this.socket = socket;
	  }
	  
	  private long getRandomSeconds(){
		 return Math.round(Math.random()*1000);
	  }
	  
	  @Override
	  public void run(){
		System.out.println(Thread.currentThread().getName()+" is runing...");
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		
		try{
			//send the role_id
			bos = new BufferedOutputStream(socket.getOutputStream());
			JSONObject jsonObject = new JSONObject("{'role_id':"+socket.hashCode()+"}");
			bos.write(jsonObject.toString().getBytes());
			bos.flush();
			
			//receive confirmation message
			bis = new BufferedInputStream(socket.getInputStream());
			byte[] content;
			byte[] buffer = new byte[1024];
			int length = bis.read(buffer);
			content = Arrays.copyOf(buffer, length);
			System.out.println(new String(content));
			
			//start sending game message
			int i = 0;
			while(socket.isConnected()){
			  Thread.sleep(getRandomSeconds());
			  bos.write((socket.getLocalPort()+"-Action "+i++).getBytes());
			  bos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			
			try {
				if(bos!=null) bos.close();
				if(bis!=null) bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		System.out.println(Thread.currentThread().getName()+" stops");
	    
	  }
}
