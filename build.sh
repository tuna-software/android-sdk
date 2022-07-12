#!/bin/bash

./gradlew tuna:clean &&
./gradlew tuna-android:clean &&
./gradlew tuna-android-kt:clean &&
./gradlew tuna-google-pay:clean &&
./gradlew tuna-java:clean &&
./gradlew tuna-wallets:clean &&
./gradlew tunacommons:clean &&
./gradlew tunacr:clean &&
./gradlew tunakt:clean &&
./gradlew tunaui:clean &&

./gradlew tuna:assemble &&
./gradlew tuna-android:assemble &&
./gradlew tuna-android-kt:assemble &&
./gradlew tuna-google-pay:assemble &&
./gradlew tuna-java:assemble &&
./gradlew tuna-wallets:assemble &&
./gradlew tunacommons:assemble &&
./gradlew tunacr:assemble &&
./gradlew tunakt:assemble &&
./gradlew tunaui:assemble &&

./gradlew tuna:publishReleasePublicationToSonatypeRepository &&
./gradlew tuna-android:publishReleasePublicationToSonatypeRepository &&
./gradlew tuna-android-kt:publishReleasePublicationToSonatypeRepository &&
./gradlew tuna-google-pay:publishReleasePublicationToSonatypeRepository &&
./gradlew tuna-java:publishReleasePublicationToSonatypeRepository &&
./gradlew tuna-wallets:publishReleasePublicationToSonatypeRepository &&
./gradlew tunacommons:publishReleasePublicationToSonatypeRepository &&
./gradlew tunacr:publishReleasePublicationToSonatypeRepository &&
./gradlew tunakt:publishReleasePublicationToSonatypeRepository &&
./gradlew tunaui:publishReleasePublicationToSonatypeRepository
