package com.example.scavengingscannables;

import java.lang.Object;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.img.display.imagej.ImageJFunctions; // not sure why this won't import

public class QrInfo {
    // Will generate a visualizationCode based on the hash of qrID
    public String GenerateVisualCode(String hash) {
        // Each hexa input corresponds to a different element on the visualization, use only first 6 characters
        //visualization code can then be used to generate a unique image using image generator
        String visualizationCode =hash.substring(0,6);
        return visualizationCode;
    }
    public void generateVisual(String visualizationCode) {
        //Will generate an image based on the code given, currently just testing
        final Img<UnsignedByteType> visual = new ArrayImgFactory< UnsignedByteType>().create(new long[] {400,320}, new UnsignedByteType());
        ImageJFunctions.show(visual);
    }
}
