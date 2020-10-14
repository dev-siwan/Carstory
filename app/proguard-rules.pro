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
#-dontobfuscate

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



## firebase crashlytics
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-renamesourcefileattribute SourceFile

## Retrofit 2.x
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn kotlin.Unit
-dontwarn javax.annotation.**
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-dontwarn retrofit2.Call


# Keep the names of classes/members we need for client functionality.
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

# Needed for Parcelable/SafeParcelable Creators to not get stripped
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

## Retrofit 2.x
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

##Firebase
-dontwarn com.google.firebase.analytics.connector.AnalyticsConnector
# react-native-firebase section
-keep class io.invertase.firebase.** { *; }
-dontwarn io.invertase.firebase.**
# end react-native-firebase section

## okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontnote okhttp3.**

# Preference
-keep public class * extends android.preference.Preference

# For using GSON @Expose annotatio
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
#-keep class kr.co.station3.dabang.pro.data.** { *; }
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}


#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Uncomment for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Rules for Kotlin Coroutines
# https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/kotlinx-coroutines-android/example-app/app/proguard-rules.pro
-keepnames class kotlinx.** { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.atomicfu.** # https://github.com/Kotlin/kotlinx.coroutines/issues/1155
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}



#RecyclerView

-keep public class * extends android.support.v7.widget.RecyclerView.ItemDecoration
-keep class android.support.v7.widget.RecyclerView
-keep class android.support.v7.widget.RecyclerView { *; }

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep
-keep class androidx.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}
-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

#enum class

-keep enum * { *; }


-keepnames class com.like.drive.carstory.R$raw { public static <fields>; }
-keep class **.R$*

-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# Keep custom model classes
-keep class com.google.firebase.example.fireeats.java.model.** { *; }
-keep class com.google.firebase.example.fireeats.kotlin.model.** { *; }

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keep class !com.like.drive.carstory.** { *; }
