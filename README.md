# SplittyEasy

A modern, minimal bill‑splitting app built with Kotlin and Jetpack Compose. Split costs with friends, add bills in a friendly flow, and preview who pays what — all with a clean Material 3 UI.

## Highlights
- Split Calculator for quick equal splits
- Guided Add Bill flow (details → friends → method → amounts → summary)
- Lightweight history of recent transactions
- Compose-first UI with Material 3 theming and Navigation
- Room (DAO) powered data layer with Flows

## Tech Stack
- Kotlin + Jetpack Compose (Material 3)
- Compose Navigation
- ViewModel + State hoisting
- Room (DAO/Entities) + Coroutines/Flow

## Project Structure
- app/src/main/java/com/splitwise/easy/ui – Compose screens and navigation (Home, Add Bill, Friends, Groups, History, Settings, Bill Details, Split Calculator)
- app/src/main/java/com/splitwise/easy/data – Room entities, DAOs, database, and repository
- app/src/main/java/com/splitwise/easy/viewmodel – ViewModels for feature screens
- app/src/main/java/com/splitwise/easy/util – Utilities (e.g., split calculator helpers)

## Quick Start
1) Open in Android Studio (latest stable recommended)
2) Sync Gradle
3) Run on a device or emulator (Android 8.0+ recommended)

Or from a terminal:
```bash
# Build Debug APK
./gradlew :app:assembleDebug

# Install to a connected device
./gradlew :app:installDebug

# Run unit tests
./gradlew test

# Run instrumentation tests (device/emulator required)
./gradlew connectedAndroidTest
```

## Using the App
- Home
  - See quick actions and recent transactions
- Split Calculator
  - Enter total and number of people → get per-person share instantly
  - Done closes the calculator and returns you to the previous screen
- Add Bill Flow
  - Enter title, amount, category, description
  - Select friends involved
  - Pick split method (Equal available; other methods are shown for future expansion)
  - Record who already paid (optional)
  - Review final amounts (who pays/receives)

## Screenshots
Add your screenshots to a folder like `docs/images/` and reference them here:
- Split Calculator: ![Split Calculator](docs/images/split_calculator.png)
- Add Bill Flow: ![Add Bill](docs/images/add_bill.png)

## Roadmap
- Percentage and custom split inputs
- Item-wise split UX
- Settle up flows and payment integrations
- Real friend/group management
- Dark mode tuning and accessibility polish

## Contributing
- Fork the repo and create a feature branch
- Keep changes focused and tested
- Open a PR describing the motivation and approach

## License
This project currently does not specify a license. If you plan to reuse or redistribute, please add a LICENSE file first.

