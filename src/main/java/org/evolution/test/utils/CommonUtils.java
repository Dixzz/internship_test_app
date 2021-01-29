package org.evolution.test.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {
    public static final float ONE_F = 1f;
    public static final float ZERO_F = 0f;
    public static final float HALF_F = 0.5f;
    public static final long VIEW_ANIM_DUR = 1000;
    public static final float VIEW_TRANS_Y = 50f;

    public static boolean hasNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
