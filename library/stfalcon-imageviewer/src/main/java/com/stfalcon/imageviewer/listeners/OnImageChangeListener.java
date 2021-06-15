package com.stfalcon.imageviewer.listeners;

/**
 * Interface definition for a callback to be invoked when current image position was changed.
 */
//N.B.! This class is written in Java for convenient use of lambdas due to languages compatibility issues.
public interface OnImageChangeListener {

    void onImageChange(int position);
}