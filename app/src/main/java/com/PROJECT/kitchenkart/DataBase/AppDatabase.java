package com.PROJECT.kitchenkart.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.PROJECT.kitchenkart.DataBase.UserDao;
import com.PROJECT.kitchenkart.Models.User;

// Increment version from 1 to 2
@Database(entities = {User.class}, version = 2, exportSchema = false) // <--- Changed version to 2
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "kitchenkart_database")
                            .fallbackToDestructiveMigration() // <--- This will wipe old data on upgrade
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}