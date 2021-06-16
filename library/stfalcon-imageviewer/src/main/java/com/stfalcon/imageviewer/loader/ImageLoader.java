package com.stfalcon.imageviewer.loader;

import android.widget.ImageView;

/**
 * Interface definition for a callback to be invoked when image should be loaded
 */
//N.B.! This class is written in Java for convenient use of lambdas due to languages compatibility issues.
public interface ImageLoader<T> {

    /**
     * Fires every time when image object should be displayed in a provided {@link ImageView}
     *
     * @param imageView an {@link ImageView} object where the image should be loaded
     * @param image     image data from which image should be loaded
     */
    void loadImage(ImageView imageView, T image);
}
