package com.momentum.app.domain.usecase;

import com.momentum.app.data.repository.EntertainmentRepository;
import com.momentum.app.data.repository.HabitRepository;
import com.momentum.app.data.repository.TaskRepository;
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
public final class GetTodayProgressSummaryUseCase_Factory implements Factory<GetTodayProgressSummaryUseCase> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  private final Provider<HabitRepository> habitRepositoryProvider;

  private final Provider<EntertainmentRepository> entertainmentRepositoryProvider;

  public GetTodayProgressSummaryUseCase_Factory(Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<EntertainmentRepository> entertainmentRepositoryProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
    this.habitRepositoryProvider = habitRepositoryProvider;
    this.entertainmentRepositoryProvider = entertainmentRepositoryProvider;
  }

  @Override
  public GetTodayProgressSummaryUseCase get() {
    return newInstance(taskRepositoryProvider.get(), habitRepositoryProvider.get(), entertainmentRepositoryProvider.get());
  }

  public static GetTodayProgressSummaryUseCase_Factory create(
      Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<EntertainmentRepository> entertainmentRepositoryProvider) {
    return new GetTodayProgressSummaryUseCase_Factory(taskRepositoryProvider, habitRepositoryProvider, entertainmentRepositoryProvider);
  }

  public static GetTodayProgressSummaryUseCase newInstance(TaskRepository taskRepository,
      HabitRepository habitRepository, EntertainmentRepository entertainmentRepository) {
    return new GetTodayProgressSummaryUseCase(taskRepository, habitRepository, entertainmentRepository);
  }
}
