package com.momentum.app.ui.settings;

import com.momentum.app.data.repository.BackupRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<BackupRepository> backupRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public SettingsViewModel_Factory(Provider<BackupRepository> backupRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.backupRepositoryProvider = backupRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(backupRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new SettingsViewModel_Factory(backupRepositoryProvider, settingsRepositoryProvider);
  }

  public static SettingsViewModel newInstance(BackupRepository backupRepository,
      SettingsRepository settingsRepository) {
    return new SettingsViewModel(backupRepository, settingsRepository);
  }
}
