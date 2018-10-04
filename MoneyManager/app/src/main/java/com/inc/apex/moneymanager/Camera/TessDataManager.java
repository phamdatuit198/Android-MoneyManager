package com.inc.apex.moneymanager.Camera;

import android.content.Context;
import android.util.Log;

import com.inc.apex.moneymanager.R;
import com.inc.apex.moneymanager.Unit.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dat.Pham on 9/29/2017.
 */

public class TessDataManager {

    private static final String tessdir = "tesseract";
    private static final String subdir  = "tessdata";
    private static final String filename = "eng.traineddata";

    private static String trainedDataPath;
    private static String tesseractFolder;

    public static String getTesseractFolder(){
        return tesseractFolder;
    }

    private static boolean initiated;

    public static String getTrainedDataPath(){
        if(initiated)
            return trainedDataPath;
        else
            return null;
    }

    public static void initTessTrainedData(Context context){

        if(initiated)
            return;

        File appFolder = context.getFilesDir();
        File folder    = new File(appFolder, tessdir);

        if(!folder.exists())
            folder.mkdirs();
        tesseractFolder = folder.getAbsolutePath();

        File subFolder = new File(folder,subdir);
        if(!subFolder.exists())
            subFolder.mkdirs();

        File file = new File(subFolder,filename);
        trainedDataPath = file.getAbsolutePath();

        if(!file.exists()) {

            try {
                FileOutputStream fileOutputStream;
                byte[] bytes = readRawTrainingData(context);
                if (bytes == null)
                    return;
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
                initiated = true;
                Log.d(Constant.TAG, "Prepared training data file");
            } catch (FileNotFoundException e) {
                Log.e(Constant.TAG, "Error opening training data file\n" + e.getMessage());
            } catch (IOException e) {
                Log.e(Constant.TAG, "Error opening training data file\n" + e.getMessage());
            }
        }
        else{
            initiated = true;
        }

    }


    private static byte[] readRawTrainingData(Context context){

        try {
            InputStream fileInputStream = context.getResources()
                    .openRawResource(R.raw.eng_traineddata);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int bytesRead;

            while (( bytesRead = fileInputStream.read(b))!=-1){
                bos.write(b, 0, bytesRead);
            }

            fileInputStream.close();

            return bos.toByteArray();

        } catch (FileNotFoundException e) {
            Log.e(Constant.TAG, "Error reading raw training data file\n"+e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(Constant.TAG, "Error reading raw training data file\n" + e.getMessage());
        }

        return null;
    }

}
