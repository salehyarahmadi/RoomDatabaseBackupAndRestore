package ir.androidexception.sqlitebackupandrestoreexample.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import ir.androidexception.sqlitebackupandrestoreexample.database.entity.Category;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

}
