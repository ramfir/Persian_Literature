# ================================================================================================
# Persian Literature App - ProGuard Configuration for Release Builds
# ================================================================================================
# This file contains comprehensive rules for R8/ProGuard to safely minify and obfuscate the app
# while preserving functionality for all dependencies and multi-module architecture.
# ================================================================================================

# ================================================================================================
# DEBUGGING & CRASH REPORTS
# ================================================================================================
# Keep source file names and line numbers for readable crash reports in Play Console
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations for better debugging
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# ================================================================================================
# KOTLIN
# ================================================================================================
# Keep Kotlin metadata for reflection
-keep class kotlin.Metadata { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.** {
    volatile <fields>;
}

# Keep Kotlin intrinsics
-keep class kotlin.jvm.internal.** { *; }

# ================================================================================================
# JETPACK COMPOSE
# ================================================================================================
# Keep @Stable and @Immutable annotated classes for Compose stability
-keep @androidx.compose.runtime.Stable class * { *; }
-keep @androidx.compose.runtime.Immutable class * { *; }

# Keep Composable functions
-keep class androidx.compose.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep Compose compiler
-dontwarn androidx.compose.compiler.**

# ================================================================================================
# DATA CLASSES & MODELS (Multi-Module Architecture)
# ================================================================================================
# Keep all data classes used for Firebase, Room, and state management across all modules

# Author API module models
-keep class com.firdavs.persianliterature.author_api.model.** { *; }
-keepclassmembers class com.firdavs.persianliterature.author_api.model.** { *; }

# Core module models (shared across app)
-keep class com.firdavs.persianliterature.core.model.** { *; }
-keepclassmembers class com.firdavs.persianliterature.core.model.** { *; }

# Quiz API module models
-keep class com.firdavs.persianliterature.quiz_api.model.** { *; }
-keepclassmembers class com.firdavs.persianliterature.quiz_api.model.** { *; }

# Audio API module models
-keep class com.firdavs.persianliterature.audio_api.model.** { *; }
-keepclassmembers class com.firdavs.persianliterature.audio_api.model.** { *; }

# Settings API module models
-keep class com.firdavs.persianliterature.settings_api.model.** { *; }
-keepclassmembers class com.firdavs.persianliterature.settings_api.model.** { *; }

# Keep all data classes (generic rule for any module)
-keep @kotlin.Metadata class **.*$data$* { *; }
-keepclassmembers class * {
    public <init>(...);
}

# ================================================================================================
# ROOM DATABASE
# ================================================================================================
# Keep Room entities
-keep @androidx.room.Entity class * { *; }
-keepclassmembers @androidx.room.Entity class * { *; }

# Keep Room DAOs
-keep @androidx.room.Dao interface * { *; }
-keep @androidx.room.Dao class * { *; }

# Keep Room Database classes
-keep @androidx.room.Database class * { *; }

# Keep Room type converters
-keep @androidx.room.TypeConverter class * { *; }
-keepclassmembers class * {
    @androidx.room.TypeConverter <methods>;
}

# Keep Room query methods
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public <methods>;
}

# ================================================================================================
# FIREBASE FIRESTORE
# ================================================================================================
# Keep Firestore SDK classes
-keep class com.google.firebase.firestore.** { *; }
-dontwarn com.google.firebase.**

# CRITICAL: Keep all DTO classes completely (used for Firebase deserialization)
# These classes are deserialized from Firestore documents using reflection
-keep class **.*DTO { *; }
-keepclassmembers class **.*DTO { *; }

# Keep all classes in model packages (includes DTOs and entities)
-keep class com.firdavs.persianliterature.**.model.** { *; }
-keepclassmembers class com.firdavs.persianliterature.**.model.** { *; }

# Keep all members annotated with @PropertyName
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName *;
}

# Keep default constructors for Firestore (required for deserialization)
-keepclassmembers class * {
    public <init>();
}

# Keep all getters and setters in model classes
-keepclassmembers class com.firdavs.persianliterature.**.model.** {
    public *** get*();
    public void set*(***);
}

# ================================================================================================
# KOTLINX SERIALIZATION
# ================================================================================================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**

-keep,includedescriptorclasses class com.firdavs.persianliterature.**$$serializer { *; }
-keepclassmembers class com.firdavs.persianliterature.** {
    *** Companion;
}
-keepclasseswithmembers class com.firdavs.persianliterature.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ================================================================================================
# KOIN DEPENDENCY INJECTION
# ================================================================================================
# Keep Koin classes
-keep class org.koin.** { *; }
-keep class org.koin.core.** { *; }
-keep class org.koin.androidx.** { *; }

# Keep module definitions
-keepclassmembers class * {
    public <init>(...);
}

# Keep ViewModels for Koin injection
-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# ================================================================================================
# NAVIGATION3 LIBRARY
# ================================================================================================
# Keep Navigation3 classes (custom navigation library)
-keep class io.github.tabilzad.entripoint.** { *; }
-dontwarn io.github.tabilzad.entripoint.**

# Keep route classes
-keep class com.firdavs.persianliterature.navigation.** { *; }

# ================================================================================================
# MEDIA3 EXOPLAYER
# ================================================================================================
# Keep ExoPlayer classes
-keep class androidx.media3.** { *; }
-keep interface androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Keep ExoPlayer renderer classes
-keep class * extends androidx.media3.exoplayer.Renderer {
    public <init>(...);
}

# ================================================================================================
# WORKMANAGER & ALARMMANAGER
# ================================================================================================
# Keep WorkManager classes
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker {
    public <init>(...);
}

# Keep AlarmManager receiver classes
-keep class * extends android.content.BroadcastReceiver {
    public <init>(...);
}

# ================================================================================================
# ANDROIDX & ANDROID SYSTEM
# ================================================================================================
# Keep AppCompat
-keep class androidx.appcompat.** { *; }

# Keep Activity and Fragment
-keep class * extends android.app.Activity
-keep class * extends androidx.fragment.app.Fragment
-keep class * extends android.app.Application

# Keep Parcelables
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ================================================================================================
# LOGGING - REMOVE DEBUG LOGS FOR RELEASE
# ================================================================================================
# Remove all Log.d, Log.v, Log.i calls to reduce APK size and improve security
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}

# Keep Log.w and Log.e for production error tracking
-keep class android.util.Log {
    public static int w(...);
    public static int e(...);
}

# ================================================================================================
# GSON (if used)
# ================================================================================================
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ================================================================================================
# GENERAL RULES
# ================================================================================================
# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep service and receiver classes
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep custom views
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# ================================================================================================
# OPTIMIZATION FLAGS
# ================================================================================================
# Optimize code (R8 default optimizations are generally safe)
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# ================================================================================================
# WARNINGS TO IGNORE
# ================================================================================================
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn javax.annotation.**
-dontwarn okhttp3.**
-dontwarn okio.**

# ================================================================================================
# END OF PROGUARD CONFIGURATION
# ================================================================================================