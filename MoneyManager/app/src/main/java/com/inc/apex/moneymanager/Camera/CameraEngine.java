package com.inc.apex.moneymanager.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inc.apex.moneymanager.Unit.Constant;

import java.io.IOException;
import java.util.List;

/**
 * Created by Dat.Pham on 9/28/2017.
 */

public class CameraEngine {

    private boolean             on = false;
    private static CameraEngine isInstance;
    public static Camera        mCamera;
    private CameraPreview       mCameraPreview;
    private Context             mContext;
    private SurfaceHolder       mSurfaceHolder;

    private CameraEngine(Context context,SurfaceHolder surfaceHolder){

        this.mContext = context;
        this.mSurfaceHolder = surfaceHolder;

    }

    public static CameraEngine getInstance(Context context,SurfaceHolder surfaceHolder){

        //Create Camera Engine
        if(isInstance == null)
            isInstance = new CameraEngine(context,surfaceHolder);

        return isInstance;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean getOn(){
        return on;
    }


    private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };



    public void refreshCamera(){

        try{
           mCamera.stopPreview();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void turnOn(){
        //Default turn on back Camera

        mCamera = Camera.open(findBackFacingCamera());

        if(mCamera == null)
            return;

        try{
            autoForcus();
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();

            on = true;
        }catch (IOException e){
            Log.e(Constant.TAG,"Error setting camera preview: "+e.getMessage());
        }

    }



    public void autoForcus(){
        //set Forcus Auto
        Camera.Parameters parameters = mCamera.getParameters();
       if(parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
           parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
    }



/*******************************
 Get Back Camera of Phone
 ******************************/
    private int findBackFacingCamera(){
        int cameraId= -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i=0;i<numberOfCameras;i++){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i,info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

   /*****************************
    Pause Camera
     *************************/
    public void pauseCamera(){


        releaseCamera();

    }


    public void releaseCamera(){

        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            on = false;
            isInstance = null;
        }
    }

    /******************************
     * Take photo
     ******************************/

    public void takePhoto(Camera.PictureCallback jpegPictureCallback ){

        if(on)
             mCamera.takePicture(shutterCallback,null,jpegPictureCallback);
    }


}
