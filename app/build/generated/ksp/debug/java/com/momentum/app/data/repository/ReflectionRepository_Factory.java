package com.momentum.app.data.repository;

import com.momentum.app.data.local.ReflectionDao;
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
public final class ReflectionRepository_Factory implements Factory<ReflectionRepository> {
  private final Provider<ReflectionDao> daoProvider;

  public ReflectionRepository_Factory(Provider<ReflectionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ReflectionRepository get() {
    return newInstance(daoProvider.get());
  }

  public static ReflectionRepository_Factory create(Provider<ReflectionDao> daoProvider) {
    return new ReflectionRepository_Factory(daoProvider);
  }

  public static ReflectionRepository newInstance(ReflectionDao dao) {
    return new ReflectionRepository(dao);
  }
}
