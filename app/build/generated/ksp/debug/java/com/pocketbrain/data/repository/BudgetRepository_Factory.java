package com.pocketbrain.data.repository;

import com.pocketbrain.data.db.dao.BudgetDao;
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
public final class BudgetRepository_Factory implements Factory<BudgetRepository> {
  private final Provider<BudgetDao> daoProvider;

  public BudgetRepository_Factory(Provider<BudgetDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public BudgetRepository get() {
    return newInstance(daoProvider.get());
  }

  public static BudgetRepository_Factory create(Provider<BudgetDao> daoProvider) {
    return new BudgetRepository_Factory(daoProvider);
  }

  public static BudgetRepository newInstance(BudgetDao dao) {
    return new BudgetRepository(dao);
  }
}
