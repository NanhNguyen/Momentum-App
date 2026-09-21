package com.momentum.app.ui.insights;

import com.momentum.app.data.repository.HabitRepository;
import com.momentum.app.data.repository.ReflectionRepository;
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
public final class InsightsViewModel_Factory implements Factory<InsightsViewModel> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  private final Provider<HabitRepository> habitRepositoryProvider;

  private final Provider<ReflectionRepository> reflectionRepositoryProvider;

  public InsightsViewModel_Factory(Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<ReflectionRepository> reflectionRepositoryProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
    this.habitRepositoryProvider = habitRepositoryProvider;
    this.reflectionRepositoryProvider = reflectionRepositoryProvider;
  }

  @Override
  public InsightsViewModel get() {
    return newInstance(taskRepositoryProvider.get(), habitRepositoryProvider.get(), reflectionRepositoryProvider.get());
  }

  public static InsightsViewModel_Factory create(Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<ReflectionRepository> reflectionRepositoryProvider) {
    return new InsightsViewModel_Factory(taskRepositoryProvider, habitRepositoryProvider, reflectionRepositoryProvider);
  }

  public static InsightsViewModel newInstance(TaskRepository taskRepository,
      HabitRepository habitRepository, ReflectionRepository reflectionRepository) {
    return new InsightsViewModel(taskRepository, habitRepository, reflectionRepository);
  }
}
