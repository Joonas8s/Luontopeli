# proguard-rules.pro
# 📁 app/proguard-rules.pro

# Room – säilytä entiteetit ja DAO:t (Room käyttää reflektiota)
-keep class com.example.luontopeli.data.local.** { *; }

# Firebase – ei minifioida Firebase SDK:ta
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# ML Kit – säilytä tunnistusmallit
-keep class com.google.mlkit.** { *; }

# Säilytä annotaatiot (Room, Hilt, Firebase tarvitsevat)
-keepattributes Signature
-keepattributes *Annotation*