package com.inc.apex.moneymanager.Camera;

import android.content.Context;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inc.apex.moneymanager.Unit.Constant;

/**
 * Created by Dat.Pham on 9/28/2017.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;
    private CameraEngine mCameraEngine;
    public CameraPreview(Context context, SurfaceView surfaceView){
        super(context);

        mContext = context;
        mSurfaceView = surfaceView;

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraEngine = CameraEngine.getInstance(mContext,mHolder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        turnOnCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraEngine.releaseCamera();
    }



    public void pauseSurface(){
        mCameraEngine.pauseCamera();
        mHolder.removeCallback(this);
    }


    public void refreshCamera(){

        try{
            if(mHolder.getSurface() == null)
                return;
            mCameraEngine.refreshCamera();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void turnOnCamera(){

        if(mCameraEngine != null && !mCameraEngine.getOn())
            mCameraEngine.turnOn();
    }



    public void takePhoto(Camera.PictureCallback callback){
        if(mCameraEngine != null && mCameraEngine.getOn()){
            mCameraEngine.takePhoto(callback);
        }
    }


}
