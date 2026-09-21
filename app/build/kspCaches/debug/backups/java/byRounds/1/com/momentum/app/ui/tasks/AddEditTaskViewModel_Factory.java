package com.momentum.app.ui.tasks;

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
public final class AddEditTaskViewModel_Factory implements Factory<AddEditTaskViewModel> {
  private final Provider<TaskRepository> repositoryProvider;

  public AddEditTaskViewModel_Factory(Provider<TaskRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddEditTaskViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddEditTaskViewModel_Factory create(Provider<TaskRepository> repositoryProvider) {
    return new AddEditTaskViewModel_Factory(repositoryProvider);
  }

  public static AddEditTaskViewModel newInstance(TaskRepository repository) {
    return new AddEditTaskViewModel(repository);
  }
}
