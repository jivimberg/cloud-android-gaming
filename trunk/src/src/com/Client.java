package src.com;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import src.com.networking.transaction.TransactionManagerImpl;

public class Client implements Runnable {
	
	public static final String SERVERIP = "127.0.0.1"; 
	public static final int SERVERPORT = 5000;
	public static final int TCPPORT = 4444;
	public boolean sending = false;
	
	private static final String FILE_NAME = "Java";
	
	private List<File> files;
	private TransactionManagerImpl txManager;
	
	public Client(){
		files = new ArrayList<File>();
		File file;
		for(int i = 9; i > 2; i--){
			file = new File("src/resources/" + FILE_NAME + i + ".jpg");
			files.add(file);
		}
		txManager = new TransactionManagerImpl();
	}
	
	public void run() {
		while(true)
			startTCPSendListen();
			//startUDPTransaction();
	}
	
	private void startTCPSendListen() {
		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(TCPPORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port:"+TCPPORT+".");
        }
 
        Socket clientSocket = null;
        try {
        	System.out.println("listening to first signal");
            clientSocket = serverSocket.accept();
            clientSocket.close();
            System.out.println("sending packets!");
            new Thread(){
            	@Override
            	public void run() {
            		ServerSocket serverSocket = null;
            		try {
                        serverSocket = new ServerSocket(TCPPORT);
                    } catch (IOException e) {
                        System.err.println("Could not listen on port:"+TCPPORT+".");
                    }
             
                    Socket clientSocket = null;
                    try {
                    	System.out.println("listening to second signal");
                        clientSocket = serverSocket.accept();
                        clientSocket.close();
                        System.out.println("Closing signal!!");
                        sending = false;
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                    }
            	}
            };
            startUDPTransaction();
        } catch (IOException e) {
            System.err.println("Accept failed.");
        }
		
	}
	
	 

	private void startUDPTransaction(){
		DatagramSocket socket = null;
		try {
			// Retrieve the ServerName
			sending = true;
			
			/* Create new UDP-Socket */
			socket = new DatagramSocket();
			System.out.println("creating new socket");

			int idx = 0; //for benchmarking 
			while(sending){
				//Thread.sleep(500);
				for(int i=0;i<files.size();i++){
					
					/* Prepare some data to be sent. */
					File file = files.get(i);
					txManager.sendImage(socket, file);
					
					idx++;
					
					if(idx == 500){
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			socket.close();
		}
	}

}
