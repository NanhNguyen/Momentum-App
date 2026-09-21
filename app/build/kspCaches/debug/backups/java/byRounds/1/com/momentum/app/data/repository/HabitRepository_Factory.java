package com.momentum.app.data.repository;

import com.momentum.app.data.local.HabitDao;
import com.momentum.app.data.local.HabitLogDao;
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
public final class HabitRepository_Factory implements Factory<HabitRepository> {
  private final Provider<HabitDao> habitDaoProvider;

  private final Provider<HabitLogDao> logDaoProvider;

  public HabitRepository_Factory(Provider<HabitDao> habitDaoProvider,
      Provider<HabitLogDao> logDaoProvider) {
    this.habitDaoProvider = habitDaoProvider;
    this.logDaoProvider = logDaoProvider;
  }

  @Override
  public HabitRepository get() {
    return newInstance(habitDaoProvider.get(), logDaoProvider.get());
  }

  public static HabitRepository_Factory create(Provider<HabitDao> habitDaoProvider,
      Provider<HabitLogDao> logDaoProvider) {
    return new HabitRepository_Factory(habitDaoProvider, logDaoProvider);
  }

  public static HabitRepository newInstance(HabitDao habitDao, HabitLogDao logDao) {
    return new HabitRepository(habitDao, logDao);
  }
}
