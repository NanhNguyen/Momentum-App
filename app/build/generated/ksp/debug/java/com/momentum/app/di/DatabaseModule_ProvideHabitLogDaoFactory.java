package com.momentum.app.di;

import com.momentum.app.data.local.HabitLogDao;
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
public final class DatabaseModule_ProvideHabitLogDaoFactory implements Factory<HabitLogDao> {
  private final Provider<MomentumDatabase> dbProvider;

  public DatabaseModule_ProvideHabitLogDaoFactory(Provider<MomentumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public HabitLogDao get() {
    return provideHabitLogDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideHabitLogDaoFactory create(
      Provider<MomentumDatabase> dbProvider) {
    return new DatabaseModule_ProvideHabitLogDaoFactory(dbProvider);
  }

  public static HabitLogDao provideHabitLogDao(MomentumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideHabitLogDao(db));
  }
}
