package com;

import java.util.HashMap;
import java.util.Map;

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

	private Map<String, ImageView> views;
	
	private Client server;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout1);
		
		views = new HashMap<String, ImageView>();
		
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLS; j++){
				final ImageView myImageView = new ImageView(this);
				//myImageView.setBackgroundColor(color.holo_orange_dark);
				//myImageView.setId(j*i); //usar esto para no tener que usar el mapa horrible
				
				//linearLayout.addView(myImageView, j+4, LayoutParams.MATCH_PARENT);
				gridLayout.addView(myImageView);
				
				views.put(j+"-"+i, myImageView);
				Log.d("UI","id entered in the map: " + j+"-"+i);
			}
		}

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// do something in the user interface to display data from message
				final Drawable img = (Drawable) msg.obj;
				final Bundle bundle = msg.getData();
				final int x = bundle.getInt("xOffset");
				final int y = bundle.getInt("yOffset");
				Log.d("UI", "Got an image to draw! ImgId: " + bundle.getInt("ImageIdx") + ", x: " + x + ", y: " + y);
				final String idSearched = x+"-"+y;
				final ImageView myImage = views.get(idSearched);
				//Log.d("UI","id looked in the map: " + idSearched);
				myImage.setBackgroundDrawable(img);
				//Log.i("UI", "Drawing!");
			}
		};
		
		startUDPTrafficButton = (Button) findViewById(R.id.search_button);
		startUDPTrafficButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				server = new Client(handler);
				server.start();
				/*try {
					//Thread.sleep(500);
					//server.closeTransaction();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
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