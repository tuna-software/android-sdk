plugins {
    `java-library`
    id("kotlin")
    id("org.jetbrains.dokka")
}

ext {
    set("PUBLISH_ARTIFACT_ID", "tuna-kt")
}

apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

dependencies {
    implementation(Libraries.stdlib)

    implementation(project(":tuna-java"))

    testImplementation(Libraries.junit)
    testImplementation(Libraries.mockitoKotlin)
    testImplementation(Libraries.mockitoInline)
}
