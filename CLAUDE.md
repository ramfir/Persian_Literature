# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Persian Literature is an Android app showcasing Persian poetry and literary works. The app features a multi-language interface (English, Russian, Tajik) and allows users to browse authors, read their works, and manage favorites. Data is fetched from Firebase Firestore.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Clean build
./gradlew clean assembleDebug

# Install on connected device
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk

# Run tests
./gradlew test

# Run Koin module tests specifically
./gradlew runKoinModuleTests

# Run detekt (code quality)
./gradlew detekt
```

## Architecture

### Modular Structure

The project uses a **multi-module architecture** with API/implementation separation:

- **app** - Main application module, contains navigation and DI setup
- **core** - Shared models, base classes, common resources
- **ui_kit** - Reusable UI components and theme

Feature modules follow an **API/Implementation pattern**:
- **author_api** / **author** - Author domain logic (Room DB, repository)
- **author_ui** - Author UI screens (list, details, work details, favourites)
- **settings_api** / **settings** - Language settings
- **about_app** - About screen
- **util** - Utilities

### Key Architectural Patterns

**1. BaseViewModel Pattern**
All ViewModels extend `BaseViewModel<T : UiState>` (core/presentation/BaseViewModel.kt):
- Uses StateFlow for UI state management
- Provides `post(block: (T) -> T)` for state updates
- Includes `onViewResumed()` lifecycle hook

**2. BaseEntryPoint Pattern**
UI screens use `BaseEntryPoint` composable (ui_kit/BaseEntryPoint.kt):
- Integrates Koin for dependency injection
- Automatically collects state with `collectAsStateWithLifecycle()`
- Calls `onViewResumed()` on composition

Example:
```kotlin
@Composable
fun MyScreenEntryPoint() {
    BaseEntryPoint(MyViewModel::class) { state, viewModel ->
        MyScreen(state, viewModel::onAction)
    }
}
```

**3. UiState Pattern**
Each screen has a data class extending `UiState` that includes:
- `chapters: List<Chapter>` - Navigation drawer items
- Feature-specific state fields
- Inherited from `core.model.Chapter`

**4. Dependency Injection (Koin)**
- Main DI setup in `app/di/Module.kt`
- Each feature module has its own `di/Module.kt`
- App module includes all feature modules via `includes()`

**5. Navigation (Navigation3 Library)**
- Type-safe routes defined in `app/navigation/Route.kt`
- Navigator composable in `app/navigation/Navigator.kt`
- Uses `backStack.startNewRoot()` for main chapters
- Uses `backStack.next()` for detail screens

### Data Layer

**Room Database** (author module):
- `AuthorsDb` - Main database
- `AuthorDao` / `WorkDao` - Data access objects
- Repositories expose data via StateFlow

**Firebase Firestore**:
- Authors and works data synced from cloud
- Logging enabled in `App.onCreate()`

## Localization

### Language Support
The app supports **English (en)**, **Russian (ru)**, and **Tajik (tg)**.

**Critical Configuration**:
- `app/src/main/res/xml/locales_config.xml` - Declares supported locales
- `AndroidManifest.xml` - References locale config via `android:localeConfig`
- `gradle/scripts/android-config.gradle` - Line 26: `resourceConfigurations += ['en', 'ru', 'tg']`

**Important**: All three languages MUST be listed in `resourceConfigurations` or they will be excluded from the APK.

### Language Switching
- Managed by `LanguageManager` interface (settings_api)
- Implemented in `LanguageManagerImpl` (settings module)
- Uses `AppCompatDelegate.setApplicationLocales()` for runtime switching
- Language preference stored in SharedPreferences

### String Resources
All modules require localized strings in:
- `values/strings.xml` - English (default)
- `values-ru/strings.xml` - Russian
- `values-tg/strings.xml` - Tajik

Key modules: **core**, **author_ui**, **about_app**, **app**, **settings**

## Common Development Patterns

### Adding a New Feature Module

1. Create two modules: `feature_api` (interfaces) and `feature` (implementation)
2. Add to `settings.gradle`: `include ':feature_api'` and `include ':feature'`
3. Create `di/Module.kt` with Koin definitions
4. Include module in `app/di/Module.kt`
5. Follow BaseViewModel + BaseEntryPoint pattern for UI

### Adding a New Screen

1. Create route in `app/navigation/Route.kt`
2. Create ViewModel extending `BaseViewModel<YourUiState>`
3. Create EntryPoint composable using `BaseEntryPoint`
4. Add entry to `Navigator.kt` entryProvider
5. Register ViewModel in appropriate DI module

### Working with State

Always use the `post` function to update state immutably:
```kotlin
fun onAction() {
    post { currentState ->
        currentState.copy(field = newValue)
    }
}
```

## Target SDK & Build Configuration

- **compileSdk**: 36
- **minSdk**: 24
- **targetSdk**: 36
- **Kotlin**: 2.2.0
- **Compose**: Enabled with Kotlin Compiler Extension
- **JVM Toolchain**: 17

## Important Notes

- The app uses **Navigation3** library (not Jetpack Navigation)
- Firebase is required for app functionality
- Language changes require setting `AppCompatDelegate.setApplicationLocales()` - the manifest service handles persistence automatically
- Back button behavior includes double-press-to-exit on root screens (Navigator.kt)
