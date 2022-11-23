package com.mauricio.githubcommitviewer;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void makeToast(String msg, Context context){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
