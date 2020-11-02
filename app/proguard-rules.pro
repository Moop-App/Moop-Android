# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# AndroidX + support library
-dontwarn android.support.**
-dontwarn androidx.**

# Kakao SDK
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Dagger2
-dontwarn com.google.errorprone.annotations.*

# For kotlin-reflect
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }

# For dynamic-features module
-keep class soup.movie.theatermap.** { *; }

# For kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class soup.movie.**$$serializer { *; }
-keepclassmembers class soup.movie.** {
    *** Companion;
}
-keepclasseswithmembers class soup.movie.** {
    kotlinx.serialization.KSerializer serializer(...);
}
