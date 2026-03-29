# Amma Sarees - Android App

## How to Build the AAB (Android App Bundle)

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (latest version)
- JDK 17 (bundled with Android Studio)
- Internet connection (to download Gradle dependencies)

---

### Step 1 — Open the Project
1. Open **Android Studio**
2. Click **"Open"** and select the `AmmaSarees` folder
3. Wait for Gradle sync to finish (may take 2–5 minutes first time)

---

### Step 2 — Build the AAB
Go to the menu bar:
```
Build → Generate Signed Bundle / APK
```

1. Select **Android App Bundle** → click **Next**
2. Create a new **Keystore** (or use an existing one):
   - Key store path: choose a location & filename (e.g. `amma_sarees.jks`)
   - Password: set a strong password
   - Alias: `ammasarees`
   - Fill in your name/organization details
3. Click **Next**
4. Choose **release** build variant
5. Click **Finish**

Your `.aab` file will be at:
```
app/release/app-release.aab
```

---

### Step 3 — Upload to Google Play
1. Go to [Google Play Console](https://play.google.com/console)
2. Create a new app → **Production** → **Create new release**
3. Upload the `.aab` file
4. Fill in store listing details and publish!

---

## App Features
- ✅ Full website loaded via WebView
- ✅ Pull-to-refresh support
- ✅ Offline error screen with retry button
- ✅ Splash screen with branding
- ✅ Back navigation support
- ✅ Purple saree-themed launcher icon
- ✅ Supports Android 5.0+ (API 21+)

## App Details
- Package: `com.ammasarees.app`
- Min SDK: 21 (Android 5.0)
- Target SDK: 34 (Android 14)
- Website: https://timeless-sarees.lovable.app
