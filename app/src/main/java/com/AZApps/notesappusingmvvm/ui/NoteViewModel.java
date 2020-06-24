package com.AZApps.notesappusingmvvm.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.AZApps.notesappusingmvvm.pojo.Note;
import com.AZApps.notesappusingmvvm.repository.NoteRepository;

import java.util.List;

/* This class is the intermediate between my Activity (MainActivity)
 * and the repository class that has our data from the database
 * WARNING: you should not hold any view that references an Activity
 * or a context of an activity reference because the ViewModel
 * is designed to outlive in activity after it is destroyed and
 * if you hold a ref to a destroyed activity we have a memory leak
 * ----------------------------------------------------------------
 * remember this is the between db and activities so you have to call
 * repo operations here using raper methods and call these raper methods
 * form the activity
 */
public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        // here we can instantiate our repo
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }
    // here we make the raper methods for every repo operation
    // we have 5 operations (insert, update, delete,
    // deleteAllNotes, getAllNotes)

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAll() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
