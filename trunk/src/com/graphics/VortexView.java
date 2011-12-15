package com.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class VortexView extends GLSurfaceView {
    private static final String LOG_TAG = VortexView.class.getSimpleName();
    private VortexRenderer _renderer;
    
    public VortexView(Context context) {
        super(context);
        _renderer = new VortexRenderer();
        setRenderer(_renderer);
    }
    
    /*public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable() {
            public void run() {
                _renderer.setColor(event.getX() / getWidth(), event.getY() / getHeight(), 1.0f);
            }
        });
        return true;
    }*/
}