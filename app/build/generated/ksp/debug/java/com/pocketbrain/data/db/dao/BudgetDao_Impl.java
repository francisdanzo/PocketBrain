package com.pocketbrain.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.pocketbrain.data.db.entities.BudgetEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BudgetEntity> __insertionAdapterOfBudgetEntity;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetEntity = new EntityInsertionAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budget` (`id`,`monthlyIncome`,`dailyLifePct`,`savingsPct`,`investmentPct`,`funPct`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getMonthlyIncome());
        statement.bindDouble(3, entity.getDailyLifePct());
        statement.bindDouble(4, entity.getSavingsPct());
        statement.bindDouble(5, entity.getInvestmentPct());
        statement.bindDouble(6, entity.getFunPct());
      }
    };
  }

  @Override
  public Object insertOrUpdate(final BudgetEntity budget,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBudgetEntity.insert(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BudgetEntity> getBudget() {
    final String _sql = "SELECT * FROM budget WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budget"}, new Callable<BudgetEntity>() {
      @Override
      @Nullable
      public BudgetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMonthlyIncome = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyIncome");
          final int _cursorIndexOfDailyLifePct = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLifePct");
          final int _cursorIndexOfSavingsPct = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsPct");
          final int _cursorIndexOfInvestmentPct = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentPct");
          final int _cursorIndexOfFunPct = CursorUtil.getColumnIndexOrThrow(_cursor, "funPct");
          final BudgetEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpMonthlyIncome;
            _tmpMonthlyIncome = _cursor.getDouble(_cursorIndexOfMonthlyIncome);
            final float _tmpDailyLifePct;
            _tmpDailyLifePct = _cursor.getFloat(_cursorIndexOfDailyLifePct);
            final float _tmpSavingsPct;
            _tmpSavingsPct = _cursor.getFloat(_cursorIndexOfSavingsPct);
            final float _tmpInvestmentPct;
            _tmpInvestmentPct = _cursor.getFloat(_cursorIndexOfInvestmentPct);
            final float _tmpFunPct;
            _tmpFunPct = _cursor.getFloat(_cursorIndexOfFunPct);
            _result = new BudgetEntity(_tmpId,_tmpMonthlyIncome,_tmpDailyLifePct,_tmpSavingsPct,_tmpInvestmentPct,_tmpFunPct);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBudgetOnce(final Continuation<? super BudgetEntity> $completion) {
    final String _sql = "SELECT * FROM budget WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BudgetEntity>() {
      @Override
      @Nullable
      public BudgetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMonthlyIncome = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyIncome");
          final int _cursorIndexOfDailyLifePct = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyLifePct");
          final int _cursorIndexOfSavingsPct = CursorUtil.getColumnIndexOrThrow(_cursor, "savingsPct");
          final int _cursorIndexOfInvestmentPct = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentPct");
          final int _cursorIndexOfFunPct = CursorUtil.getColumnIndexOrThrow(_cursor, "funPct");
          final BudgetEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpMonthlyIncome;
            _tmpMonthlyIncome = _cursor.getDouble(_cursorIndexOfMonthlyIncome);
            final float _tmpDailyLifePct;
            _tmpDailyLifePct = _cursor.getFloat(_cursorIndexOfDailyLifePct);
            final float _tmpSavingsPct;
            _tmpSavingsPct = _cursor.getFloat(_cursorIndexOfSavingsPct);
            final float _tmpInvestmentPct;
            _tmpInvestmentPct = _cursor.getFloat(_cursorIndexOfInvestmentPct);
            final float _tmpFunPct;
            _tmpFunPct = _cursor.getFloat(_cursorIndexOfFunPct);
            _result = new BudgetEntity(_tmpId,_tmpMonthlyIncome,_tmpDailyLifePct,_tmpSavingsPct,_tmpInvestmentPct,_tmpFunPct);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
