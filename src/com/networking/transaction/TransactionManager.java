package com.networking.transaction;

import java.io.File;
import java.net.DatagramSocket;

public interface TransactionManager {

	boolean sendImage(DatagramSocket socket, File file);
	
}
