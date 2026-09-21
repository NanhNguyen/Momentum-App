package com.momentum.app.ui.reflection;

import com.momentum.app.data.repository.ReflectionRepository;
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
public final class ReflectionViewModel_Factory implements Factory<ReflectionViewModel> {
  private final Provider<ReflectionRepository> repositoryProvider;

  public ReflectionViewModel_Factory(Provider<ReflectionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ReflectionViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ReflectionViewModel_Factory create(
      Provider<ReflectionRepository> repositoryProvider) {
    return new ReflectionViewModel_Factory(repositoryProvider);
  }

  public static ReflectionViewModel newInstance(ReflectionRepository repository) {
    return new ReflectionViewModel(repository);
  }
}
