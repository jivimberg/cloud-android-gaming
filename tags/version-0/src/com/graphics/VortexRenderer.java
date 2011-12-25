package com.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class VortexRenderer implements GLSurfaceView.Renderer {
    private static final String LOG_TAG = VortexRenderer.class.getSimpleName();
    
    private float _red = 0f;
    private float _green = 0f;
    private float _blue = 0f;
    
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
    }
    
    public void onDrawFrame(GL10 gl) {
        // define the color we want to be displayed as the "clipping wall"
        gl.glClearColor(_red, _green, _blue, 1.0f);
        // clear the color buffer to show the ClearColor we called above...
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void setColor(float r, float g, float b) {
        _red = r;
        _green = g;
        _blue = b;
    }
}