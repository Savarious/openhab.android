/*
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.habdroid.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.habdroid.model.thing.ThingType;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by savariousbig
 * This class represents an openHAB2 picture
 */

public class OpenHABPicture {

    Bitmap bitmap;
    int imageHeight;
    int imageWidth;
    String imageType;


    public OpenHABPicture(byte[] Image) {
       if (Image != null) {
           /*ByteArrayInputStream imageStream = new ByteArrayInputStream(Images);
           Bitmap bitmap = BitmapFactory.decodeStream(imageStream);*/
           BitmapFactory.Options options = new BitmapFactory.Options();
           options.inJustDecodeBounds = false;
           bitmap = BitmapFactory.decodeByteArray(Image, 0,Image.length, options);
           imageHeight = options.outHeight;
           imageWidth = options.outWidth;
           imageType = options.outMimeType;
         //  bitmap = BitmapFactory.decodeByteArray(Image, 0, options);
       }
    }

    public Bitmap getPicture() {
        return bitmap;
    }
    public int getImageHeight() {return imageHeight;}
    public int getImageWidth() {return imageWidth;}
    public String getImageType() {return imageType;}
}
