package com.momentum.app.di;

import com.momentum.app.data.local.AppSettingsDao;
import com.momentum.app.data.local.MomentumDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAppSettingsDaoFactory implements Factory<AppSettingsDao> {
  private final Provider<MomentumDatabase> dbProvider;

  public DatabaseModule_ProvideAppSettingsDaoFactory(Provider<MomentumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AppSettingsDao get() {
    return provideAppSettingsDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAppSettingsDaoFactory create(
      Provider<MomentumDatabase> dbProvider) {
    return new DatabaseModule_ProvideAppSettingsDaoFactory(dbProvider);
  }

  public static AppSettingsDao provideAppSettingsDao(MomentumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAppSettingsDao(db));
  }
}
