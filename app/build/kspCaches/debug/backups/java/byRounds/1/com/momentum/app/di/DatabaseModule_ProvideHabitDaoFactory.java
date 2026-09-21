package com.momentum.app.di;

import com.momentum.app.data.local.HabitDao;
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
public final class DatabaseModule_ProvideHabitDaoFactory implements Factory<HabitDao> {
  private final Provider<MomentumDatabase> dbProvider;

  public DatabaseModule_ProvideHabitDaoFactory(Provider<MomentumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public HabitDao get() {
    return provideHabitDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideHabitDaoFactory create(
      Provider<MomentumDatabase> dbProvider) {
    return new DatabaseModule_ProvideHabitDaoFactory(dbProvider);
  }

  public static HabitDao provideHabitDao(MomentumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideHabitDao(db));
  }
}
