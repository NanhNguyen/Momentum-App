package com.momentum.app.ui.balance;

import com.momentum.app.data.repository.EntertainmentRepository;
import com.momentum.app.data.repository.HabitRepository;
import com.momentum.app.data.repository.SettingsRepository;
import com.momentum.app.data.repository.TaskRepository;
import com.momentum.app.domain.usecase.GetTodayProgressSummaryUseCase;
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

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<GetTodayProgressSummaryUseCase> getTodayProgressSummaryUseCaseProvider;

  public BalanceViewModel_Factory(Provider<EntertainmentRepository> entertainmentRepositoryProvider,
      Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<GetTodayProgressSummaryUseCase> getTodayProgressSummaryUseCaseProvider) {
    this.entertainmentRepositoryProvider = entertainmentRepositoryProvider;
    this.taskRepositoryProvider = taskRepositoryProvider;
    this.habitRepositoryProvider = habitRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.getTodayProgressSummaryUseCaseProvider = getTodayProgressSummaryUseCaseProvider;
  }

  @Override
  public BalanceViewModel get() {
    return newInstance(entertainmentRepositoryProvider.get(), taskRepositoryProvider.get(), habitRepositoryProvider.get(), settingsRepositoryProvider.get(), getTodayProgressSummaryUseCaseProvider.get());
  }

  public static BalanceViewModel_Factory create(
      Provider<EntertainmentRepository> entertainmentRepositoryProvider,
      Provider<TaskRepository> taskRepositoryProvider,
      Provider<HabitRepository> habitRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<GetTodayProgressSummaryUseCase> getTodayProgressSummaryUseCaseProvider) {
    return new BalanceViewModel_Factory(entertainmentRepositoryProvider, taskRepositoryProvider, habitRepositoryProvider, settingsRepositoryProvider, getTodayProgressSummaryUseCaseProvider);
  }

  public static BalanceViewModel newInstance(EntertainmentRepository entertainmentRepository,
      TaskRepository taskRepository, HabitRepository habitRepository,
      SettingsRepository settingsRepository,
      GetTodayProgressSummaryUseCase getTodayProgressSummaryUseCase) {
    return new BalanceViewModel(entertainmentRepository, taskRepository, habitRepository, settingsRepository, getTodayProgressSummaryUseCase);
  }
}
