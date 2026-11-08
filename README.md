# PatriotsBattery 🏈

Native Android application for a **Patriots-inspired battery badge** that floats near the top-right status bar.

## Current features
- Live battery percentage and charging indicator
- Original red, white and blue flying-pennant badge (not the official Patriots logo)
- Drag to reposition; tap to lock or unlock dragging
- Saves the badge position and lock state
- Start/stop controls and persistent foreground notification
- No internet permission, tracking or ad SDK

## Building
Open the repository root in **Android Studio** on a computer. Sync with **Android Gradle Plugin 8.7.3**, **Gradle 8.9**, **JDK 17**, and Android SDK 35. The project contains Gradle build files and wrapper *properties*, but **does not yet contain the Gradle wrapper executable scripts or JAR**; use a locally installed Gradle 8.9 or generate the wrapper with `gradle wrapper --gradle-version 8.9` before using `./gradlew`.

Then select **Build > Build APK(s)**. Install the generated `app/build/outputs/apk/debug/app-debug.apk` on your Samsung Galaxy. Android Studio and the Android SDK are required to compile; **this source has not yet been built or device-tested**.

## Using the overlay
1. Open Patriots Battery and tap **Grant overlay permission**.
2. Allow **Display over other apps** for Patriots Battery.
3. Return to the app and tap **Start battery overlay**.
4. Drag the badge near the upper-right of the screen. Tap to lock/unlock.
5. Use **Stop overlay** to dismiss it.

## Limitations
Android/Samsung do not permit normal apps to replace the protected status bar battery indicator. This uses `TYPE_APPLICATION_OVERLAY` to display a badge near it. Some system screens or background restrictions may hide/stop the overlay. The status bar itself remains unchanged. No official NFL logo is bundled.

## Repository
`jbotts-astra/PatriotsBattery` — first source implementation, awaiting Android Studio build and on-device verification.
