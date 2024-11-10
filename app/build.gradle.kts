import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.android.gms.oss-licenses-plugin")
    id("kotlin-kapt")
}

kotlin {
    jvmToolchain(17)
}


android {
    namespace = "org.map_bd.surveycalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.map_bd.surveycalculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        archivesName ="surveycalculator-v${versionName}-${versionCode}"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }



//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
        compose = true
    }
    androidResources {
        generateLocaleConfig = false
    }
//    testOptions {
//        managedDevices {
//            devices {
//                maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel9api34").apply {
//                    device = "Pixel 9"
//                    apiLevel = 34
//                    systemImageSource = "aosp"
//                }
//            }
//        }
//    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        warning += "MissingTranslation"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("com.google.android.gms:play-services-oss-licenses:17.1.0")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    //androidTestImplementation(libs.screengrab)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    
    implementation("androidx.compose.material3:material3")
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.0")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))

    implementation ("com.airbnb.android:lottie:6.5.2")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.3")


    //implementation("com.itextpdf:itextg:5.5.10")

    //implementation ("com.uttampanchasara.pdfgenerator:pdfgenerator:1.3")



}