package com.pocketbrain.ui.viewmodel;

import com.pocketbrain.data.preferences.PreferencesManager;
import com.pocketbrain.data.repository.BudgetRepository;
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
    "cast"
})
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<BudgetRepository> budgetRepoProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public SettingsViewModel_Factory(Provider<BudgetRepository> budgetRepoProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.budgetRepoProvider = budgetRepoProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(budgetRepoProvider.get(), preferencesManagerProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<BudgetRepository> budgetRepoProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new SettingsViewModel_Factory(budgetRepoProvider, preferencesManagerProvider);
  }

  public static SettingsViewModel newInstance(BudgetRepository budgetRepo,
      PreferencesManager preferencesManager) {
    return new SettingsViewModel(budgetRepo, preferencesManager);
  }
}
