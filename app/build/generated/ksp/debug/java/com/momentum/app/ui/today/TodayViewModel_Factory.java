package com.momentum.app.ui.today;

import com.momentum.app.data.repository.EntertainmentRepository;
import com.momentum.app.data.repository.HabitRepository;
import com.momentum.app.data.repository.TaskRepository;
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
public final class TodayViewModel_Factory implements Factory<TodayViewModel> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  private final Provider<HabitRepository> habitRepositoryProvider;

  private final Provider<EntertainmentRepository> entertainmentRepositoryProvider;

  public TodayViewModel_Factory(Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<EntertainmentRepository> entertainmentRepositoryProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
    this.habitRepositoryProvider = habitRepositoryProvider;
    this.entertainmentRepositoryProvider = entertainmentRepositoryProvider;
  }

  @Override
  public TodayViewModel get() {
    return newInstance(taskRepositoryProvider.get(), habitRepositoryProvider.get(), entertainmentRepositoryProvider.get());
  }

  public static TodayViewModel_Factory create(Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<EntertainmentRepository> entertainmentRepositoryProvider) {
    return new TodayViewModel_Factory(taskRepositoryProvider, habitRepositoryProvider, entertainmentRepositoryProvider);
  }

  public static TodayViewModel newInstance(TaskRepository taskRepository,
      HabitRepository habitRepository, EntertainmentRepository entertainmentRepository) {
    return new TodayViewModel(taskRepository, habitRepository, entertainmentRepository);
  }
}
