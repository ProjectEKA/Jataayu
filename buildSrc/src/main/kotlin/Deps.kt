
package dependancies

import org.gradle.api.JavaVersion

object Config {
    const val applicationId = "in.projecteka.jataayu"
    const val compileSdkVersion = 29
    const val minSdkVersion = 23
    const val buildToolsVersion = "29.0.2"
    val javaVersion = JavaVersion.VERSION_1_8
}

object LibVersions {
    const val appcompat = "1.1.0"
    const val coreKtx = "1.1.0"
    const val material = "1.2.0-alpha04"
    const val androidX = "1.1.1"
    const val androidXFragmentTesting = "1.1.0"
    const val androidXLifecycle = "2.1.0"
    const val jUnit = "4.12"
    const val espresso = "3.2.0"
    const val jacoco = "0.8.5"
    const val koin = "2.0.1"
    const val kotlin = "1.3.61"
    const val constraintLayout = "1.1.3"
    const val retrofit = "2.6.2"
    const val gradle = "0.21.0"
    const val androidStudio = "3.5.3"
    const val dicemelonJacoco = "0.1.4"
    const val loggingInterceptor = "4.0.0"
    const val mockito = "3.2.0"
    const val coreTesting = "2.1.0"
    const val gson = "2.8.6"
    const val gson_retrofit_converter = "2.7.0"
    const val apacheCommonsIo = "1.3.2"
    const val mockWebServer = "4.2.1"
    const val timber = "4.7.1"
    const val jacocoPalantir = "0.4.0"
    const val eventBus = "3.1.1"
    const val kappuccino = "1.2.1"
    const val preferences = "1.1.0"
    const val rxKotlin = "2.4.0"
    const val rxAndroid = "2.1.1"
}

object Modules {
    const val network = ":commons:network"
    const val utils = ":commons:utils"
    const val presentation = ":commons:presentation"
    const val logger = ":addons:logger"
    const val analytics = ":addons:analytics"
    const val provider = ":features:provider"
    const val consent = ":features:consent"
    const val core = ":features:core"
    const val account = ":user:account"
    const val registration = ":user:registration"
}

object Deps {
    object Koin {
        const val androidXScope = "org.koin:koin-androidx-scope:${LibVersions.koin}"
        const val main = "org.koin:koin-android:${LibVersions.koin}"
        const val androidXViewModel = "org.koin:koin-androidx-viewmodel:${LibVersions.koin}"
        const val test = "org.koin:koin-test:${LibVersions.koin}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:${LibVersions.appcompat}"
        const val material = "com.google.android.material:material:${LibVersions.material}"
        const val coreKtx = "androidx.core:core-ktx:${LibVersions.coreKtx}"
        const val lifecyle = "androidx.lifecycle:lifecycle-extensions:${LibVersions.androidXLifecycle}"
        const val lifecyleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibVersions.androidXLifecycle}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${LibVersions.constraintLayout}"
        const val test = "androidx.test.ext:junit:${LibVersions.androidX}"
        const val fragmentTesting = "androidx.fragment:fragment-testing:${LibVersions.androidXFragmentTesting}"
        const val coreTesting = "androidx.arch.core:core-testing:${LibVersions.coreTesting}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${LibVersions.espresso}"
        const val espressoIntents = "androidx.test.espresso:espresso-intents:${LibVersions.espresso}"
        const val espressoContrib = "androidx.test.espresso:espresso-contrib:${LibVersions.espresso}"
        const val dataBindingCompiler = "androidx.databinding:databinding-compiler:${LibVersions.androidStudio}"
        const val preferences = "androidx.preference:preference-ktx:${LibVersions.preferences}"
    }

    object Retrofit {
        const val main = "com.squareup.retrofit2:retrofit:${LibVersions.retrofit}"
        const val adapter = "com.squareup.retrofit2:adapter-rxjava2:${LibVersions.retrofit}"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${LibVersions.loggingInterceptor}"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:${LibVersions.gson_retrofit_converter}"
    }

    object Testing {
        const val jUnit = "junit:junit:${LibVersions.jUnit}"
        const val mockito = "org.mockito:mockito-core:${LibVersions.mockito}"
        const val mockitoAndroid = "org.mockito:mockito-android:${LibVersions.mockito}"
        const val androidXTest = AndroidX.test
        const val fragmentTesting = AndroidX.fragmentTesting
        const val coreTesting = AndroidX.coreTesting
        const val espressoCore = AndroidX.espressoCore
        const val espressoIntents = AndroidX.espressoIntents
        const val espressoContrib = AndroidX.espressoContrib
        object Helpers {
            const val gson = "com.google.code.gson:gson:${LibVersions.gson}"
            const val apacheCommonsIo = "org.apache.commons:commons-io:${LibVersions.apacheCommonsIo}"
            const val kappuccino = "br.com.concretesolutions:kappuccino:${LibVersions.kappuccino}"
        }
    }

    object MockWebServer {
        const val main = "com.squareup.okhttp3:mockwebserver:${LibVersions.mockWebServer}"
    }

    object Logger {
        const val timber = "com.jakewharton.timber:timber:${LibVersions.timber}"
    }

    object GreenRobot {
        const val eventBus = "org.greenrobot:eventbus:${LibVersions.eventBus}"
    }

    object Rx {
        const val Kotlin = "io.reactivex.rxjava2:rxkotlin:${LibVersions.rxKotlin}"
        const val Android = "io.reactivex.rxjava2:rxandroid:${LibVersions.rxAndroid}"
    }

    object Tools {
        const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${LibVersions.gradle}"
        const val androidStudio = "com.android.tools.build:gradle:${LibVersions.androidStudio}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${LibVersions.kotlin}"
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${LibVersions.kotlin}"
        const val jacocoCore = "org.jacoco:org.jacoco.core:${LibVersions.jacoco}"
        const val jacocoAgent = "org.jacoco:org.jacoco.agent:${LibVersions.jacoco}"
        const val dicemelonJacoco = "com.dicedmelon.gradle:jacoco-android:${LibVersions.dicemelonJacoco}"
        const val jacocoPalantir = "com.palantir:jacoco-coverage:${LibVersions.jacocoPalantir}"
    }
}
