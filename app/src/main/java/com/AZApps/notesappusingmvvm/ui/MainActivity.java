package com.AZApps.notesappusingmvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AZApps.notesappusingmvvm.R;
import com.AZApps.notesappusingmvvm.adapter.NoteAdapter;
import com.AZApps.notesappusingmvvm.pojo.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/*
 *This code is basically form www.codinginflow.com i and i am thankful for
 *his great explanations and i wish him a good luck
 *this app is modified by Omar AZ (me) and i will continue modifying it in the future
 * the first modify is changing how the data is passed as an extras
 * i used the Serializable approach instead of putExtra
*/

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;
    RecyclerView recyclerView;
    NoteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareFab();
        // add the recyclerView
        setRecyclerView();
        modelViewInstantiate();
        // Here we want to make swipe to delete from the recyclerView function
        addSwipeFunction();
        // Here we can detect the clicks on our recyclerView
        addClickEvent();

    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // if the recycler size will not be changed add hasFixedSize method
        recyclerView.hasFixedSize();
        // create the adapter
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void modelViewInstantiate() {
        /*
         * assign viewModel var  here but do not use new keyword
         * let the android decide when to make a new viewModel or
         * when to use an existing one
         * make sure to choose
         *------------------------------------------------------------------
         * this line will not work unless you add
         * implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
         * to gradle.build or just use the next un commented line
         * noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
         */
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // update recyclerview
                adapter.submitList(notes);
            }
        });
    }

    private void addClickEvent() {
        adapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddAndEditNoteActivity.class);
                // put the note we got from the click as extra
                intent.putExtra(AddAndEditNoteActivity.SEND_NOTE, note);
                // we have to send the id (of the note to be add) as extra
                intent.putExtra(AddAndEditNoteActivity.EXTRA_ID, note.getId());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    private void addSwipeFunction() {
        /*
         * ItemTouchHelper detects the drags , drops and swipes
         * in the simpleCallback the First pram is the drag direction but we will not
         * implement this in our app
         * The second pram is the direction you want to use (we will use both left and right)
         * REMEMBER we have to attach the swipe to the recyclerView
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                /* here you should delete the swiped note at the swiped position
                 * remember all operations are at noteModelView class
                 * but we do not have method that delete by position passed to it
                 * but we can not detect the right position outside the adapter so
                 * we will make a public helper method in the adapter we will call it getNoteAt(int position)
                 */
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void prepareFab() {
        // fab
        FloatingActionButton fabAddNote = findViewById(R.id.button_add_note);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddAndEditNoteActivity.class), ADD_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note dataNote = null;
        if (data != null) {
            dataNote = (Note) data.getSerializableExtra(AddAndEditNoteActivity.SEND_NOTE);
        }
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            noteViewModel.insert(dataNote);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            int id = dataNote.getId();
            dataNote.setId(id);
            noteViewModel.update(dataNote);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this is for delete all option
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        // return true means you want to use this modified menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            noteViewModel.deleteAll();
            Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}