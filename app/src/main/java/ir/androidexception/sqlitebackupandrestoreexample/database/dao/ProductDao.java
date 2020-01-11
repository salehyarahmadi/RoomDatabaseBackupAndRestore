package ir.androidexception.sqlitebackupandrestoreexample.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import ir.androidexception.sqlitebackupandrestoreexample.database.entity.Product;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

}
