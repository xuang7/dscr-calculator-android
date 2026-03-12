# DSCR Calculator Android

A native Android DSCR (Debt Service Coverage Ratio) loan calculator built with Kotlin and Jetpack Compose.

This project was completed for the AI Bridge Android take-home assignment and focuses on clean architecture, clear UX, and production-minded implementation quality.

## Demo and Artifacts
- Repository: [https://github.com/xuang7/dscr-calculator-android](https://github.com/xuang7/dscr-calculator-android)
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Live Demo (Appetize): [https://appetize.io/app/b_6fskc4mvrnelhqdhc5u2iblaie](https://appetize.io/app/b_6fskc4mvrnelhqdhc5u2iblaie)
- Walkthrough Video: [https://youtu.be/7CMnZoB28ak](https://youtu.be/7CMnZoB28ak)

## Requirement Coverage

### Required Feature 1: DSCR Calculator
- Dedicated calculator screen built with Compose.
- Inputs: Property Address, Monthly Rental Income, Mortgage Payment (P&I), Property Tax, Insurance, HOA Fee (optional, defaults to 0).
- Formula: `DSCR = Monthly Rental Income / (Mortgage + Tax + Insurance + HOA)`
- Real-time calculation while user types.
- Validation: rejects negative values, max $1,000,000 per field, Save enabled only when valid.
- Color-coded result: Green (≥ 1.25 Strong), Yellow (1.0–1.24 Marginal), Red (< 1.0 Not Qualified).
- Reset/Clear functionality included.

### Required Feature 2: Calculation History
- Local persistence with Room.
- History list: property address, DSCR ratio, date/time, color-coded status.
- Tap to view full details. Swipe left to delete.

### Required Feature 3: Bottom Navigation
- Two tabs: Calculator and History.

### Required Feature 4: Multi-language Support
- English, Simplified Chinese, Spanish (bonus).
- In-app language selector via top bar menu.
- Proper Android string resources (`strings.xml`).

## Bonus Features Implemented
- Dark mode: system default, force light, force dark.
- Animated DSCR gauge/ring.
- Unit tests for calculation logic and ViewModel behavior.

## Tech Stack
| Item         | Detail                        |
|--------------|-------------------------------|
| Language     | Kotlin                        |
| UI           | Jetpack Compose + Material 3  |
| Architecture | MVVM                          |
| DI           | Hilt                          |
| Database     | Room                          |
| Navigation   | Navigation Compose            |
| Async        | Coroutines + StateFlow        |
| Testing      | JUnit4 + kotlinx-coroutines-test |
| Min SDK      | 26                            |
| Target SDK   | 35                            |

## Architecture Decisions and Rationale

The app uses a lightweight layered MVVM structure to keep UI logic, domain logic, and data access separate while avoiding unnecessary abstraction.

### Layers
- **ui/** — Compose screens, ViewModels, UiState data classes, reusable components.
- **domain/** — Pure Kotlin business logic (DSCRCalculator, DSCRStatus). Easy to test in isolation.
- **data/** — Room entities, DAO, repository, app preferences.
- **di/** — Hilt modules for dependency injection.

### Why this approach
- Improves maintainability and readability.
- Makes core logic easy to test in isolation.
- Keeps complexity appropriate for take-home scope (no over-engineering).

## Project Structure
```text
app/src/main/java/com/example/dscrcalculator
├── data
│   ├── local          # Room: Entity, DAO, Database
│   ├── preferences    # Theme preferences
│   └── repository     # Repository pattern
├── di                 # Hilt modules
├── domain
│   ├── model          # DSCRStatus enum
│   └── util           # DSCRCalculator logic
├── ui
│   ├── calculator     # Calculator Screen, ViewModel, UiState
│   ├── common         # LanguageMenu, status extensions
│   ├── history        # History list + detail
│   ├── navigation     # Bottom nav routes
│   └── theme          # Material 3 theme
├── DSCRApplication.kt
└── MainActivity.kt
```

## Setup and Build

### Prerequisites
- Android Studio Ladybug (2024.2.1) or newer
- JDK 21
- Android SDK: Min SDK 26, Target SDK 35

### Run in Android Studio
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle.
4. Run `app` on an emulator or device.

### CLI Commands
```bash
./gradlew assembleDebug        # Build debug APK
./gradlew testDebugUnitTest    # Run unit tests
./gradlew lintDebug            # Run lint
```

## Testing
- `DSCRCalculatorTest.kt` — calculation logic and status thresholds
- `CalculatorViewModelTest.kt` — input validation, save behavior, state management
- `MainDispatcherRule.kt` — coroutine test utility

All unit tests pass. Lint passes (warnings only for newer dependency versions).

## Screenshots

> TODO: Add screenshots to `screenshots/` folder

## AI Tools Used
- **Codex (OpenAI)**: Architecture design, feature implementation, and code generation.
- **Claude Opus 4.6 (Anthropic)**: Code review, edge case analysis, and quality refinement.

How they helped:
- Codex accelerated initial project setup, UI implementation, and localization structure.
- Claude reviewed each module for correctness, identified edge cases (input validation, save deduplication, boundary conditions), and refined unit tests.

All generated code was reviewed, understood, and manually validated before finalizing.

## Trade-offs and Known Limitations
- No instrumentation/UI tests (unit tests only).
- Export/share and comparison features were out of scope for time.
- `AppCompatActivity` required for per-app language API.
- Some lint warnings remain for latest SDK/dependency versions.

## Future Improvements
1. Add Compose UI tests for critical user flows.
2. Improve locale-aware numeric input parsing/formatting while typing.
3. Add export/share and comparison view features.