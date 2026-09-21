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
public final class ReflectionHistoryViewModel_Factory implements Factory<ReflectionHistoryViewModel> {
  private final Provider<ReflectionRepository> repositoryProvider;

  public ReflectionHistoryViewModel_Factory(Provider<ReflectionRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ReflectionHistoryViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ReflectionHistoryViewModel_Factory create(
      Provider<ReflectionRepository> repositoryProvider) {
    return new ReflectionHistoryViewModel_Factory(repositoryProvider);
  }

  public static ReflectionHistoryViewModel newInstance(ReflectionRepository repository) {
    return new ReflectionHistoryViewModel(repository);
  }
}
