package ir.androidexception.roomdatabasebackupandrestore;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Json {
    private JsonObject jsonObject;

    private Json(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonObject getAsJsonObject() {
        return jsonObject;
    }

    public static class Builder{
        private JsonObject object;
        
        public Builder(){
            this.object = new JsonObject();
        }
        
        public Builder putItem(String key, String value){
            Gson gson = new Gson();
            JsonElement je = gson.toJsonTree(value);
            object.add(key,je);
            return this;
        }
        
        public Builder putItem(String key, ArrayList<Json> items){
            JsonArray jsonArray = new JsonArray();
            for (Json item: items) {
                jsonArray.add(item.getAsJsonObject());
            }
            this.object.add(key,jsonArray);
            return this;
        }

        public Json build(){
            return new Json(this.object);
        }
    }
}
