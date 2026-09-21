package com.momentum.app.data.repository;

import com.momentum.app.data.local.AppSettingsDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class SettingsRepository_Factory implements Factory<SettingsRepository> {
  private final Provider<AppSettingsDao> daoProvider;

  public SettingsRepository_Factory(Provider<AppSettingsDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public SettingsRepository get() {
    return newInstance(daoProvider.get());
  }

  public static SettingsRepository_Factory create(Provider<AppSettingsDao> daoProvider) {
    return new SettingsRepository_Factory(daoProvider);
  }

  public static SettingsRepository newInstance(AppSettingsDao dao) {
    return new SettingsRepository(dao);
  }
}
