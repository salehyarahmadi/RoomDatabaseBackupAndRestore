package ir.androidexception.sqlitebackupandrestoreexample.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ir.androidexception.sqlitebackupandrestoreexample.database.dao.CategoryDao;
import ir.androidexception.sqlitebackupandrestoreexample.database.dao.ProductDao;
import ir.androidexception.sqlitebackupandrestoreexample.database.dao.UserDao;
import ir.androidexception.sqlitebackupandrestoreexample.database.entity.Category;
import ir.androidexception.sqlitebackupandrestoreexample.database.entity.Product;
import ir.androidexception.sqlitebackupandrestoreexample.database.entity.User;

@Database(entities = {
        User.class,
        Category.class,
        Product.class
}, version = 2)
public abstract class MyDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract CategoryDao categoryDao();

    public abstract ProductDao productDao();

}
