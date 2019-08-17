/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Build;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class to handle icon treatments (e.g., shadow generation) for the Launcher icons.
 */
public class LauncherIcons {

    // Percent of actual icon size
    private static final float ICON_SIZE_BLUR_FACTOR = 0.5f/48;
    // Percent of actual icon size
    private static final float ICON_SIZE_KEY_SHADOW_DELTA_FACTOR = 1f/48;

    private static final int KEY_SHADOW_ALPHA = 61;
    private static final int AMBIENT_SHADOW_ALPHA = 30;

    private final SparseArray<Bitmap> mShadowCache = new SparseArray<>();
    private final int mIconSize;
    private final Resources mRes;

    public LauncherIcons(Context context) {
        mRes = context.getResources();
        mIconSize = mRes.getDimensionPixelSize(android.R.dimen.app_icon_size);
    }

    @Nullable
    public Drawable getAppIcon(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return getShadowedIcon(pm.getApplicationIcon(appInfo));
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public Drawable getShadowedIcon(Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return drawable;
        }
        if (!(drawable instanceof AdaptiveIconDrawable)) {
            return drawable;
        }
        Bitmap shadow = getShadowBitmap((AdaptiveIconDrawable) drawable);
        return new ShadowDrawable(shadow, drawable);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getShadowBitmap(AdaptiveIconDrawable d) {
        int shadowSize = Math.max(mIconSize, d.getIntrinsicHeight());
        synchronized (mShadowCache) {
            Bitmap shadow = mShadowCache.get(shadowSize);
            if (shadow != null) {
                return shadow;
            }
        }

        d.setBounds(0, 0, shadowSize, shadowSize);

        float blur = ICON_SIZE_BLUR_FACTOR * shadowSize;
        float keyShadowDistance = ICON_SIZE_KEY_SHADOW_DELTA_FACTOR * shadowSize;

        int bitmapSize = (int) (shadowSize + 2 * blur + keyShadowDistance);
        Bitmap shadow = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(shadow);
        canvas.translate(blur + keyShadowDistance / 2, blur);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.TRANSPARENT);

        // Draw ambient shadow
        paint.setShadowLayer(blur, 0, 0, AMBIENT_SHADOW_ALPHA << 24);
        canvas.drawPath(d.getIconMask(), paint);

        // Draw key shadow
        canvas.translate(0, keyShadowDistance);
        paint.setShadowLayer(blur, 0, 0, KEY_SHADOW_ALPHA << 24);
        canvas.drawPath(d.getIconMask(), paint);

        canvas.setBitmap(null);
        synchronized (mShadowCache) {
            mShadowCache.put(shadowSize, shadow);
        }
        return shadow;
    }

    /**
     * A drawable which draws a shadow bitmap behind a drawable
     */
    private static class ShadowDrawable extends DrawableWrapper {

        final MyConstantState mState;

        ShadowDrawable(Bitmap shadow, Drawable dr) {
            super(dr);
            mState = new MyConstantState(shadow, dr.getConstantState());
        }

        ShadowDrawable(MyConstantState state) {
            super(state.mChildState.newDrawable());
            mState = state;
        }

        @Override
        public ConstantState getConstantState() {
            return mState;
        }

        @Override
        public void draw(@NotNull Canvas canvas) {
            Rect bounds = getBounds();
            canvas.drawBitmap(mState.mShadow, null, bounds, mState.mPaint);
            canvas.save();
            // Ratio of child drawable size to shadow bitmap size
            float factor = 1 / (1 + 2 * ICON_SIZE_BLUR_FACTOR + ICON_SIZE_KEY_SHADOW_DELTA_FACTOR);

            canvas.translate(
                    bounds.width() * factor *
                            (ICON_SIZE_BLUR_FACTOR + ICON_SIZE_KEY_SHADOW_DELTA_FACTOR / 2),
                    bounds.height() * factor * ICON_SIZE_BLUR_FACTOR);
            canvas.scale(factor, factor);
            super.draw(canvas);
            canvas.restore();
        }

        private static class MyConstantState extends ConstantState {

            final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
            final Bitmap mShadow;
            final ConstantState mChildState;

            MyConstantState(Bitmap shadow, ConstantState childState) {
                mShadow = shadow;
                mChildState = childState;
            }

            @NotNull
            @Override
            public Drawable newDrawable() {
                return new ShadowDrawable(this);
            }

            @Override
            public int getChangingConfigurations() {
                return mChildState.getChangingConfigurations();
            }
        }
    }
}
