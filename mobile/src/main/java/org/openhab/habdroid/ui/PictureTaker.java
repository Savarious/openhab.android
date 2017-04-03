package org.openhab.habdroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.openhab.habdroid.R;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PictureTaker extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private RequestHandle mRequestHandle;
    private AsyncHttpClient mAsyncHttpClient;

    private static final String TAG = OpenHABPicturesFragment.class.getSimpleName();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picturetaker);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "JDFKBGNDFKJIBOGLDFGHBNJ");
            if( requestCode == 1888 ) {
                Log.d(TAG, "JDFKBGNDFKJIBOGLDFGHBNJ");
                byte[] photo =  data.getExtras().getByteArray("data");
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("name", "test");
                Log.d(TAG, "TEST POST");

                /*mRequestHandle = mAsyncHttpClient.post(getApplicationContext(),"https://myopenhab.org/rest/pictures", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(TAG, "Picture sent");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d(TAG, "Failed sending picture");
                    }
                });*/
                postData();
            }
        }
    }

    public void postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://myopenhab.org/rest/pictures");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", "test"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
}
