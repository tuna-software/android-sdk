import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("kotlin")
    id("org.jetbrains.dokka")
}

ext {
    set("PUBLISH_VERSION", "0.0.1")
    set("PUBLISH_ARTIFACT_ID", "tuna-java")
}

apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")

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
    implementation(Libraries.coroutinesCore)

    api(project(":tuna"))
}