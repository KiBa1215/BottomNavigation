package com.kiba.bottomnavigation;

import android.content.Context;

/**
 * Created by KiBa-PC on 2016/6/22.
 */
public class Utils {

    public static int dip2px(Context context, float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
