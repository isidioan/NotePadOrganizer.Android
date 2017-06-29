package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    public static final String ADDNEWNOTE = "newNote";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        addFragment();
    }

    private void addFragment (){
        Intent intent = getIntent();
        MainActivity.fragmentToChoose fragm = (MainActivity.fragmentToChoose)
                intent.getSerializableExtra(MainActivity.FRAGMENTTOCHOOSE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragm){

            case DETAIL:
                NoteDetailFragment detailFragment = new NoteDetailFragment();
                transaction.add(R.id.noteDetailContainer, detailFragment, "FRAGMENT_NOTE_DETAIL");
                setTitle(R.string.detail_note);
                break;
            case EDIT:
                NoteEditFragment editFragment = new NoteEditFragment();
                transaction.add(R.id.noteDetailContainer, editFragment, "FRAGMENT_NOTE_EDIT");
                setTitle(R.string.edit_note);
                break;
            case ADD:
                NoteEditFragment createFragment = new NoteEditFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(ADDNEWNOTE,true);
                createFragment.setArguments(bundle);
                transaction.add(R.id.noteDetailContainer,createFragment,"FRAGMENT_NOTE_CREATE");
                setTitle(R.string.new_note);
                break;
        }
        transaction.commit();
    }
}
