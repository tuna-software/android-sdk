import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("kotlin")
    id("org.jetbrains.dokka")
}

ext {
    set("PUBLISH_ARTIFACT_ID", "tuna")
}

apply(from = "${rootProject.projectDir}/scripts/publish-module-with-dependencies.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType(KotlinCompile::class).all {
    kotlinOptions {
        jvmTarget = Options.jvmTarget
    }
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

dependencies {
    implementation(Libraries.stdlib)

    api(Libraries.retrofit)
    api(Libraries.retrofitConverterGson)
    api(Libraries.gson)
    api(Libraries.okhttp)
    api(Libraries.okhttpLoggingInterceptor)
    api(Libraries.coroutinesCore)

    testImplementation(Libraries.junit)
    testImplementation(Libraries.mockitoKotlin)
    testImplementation(Libraries.mockitoInline)
    testImplementation(Libraries.coroutinesTest)
    testImplementation(Libraries.okhttpMockWebserver)
}
