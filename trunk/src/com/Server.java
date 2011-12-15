package com;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Server extends Thread {

	public static final String SERVERIP = "10.0.2.15"; 
	public static final int UDP_LISTENING_PORT = 5000;
	public static final int TCP_COMMUNICATING_PORT = 4444;
	
	private DatagramSocket socket;
	private boolean receiving = false;
	
	private Handler uiHandler;
	
	public Server(Handler uiHandler){
		this.uiHandler = uiHandler;
	}
	
	@Override
	public void run() {
		Log.d("TCP", "S: Starting handshake...");
		doTCPHandshake();
		Log.d("TCP", "S: Handshake done!");
		try {
			/* Retrieve the ServerName */
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);

			Log.d("UDP", "S: Connecting...");
			/* Create new UDP-Socket */
			socket = new DatagramSocket(UDP_LISTENING_PORT, serverAddr);
			receiving = true;
			
			int idx = 0;
			while(receiving){
				
				byte[] buf = new byte[8000]; //This shouldn't be harcoded
				/* Prepare a UDP-Packet that can 
				 * contain the data we want to receive */
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				Log.d("UDP", "S: Receiving...");

				/* Receive the UDP-Packet */
				socket.receive(packet);
				Log.d("UDP", "S: Packet received");
				System.out.println(System.currentTimeMillis());
				System.out.println("packet index: " + idx + " -> " + System.currentTimeMillis());
				idx++;
				
				byte[] data = packet.getData();
				InputStream img = new ByteArrayInputStream(data); //Sin desencodear
				
				/* Create Drawable */
				Drawable drawable = Drawable.createFromStream(img, "StreamName"); 
				
				/* Create message to be send to the UI Thread */
				Message message = new Message();
				message.obj = drawable;
				if (uiHandler != null) {
					// sendMessage
					uiHandler.sendMessage(message);
					Log.d("Server", "S: Message send to UI");
				}
				
			}
		} catch (Exception e) {
			Log.e("UDP", "S: Error", e);
		}finally{
			socket.close();
		}
	}
	
	public void closeTransaction(){
		Log.d("UDP", "S: Starting closure...");
		receiving = false;
		closeUDPTransaction();
		Log.d("UDP", "S: Connection closed!!");
	}
	
	private void doTCPHandshake() {
		Socket socket = null;
		try {
			socket = new Socket(InetAddress.getByName("10.0.2.2"),Server.TCP_COMMUNICATING_PORT);
			Log.d("TCP", "S: 1st Socket created.");
		} catch (IOException e) {
			Log.e("TCP", "S: Error", e);
		}finally{
			try {
				socket.close();
				Log.d("TCP", "S: 1st Socket closed.");
			} catch (IOException e) {
				Log.e("TCP", "S: Error", e);
			}
		}
		
	}
	
	private void closeUDPTransaction() {
		Socket socket = null;
		try {
			socket = new Socket(InetAddress.getByName("10.0.2.2"), Server.TCP_COMMUNICATING_PORT);
			Log.d("TCP", "S: Closing Socket created.");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				socket.close();
				Log.d("TCP", "S: Closing Socket closed.");
			} catch (IOException e) {
				Log.e("TCP", "S: Error", e);
			}
		}
	}
	
}

