package com.AZApps.notesappusingmvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.AZApps.notesappusingmvvm.R;
import com.AZApps.notesappusingmvvm.pojo.Note;

import java.util.Objects;

public class AddAndEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";
    public static final String SEND_NOTE = "note";
    private EditText editTextTitle, editTextDescription;
    private NumberPicker numberPickerPriority;
    private Note editNote;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        init();
        setCloseButton();
        addOrEdit();

    }

    private void addOrEdit() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // set Title to the activity
            setTitle("Edit Note");
            editNote = (Note) intent.getSerializableExtra(SEND_NOTE);
            editTextTitle.setText(editNote.getTitle());
            editTextDescription.setText(editNote.getDescription());
            numberPickerPriority.setValue(editNote.getId());
            noteId = editNote.getId();
        } else {
            // set Title to the activity
            setTitle("Add Note");
            // 0 is any value we do not care
            noteId = 0;
        }
    }

    private void setCloseButton() {
        // here we add ( X ) the close icon to back to the main activity
        //// getActionBar().setHomeAsUpIndicator(R.drawable.ic_close); is not working
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void init() {
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        // here set the min and max value for the numberPicker
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // here we can add our menu items (we only have save icon) to this layout
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        // return true means you want to use this menu / false means you do not want to use it
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // to handle menu item clicks
        // if the menu has more than one item use switch case
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        Note note = checkForError(noteId, title, description, priority);
        if (note != null) {
            Intent data = new Intent();
            data.putExtra(SEND_NOTE, note);
            // make sure to add (data) the intent that has the Serializable data
            // or you will get an nullPointerException
            setResult(RESULT_OK, data);
            finish();
        }

    }

    private Note checkForError(int id, String mTitle, String mDescription, int priority) {
        boolean allIsFine = true;
        if (mTitle == null || mTitle.isEmpty()) {
            editTextTitle.setError("Enter A Name!");
            allIsFine = false;
        }
        if (mDescription == null || mDescription.isEmpty()) {
            editTextDescription.setError("Enter A Number!");
            allIsFine = false;
        }
        if (allIsFine) {
            return new Note(id, mTitle, mDescription, priority);
        } else {
            return null;
        }
    }
}