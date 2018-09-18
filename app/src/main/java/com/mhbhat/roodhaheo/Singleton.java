package com.mhbhat.roodhaheo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mahmo on 11-09-2018.
 */

public class Singleton {
    private static Singleton mInstance;
    private RequestQueue requestQueue;
    private static Context mcontext;

    private Singleton(Context context){
        mcontext = context;
        requestQueue = getRequestQueue();
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized Singleton getInstance(Context context){
        if (mInstance == null){
            mInstance = new Singleton(context);
        }
        return mInstance;
    }
    public void addToRequestQueue(Request request){
        requestQueue.add(request);
    }
}