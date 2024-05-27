import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.spotless)
}

android {
  namespace = "dev.dot.files"
  compileSdk = libs.versions.targetSdk.get().toInt()
  buildToolsVersion = "35.0.0 rc3"

  defaultConfig {
    applicationId = "dev.dot.files"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 1
    versionName = "0.1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables { useSupportLibrary = true }
  }

  signingConfigs {
    create("release") {
      val properties = Properties().apply { load(file("${rootDir}/local.properties").reader()) }
      storeFile = file(properties.getProperty("store"))
      storePassword = properties.getProperty("password")
      keyAlias = properties.getProperty("alias")
      keyPassword = properties.getProperty("keyPassword")
    }
  }

  buildTypes {
    debug {
      isDebuggable = true
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
          getDefaultProguardFile("proguard-android-optimize.txt"),
          "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("release")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = JavaVersion.VERSION_17.toString() }
  buildFeatures {
    buildConfig = true
    compose = true
  }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

spotless {
  kotlin {
    target("**/*.kt")
    ktlint("1.2.1")
        .customRuleSets(listOf("io.nlopez.compose.rules:ktlint:0.4.2"))
        .editorConfigOverride(
            mapOf("ktlint_function_naming_ignore_when_annotated_with" to "Composable"))
    ktfmt("0.49").dropboxStyle()
  }
  kotlinGradle {
    target("**/*.gradle.kts")
    ktfmt("0.49")
  }
}

composeCompiler { enableStrongSkippingMode = true }

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.datastore)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.workmanager)

  implementation(libs.compose.material.icons)
  implementation(libs.compose.material3)
  implementation(libs.compose.material3.window.sizeclass)
  implementation(libs.compose.runtime.livedata)
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.graphics)
  implementation(libs.compose.ui.tooling.preview)

  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlinx.immutable.collections)
  implementation(libs.kotlinx.serialization)

  implementation(libs.coil)
  implementation(libs.destinations)
  ksp(libs.destinations.compiler)
  implementation(libs.timber)

  testImplementation(libs.junit)
  testImplementation(libs.truth)

  androidTestImplementation(libs.androidx.espresso)
  androidTestImplementation(libs.androidx.test)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.test.truth)
  androidTestImplementation(libs.androidx.test.uiautomator)
  androidTestImplementation(libs.androidx.workmanager.testing)
  androidTestImplementation(libs.compose.ui.test.junit4)

  debugImplementation(libs.compose.ui.tooling)
  debugImplementation(libs.compose.ui.test.manifest)
}
