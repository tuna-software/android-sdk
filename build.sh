#!/bin/bash

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