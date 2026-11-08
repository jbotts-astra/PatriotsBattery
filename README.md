# PatriotsBattery
A stylized battery icon

Open this folder in Android Studio, allow Gradle to sync, then Build > Build APK(s). Transfer the resulting app-debug. apk to your Galaxy and install it. This is SOURCE CODE, not a compiled APK. Android SDK / Gradle were not available in the creation environment, so the

project has not been compiled or device-tested.

Tap Allow display over other apps, grant permission, return, and tap Start battery overlay. Drag the badge below the top-right system icons. Tap the badge to toggle drag locking. Stop from the app. The foreground notification is intentional and the overlay does not request Internet

access.

Limitations: Samsung/Android may disallow drawing inside the protected status bar; this overlay appears immediately below it. Battery percentage is live; the graphic is a custom NE monogram rather than an official NFL logo. Android may stop the service depending on battery/background policies
