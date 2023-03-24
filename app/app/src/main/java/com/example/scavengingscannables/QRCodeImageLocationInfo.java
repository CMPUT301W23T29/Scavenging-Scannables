package com.example.scavengingscannables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;

/**
 * Class that stores the location info and base64 representation of images taken of qrcodes. Also includes Boolean fields to control whether or not the location/image is valid (privacy concerns)
 */
public class QRCodeImageLocationInfo {
    private String imageBase64;
    private GeoPoint imageLocation;
    private Boolean isImagePrivate;
    private Boolean isLocationPrivate;

    public QRCodeImageLocationInfo(Bitmap image, GeoPoint imageLocation, Boolean isImagePrivate, Boolean isLocationPrivate) {
        this.imageBase64 = convertImageToBase64(image);
        this.imageLocation = imageLocation;
        this.isImagePrivate = isImagePrivate;
        this.isLocationPrivate = isLocationPrivate;
    }

    public QRCodeImageLocationInfo(){}

    /**
     * Converts a Bitmap to base64
     * @param image image to get converted to base64 string
     * @return returns the base64 representation of image
     */
    private String convertImageToBase64(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte [] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * Returns the Bitmap from base64
     * @return returns bitmap of base64 image
     */
    // don't change this method to getImage, will give an error because firestore tries to serialize a bitmap
    public Bitmap convertBase64ToImage(){
        byte [] encodeByte = Base64.decode(this.imageBase64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public GeoPoint getImageLocation() {
        return imageLocation;
    }

    public Boolean getImagePrivate() {
        return isImagePrivate;
    }

    public Boolean getLocationPrivate() {
        return isLocationPrivate;
    }
}
