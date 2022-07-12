// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://plugins.gradle.org/m2/"))
        maven(url = uri("https://s01.oss.sonatype.org/content/repositories/releases/"))
    }

    dependencies {
        classpath(Libraries.classpathGradle)
        classpath(Libraries.classpathKotlinGradle)
        classpath(Libraries.classpathNavigation)
        classpath(Libraries.classpathNexus)
        classpath(Libraries.classpathDokka)
    }
}

apply(plugin = "io.github.gradle-nexus.publish-plugin")
apply(from = "${rootDir}/scripts/publish-root.gradle")
apply(plugin = "org.jetbrains.dokka")

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://s01.oss.sonatype.org/content/repositories/releases/"))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
