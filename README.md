# Shishu-Sneh

Production-style Android app for a baby's first-year healthcare journey.

## App Screenshots

<p align="center">
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/raw/main/docs/dashboard.png" alt="Dashboard" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/raw/main/docs/growth.png" alt="Growth Chart" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/raw/main/docs/vaccines.png" alt="Vaccination Schedule" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/raw/main/docs/login.png" alt="Login Screen" width="200" />
</p>

### Features Shown:
- **Dashboard**: Quick overview of baby's health metrics, latest weight, vaccination schedules, milestones, and feeding tips
- **Growth Chart**: Interactive WHO reference growth chart to track baby's weight progression
- **Vaccination Schedule**: Complete immunization schedule with status tracking (completed, overdue, pending)
- **Login**: Secure authentication with email and password

## Android Studio Setup

1. Open Android Studio Hedgehog or newer with Android SDK 35 installed.
2. Choose **Open** and select the `ShishuSneh` folder.
3. Let Android Studio sync the Gradle Kotlin DSL project.
4. If prompted, install:
   - Android SDK Platform 35
   - Build Tools for API 35
   - Kotlin / Compose support plugins
5. Create a device or connect a phone running Android 8.0+.
6. Run the `app` configuration.

## What Is Included

- Kotlin + Jetpack Compose
- MVVM + Repository pattern
- Hilt dependency injection
- Room offline database
- WorkManager vaccine reminders
- MPAndroidChart growth chart
- Material 3 theme with light and dark mode
- Local auth, session management, export backup, feeding guide, vaccines, milestones, settings

## Run Notes

- Notification reminders require Android 13+ permission approval.
- Vaccine schedules are generated after the baby profile is created.
- Feeding tips are seeded locally on first app launch.
- Backup export writes a JSON file using the system document picker.
- The standard wrapper scripts and properties are included, but `gradle/wrapper/gradle-wrapper.jar` is a generated binary and is not present in this offline-created project. Android Studio can still sync the project.

## Testing Checklist

1. Sign up with a new email and password.
2. Create the baby profile.
3. Add 2-3 growth entries and verify the chart updates.
4. Open Vaccines and mark one vaccine as completed.
5. Open Milestones and toggle a few items.
6. Open Feeding Guide and verify age-based tips.
7. In Settings:
   - toggle notifications
   - switch English/Hindi
   - switch theme modes
   - export backup JSON
   - edit profile
   - logout

## Verification Limitation

The local environment used to generate this project does not have a Gradle executable installed, so I could not run a final build command here. The project structure and source were reviewed and prepared carefully for production use.
