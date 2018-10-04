package com.inc.apex.moneymanager;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.inc.apex.moneymanager.AsyncTask.AsyncTaskEngine;
import com.inc.apex.moneymanager.Camera.CameraEngine;
import com.inc.apex.moneymanager.Camera.CameraPreview;
import com.inc.apex.moneymanager.Camera.FocusBoxView;
import com.inc.apex.moneymanager.Camera.Tools;
import com.inc.apex.moneymanager.Unit.Common;
import com.inc.apex.moneymanager.Unit.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class ScanReceiptActivity extends Activity implements View.OnClickListener,Camera.PictureCallback,Camera.ShutterCallback  {

    private SurfaceView   mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private CameraPreview mCameraPreview;
    private CameraEngine  mCameraEngine;
    private Button        mScan;
    private TessBaseAPI   mTess;
    private String        mBitmapPath;
    private File          mFileBitmapScan;
    private FocusBoxView  mForcusBox;
    private TextView      mOCRresult;
    private boolean       mFlgPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_receipt);
        mScan        = (Button)findViewById(R.id.shutter_button);
        mForcusBox   = (FocusBoxView)findViewById(R.id.focus_box);
        mSurfaceView = (SurfaceView)findViewById(R.id.camera_frame);
        mScan.setOnClickListener(this);


    }



    @Override
    protected void onResume() {
        super.onResume();

        mCameraPreview = new CameraPreview(this,mSurfaceView);

    }



    @Override
    protected void onPause() {
        super.onPause();
            mCameraPreview.pauseSurface();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shutter_button:
                mCameraPreview.takePhoto(this);
                break;
            default:
                break;
        }
    }



    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d(Constant.TAG, "Picture taken");
        Bitmap bitmap = Tools.getFocusedBitmap(this, camera, data, mForcusBox.getBox());
        //createFileBitmap(bitmap);
        new AsyncTaskEngine(this,bitmap).execute();

        mCameraPreview.refreshCamera();
    }


    @Override
    public void onShutter() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    public File createDirectorImageFile() throws IOException {

        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File folder = new File(pictureFolder,"Presto");
        if (!folder.exists()) {

            folder.mkdirs();
        }
        //create file name
        String timeStamp = Common.convertDatetoString(Constant.dayFormat_seven,new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File file = new File(new File(folder.getPath()), imageFileName);

        if (file.exists()) {
            file.delete();
        }
        return file;
    }

//    private void createFileBitmap(Bitmap bitmap){
//        try {
////            String folderPath = mBitmapPath+ "/tessdata/";
////
////            //Create Folder
////            File folder = new File(folderPath);
////
////            if (!folder.exists()) {
////                folder.mkdirs();
////
////            }
////
////            //Create file to write Bitmap
////            mFileBitmapScan = new File(new File(folderPath),"eng.traineddata");
////
////            if (mFileBitmapScan.exists()) {
////               mFileBitmapScan.delete();
////            }
//
//            File pictureFile = null;
//            try {
//                pictureFile =createDirectorImageFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if(pictureFile == null)
//                return;
//
//            //Convert bitmap to byte array
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);   //file with PNG
//            byte[] bitmapdata = bos.toByteArray();
//
//            //write the byte in file
//            FileOutputStream fos = new FileOutputStream(pictureFile);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//        }catch (IOException e){
//            //Log.e(Constant.TAG,"Error create file Bitmap: "+e.printStackTrace());
//            e.printStackTrace();
//        }
//    }
}
