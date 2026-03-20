package com.pocketbrain.ui.viewmodel;

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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<TransactionRepository> repoProvider;

  public HistoryViewModel_Factory(Provider<TransactionRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<TransactionRepository> repoProvider) {
    return new HistoryViewModel_Factory(repoProvider);
  }

  public static HistoryViewModel newInstance(TransactionRepository repo) {
    return new HistoryViewModel(repo);
  }
}
