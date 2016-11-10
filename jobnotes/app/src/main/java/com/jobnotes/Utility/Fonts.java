package com.jobnotes.Utility;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {

    public static Typeface getRegularFont(Context context) {
        Typeface myFace = Typeface.createFromAsset(context.getAssets(),
                "Raleway-Regular.ttf");
        return myFace;
    }

    public static Typeface getBoldFont(Context context) {
        Typeface myFace = Typeface.createFromAsset(context.getAssets(),
                "Raleway-Bold.ttf");
        return myFace;
    }

    public static Typeface getMediumFont(Context context) {
        Typeface myFace = Typeface.createFromAsset(context.getAssets(),
                "Raleway-Medium.ttf");
        return myFace;
    }

}
