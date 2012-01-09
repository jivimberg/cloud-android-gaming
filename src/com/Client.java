package com;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import com.utils.ImageUtils;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Client extends Thread {

	public static final String SERVER_IP = "10.0.2.15"; 
	public static final int UDP_LISTENING_PORT = 5000;
	public static final int TCP_COMMUNICATING_PORT = 4444;
	private static final int MAX_UDP_PACKET_SIZE = 65507;
	
	private DatagramSocket socket;
	private boolean receiving = false;
	
	private Handler uiHandler;
	private int lastImageIdx;
	
	public Client(Handler uiHandler){
		this.uiHandler = uiHandler;
	}
	
	@Override
	public void run() {
		Log.i("TCP", "S: Starting handshake...");
		doTCPHandshake();
		Log.i("TCP", "S: Handshake done!");
		try {
			/* Retrieve the ServerName */
			InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

			Log.i("UDP", "S: Connecting...");
			/* Create new UDP-Socket */
			socket = new DatagramSocket(UDP_LISTENING_PORT, serverAddr);
			receiving = true;
			
			//int idx = 0;
			while(receiving){
				
				byte[] buf = new byte[MAX_UDP_PACKET_SIZE];
				/* Prepare a UDP-Packet that can 
				 * contain the data we want to receive */
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				Log.i("UDP", "Receiving...");

				/* Receive the UDP-Packet */
				socket.receive(packet);
				Log.i("UDP", "Packet received");
				//Log.d("UDP", ""+System.currentTimeMillis());
				//Log.d("UDP","packet index: " + idx + " -> " + System.currentTimeMillis());
				//idx++;
				
				byte[] data = new byte[MAX_UDP_PACKET_SIZE];
				data = ImageUtils.extractBytes(packet.getData());
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				DataInputStream dis = new DataInputStream(bais);
				int imageIdx = dis.readInt();
				
				if(imageIdx < lastImageIdx){
					continue;
				}else if(imageIdx > lastImageIdx){
					lastImageIdx = imageIdx;
				}
				
				int xOffset = dis.readInt();
				int yOffset = dis.readInt();
				dis.read(data);
				//Log.d("UDP", "data length: "+data.length);
				InputStream img = new ByteArrayInputStream(data); //Sin desencodear
				
				/* Create Drawable */
				Drawable drawable = Drawable.createFromStream(img, "StreamName"); 
				
				/* Create message to be send to the UI Thread */
				Message message = new Message();
				message.obj = drawable;
				Bundle bundle = new Bundle();
				bundle.putInt("ImageIdx", imageIdx);
				bundle.putInt("xOffset", xOffset);
				bundle.putInt("yOffset", yOffset);
				message.setData(bundle);
				if (uiHandler != null) {
					uiHandler.sendMessage(message); // sendMessage
					Log.i("Server", "Message sent to UI");
				}
				
			}
		} catch (Exception e) {
			Log.e("UDP", "Error", e);
		}finally{
			socket.close();
		}
	}
	
	public void closeTransaction(){
		Log.i("UDP", "Starting socket closure...");
		receiving = false;
		closeUDPTransaction();
		Log.i("UDP", "Connection closed!!");
	}
	
	private void doTCPHandshake() {
		Socket socket = null;
		try {
			Log.i("TCP", "Pinging TCP handshake in port: " + TCP_COMMUNICATING_PORT);
			socket = new Socket(InetAddress.getByName("10.0.2.2"), TCP_COMMUNICATING_PORT);
			Log.i("TCP", "Adress reached.");
		} catch (IOException e) {
			Log.e("TCP", "Error", e);
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				Log.e("TCP", "Error", e);
			}
		}
		
	}
	
	private void closeUDPTransaction() {
		Socket socket = null;
		try {
			socket = new Socket(InetAddress.getByName("10.0.2.2"), TCP_COMMUNICATING_PORT);
			Log.i("TCP", "Closing Socket created.");
		} catch (IOException e) {
			Log.e("TCP", "Error", e);
		}finally{
			try {
				socket.close();
				Log.i("TCP", "Closing Socket closed.");
			} catch (IOException e) {
				Log.e("TCP", "Error", e);
			}
		}
	}
	
}

