package com.pocketbrain.ui.viewmodel;

import com.pocketbrain.data.repository.BudgetRepository;
import com.pocketbrain.data.repository.TransactionRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<TransactionRepository> transactionRepoProvider;

  private final Provider<BudgetRepository> budgetRepoProvider;

  public DashboardViewModel_Factory(Provider<TransactionRepository> transactionRepoProvider,
      Provider<BudgetRepository> budgetRepoProvider) {
    this.transactionRepoProvider = transactionRepoProvider;
    this.budgetRepoProvider = budgetRepoProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(transactionRepoProvider.get(), budgetRepoProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<TransactionRepository> transactionRepoProvider,
      Provider<BudgetRepository> budgetRepoProvider) {
    return new DashboardViewModel_Factory(transactionRepoProvider, budgetRepoProvider);
  }

  public static DashboardViewModel newInstance(TransactionRepository transactionRepo,
      BudgetRepository budgetRepo) {
    return new DashboardViewModel(transactionRepo, budgetRepo);
  }
}
