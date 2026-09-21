package com.momentum.app.data.repository;

import com.momentum.app.data.local.EntertainmentDao;
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
public final class EntertainmentRepository_Factory implements Factory<EntertainmentRepository> {
  private final Provider<EntertainmentDao> daoProvider;

  public EntertainmentRepository_Factory(Provider<EntertainmentDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public EntertainmentRepository get() {
    return newInstance(daoProvider.get());
  }

  public static EntertainmentRepository_Factory create(Provider<EntertainmentDao> daoProvider) {
    return new EntertainmentRepository_Factory(daoProvider);
  }

  public static EntertainmentRepository newInstance(EntertainmentDao dao) {
    return new EntertainmentRepository(dao);
  }
}
