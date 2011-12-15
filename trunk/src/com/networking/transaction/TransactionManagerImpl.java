package com.networking.transaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TransactionManagerImpl implements TransactionManager {

	public static final String SERVERIP = "127.0.0.1";
	public static final int SERVERPORT = 5000;

	public boolean sendImage(DatagramSocket socket, File file) {
		InetAddress serverAddr;
		try {
			serverAddr = InetAddress.getByName(SERVERIP);

			byte[] buf = imgToByte(file);

			System.out.println("image size: " + buf.length);
			
			/* Create UDP-packet with 
			 * data & destination(url+port) */
			DatagramPacket packet = new DatagramPacket(buf, buf.length,	serverAddr, SERVERPORT);
			System.out.println("creating packet");
			
			/* Send out the packet */
			socket.send(packet);
			System.out.println("sending packet.");
		} catch (IOException e) {
			return false;
		}

		return true;
	}
	
	private byte[] imgToByte(File file) {
		//List<byte[]> bytes = new ArrayList<byte[]>();
		byte[] img = null;
		
		try {
			FileInputStream fis = new FileInputStream(file);
			
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        byte[] buf = new byte[1024];
	        try {
	            for (int readNum; (readNum = fis.read(buf)) != -1;) {
	                /*Writes len bytes from the specified byte array starting at offset 
	                off to this byte array output stream.*/
	            	bos.write(buf, 0, readNum); //no doubt here is 0
	                System.out.println("read " + readNum + " bytes,");
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        
	        //ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
	        //esto es usando encoding
//	        ByteArrayOutputStream encodedImage = encoder.encode(bis);
//	        img = encodedImage.toByteArray();
	        
	        //Esto es sin usar encoding
	        img = bos.toByteArray();
	        
	        while(img.length > 8000){
	        	//bytes.add()
	        }

		} catch (FileNotFoundException e) {
			System.out.println("File " + file.getAbsolutePath() + " was not found!");
		}
		return img;
	}
	
}
