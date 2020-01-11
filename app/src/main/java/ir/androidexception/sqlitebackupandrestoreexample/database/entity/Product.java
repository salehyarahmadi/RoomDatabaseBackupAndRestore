package ir.androidexception.sqlitebackupandrestoreexample.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true) private int id;
    @ColumnInfo(name = "code") private String code;
    @ColumnInfo(name = "name") private String name;
    @ColumnInfo(name = "price") private String price;
    @ColumnInfo(name = "category_id") private int categoryId;

    public Product(String code, String name, String price, int categoryId) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
