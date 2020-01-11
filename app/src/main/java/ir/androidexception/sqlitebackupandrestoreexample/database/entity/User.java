package ir.androidexception.sqlitebackupandrestoreexample.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true) private int id;
    @ColumnInfo(name = "first_name") private String firstName;
    @ColumnInfo(name = "last_name") private String lastName;
    @ColumnInfo(name = "profile_url") private String profileUrl;

    public User(String firstName, String lastName, String profileUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileUrl = profileUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
