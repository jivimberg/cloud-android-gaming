package com;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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

public class Client extends Thread {

	public static final String SERVER_IP = "10.0.2.15";
	public static final String TCP_IP_ADDRESS = "10.0.2.2";
	public static final int UDP_LISTENING_PORT = 5000;
	public static final int TCP_COMMUNICATING_PORT = 4444;
	private static final int MAX_UDP_PACKET_SIZE = 65507;
	
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
		DatagramSocket socket = null;
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
			socket = new DatagramSocket(UDP_LISTENING_PORT, serverAddr);
			receiving = true;
			
			int idx = 0;
			//int lost = 0;
			while(receiving){
				byte[] data = new byte[MAX_UDP_PACKET_SIZE];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				//Log.i("UDP", "Waiting for packet...");

				socket.receive(packet);
				//Log.i("UDP", "Packet received");
				Log.d("UDP", "packet index "+ idx + " and time: " +System.currentTimeMillis());
				idx++;

				//data = ImageUtils.extractBytes(packet.getData());
				final ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
				final DataInputStream dis = new DataInputStream(bais);
				int imageIdx = dis.readInt();
				
				if(imageIdx < lastImageIdx){
					//lost++;
					//Log.d("UDP", "lost one! Total: " + lost);
					continue;
				}else if(imageIdx > lastImageIdx){
					lastImageIdx = imageIdx;
				}
				int xOffset = dis.readInt();
				int yOffset = dis.readInt();
				dis.read(data);
				//Log.d("UDP", "data length: "+data.length);
				final InputStream img = new ByteArrayInputStream(data);
				
				final Drawable drawable = Drawable.createFromStream(img, "StreamName"); 
				//final Bitmap drawable = BitmapFactory.decodeByteArray(data, 0, data.length);
				/* Create message to be send to the UI Thread */
				final Message message = new Message();
				message.obj = drawable;
				message.arg1 = xOffset;
				message.arg2 = yOffset;
				if (uiHandler != null) {
					uiHandler.sendMessage(message);
					Log.i("Server", "Message sent to UI");
				}
				
			}
		} catch (Exception e) {
			Log.e("UDP", "Error", e);
		}finally{
			if(socket != null) socket.close();
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
			socket = new Socket(InetAddress.getByName(TCP_IP_ADDRESS), TCP_COMMUNICATING_PORT);
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
			socket = new Socket(InetAddress.getByName(TCP_IP_ADDRESS), TCP_COMMUNICATING_PORT);
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



