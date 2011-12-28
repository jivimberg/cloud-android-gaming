package com;

import src.com.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Main extends Activity {

	private Button startUDPTrafficButton;
	private Button stopUDPTrafficButton;
	private Handler handler;

	private Client server;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// do something in the user interface to display data from message
				Drawable img = (Drawable) msg.obj;
				Log.i("UI", "Got an image to draw");
				ImageView myImage = (ImageView) findViewById(R.id.imageToShow);
				myImage.setBackgroundDrawable(img);
				Log.i("UI", "Drawing!");
				//Date date = new Date();
				//System.out.println("* Time: " + date.getMinutes() + " : " + date.getSeconds());
			}
		};
		
		startUDPTrafficButton = (Button) findViewById(R.id.search_button);
		startUDPTrafficButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				server = new Client(handler);
				server.start();
				try {
					Thread.sleep(500);
					// server.closeTransaction();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		stopUDPTrafficButton = (Button) findViewById(R.id.close_button);
		stopUDPTrafficButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("UDP", "S: Wanting to start closure...");
				server.interrupt();
				server.closeTransaction();
			}
		});
		
	}
}