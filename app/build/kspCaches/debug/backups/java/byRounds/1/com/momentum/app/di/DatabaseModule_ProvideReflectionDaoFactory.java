package com.momentum.app.di;

import com.momentum.app.data.local.MomentumDatabase;
import com.momentum.app.data.local.ReflectionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideReflectionDaoFactory implements Factory<ReflectionDao> {
  private final Provider<MomentumDatabase> dbProvider;

  public DatabaseModule_ProvideReflectionDaoFactory(Provider<MomentumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ReflectionDao get() {
    return provideReflectionDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideReflectionDaoFactory create(
      Provider<MomentumDatabase> dbProvider) {
    return new DatabaseModule_ProvideReflectionDaoFactory(dbProvider);
  }

  public static ReflectionDao provideReflectionDao(MomentumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideReflectionDao(db));
  }
}
