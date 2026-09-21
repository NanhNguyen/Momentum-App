package com.momentum.app.ui.balance;

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
public final class BalanceViewModel_Factory implements Factory<BalanceViewModel> {
  private final Provider<EntertainmentRepository> entertainmentRepositoryProvider;

  private final Provider<TaskRepository> taskRepositoryProvider;

  private final Provider<HabitRepository> habitRepositoryProvider;

  public BalanceViewModel_Factory(Provider<EntertainmentRepository> entertainmentRepositoryProvider,
      Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider) {
    this.entertainmentRepositoryProvider = entertainmentRepositoryProvider;
    this.taskRepositoryProvider = taskRepositoryProvider;
    this.habitRepositoryProvider = habitRepositoryProvider;
  }

  @Override
  public BalanceViewModel get() {
    return newInstance(entertainmentRepositoryProvider.get(), taskRepositoryProvider.get(), habitRepositoryProvider.get());
  }

  public static BalanceViewModel_Factory create(
      Provider<EntertainmentRepository> entertainmentRepositoryProvider,
      Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider) {
    return new BalanceViewModel_Factory(entertainmentRepositoryProvider, taskRepositoryProvider, habitRepositoryProvider);
  }

  public static BalanceViewModel newInstance(EntertainmentRepository entertainmentRepository,
      TaskRepository taskRepository, HabitRepository habitRepository) {
    return new BalanceViewModel(entertainmentRepository, taskRepository, habitRepository);
  }
}
