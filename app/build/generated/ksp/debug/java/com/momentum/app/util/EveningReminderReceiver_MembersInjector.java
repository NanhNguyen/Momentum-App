package com.momentum.app.util;

import com.momentum.app.data.local.AppSettingsDao;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class EveningReminderReceiver_MembersInjector implements MembersInjector<EveningReminderReceiver> {
  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  public EveningReminderReceiver_MembersInjector(Provider<AppSettingsDao> appSettingsDaoProvider) {
    this.appSettingsDaoProvider = appSettingsDaoProvider;
  }

  public static MembersInjector<EveningReminderReceiver> create(
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new EveningReminderReceiver_MembersInjector(appSettingsDaoProvider);
  }

  @Override
  public void injectMembers(EveningReminderReceiver instance) {
    injectAppSettingsDao(instance, appSettingsDaoProvider.get());
  }

  @InjectedFieldSignature("com.momentum.app.util.EveningReminderReceiver.appSettingsDao")
  public static void injectAppSettingsDao(EveningReminderReceiver instance,
      AppSettingsDao appSettingsDao) {
    instance.appSettingsDao = appSettingsDao;
  }
}
