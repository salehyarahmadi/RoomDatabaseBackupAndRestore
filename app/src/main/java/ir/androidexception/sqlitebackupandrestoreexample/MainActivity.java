package ir.androidexception.sqlitebackupandrestoreexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import ir.androidexception.roomdatabasebackupandrestore.Backup;
import ir.androidexception.roomdatabasebackupandrestore.Restore;
import ir.androidexception.sqlitebackupandrestoreexample.database.MyDatabase;
import ir.androidexception.sqlitebackupandrestoreexample.database.entity.Category;
import ir.androidexception.sqlitebackupandrestoreexample.database.entity.Product;
import ir.androidexception.sqlitebackupandrestoreexample.database.entity.User;

public class MainActivity extends AppCompatActivity {
    private MyDatabase database;
    private ArrayList<String> SQLITE_TABLES = new ArrayList<String>() {{
            add("android_metadata");
            add("room_master_table");
            add("sqlite_sequence");
    }};
    private boolean isInSQLiteTables(String table){
        return SQLITE_TABLES.contains(table);
    }

    private Button insert;
    private Button delete;
    private Button backup;
    private Button restore;
    private Button show;
    private TextView values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = Room.databaseBuilder(this, MyDatabase.class, "MyDatabase").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        insert = findViewById(R.id.btn_insert);
        delete = findViewById(R.id.btn_delete);
        backup = findViewById(R.id.btn_backup);
        restore = findViewById(R.id.btn_restore);
        show = findViewById(R.id.btn_show);
        values = findViewById(R.id.tv_database_value);
        insert.setOnClickListener(v -> insertDataToDatabase());
        delete.setOnClickListener(v -> delete());
        backup.setOnClickListener(v -> backup());
        restore.setOnClickListener(v -> restore());
        show.setOnClickListener(v -> show());
    }


    private void insertDataToDatabase(){
        database.userDao().insert(new User("Dorris","Yukhnin","Dorris Profile Url"));
        database.userDao().insert(new User("Laureen","Vallentin","Laureen Profile Url"));
        database.userDao().insert(new User("Zachery","Tremmel","Zachery Profile Url"));
        database.userDao().insert(new User("Caresa","Wickett","Caresa Profile Url"));
        database.userDao().insert(new User("Iorgos","Annies","Iorgos Profile Url"));
        database.categoryDao().insert(new Category("10001" , "sandwich"));
        database.categoryDao().insert(new Category("10002" , "burger"));
        database.productDao().insert(new Product("20001","sandwich 1" , "10$", 1));
        database.productDao().insert(new Product("20002","sandwich 2" , "12$", 1));
        database.productDao().insert(new Product("20003","sandwich 3" , "9$", 1));
        database.productDao().insert(new Product("20004","sandwich 4" , "23$", 1));
        database.productDao().insert(new Product("20005","sandwich 5" , "18$", 1));
        database.productDao().insert(new Product("20006","burger 1" , "14$", 2));
        database.productDao().insert(new Product("20007","burger 2" , "5$", 2));
        database.productDao().insert(new Product("20008","burger 3" , "30$", 2));
        database.productDao().insert(new Product("20009","burger 4" , "27$", 2));
        database.productDao().insert(new Product("20010","burger 5" , "11$", 2));
        show();
    }

    private void delete(){
        Cursor tablesCursor = database.query("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String> tables = new ArrayList<>();
        if (tablesCursor.moveToFirst()) {
            while (!tablesCursor.isAfterLast()) {
                tables.add(tablesCursor.getString(0));
                tablesCursor.moveToNext();
            }
            for(String table : tables) {
                if(isInSQLiteTables(table)) continue;
                try {
                    Cursor f = database.query("delete from " + table, null);
                    int gg = f.getCount();
                    int ggg = f.getColumnCount();
                    Log.e("fvf",String.valueOf(ggg));
                }catch (Exception e){
                    Log.e("rf", e.toString());
                }
            }
        }
        show();
    }

    private void backup(){
        new Backup.Init()
                .database(database)
                .path(Environment.getExternalStorageDirectory().getPath())
                .fileName("MyBackupDatabaseEncrypted.txt")
                .secretKey("SalehYarahmadi")
                .onWorkFinishListener((success, message) -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show())
                .execute();

    }

    private void restore(){
        new Restore.Init()
                .database(database)
                .backupFilePath(Environment.getExternalStorageDirectory() + File.separator + "MyBackupDatabaseEncrypted.txt")
                .secretKey("SalehYarahmadi")
                .onWorkFinishListener((success, message) -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    show();
                })
                .execute();
    }

    private void show(){
        Cursor tablesCursor = database.query("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String> tables = new ArrayList<>();
        Json.Builder dbBuilder = new Json.Builder();
        if (tablesCursor.moveToFirst()) {
            while ( !tablesCursor.isAfterLast() ) {
                tables.add(tablesCursor.getString(0));
                tablesCursor.moveToNext();
            }
            for(String table : tables) {
                if(isInSQLiteTables(table)) continue;
                ArrayList<Json> rows = new ArrayList<>();
                Cursor rowsCursor = database.query("select * from " + table,null);
                Cursor tableSqlCursor = database.query("select sql from sqlite_master where name= \'"+ table + "\'" , null);
                tableSqlCursor.moveToFirst();
                String tableSql = tableSqlCursor.getString(0);
                tableSql = tableSql.substring(tableSql.indexOf("("));
                String aic = "";
                if(tableSql.contains("AUTOINCREMENT")){
                    tableSql = tableSql.substring(0,tableSql.indexOf("AUTOINCREMENT"));
                    tableSql = tableSql.substring(0,tableSql.lastIndexOf("`"));
                    aic = tableSql.substring(tableSql.lastIndexOf("`") + 1);
                }
                if (rowsCursor.moveToFirst()) {
                    do {
                        int columnCount = rowsCursor.getColumnCount();
                        Json.Builder rowBuilder = new Json.Builder();
                        for(int i=0; i<columnCount; i++){
                            String columnName = rowsCursor.getColumnName(i);
                            if(columnName.equals(aic)) continue;
                            rowBuilder.putItem(columnName,(rowsCursor.getString(i)!=null) ? rowsCursor.getString(i) : "");
                        }
                        rows.add(rowBuilder.build());
                    } while (rowsCursor.moveToNext());
                }
                dbBuilder.putItem(table,rows);
            }
            JsonObject jsonDB = dbBuilder.build().getAsJsonObject();
            Gson gson = new Gson();
            Type type = new TypeToken<JsonObject>(){}.getType();
            String jsonTextDB = gson.toJson(jsonDB, type);
            jsonTextDB = jsonTextDB.replaceAll("\\[" , "\n[\n\t");
            jsonTextDB = jsonTextDB.replaceAll("\\}," , "},\n\t");
            jsonTextDB = jsonTextDB.replaceAll("\\]," , "\n],\n");
            jsonTextDB = jsonTextDB.replaceAll("\\]\\}" , "\n]}");
            values.setText(jsonTextDB);
        }
    }

}
