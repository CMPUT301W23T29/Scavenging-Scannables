package com.example.scavengingscannables;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRCodeImageLocationInfoTest {
    Context appContext;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.scavengingscannables", appContext.getPackageName());

        GeoPoint gp = new GeoPoint(0, 0);
        Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.vector_camera);
        QRCodeImageLocationInfo qrCodeImageLocationInfo = new QRCodeImageLocationInfo(bitmap, gp, false, false);

        assertEquals(bitmap, qrCodeImageLocationInfo.convertBase64ToImage());
    }
}
