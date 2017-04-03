/*
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.habdroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import org.openhab.habdroid.R;
import org.openhab.habdroid.model.OpenHABBinding;
import org.openhab.habdroid.model.OpenHABPicture;
import org.openhab.habdroid.util.MySmartImageView;

import java.util.ArrayList;

public class OpenHABPicturesAdapter extends ArrayAdapter<OpenHABPicture> {
    private int mResource;
    private String mOpenHABUsername;
    private String mOpenHABPassword;
    private String mOpenHABBaseUrl;



    public OpenHABPicturesAdapter(Context context, int resource, ArrayList<OpenHABPicture> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpenHABPicture picture = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.pictureImage);
        imageView.setImageBitmap(picture.getPicture());
     return convertView;
    }


    public String getOpenHABUsername() {
        return mOpenHABUsername;
    }

    public void setOpenHABUsername(String openHABUsername) {
        this.mOpenHABUsername = openHABUsername;
    }

    public String getOpenHABPassword() {
        return mOpenHABPassword;
    }

    public void setOpenHABPassword(String openHABPassword) {
        this.mOpenHABPassword = openHABPassword;
    }


}
