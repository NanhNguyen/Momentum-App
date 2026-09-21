package com.momentum.app.di;

import android.content.Context;
import com.momentum.app.data.local.MomentumDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideMomentumDatabaseFactory implements Factory<MomentumDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideMomentumDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MomentumDatabase get() {
    return provideMomentumDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideMomentumDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideMomentumDatabaseFactory(contextProvider);
  }

  public static MomentumDatabase provideMomentumDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMomentumDatabase(context));
  }
}
