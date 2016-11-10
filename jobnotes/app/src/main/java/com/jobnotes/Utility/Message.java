package com.jobnotes.Utility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lipl-android on 7/6/2016.
 */
public class Message {
    public static void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    /*public static String toString(String[] data){
        String string = "";
        for(int i = 0;i<data.length;i++){
            string = string+data[i]+"\n";
        }
        return string;
    }*/
}
