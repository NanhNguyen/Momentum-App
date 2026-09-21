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
public final class AddEditHabitViewModel_Factory implements Factory<AddEditHabitViewModel> {
  private final Provider<HabitRepository> repositoryProvider;

  public AddEditHabitViewModel_Factory(Provider<HabitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddEditHabitViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddEditHabitViewModel_Factory create(Provider<HabitRepository> repositoryProvider) {
    return new AddEditHabitViewModel_Factory(repositoryProvider);
  }

  public static AddEditHabitViewModel newInstance(HabitRepository repository) {
    return new AddEditHabitViewModel(repository);
  }
}
