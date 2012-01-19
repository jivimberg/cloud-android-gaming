package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class ImageUtils {

	public static byte[] extractBytes(byte[] input) {
		Inflater ifl = new Inflater(); // mainly generate the extraction
		// df.setLevel(Deflater.BEST_COMPRESSION);
		ifl.setInput(input);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
		byte[] buff = new byte[1024];
		try {
			while (!ifl.finished()) {
				int count;
				count = ifl.inflate(buff);
				baos.write(buff, 0, count);

			}
		} catch (DataFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return baos.toByteArray();
	}
	
}
