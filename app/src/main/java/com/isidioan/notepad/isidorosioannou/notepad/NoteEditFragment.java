package com.isidioan.notepad.isidorosioannou.notepad;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

        private String title,summary;
        private EditText editTextTitle, editTextSummary;
        private Button saveEditButton;
        private boolean createNote=false;
        private int noteId;
    public NoteEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentEdit = inflater.inflate(R.layout.fragment_note_edit, container, false);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            createNote = bundle.getBoolean(DetailActivity.ADDNEWNOTE, false);
        }
        editTextTitle = (EditText)fragmentEdit.findViewById(R.id.editNoteTitle);
        editTextSummary= (EditText) fragmentEdit.findViewById(R.id.editNoteSummary);
        saveEditButton = (Button) fragmentEdit.findViewById(R.id.editSaveButton);

        if(savedInstanceState!=null){
            title= savedInstanceState.getString("TITLETEXT");
            summary=savedInstanceState.getString("SUMMARY");
            editTextSummary.setText(summary);
            editTextTitle.setText(title);
        }
        Intent intent = getActivity().getIntent();
        title = intent.getExtras().getString(MainActivity.NOTETITLE,"");
        summary=intent.getExtras().getString(MainActivity.NOTESUMMARY,"");
        noteId = intent.getExtras().getInt(MainActivity.NOTEID,0);
        editTextTitle.setText(title);
        editTextSummary.setText(summary);

        saveEditButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                DatabaseAdapt dbAdapter = new DatabaseAdapt(getActivity().getBaseContext());
                dbAdapter.open();
                title = editTextTitle.getText().toString();
                summary=editTextSummary.getText().toString();
                String date = new SimpleDateFormat("dd/MM/yy").format(new Date());
                if(createNote){

                    dbAdapter.createNote(title,summary,date);
                } else {
                    dbAdapter.updateNote(noteId,title,summary,date);
                }

                dbAdapter.close();
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }

        });

        return fragmentEdit;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TITLETEXT",editTextTitle.getText().toString());
        outState.putString("SUMMARY", editTextSummary.getText().toString());
    }
}
