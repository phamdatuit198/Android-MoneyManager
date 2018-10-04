package com.inc.apex.moneymanager.Camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.inc.apex.moneymanager.Unit.Constant;


/**
 * Created by Dat.Pham on 9/29/2017.
 */

public class TessEngine {

    private Context context;

    private TessEngine(Context context){
        this.context = context;
    }

    public static TessEngine Gnerate(Context context){
        return new TessEngine(context);
    }

    public String detectText(Bitmap bitmap){

        TessDataManager.initTessTrainedData(context);
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        String path = TessDataManager.getTesseractFolder();
        Log.d(Constant.TAG, "Tess folder: " + path);
        tessBaseAPI.setDebug(true);
        String language = "eng";

        tessBaseAPI.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        tessBaseAPI.setImage(bitmap);
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,"1234567890");
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST,"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz");
        tessBaseAPI.init(path,language);

        String recognizedText = tessBaseAPI.getUTF8Text();
        recognizedText = recognizedText.replaceAll("[^\\.0-9]", "");//remove space
        tessBaseAPI.end();
        System.gc();
        return recognizedText;
    }

}
