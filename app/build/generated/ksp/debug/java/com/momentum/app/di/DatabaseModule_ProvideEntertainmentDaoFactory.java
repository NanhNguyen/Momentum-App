package com.momentum.app.di;

import com.momentum.app.data.local.EntertainmentDao;
import com.momentum.app.data.local.MomentumDatabase;
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
public final class DatabaseModule_ProvideEntertainmentDaoFactory implements Factory<EntertainmentDao> {
  private final Provider<MomentumDatabase> dbProvider;

  public DatabaseModule_ProvideEntertainmentDaoFactory(Provider<MomentumDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EntertainmentDao get() {
    return provideEntertainmentDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideEntertainmentDaoFactory create(
      Provider<MomentumDatabase> dbProvider) {
    return new DatabaseModule_ProvideEntertainmentDaoFactory(dbProvider);
  }

  public static EntertainmentDao provideEntertainmentDao(MomentumDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEntertainmentDao(db));
  }
}
