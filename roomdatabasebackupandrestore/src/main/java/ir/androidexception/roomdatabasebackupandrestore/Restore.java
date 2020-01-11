package ir.androidexception.roomdatabasebackupandrestore;

import android.database.Cursor;
import android.util.Log;
import androidx.room.RoomDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Restore {
    public static class Init{
        private RoomDatabase database;
        private String backupFilePath;
        private String secretKey;
        private OnWorkFinishListener onWorkFinishListener;

        public Init database(RoomDatabase database){
            this.database = database;
            return this;
        }

        public Init backupFilePath(String backupFilePath){
            this.backupFilePath = backupFilePath;
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
            try {
                if(database==null){
                    onWorkFinishListener.onFinished(false, "Database not specified");
                    return;
                }
                if(backupFilePath==null){
                    onWorkFinishListener.onFinished(false, "Backup file path not specified");
                    return;
                }
                File fl = new File(backupFilePath);
                FileInputStream fin = new FileInputStream(fl);
                String data = convertStreamToString(fin);
                fin.close();
                String jsonTextDB;
                if(secretKey!=null){
                    jsonTextDB = AESUtils.decrypt(data, secretKey);
                }
                else{
                    jsonTextDB = data;
                }
                JsonObject jsonDB = new Gson().fromJson(jsonTextDB, JsonObject.class);
                for(String table : jsonDB.keySet()){
                    Cursor c = database.query("delete from " + table, null);
                    int p = c.getCount();
                    Log.e("TAG",String.valueOf(p));
                    JsonArray tableArray = jsonDB.get(table).getAsJsonArray();
                    for(int i=0;i<tableArray.size();i++){
                        JsonObject row = tableArray.get(i).getAsJsonObject();
                        String query = "insert into " + table + " (";
                        for(String column : row.keySet()){
                            query = query.concat(column).concat(",");
                        }
                        query = query.substring(0,query.lastIndexOf(","));
                        query = query.concat(") ");
                        query = query.concat("values(");
                        for(String column : row.keySet()){
                            query = query.concat("\'").concat(row.get(column).getAsString()).concat("\'").concat(",");
                        }
                        query = query.substring(0,query.lastIndexOf(","));
                        query = query.concat(")");
                        Cursor cc = database.query(query, null);
                        int pp = cc.getCount();
                        Log.e("TAG",String.valueOf(pp));
                    }
                }
                if(onWorkFinishListener!=null)
                    onWorkFinishListener.onFinished(true, "success");
            } catch (Exception e){
                if(onWorkFinishListener!=null)
                    onWorkFinishListener.onFinished(false, e.toString());
            }
        }
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
