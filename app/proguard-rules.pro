# Proguard rules for PocketBrain

# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.hilt.InstallIn class *

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.**

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Keep Kotlin data classes
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.pocketbrain.data.db.entities.** { *; }
-keep class com.pocketbrain.domain.model.** { *; }
