plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.chathub"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chathub"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Dependencias de Firebase
    implementation("com.google.firebase:firebase-auth:21.0.1") // Para autenticación de Firebase
    implementation("com.google.firebase:firebase-database:20.0.3") // Para la base de datos de Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.0.1")) // Para gestionar versiones de Firebase

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.4.1") // Para la autenticación con Google

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))

    // Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Dependencias comunes de la UI
    implementation("androidx.appcompat:appcompat:1.3.1") // Para compatibilidad con versiones anteriores
    implementation("com.google.android.material:material:1.4.0") // Para los componentes de UI de Google
    implementation("androidx.constraintlayout:constraintlayout:2.0.4") // Para la disposición con ConstraintLayout
    implementation("androidx.activity:activity:1.3.1") // Para la actividad de la aplicación

    // Dependencias de prueba
    testImplementation("junit:junit:4.13.2") // Dependencia de JUnit para pruebas unitarias
    androidTestImplementation("androidx.test.ext:junit:1.1.3") // Dependencia de JUnit para pruebas de interfaz de usuario
    androidTestImplementation("androidx.espresso:espresso-core:3.4.0") // Dependencia para pruebas de UI con Espresso

    implementation("com.google.android.material:material:1.5.0")
}
