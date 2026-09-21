package com.momentum.app.ui.onboarding;

import com.momentum.app.data.repository.HabitRepository;
import com.momentum.app.data.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<HabitRepository> habitRepositoryProvider;

  public OnboardingViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.habitRepositoryProvider = habitRepositoryProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(settingsRepositoryProvider.get(), habitRepositoryProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider) {
    return new OnboardingViewModel_Factory(settingsRepositoryProvider, habitRepositoryProvider);
  }

  public static OnboardingViewModel newInstance(SettingsRepository settingsRepository,
      HabitRepository habitRepository) {
    return new OnboardingViewModel(settingsRepository, habitRepository);
  }
}
