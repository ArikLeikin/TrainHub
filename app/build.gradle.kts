import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.trainhub"
    compileSdk = 34
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.trainhub"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val localPropertiesFile = File(project.rootProject.file("local.properties").path)
        FileInputStream(localPropertiesFile).use { properties.load(it) }
        val googleApiKey = properties.getProperty("GOOGLE_API_KEY")
        val exercisesApiKey = properties.getProperty("EXERCISES_API_KEY")
        buildConfigField("String", "GOOGLE_API_KEY", "\"$googleApiKey\"")
        buildConfigField("String", "EXERCISES_API_KEY", "\"$exercisesApiKey\"")
    }

    buildTypes {
        release {
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.media3.ui)
    val room_version = "2.6.1"
    val lifecycle_version = "2.8.1"
    val mediaVersion = "1.3.1"
    implementation ("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation( "androidx.room:room-ktx:$room_version")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-common-ktx:21.0.0")
    implementation(libs.androidx.navigation.fragment)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.google.code.gson:gson:2.8.8")

    implementation ("androidx.media3:media3-exoplayer:$mediaVersion")
    implementation ("androidx.media3:media3-ui:$mediaVersion")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation( "com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    //kapt( com.github.bumptech.glide:compiler:4.12.0")

}