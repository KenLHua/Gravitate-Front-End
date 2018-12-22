package com.example.ken.gravitate.Utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APIRequestSingleton extends Application {
    private static APIRequestSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private APIRequestSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized APIRequestSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new APIRequestSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    /* Cancel all the requests matching with the given tag */
    public void cancelAllRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }
}
