package ir.androidexception.roomdatabasebackupandrestore;

import android.database.Cursor;
import androidx.room.RoomDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Backup {
    private static ArrayList<String> SQLITE_TABLES = new ArrayList<String>() {{
        add("android_metadata");
        add("room_master_table");
        add("sqlite_sequence");
    }};
    private static boolean isInSQLiteTables(String table){
        return SQLITE_TABLES.contains(table);
    }
    private static String STRING_FOR_NULL_VALUE = "!!!string_for_null_value!!!";

    public static class Init{
        private RoomDatabase database;
        private String path;
        private String fileName;
        private String secretKey;
        private OnWorkFinishListener onWorkFinishListener;

        public Init database(RoomDatabase database){
            this.database = database;
            return this;
        }

        public Init path(String path){
            this.path = path;
            return this;
        }

        public Init fileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        public Init secretKey(String secretKey){
            this.secretKey = secretKey;
            return this;
        }

        public Init onWorkFinishListener(OnWorkFinishListener onWorkFinishListener){
            this.onWorkFinishListener = onWorkFinishListener;
            return this;
        }

        public void execute(){
            if(database==null){
                onWorkFinishListener.onFinished(false, "Database not specified");
                return;
            }
            if(path==null){
                onWorkFinishListener.onFinished(false, "Backup path not specified");
                return;
            }
            if(fileName==null){
                onWorkFinishListener.onFinished(false, "Backup file name not specified");
                return;
            }
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
                                rowBuilder.putItem(columnName,(rowsCursor.getString(i)!=null) ? rowsCursor.getString(i) : STRING_FOR_NULL_VALUE);
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
                try {
                    byte[] data;
                    if(secretKey!=null) {
                        String encryptedJsonTextDB = AESUtils.encrypt(jsonTextDB, secretKey);
                        data = encryptedJsonTextDB.getBytes("UTF8");
                    }
                    else{
                        data = jsonTextDB.getBytes("UTF8");
                    }
                    File root = new File(path);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File dFile = new File(root, fileName);
                    FileWriter writer = new FileWriter(dFile);
                    writer.append(new String(data));
                    writer.flush();
                    writer.close();
                    if(onWorkFinishListener!=null)
                        onWorkFinishListener.onFinished(true, "success");
                } catch (Exception e) {
                    if(onWorkFinishListener!=null)
                        onWorkFinishListener.onFinished(false,e.toString());
                }
            }
        }
    }
}
