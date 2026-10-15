# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard

# kotlinx.serialization: keep the generated serializers for the type-safe navigation routes
# so R8 does not strip them in release builds.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.priyanshusingh.campuseventexplorer.**$$serializer { *; }
-keepclassmembers class com.priyanshusingh.campuseventexplorer.** {
    *** Companion;
}
-keepclasseswithmembers class com.priyanshusingh.campuseventexplorer.** {
    kotlinx.serialization.KSerializer serializer(...);
}
