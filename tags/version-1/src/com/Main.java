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
import android.widget.GridLayout;
import android.widget.ImageView;

public class Main extends Activity {

	private static final int COLS = 4;
	private static final int ROWS = 4;
	private Button startUDPTrafficButton;
	private Button stopUDPTrafficButton;
	private Handler handler;
	
	private Client server;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout1);
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLS; j++){
				final ImageView myImageView = new ImageView(this);
				myImageView.setId(Integer.valueOf(""+j+i));
				gridLayout.addView(myImageView);
				Log.d("UI","id setted: " + j+"-"+i);
			}
		}

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// do something in the user interface to display data from message
				final Drawable img = (Drawable) msg.obj;
				final int x = msg.arg1;
				final int y = msg.arg2;
				final String idSearched = ""+x+y;
				final ImageView myImage = (ImageView) findViewById(Integer.valueOf(idSearched));
				//myImage.setImageBitmap(img); //No funciona...
				myImage.setBackgroundDrawable(img);
				//Log.i("UI", "Drawing!");
			}
		};
		
		startUDPTrafficButton = (Button) findViewById(R.id.search_button);
		startUDPTrafficButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				server = new Client(handler);
				server.start();
			}
		});
		
		
		stopUDPTrafficButton = (Button) findViewById(R.id.close_button);
		stopUDPTrafficButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				//Log.i("UDP", "S: Wanting to start closure...");
				//server.interrupt();
				//server.closeTransaction();
			}
		});
		
	}
	
	@SuppressWarnings("static-access")
	@Override
	protected void onPause() {
		super.onPause();
		try {
			Log.d("Activity", "Activity paused!");
			server.sleep(0);
		} catch (InterruptedException e) {
			Log.e("Error", e.getMessage());
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(server != null){
			server.interrupt();
			Log.d("Activity", "Activity interrupted!");
		}
	}
	
}