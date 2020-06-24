package com.AZApps.notesappusingmvvm.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.AZApps.notesappusingmvvm.pojo.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase INSTANCE;

    // method to access Dao operations
    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                    , NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    // after finishing the code below attach the callback here
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;
    }

    /*
     * The next code is to populate (add some default notes) the database
     * the first time the database is created only and this can be done
     * by using RoomDatabase.Callback and override on create method (for the database)
     * and use AsyncTask to add the data (default notes) on the background thread
     * RoomDatabase.Callback must be static to be called in getInstance
     * ( because it is a static method)
     */
    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // here execute populateDbAsyncTask
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        // ref to use Dao operations
        NoteDao noteDao;

        public PopulateDbAsyncTask(NoteDatabase db) {
            // pass the Database not the Dao
            // because we do not have a Dao global variable
            // and then reference the noteDao throw the database
            this.noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }
    }
}
