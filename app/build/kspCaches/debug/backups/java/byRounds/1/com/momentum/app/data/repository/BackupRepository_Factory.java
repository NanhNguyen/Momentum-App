package com.momentum.app.data.repository;

import com.momentum.app.data.local.AppSettingsDao;
import com.momentum.app.data.local.EntertainmentDao;
import com.momentum.app.data.local.HabitDao;
import com.momentum.app.data.local.HabitLogDao;
import com.momentum.app.data.local.MomentumDatabase;
import com.momentum.app.data.local.ReflectionDao;
import com.momentum.app.data.local.TaskDao;
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
public final class BackupRepository_Factory implements Factory<BackupRepository> {
  private final Provider<MomentumDatabase> databaseProvider;

  private final Provider<TaskDao> taskDaoProvider;

  private final Provider<HabitDao> habitDaoProvider;

  private final Provider<HabitLogDao> habitLogDaoProvider;

  private final Provider<ReflectionDao> reflectionDaoProvider;

  private final Provider<EntertainmentDao> entertainmentDaoProvider;

  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  public BackupRepository_Factory(Provider<MomentumDatabase> databaseProvider,
      Provider<TaskDao> taskDaoProvider, Provider<HabitDao> habitDaoProvider,
      Provider<HabitLogDao> habitLogDaoProvider, Provider<ReflectionDao> reflectionDaoProvider,
      Provider<EntertainmentDao> entertainmentDaoProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    this.databaseProvider = databaseProvider;
    this.taskDaoProvider = taskDaoProvider;
    this.habitDaoProvider = habitDaoProvider;
    this.habitLogDaoProvider = habitLogDaoProvider;
    this.reflectionDaoProvider = reflectionDaoProvider;
    this.entertainmentDaoProvider = entertainmentDaoProvider;
    this.appSettingsDaoProvider = appSettingsDaoProvider;
  }

  @Override
  public BackupRepository get() {
    return newInstance(databaseProvider.get(), taskDaoProvider.get(), habitDaoProvider.get(), habitLogDaoProvider.get(), reflectionDaoProvider.get(), entertainmentDaoProvider.get(), appSettingsDaoProvider.get());
  }

  public static BackupRepository_Factory create(Provider<MomentumDatabase> databaseProvider,
      Provider<TaskDao> taskDaoProvider, Provider<HabitDao> habitDaoProvider,
      Provider<HabitLogDao> habitLogDaoProvider, Provider<ReflectionDao> reflectionDaoProvider,
      Provider<EntertainmentDao> entertainmentDaoProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new BackupRepository_Factory(databaseProvider, taskDaoProvider, habitDaoProvider, habitLogDaoProvider, reflectionDaoProvider, entertainmentDaoProvider, appSettingsDaoProvider);
  }

  public static BackupRepository newInstance(MomentumDatabase database, TaskDao taskDao,
      HabitDao habitDao, HabitLogDao habitLogDao, ReflectionDao reflectionDao,
      EntertainmentDao entertainmentDao, AppSettingsDao appSettingsDao) {
    return new BackupRepository(database, taskDao, habitDao, habitLogDao, reflectionDao, entertainmentDao, appSettingsDao);
  }
}
