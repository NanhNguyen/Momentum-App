package com.momentum.app.ui.habits;

import com.momentum.app.data.repository.HabitRepository;
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
public final class HabitViewModel_Factory implements Factory<HabitViewModel> {
  private final Provider<HabitRepository> repositoryProvider;

  public HabitViewModel_Factory(Provider<HabitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public HabitViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static HabitViewModel_Factory create(Provider<HabitRepository> repositoryProvider) {
    return new HabitViewModel_Factory(repositoryProvider);
  }

  public static HabitViewModel newInstance(HabitRepository repository) {
    return new HabitViewModel(repository);
  }
}
