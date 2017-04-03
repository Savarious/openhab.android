package org.openhab.habdroid.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;

import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import org.openhab.habdroid.ui.PictureTaker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.software.shell.fab.ActionButton;

import org.openhab.habdroid.R;

import org.openhab.habdroid.model.OpenHABPicture;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import android.widget.ImageView;

import cz.msebera.android.httpclient.Header;

public class OpenHABPicturesFragment extends  ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = OpenHABPicturesFragment.class.getSimpleName();

    private static final String ARG_USERNAME = "openHABUsername";
    private static final String ARG_PASSWORD = "openHABPassword";
    private static final String ARG_BASEURL = "openHABBaseUrl";

    private String openHABUsername = "";
    private String openHABPassword = "";
    private String openHABBaseUrl = "";

    private static final int CAMERA_REQUEST = 1888;
    private OpenHABMainActivity mActivity;
    // loopj
    private AsyncHttpClient mAsyncHttpClient;
    // keeps track of current request to cancel it in onPause
    private RequestHandle mRequestHandle;

    private OpenHABPicturesAdapter mPicturesAdapter;
    private ArrayList<OpenHABPicture> mPictures;


    private SwipeRefreshLayout mSwipeLayout;


    private ActionButton addPictureButton;
    private ImageView imageView;

    public static OpenHABPicturesFragment newInstance(String baseUrl, String username, String password) {
        OpenHABPicturesFragment fragment = new OpenHABPicturesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        args.putString(ARG_BASEURL, baseUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public OpenHABPicturesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        mPictures = new ArrayList<OpenHABPicture>();
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

        View view = inflater.inflate(R.layout.openhabpictureslist_fragment, container, false);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        addPictureButton = (ActionButton) view.findViewById(R.id.addPicture_button);
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PictureTaker.class);
                startActivity(intent);
            }

        });

        return view;
    }



   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*Log.d(TAG, "JDFKBGNDFKJIBOGLDFGHBNJ TIRAN TIRAN DULE ");
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "JDFKBGNDFKJIBOGLDFGHBNJ");
            byte[] photo =  data.getExtras().getByteArray("data");
            AsyncHttpClient client = new AsyncHttpClient();
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("name", "test");
            RequestParams params = new RequestParams(param);
            Log.d(TAG, "TEST POST");
            mRequestHandle = mAsyncHttpClient.post(openHABBaseUrl + "rest/pictures", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(TAG, "Picture sent");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d(TAG, "Failed sending picture");
                }
            });

        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");
        try {
            mActivity = (OpenHABMainActivity) activity;
            mAsyncHttpClient = mActivity.getAsyncHttpClient();
            mActivity.setTitle(R.string.app_pictures);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must be OpenHABMainActivity");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPicturesAdapter = new OpenHABPicturesAdapter(this.getActivity(), R.layout.openhabpictureslist_item , mPictures);
        getListView().setAdapter(mPicturesAdapter);
        Log.d(TAG, "onActivityCreated()");
        Log.d(TAG, "isAdded = " + isAdded());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        Log.d(TAG, "isAdded = " + isAdded());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        loadListPictures();
    }

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

    @Override
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
        loadListPictures();
    }


   /* @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
        if (mPictures.get(position).getThingTypes() == null) {
            Log.d(TAG, "Source thing types == null");
            return;
        }
        if (mActivity != null) {
            mActivity.openPicturesThingTypes(mPictures.get(position).getThingTypes());
        }
    }*/



    private void loadListPictures() {
        mPictures.clear();
        if (mAsyncHttpClient != null) {
            startProgressIndicator();
            mRequestHandle = mAsyncHttpClient.get(openHABBaseUrl + "rest/pictures", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    stopProgressIndicator();
                    Log.d(TAG, "Picture list request success");
                    String list_pictures = new String(responseBody);
                    Log.d(TAG, "Pictures list : " + list_pictures);
                    String[] separated = list_pictures.split("\n");
                    for (String name_pic: separated)
                    {
                        Log.d(TAG, "Picture name : " + name_pic);
                        loadPicture(name_pic);
                    }
                    //OpenHABPicture picture = new OpenHABPicture(responseBody);
                    //mPictures.add(picture);


                    mPicturesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    stopProgressIndicator();
                    Log.d(TAG, "Pictures request failure: " + statusCode);
                }
            });
        }
    }

    private void loadPicture(String name)
    {
        if (mAsyncHttpClient != null) {
            startProgressIndicator();
            mRequestHandle = mAsyncHttpClient.get(openHABBaseUrl + "rest/pictures/" + name , new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    stopProgressIndicator();
                    Log.d(TAG, "Picture request success");
                    OpenHABPicture picture = new OpenHABPicture(responseBody);
                    mPictures.add(picture);
                    mPicturesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    stopProgressIndicator();
                    Log.d(TAG, "Picture request failure: " + statusCode);
                }
            });
        }
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
        mSwipeLayout.setRefreshing(false);
    }


}
