package ir.androidexception.sqlitebackupandrestoreexample.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import ir.androidexception.sqlitebackupandrestoreexample.database.entity.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

}
