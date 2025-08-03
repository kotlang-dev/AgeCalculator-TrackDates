# This rule tells ProGuard to keep the main entry point of your application.
-keep public class com.synac.agecalculator.MainKt {
    public static void main(java.lang.String[]);
}

# --- Suppress Warnings for Missing or Unresolved Classes ---
-dontwarn kotlin.jvm.internal.EnhancedNullability
-dontwarn kotlin.concurrent.atomics.**
-dontwarn okio.**
-dontwarn java.lang.Thread$Builder**


# --- Rules for Application Code ---
# It tells ProGuard to keep all of our application's own classes.
-keep class com.synac.agecalculator.** { *; }


# --- Rules for Koin (Dependency Injection) ---
# These rules are still good practice to have.
-keep class org.koin.** { *; }


# --- Rules for Room (Database) ---
# This prevents ProGuard from removing the classes that Room generates.
-keep class androidx.room.** { *; }
-keepclassmembers class **.database.*_Impl { *; }
-keepclassmembers class **.dao.*_Impl { *; }


# --- Rules for Kotlinx Serialization ---
-keep @kotlinx.serialization.Serializable class * { *; }
-keep class **$$serializer { *; }
-keep class kotlinx.serialization.internal.* { *; }


# --- Rules for Kotlinx Coroutines ---
-keep class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }


# --- General Rules for Kotlin ---
-keep class kotlin.reflect.jvm.internal.** { *; }
-dontwarn kotlin.reflect.jvm.internal.**
-keep class kotlin.Metadata
-keep class kotlin.coroutines.jvm.internal.DebugMetadata { *; }


# --- Rule for Duplicate Definitions ---
-dontwarn **.module-info

