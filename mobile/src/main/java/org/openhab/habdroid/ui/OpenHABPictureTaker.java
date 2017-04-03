package org.openhab.habdroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.openhab.habdroid.R;
import org.openhab.habdroid.model.OpenHABBinding;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Darty on 02/04/2017.
 */

public class OpenHABPictureTaker extends ListFragment {
    private static final String TAG = OpenHABBindingFragment.class.getSimpleName();

    private static final String ARG_USERNAME = "openHABUsername";
    private static final String ARG_PASSWORD = "openHABPassword";
    private static final String ARG_BASEURL = "openHABBaseUrl";

    private String openHABUsername = "";
    private String openHABPassword = "";
    private String openHABBaseUrl = "";

    private OpenHABMainActivity mActivity;
    // loopj
    private AsyncHttpClient mAsyncHttpClient;
    // keeps track of current request to cancel it in onPause
    private RequestHandle mRequestHandle;



    public static OpenHABPictureTaker newInstance(String baseUrl, String username, String password) {
        OpenHABPictureTaker fragment = new OpenHABPictureTaker();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        args.putString(ARG_BASEURL, baseUrl);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OpenHABPictureTaker() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        if (getArguments() != null) {
            openHABUsername = getArguments().getString(ARG_USERNAME);
            openHABPassword = getArguments().getString(ARG_PASSWORD);
            openHABBaseUrl = getArguments().getString(ARG_BASEURL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView");
        Log.d(TAG, "isAdded = " + isAdded());
        View view = inflater.inflate(R.layout.openhabbindinglist_fragment, container, false);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");
        try {
            mActivity = (OpenHABMainActivity) activity;
            mAsyncHttpClient = mActivity.getAsyncHttpClient();
            mActivity.setTitle(R.string.app_bindings);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must be OpenHABMainActivity");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated()");
        Log.d(TAG, "isAdded = " + isAdded());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        // Cancel request for notifications if there was any
        if (mRequestHandle != null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mRequestHandle.cancel(true);
                }
            });
            thread.start();
        }
    }


    public void onRefresh() {
        Log.d(TAG, "onRefresh()");
        refresh();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach()");
        mActivity = null;
    }

    public void refresh() {
        Log.d(TAG, "refresh()");
    }




    private void stopProgressIndicator() {
        if (mActivity != null) {
            Log.d(TAG, "Stop progress indicator");
            mActivity.setProgressIndicatorVisible(false);
        }
    }

    private void startProgressIndicator() {
        if (mActivity != null) {
            Log.d(TAG, "Start progress indicator");
            mActivity.setProgressIndicatorVisible(true);
        }
    }

}
