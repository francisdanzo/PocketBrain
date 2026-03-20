package com.pocketbrain.ui.viewmodel;

import android.content.Context;
import com.pocketbrain.data.repository.BudgetRepository;
import com.pocketbrain.data.repository.TransactionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
    "cast"
})
public final class ReportsViewModel_Factory implements Factory<ReportsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<TransactionRepository> transactionRepoProvider;

  private final Provider<BudgetRepository> budgetRepoProvider;

  public ReportsViewModel_Factory(Provider<Context> contextProvider,
      Provider<TransactionRepository> transactionRepoProvider,
      Provider<BudgetRepository> budgetRepoProvider) {
    this.contextProvider = contextProvider;
    this.transactionRepoProvider = transactionRepoProvider;
    this.budgetRepoProvider = budgetRepoProvider;
  }

  @Override
  public ReportsViewModel get() {
    return newInstance(contextProvider.get(), transactionRepoProvider.get(), budgetRepoProvider.get());
  }

  public static ReportsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<TransactionRepository> transactionRepoProvider,
      Provider<BudgetRepository> budgetRepoProvider) {
    return new ReportsViewModel_Factory(contextProvider, transactionRepoProvider, budgetRepoProvider);
  }

  public static ReportsViewModel newInstance(Context context, TransactionRepository transactionRepo,
      BudgetRepository budgetRepo) {
    return new ReportsViewModel(context, transactionRepo, budgetRepo);
  }
}
