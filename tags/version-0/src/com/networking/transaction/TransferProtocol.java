package com.networking.transaction;

public enum TransferProtocol {

	/*
	 * Packet: PACKET_SIZE + int (4 bytes) + IMAGE_SECTION + int (4 bytes) + image
	 */
	
	PACKET_SIZE, IMAGE_SECTION;
	
}
