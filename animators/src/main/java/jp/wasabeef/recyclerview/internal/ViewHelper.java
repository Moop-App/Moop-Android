package jp.wasabeef.recyclerview.internal;

import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * Copyright (C) 2018 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public final class ViewHelper {

  public static void clear(View v) {
    v.setAlpha(1f);
    v.setScaleX(1f);
    v.setScaleX(1);
    v.setTranslationY(0);
    v.setTranslationX(0);
    v.setRotation(0);
    v.setRotationY(0);
    v.setRotationX(0);
    v.setPivotY(v.getMeasuredHeight() / 2);
    v.setPivotX(v.getMeasuredWidth() / 2);
    ViewCompat.animate(v).setInterpolator(null).setStartDelay(0);
  }
}
