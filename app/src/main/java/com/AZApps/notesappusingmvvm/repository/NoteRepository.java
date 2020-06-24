package com.AZApps.notesappusingmvvm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.AZApps.notesappusingmvvm.db.NoteDao;
import com.AZApps.notesappusingmvvm.db.NoteDatabase;
import com.AZApps.notesappusingmvvm.pojo.Note;

import java.util.List;

// this class to get the data from the database or internet
//and send the data to viewModule
// here we should do the background threads tasks
public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        // database instance
        NoteDatabase database = NoteDatabase.getInstance(application);
        // ref to the Dao in the database class to access the info from it
        noteDao = database.noteDao();
        // use noteDao to get all notes from the database
        allNotes = noteDao.getAllNotes();
    }

    // def methods for every operation needed (that def in the Dao)
    // these methods will get called when operation is needed
    public void insert(Note note) {
        // here call insert AsyncTask to get the data
        // pass the note you want to insert in execute
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        // here call update AsyncTask to get the data
        // pass the note you want to update in execute
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        // here call delete AsyncTask to remove 1 note
        // pass the note you want to delete in execute
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        // here call delete all notes AsyncTask to remove all the data
        new DeleteAllNotesNoteAsyncTask(noteDao).execute();
    }

    // do not make background thread for this method as Room does it for you
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // make static AsyncTask class for every operation
    // for insert
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        // this ref to access the methods in the Dao
        // initialize  it using the constructor
        private NoteDao noteDao;

        public InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            // now you can access the operation using Dao
            // notes[0] as we pass only 1 note
            noteDao.insert(notes[0]);
            return null;
        }
    }

    // for update
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        // this ref to access the methods in the Dao
        // initialize  it using the constructor
        private NoteDao noteDao;

        public UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            // now you can access the operation using Dao
            // notes[0] as we pass only 1 note
            noteDao.update(notes[0]);
            return null;
        }
    }

    // for delete 1 note
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        // this ref to access the methods in the Dao
        // initialize  it using the constructor
        private NoteDao noteDao;

        public DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            // now you can access the operation using Dao
            // notes[0] as we pass only 1 note
            noteDao.delete(notes[0]);
            return null;
        }
    }

    // for delete all notes do not pass any note
    private static class DeleteAllNotesNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        // this ref to access the methods in the Dao
        // initialize  it using the constructor
        private NoteDao noteDao;

        public DeleteAllNotesNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // now you can access the operation using Dao
            // notes[0] as we pass only 1 note
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
