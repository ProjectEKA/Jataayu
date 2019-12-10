import org.gradle.api.JavaVersion

object Config {
    const val applicationId = "in.org.projecteka.jataayu"
    const val compileSdkVersion = 29
    const val minSdkVersion = 23
    const val buildToolsVersion = "29.0.2"
    val javaVersion = JavaVersion.VERSION_1_8
}

object LibVersions {
    const val appcompat = "1.1.0"
    const val coreKtx = "1.1.0"
    const val material = "1.0.0"
    const val androidX = "1.1.1"
    const val androidXLifecycle = "2.1.0"
    const val jUnit = "4.12"
    const val espresso = "3.2.0"
    const val jacoco = "0.8.5"
    const val koin = "2.0.1"
    const val kotlin = "1.3.61"
    const val support = "28.0.0"
    const val constraintLayout = "1.1.3"
    const val retrofit = "2.6.2"
    const val gradle = "0.21.0"
    const val androidStudio = "3.5.2"
    const val dicemelonJacoco = "0.1.4"
    const val loggingInterceptor = "4.0.0"
    const val mockito = "3.2.0"
    const val coreTesting = "2.1.0"
    const val moshi = "1.9.2"
    const val apacheCommonsIo = "1.3.2"
}

object Modules {
    const val network = ":network"
    const val utils = ":utils"
    const val presentation = ":presentation"
    const val provider = ":features:provider"
}

object Deps {
    const val appcompat = "androidx.appcompat:appcompat:${LibVersions.appcompat}"
    const val coreKtx = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${LibVersions.kotlin}"
    const val material = "com.google.android.material:material:${LibVersions.material}"
    const val androidX = "androidx.core:core-ktx:${LibVersions.androidX}"
    const val androidXLifecyle = "androidx.lifecycle:lifecycle-extensions:${LibVersions.androidXLifecycle}"
    const val androidXLifecyleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibVersions.androidXLifecycle}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${LibVersions.constraintLayout}"
    const val koinAndroid = "org.koin:koin-android:${LibVersions.koin}"
    const val koinAndroidXViewModel = "org.koin:koin-androidx-viewmodel:${LibVersions.koin}"
    const val koinAndroidXScope = "org.koin:koin-androidx-scope:${LibVersions.koin}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${LibVersions.retrofit}"
    const val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:${LibVersions.retrofit}"
    const val retrofitConverter = "com.squareup.retrofit2:converter-moshi:${LibVersions.retrofit}"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${LibVersions.loggingInterceptor}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${LibVersions.moshi}"
    const val moshiKotlinCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${LibVersions.moshi}"
    const val apacheCommonsIo = "org.apache.commons:commons-io:${LibVersions.apacheCommonsIo}"

    const val jUnit = "junit:junit:${LibVersions.jUnit}"
    const val mockito = "org.mockito:mockito-core:${LibVersions.mockito}"
    const val coreTesting = "androidx.arch.core:core-testing:${LibVersions.coreTesting}"
    const val koinText = "org.koin:koin-test:${LibVersions.koin}"

    const val androidXTest = "androidx.test.ext:junit:${LibVersions.androidX}"
    const val espresso = "androidx.test.espresso:espresso-core:${LibVersions.espresso}"
    const val toolsGradleVersions = "com.github.ben-manes:gradle-versions-plugin:${LibVersions.gradle}"

    const val toolsAndroidStudio = "com.android.tools.build:gradle:${LibVersions.androidStudio}"
    const val toolsKotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${LibVersions.kotlin}"
    const val toolsKotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${LibVersions.kotlin}"
    const val toolsDicemelonJacoco = "com.dicedmelon.gradle:jacoco-android:${LibVersions.dicemelonJacoco}"
    const val toolsJacocoCore = "org.jacoco:org.jacoco.core:${LibVersions.jacoco}"
    const val toolsJacocoAgent = "org.jacoco:org.jacoco.agent:${LibVersions.jacoco}"
}