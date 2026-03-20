package com.pocketbrain.data.repository;

import com.pocketbrain.data.db.dao.TransactionDao;
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
    "cast"
})
public final class TransactionRepository_Factory implements Factory<TransactionRepository> {
  private final Provider<TransactionDao> daoProvider;

  public TransactionRepository_Factory(Provider<TransactionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public TransactionRepository get() {
    return newInstance(daoProvider.get());
  }

  public static TransactionRepository_Factory create(Provider<TransactionDao> daoProvider) {
    return new TransactionRepository_Factory(daoProvider);
  }

  public static TransactionRepository newInstance(TransactionDao dao) {
    return new TransactionRepository(dao);
  }
}
