package com;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.login.LoginService;
import com.sync.SyncService;

public class Server {
	
	public static void main(String[] args) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input;
		
		System.out.println("Please input the maximum player number: ");
		int playerNumber = 0;
		while(true){
			input = reader.readLine();
			try{
				playerNumber = Integer.parseInt(input);
			}catch(Exception e){
				System.out.println("Please input a valid number");
				continue;
			}
			if(playerNumber<=0 || playerNumber>10){
				System.out.println("The player number should be from 1 to 10");
				continue;
			}
			break;
		}
		LoginService.MAX_PLAYERS = playerNumber;
		
		LoginService.getInstance().start();
		SyncService.getInstance().start();
	}
	
}
