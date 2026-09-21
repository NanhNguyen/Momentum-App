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
public final class TaskViewModel_Factory implements Factory<TaskViewModel> {
  private final Provider<TaskRepository> repositoryProvider;

  public TaskViewModel_Factory(Provider<TaskRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TaskViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static TaskViewModel_Factory create(Provider<TaskRepository> repositoryProvider) {
    return new TaskViewModel_Factory(repositoryProvider);
  }

  public static TaskViewModel newInstance(TaskRepository repository) {
    return new TaskViewModel(repository);
  }
}
