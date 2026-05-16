# Shishu-Sneh

**Production-style Android app for a baby's first-year healthcare journey.**

## 🎯 Problem Statement

New parents often struggle to track their baby's health milestones, vaccination schedules, growth metrics, and nutritional needs during the critical first year. Shishu-Sneh solves this by providing a comprehensive, offline-first solution designed for the Indian healthcare context.

## 🌟 Key Highlights

- Track baby's weight and growth using WHO standards
- Manage vaccination schedules with automated reminders
- Monitor developmental milestones month-by-month
- Access age-appropriate feeding tips in multiple languages
- Maintain secure, offline-accessible health records
- Get AI-powered baby care guidance and health summaries
- Voice-enabled Q&A for hands-free assistance
- Family member access for shared childcare

**Target Users:** New parents, caregivers, and healthcare workers in India and South Asian regions.

## ✨ Key Features

### Core Features
- 👶 **Baby Profile Management** - Create and manage your baby's health profile with birth date and basic info
- 📊 **Growth Chart Tracking** - Interactive WHO reference charts to visualize baby's weight progression with percentile interpretation
- 💉 **Vaccination Scheduler** - Complete Indian immunization schedule with status tracking (pending, completed, overdue)
- 🎯 **Milestone Tracker** - Monthly developmental milestones with checkoff functionality
- 🍼 **Feeding Guide** - Age-appropriate feeding tips updated dynamically (English & Hindi)
- 🔔 **Smart Reminders** - WorkManager-powered notifications for upcoming vaccinations
- 🔐 **Secure Authentication** - Email/password-based auth with session management
- 💾 **Data Backup** - Export health records as JSON for backup and portability
- 🌙 **Dark/Light Mode** - Material 3 theme with system preference support
- 🌐 **Multilingual Support** - English and Hindi language preferences

### 🚀 Advanced Features
- 🤖 **AI Baby Care Assistant** - Intelligent Q&A system for personalized baby care guidance
  - Rule-based care Q&A inside Smart Care Hub screen
  - Context-aware responses based on baby's age and health profile
- 📋 **Smart Care Hub** - Centralized view for all AI-generated insights
  - AI-generated weekly baby health summary
  - Generated from profile, growth, vaccines, milestones, and appointments
- 🎤 **Voice Assistant** - Hands-free interaction with the app
  - Text-to-speech playback for health summaries
  - Speech input via Android speech recognizer (Hindi & English)
  - Ideal for busy parents
- 📸 **OCR Vaccination Card Scan** - Digitize paper vaccination records
  - Image picker + ML Kit Text Recognition
  - Extracted text is stored in Room database
  - Automatic extraction and categorization of vaccine data
- 📅 **Doctor Appointment Reminders** - Manage healthcare visits
  - New AppointmentEntity with DAO and ViewModel
  - Scheduled reminders via WorkManager
  - Complete appointment tracking and history
- 👨‍👩‍👧 **Family Member Multi-User Access** - Share childcare responsibilities
  - Multiple family members can access baby profile
  - FamilyMemberEntity with role management
  - Dedicated family access management screen
- 🆘 **Emergency Guidance Module** - Critical information when needed
  - Offline emergency content seeded locally
  - Dedicated emergency guidance screen
  - Quick access to life-saving information
- 📄 **PDF Health Report Export** - Generate comprehensive health records
  - Exported from live baby health data using PdfDocument
  - Includes growth, vaccines, milestones, and appointments
  - Share with healthcare providers
- ☁️ **Cloud Sync Readiness** - Future-proof architecture
  - Architecture and status placeholders for Firebase integration
  - Seamless sync when enabled

## 📱 App Screenshots

<p align="center">
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/dashboard.jpeg?raw=true" alt="Dashboard" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/growth.jpeg?raw=true" alt="Growth Chart" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/vaccination.jpeg?raw=true" alt="Vaccination Schedule" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/login.jpeg?raw=true" alt="Login Screen" width="200" />
</p>

<p align="center">
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/app/WhatsApp%20Image%202026-05-16%20at%208.53.07%20AM%20(2).jpeg?raw=true" alt="Screenshot 1" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/app/WhatsApp%20Image%202026-05-16%20at%208.53.07%20AM%20(3).jpeg?raw=true" alt="Screenshot 2" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/app/WhatsApp%20Image%202026-05-16%20at%208.53.08%20AM%20(1).jpeg?raw=true" alt="Screenshot 3" width="200" />
  <img src="https://github.com/harsh17-ops/Shishu-Sneh/blob/main/app/WhatsApp%20Image%202026-05-16%20at%208.56.02%20AM.jpeg?raw=true" alt="Screenshot 4" width="200" />
</p>

**Feature Highlights:**
- **Dashboard**: Quick overview of baby's health metrics, latest weight, vaccination schedules, milestones, and feeding tips
- **Growth Chart**: Interactive WHO reference growth chart with percentile interpretation to track baby's weight progression
- **Vaccination Schedule**: Complete immunization schedule with status tracking (completed, overdue, pending)
- **Smart Care Hub**: AI-generated baby health summary and rule-based Q&A for personalized care guidance
- **Appointments**: Manage doctor visits with reminders and complete appointment history
- **Family Access**: Add family members for shared childcare access
- **Emergency Guidance**: Offline emergency care information for critical situations
- **Login**: Secure authentication with email and password

## 🛠 Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose with Material 3 |
| **Architecture** | MVVM + Repository Pattern |
| **Dependency Injection** | Hilt |
| **Local Database** | Room (SQLite) |
| **Data Persistence** | DataStore Preferences |
| **Background Tasks** | WorkManager |
| **Charting** | MPAndroidChart |
| **Navigation** | Jetpack Navigation Compose |
| **JSON Processing** | Gson |
| **ML Kit** | Text Recognition (OCR) |
| **Text-to-Speech** | Android TTS |
| **Speech Recognition** | Android Speech Recognizer |
| **PDF Generation** | PdfDocument |
| **Min SDK** | Android 8.0 (API 26) |
| **Target SDK** | Android 15 (API 35) |

## 🚀 Quick Start

### Prerequisites
- Android Studio Hedgehog or newer
- Android SDK 35
- JDK 17 or higher
- Android Device or Emulator running Android 8.0+

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/harsh17-ops/Shishu-Sneh.git
   cd Shishu-Sneh
   ```

2. **Open in Android Studio:**
   - Launch Android Studio
   - Click **File → Open**
   - Navigate to the cloned `Shishu-Sneh` folder and select it
   - Click **OK** to open the project

3. **Sync Gradle:**
   - Android Studio will automatically sync the project
   - Let the Gradle build process complete (this may take 2-3 minutes on first sync)
   - If prompted, install recommended Android SDK components:
     - Android SDK Platform 35
     - Android SDK Build-Tools 35.x
     - Kotlin plugin with Compose support

4. **Set up an Emulator/Device:**
   - In Android Studio, go to **Tools → Device Manager**
   - Create a new virtual device with Android 8.0 or higher, OR
   - Connect a physical Android device via USB with Developer Mode enabled

5. **Run the App:**
   - In Android Studio toolbar, select the `app` configuration
   - Click the **▶ Run** button (or press Shift+F10)
   - The app will build and launch on your selected device

### Key Run Notes

- **Notifications**: Android 13+ requires runtime permission approval (popup will appear on first vaccination reminder)
- **Baby Profile**: Vaccine schedules are automatically generated after creating your baby profile
- **Feeding Tips**: Seeded locally on first app launch; no network required
- **Backup Export**: Uses system document picker to save JSON files and PDFs
- **Voice Features**: Hindi and English speech recognition available in Smart Care Hub
- **OCR Scanning**: Place vaccination card under good lighting for best results
- **Family Access**: Add family members through the Family Access screen in settings
- **Offline-First**: All data stored locally in Room database; works without internet
- **Language**: Default is English; change to Hindi in Settings > Preferences

## 📥 Download APK

### Direct APK Installation on Phone

You can download the pre-built APK file and install it directly on your Android phone without needing Android Studio:

- **Download**: [base.apk](https://github.com/harsh17-ops/Shishu-Sneh/releases/download/v1.0/base.apk)

**Installation Steps:**
1. Download the `base.apk` file on your Android phone
2. Open your file manager and navigate to the Downloads folder
3. Tap on `base.apk` to install
4. If prompted about unknown sources, enable "Install from Unknown Sources" in your security settings
5. Tap **Install** and wait for the installation to complete
6. Once installed, tap **Open** to launch Shishu-Sneh

**Requirements:**
- Android 8.0 (API 26) or higher
- ~50 MB free storage space
- Internet connection for initial setup (optional for later use)

## 📁 Project Structure

```
Shishu-Sneh/
├── app/                                      # Main application module
│   ├── src/main/java/com/shishusneh/app/
│   │   ├── MainActivity.kt                   # Entry point with theme & auth routing
│   │   ├── data/
│   │   │   ├── dao/                          # Database Access Objects (Room)
│   │   │   │   ├── BabyProfileDao.kt
│   │   │   │   ├── VaccinationDao.kt
│   │   │   │   ├── GrowthEntryDao.kt
│   │   │   │   ├── MilestoneDao.kt
│   │   │   │   ├── AppointmentDao.kt         # ✨ NEW: Appointment management
│   │   │   │   └── FamilyMemberDao.kt        # ✨ NEW: Family access management
│   │   │   ├── entity/                       # Room database entities
│   │   │   │   ├── BabyProfileEntity.kt
│   │   │   │   ├── VaccinationEntity.kt
│   │   │   │   ├── GrowthEntryEntity.kt
│   │   │   │   ├── MilestoneEntity.kt
│   │   │   │   ├── AppointmentEntity.kt      # ✨ NEW: Doctor appointments
│   │   │   │   └── FamilyMemberEntity.kt     # ✨ NEW: Family members
│   │   │   ├── database/
│   │   │   │   └── AppDatabase.kt            # Room database setup (updated)
│   │   │   └── local/
│   │   │       └── LocalDataSource.kt        # Local data operations
│   │   ├── repository/                       # Data layer abstractions
│   │   │   ├── AuthRepository.kt
│   │   │   ├── BabyRepository.kt
│   │   │   ├── VaccinationRepository.kt
│   │   │   ├── GrowthRepository.kt
│   │   │   ├── SettingsRepository.kt
│   │   │   ├── AdvancedCareRepository.kt     # ✨ NEW: AI & advanced features
│   │   │   ├── AppointmentRepository.kt      # ✨ NEW: Appointment management
│   │   │   └── FamilyRepository.kt           # ✨ NEW: Family member access
│   │   ├── ui/
│   │   │   ├── screens/                      # UI screens (Compose)
│   │   │   │   ├── auth/                     # Login/Signup screens
│   │   │   │   ├── dashboard/                # Main dashboard
│   │   │   │   ├── growth/                   # Growth tracking screens (updated)
│   │   │   │   ├── vaccines/                 # Vaccination screens
│   │   │   │   ├── milestones/               # Milestone tracker
│   │   │   │   ├── feeding/                  # Feeding guide
│   │   │   │   ├── smartcare/                # ✨ NEW: Smart Care Hub & AI Q&A
│   │   │   │   ├── appointments/             # ✨ NEW: Doctor appointments
│   │   │   │   ├── family/                   # ✨ NEW: Family member access
│   │   │   │   ├── emergency/                # ✨ NEW: Emergency guidance
│   │   │   │   └── settings/                 # Settings/profile (updated)
│   │   │   ├── components/                   # Reusable UI components
│   │   │   │   ├── SummaryCard.kt
│   │   │   │   ├── GrowthChart.kt
│   │   │   │   ├── VaccinationCard.kt
│   │   │   │   ├── VoiceInput.kt             # ✨ NEW: Voice input component
│   │   │   │   └── ... (more components)
│   │   │   ├── theme/                        # Material 3 theming
│   │   │   │   ├── Color.kt
│   │   │   │   ├── Type.kt
│   │   │   │   ├── Theme.kt
│   │   │   │   └── Shapes.kt
│   │   │   └── navigation/
│   │   │       └── AppNavGraph.kt            # Navigation setup (updated)
│   │   ├── viewmodel/                        # MVVM ViewModels
│   │   │   ├── MainViewModel.kt
│   │   │   ├── DashboardViewModel.kt
│   │   │   ├── AuthViewModel.kt
│   │   │   ├── GrowthViewModel.kt            # Updated: percentile interpretation
│   │   │   ├── VaccinationViewModel.kt
│   │   │   ├── SmartCareViewModel.kt         # ✨ NEW: AI care hub
│   │   │   ├── VoiceAssistantViewModel.kt    # ✨ NEW: Voice interaction
│   │   │   ├── AppointmentViewModel.kt       # ✨ NEW: Appointment management
│   │   │   ├── FamilyAccessViewModel.kt      # ✨ NEW: Family access
│   │   │   └── ... (more ViewModels)
│   │   ├── di/                               # Dependency Injection (Hilt)
│   │   │   ├── DatabaseModule.kt
│   │   │   ├── RepositoryModule.kt
│   │   │   └── DatastoreModule.kt
│   │   ├── utils/
│   │   │   ├── DateUtils.kt                  # Date calculations
│   │   │   ├── Validators.kt                 # Form validation
│   │   │   ├── SeedData.kt                   # Default vaccine/milestone data
│   │   │   ├── OcrUtils.kt                   # ✨ NEW: OCR text extraction
│   │   │   ├── PdfGenerator.kt               # ✨ NEW: PDF health report
│   │   │   └── Extensions.kt
│   │   └── worker/
│   │       ├── VaccinationReminderWorker.kt  # Existing: vaccination reminders
│   │       └── AppointmentReminderWorker.kt  # ✨ NEW: appointment reminders
│   ├── src/main/res/                         # Resources
│   │   ├── values/                           # Strings, colors, dimensions
│   │   ├── drawable/                         # Icons and drawables
│   │   └── mipmap/                           # App icons
│   ├── src/main/AndroidManifest.xml
│   └── build.gradle.kts                      # Module-level build config (updated)
├── build.gradle.kts                          # Project-level build config
├── settings.gradle.kts                       # Gradle settings
├── gradle.properties                         # Gradle properties
├── gradlew & gradlew.bat                     # Gradle wrapper scripts
├── README.md                                 # This file
├── .gitignore                                # Git ignore rules
└── Screenshots/                              # App screenshots
    ├── dashboard.jpeg
    ├── growth.jpeg
    ├── vaccination.jpeg
    └── login.jpeg
```

## ✅ Testing Checklist

Before evaluating or deploying, verify all features work:

### Authentication Flow
- [ ] Launch app and see login screen
- [ ] Sign up with email and password
- [ ] Successfully create account and log in
- [ ] Log out from Settings and return to login screen

### Baby Profile
- [ ] Create a new baby profile with name and DOB
- [ ] Edit baby profile (change name, update DOB)
- [ ] View profile details on dashboard

### Growth Tracking
- [ ] Add 2-3 growth entries (weight and height)
- [ ] Verify entries appear on the growth chart
- [ ] Check WHO reference lines on chart
- [ ] Verify percentile interpretation displays alongside chart
- [ ] Verify age calculation matches baby's actual age

### Vaccination Management
- [ ] Open Vaccines screen and view full schedule
- [ ] Mark a vaccine as "Completed"
- [ ] Mark a vaccine as "Pending"
- [ ] Check that overdue vaccines are highlighted
- [ ] Verify vaccine due dates are calculated correctly
- [ ] Scan vaccination card using OCR and verify extracted data

### Milestones
- [ ] Open Milestones and view age-appropriate milestones
- [ ] Toggle a milestone as achieved (checkbox)
- [ ] Verify milestones update based on baby's age

### Feeding Guide
- [ ] Open Feeding Guide and read tips for current age
- [ ] Verify tips change as baby grows
- [ ] Test English and Hindi language preferences

### Settings & Advanced Features
- [ ] Toggle Notifications on/off
- [ ] Switch between Light/Dark themes and verify UI updates
- [ ] Change language to Hindi and verify text changes
- [ ] Export backup as JSON and verify file saves
- [ ] Edit baby profile from settings

### Smart Care Hub (AI Features)
- [ ] Open Smart Care Hub and view weekly AI health summary
- [ ] Ask questions in the rule-based Q&A system
- [ ] Test voice input in Hindi and English
- [ ] Verify text-to-speech plays summaries correctly
- [ ] Check that AI responses are contextual to baby's profile

### Doctor Appointments
- [ ] Add a new doctor appointment
- [ ] Edit existing appointment
- [ ] Verify appointment reminders are scheduled
- [ ] Check appointment history on appointments screen
- [ ] Test appointment reminder notifications

### Family Access
- [ ] Add a family member with name and relationship
- [ ] View list of added family members
- [ ] Edit family member information
- [ ] Verify all family members can access baby profile

### Emergency Guidance
- [ ] Open Emergency Guidance screen
- [ ] Verify offline emergency content displays correctly
- [ ] Test access without internet connection

### PDF Health Report
- [ ] Generate PDF health report from settings
- [ ] Verify report includes growth, vaccines, milestones, and appointments
- [ ] Export and share PDF with healthcare provider

## 🌍 Localization

Shishu-Sneh supports multiple languages:

| Language | Status | Coverage |
|----------|--------|----------|
| English (en) | ✅ Complete | 100% |
| Hindi (hi) | ✅ Complete | 100% |

Change language in **Settings > Preferences > Language**.

## 🔒 Privacy & Security

- **Local-First**: All data is stored locally on your device; no cloud sync (by default)
- **No Remote Tracking**: App doesn't send health data to external servers
- **Session Management**: Logout clears all session data
- **Encryption Ready**: Database can be encrypted with Room's encryption plugin (future enhancement)
- **Cloud Sync Ready**: Architecture prepared for optional Firebase integration

## 🛣 Future Roadmap

- [ ] **Cloud Sync**: Complete Firebase integration for optional backup sync
- [ ] **Advanced AI**: Deep learning models for growth predictions and anomaly detection
- [ ] **Doctor Collaboration**: Share baby records securely with healthcare providers
- [ ] **Multiple Babies**: Support for tracking multiple children
- [ ] **Offline Mode**: Enhanced offline capabilities with sync queue
- [ ] **Wearables Integration**: Connect to baby health wearables
- [ ] **More Languages**: Regional language support (Bengali, Marathi, Tamil, etc.)
- [ ] **Video Tutorials**: In-app video guides for new parents
- [ ] **Community Features**: Connect with other parents and pediatricians
- [ ] **ML-based Insights**: Anomaly detection and predictive analytics

## 📊 Build & Dependencies

The project uses **Gradle with Kotlin DSL** for build configuration. All dependencies are managed in `app/build.gradle.kts`:

- **Compose BOM** (2024.09.00) - Latest Jetpack Compose
- **Hilt** (2.52) - Dependency injection
- **Room** (2.6.1) - Local database
- **WorkManager** (2.9.1) - Background processing and reminders
- **MPAndroidChart** (v3.1.0) - Chart rendering
- **DataStore** (1.1.1) - Preferences storage
- **ML Kit** - Text Recognition for OCR
- **Firebase** (optional, future) - Cloud sync infrastructure

## 🐛 Known Limitations

1. **Gradle Wrapper Binary**: The `gradle/wrapper/gradle-wrapper.jar` binary is not included in the repository (it's generated by Android Studio). This doesn't affect functionality; Android Studio will regenerate it automatically.

2. **Build on Fresh Clone**: On first clone and sync:
   - Android Studio will automatically download and configure the Gradle wrapper
   - The build should complete successfully
   - If you encounter issues, use **File → Invalidate Caches → Invalidate and Restart**

3. **Notification Permissions**: Android 13+ requires explicit user permission for notifications (shown on first vaccine/appointment reminder).

4. **Cloud Sync**: Firebase integration is architecture-ready but not yet fully implemented. Status placeholders are in place for future enablement.

## 📞 Support & Troubleshooting

### Issue: Project won't sync in Android Studio
- **Solution**: Go to **File → Invalidate Caches** and restart Android Studio
- Ensure you have Android SDK 35 and Build Tools installed

### Issue: Emulator won't run app
- **Solution**: Try creating a new virtual device with API 30 or higher
- Use a physical device as fallback

### Issue: Reminders not showing
- **Solution**: Check notification permissions in Settings > App Permissions > Notifications
- Ensure WorkManager is not disabled by battery saver mode

### Issue: Voice input not working
- **Solution**: Check microphone permissions in app settings
- Verify language is set to Hindi or English in Preferences
- Test on physical device (some emulators have audio limitations)

### Issue: OCR scanning not recognizing text
- **Solution**: Use good lighting and high-quality vaccination card photos
- Try rotating or adjusting the image before scanning

## 📜 License

This project is open-source and available under the MIT License.

## 👨‍💻 Author

**Harshavardhan G**
- GitHub: [@harsh17-ops](https://github.com/harsh17-ops)
- Email: harsha17jul4@gmail.com

## 🙏 Acknowledgments

- [WHO Growth Standards](https://www.who.int/standards/growth/) for vaccination and growth references
- [Indian Academy of Pediatrics](https://www.iapindia.org/) for immunization schedule
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose) for UI framework guidance
- [Material 3 Design](https://m3.material.io/) for design system inspiration
- [ML Kit](https://developers.google.com/ml-kit) for OCR and ML capabilities
- Android community for libraries and best practices

---

**Last Updated**: May 16, 2026
