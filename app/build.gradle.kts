plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    //kotlin("kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.playlistmaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.playlistmaker"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    viewBinding {
        enable = true
    }
}

dependencies {

//    configurations.all {
//        resolutionStrategy {
//            force("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
//            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.22")
//            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
//            force("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
//            force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//            force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
//            force("androidx.test.ext:junit:1.2.1")
//        }
//    }
    //implementation ("org.jetbrains.kotlinx:kotlinx-uuid:0.9.0")
    //implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    //implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.21")

    implementation("androidx.media3:media3-test-utils:1.7.1")
    val room_version = "2.7.1"
    implementation("androidx.room:room-runtime:$room_version")
    //kapt("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    val nav_version = "2.9.0"
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation ("androidx.fragment:fragment-ktx:1.8.6")
    implementation ("androidx.viewpager2:viewpager2:1.1.0")

    implementation ("io.insert-koin:koin-android:4.0.2")
    implementation ("io.insert-koin:koin-core:4.0.2")
    val lifecycle = "2.9.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle")

    implementation ("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.google.code.gson:gson:2.13.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.github.bumptech.glide:glide:4.16.0") {
        exclude(group = "com.android.support")
    }
    //kapt("com.github.bumptech.glide:compiler:4.16.0")
    //annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")
}