# Reactive Locale Changing Implementation

This document explains how runtime locale changing is implemented in the Persian Literature app using a reactive.

## Overview

The implementation allows users to change the app language at runtime **without requiring an app restart**. All UI elements automatically update when the language changes, thanks to Jetpack Compose's reactive composition system.

## Architecture

### 1. **LocaleHolder Interface** (`settings_api/LocaleHolder.kt`)

The foundation of the reactive locale system. Provides Flow-based observation of locale changes.

```kotlin
interface LocaleHolder {
    val currentLocale: Flow<Locale>
    val systemLocale: Locale
}
```

**Purpose:**
- Defines a contract for reactive locale providers
- Enables any component to observe locale changes
- Separates interface from implementation for better testability

### 2. **LanguageManager Interface** (`settings_api/LanguageManager.kt`)

Extends `LocaleHolder` and adds language management capabilities.

```kotlin
interface LanguageManager : LocaleHolder {
    fun getSavedLanguage(context: Context): Language
    fun setLanguage(context: Context, language: Language)
    val currentLanguage: Flow<Language>
}
```

**Purpose:**
- Combines locale management with language selection
- Provides both immediate and reactive access to language settings
- Manages persistence of language preferences

### 3. **LanguageManagerImpl** (`settings/LanguageManagerImpl.kt`)

Implements reactive language management using SharedPreferences and Kotlin Flows.

**Key Features:**
- **SharedPreferences Persistence:** Saves language choice locally
- **Reactive Updates:** Uses `callbackFlow` to observe SharedPreferences changes
- **Automatic Emission:** Emits updates whenever language preference changes

**Flow Pipeline:**
```
SharedPreferences change
  → callbackFlow listener triggers
  → currentLanguage Flow emits new Language
  → currentLocale Flow maps to Locale
  → All collectors automatically receive update
```

**Implementation Details:**
```kotlin
override val currentLanguage: Flow<Language> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == KEY_LANGUAGE) {
            trySend(getSavedLanguage(context))
        }
    }
    prefs.registerOnSharedPreferenceChangeListener(listener)
    trySend(getSavedLanguage(context)) // Initial value

    awaitClose {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}.distinctUntilChanged()

override val currentLocale: Flow<Locale> = currentLanguage.map { language ->
    Locale(language.code)
}
```

### 4. **CompositionLocal for Locale** (`ui_kit/theme/LocaleComposition.kt`)

Provides locale to the entire Compose tree without prop drilling.

```kotlin
val LocalAppLocale = compositionLocalOf<Locale> {
    error("No Locale provided")
}
```

**Purpose:**
- Makes current locale accessible to any Composable
- Triggers recomposition when locale changes
- Avoids passing locale as parameters through component hierarchies

**Helper Function:**
```kotlin
@Composable
@ReadOnlyComposable
fun localizedContext(): Context {
    val context = LocalContext.current
    val locale = LocalAppLocale.current
    return createLocalizedContext(context, locale)
}
```

### 5. **MainActivity Integration** (`app/MainActivity.kt`)

The composition root that collects and provides locale state.

**Implementation:**
```kotlin
setContent {
    val viewModel = koinViewModel<MainViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect locale from LanguageManager
    val localeHolder = koinInject<LocaleHolder>()
    val currentLocale by localeHolder.currentLocale.collectAsStateWithLifecycle(
        initialValue = localeHolder.systemLocale
    )

    AppTheme {
        // Provide locale to entire Compose tree
        CompositionLocalProvider(LocalAppLocale provides currentLocale) {
            Scaffold(...)
        }
    }
}
```

**Why This Works:**
- `collectAsStateWithLifecycle` converts Flow to Compose State
- When Flow emits new value, `currentLocale` updates
- CompositionLocalProvider recomposes with new locale
- All descendant Composables automatically recompose

### 6. **LanguageScreen** (`settings/LanguageScreen.kt`)

Updated to use reactive locale changing.

**Before (Old Approach):**
```kotlin
// Required using Android APIs and app restart
AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
```

**After (Reactive Approach):**
```kotlin
@Composable
fun LanguageEntryPoint(onBackClick: () -> Unit) {
    BaseEntryPoint(LanguageViewModel::class) { state, viewModel ->
        // Update Locale.setDefault for immediate effect
        val currentLocale = LocalAppLocale.current
        LaunchedEffect(currentLocale) {
            Locale.setDefault(currentLocale)
        }

        LanguageScreen(
            state = state,
            onBackClick = onBackClick,
            onLanguageSelected = viewModel::onLanguageSelected,
            onApplyClick = viewModel::onApplyClick
        )
    }
}
```

**Key Changes:**
- Removed `changeLocale()` function
- No longer uses `AppCompatDelegate` or `LocaleManager`
- Simply updates SharedPreferences, which triggers reactive flow
- Added `Locale.setDefault()` for immediate system locale update

## Complete Flow Diagram

```
User clicks "Apply"
    ↓
LanguageViewModel.onApplyClick()
    ↓
LanguageManager.setLanguage()
    ↓
SharedPreferences.edit { putString(KEY_LANGUAGE, code) }
    ↓
OnSharedPreferenceChangeListener triggers
    ↓
currentLanguage Flow emits new Language
    ↓
currentLocale Flow maps and emits new Locale
    ↓
MainActivity collectAsStateWithLifecycle receives update
    ↓
currentLocale State updates
    ↓
CompositionLocalProvider recomposes with new locale
    ↓
LocalAppLocale.current changes throughout app
    ↓
All Composables reading LocalAppLocale recompose
    ↓
UI instantly updates with new language strings
```

## Benefits of This Approach

### 1. **No App Restart Required**
- Changes take effect immediately
- Better user experience
- Faster testing during development

### 2. **Reactive and Automatic**
- Compose automatically handles recomposition
- No manual view updates needed
- Type-safe and compile-time checked

### 3. **Separation of Concerns**
- UI layer only reads locale
- Data layer manages persistence
- Domain layer provides reactive streams

### 4. **Testable**
- LocaleHolder is an interface (mockable)
- Flows can be tested independently
- No Android framework dependencies in tests

### 5. **Consistent State**
- Single source of truth (SharedPreferences)
- All components observe the same Flow
- No state synchronization issues

## Usage in Other Screens

Any Composable can access the current locale:

```kotlin
@Composable
fun MyScreen() {
    val locale = LocalAppLocale.current

    // Locale automatically updates when user changes language
    Text(text = stringResource(R.string.my_text))
}
```

For accessing localized Context:

```kotlin
@Composable
fun MyScreen() {
    val localizedContext = localizedContext()

    // Use localizedContext for Android APIs that need Context
    val localizedString = localizedContext.getString(R.string.my_text)
}
```

## Dependency Injection (Koin)

The Koin module is configured in `app/di/Module.kt`:

```kotlin
val appModule = module {
    single<LanguageManager> { LanguageManagerImpl(get()) }
    // ... other dependencies
}
```

This ensures:
- Single instance of LanguageManager
- Context is automatically injected
- Interface binding for testability

## Migration Notes

### What Changed:

1. ✅ Added `LocaleHolder` interface
2. ✅ Made `LanguageManager` extend `LocaleHolder`
3. ✅ Added Flow support to `LanguageManagerImpl`
4. ✅ Created `LocalAppLocale` CompositionLocal
5. ✅ Updated MainActivity to provide locale reactively
6. ✅ Simplified LanguageScreen (removed Android API calls)
7. ✅ Updated Koin module for constructor injection

### What Stayed the Same:

- Language enum definition
- SharedPreferences storage
- UI components (RadioButton, PrimaryButton, etc.)
- Navigation flow
- Data refresh logic after language change

## Testing Locale Changes

1. **Run the app**
2. **Navigate to Settings → Language**
3. **Select a different language**
4. **Click "Apply"**
5. **Observe:** UI updates instantly without app restart

## Troubleshooting

### Issue: Locale doesn't update
**Solution:** Ensure `LocalAppLocale` is provided in CompositionLocalProvider

### Issue: Some strings don't update
**Solution:** Make sure all Composables use `stringResource()` or `LocalAppLocale.current`

### Issue: Compilation error about Context
**Solution:** Verify LanguageManagerImpl receives Context through Koin injection

## Comparison with Old Approach

| Aspect | Old Approach | New Reactive Approach |
|--------|-------------|----------------------|
| **Restart Required** | Yes (Activity recreation) | No (instant update) |
| **Implementation** | Android API calls | Reactive Flows |
| **Testability** | Difficult (Android dependencies) | Easy (interface-based) |
| **State Management** | Imperative | Declarative |
| **Compose Integration** | Manual | Automatic |
| **Type Safety** | Runtime checks | Compile-time checks |
