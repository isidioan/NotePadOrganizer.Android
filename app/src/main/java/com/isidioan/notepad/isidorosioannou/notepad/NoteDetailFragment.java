package com.isidioan.notepad.isidorosioannou.notepad;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetailFragment extends Fragment {
    private String title,date;
    private String summary;

    public NoteDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_note_detail, container, false);

        Intent intent = getActivity().getIntent();
        title = intent.getStringExtra(MainActivity.NOTETITLE);
        summary = intent.getStringExtra(MainActivity.NOTESUMMARY);
        date = intent.getStringExtra(MainActivity.NOTEDATE);
        TextView titleView = (TextView) fragmentView.findViewById(R.id.detailTitle);
        TextView summaryView = (TextView) fragmentView.findViewById(R.id.detailSummary);
        TextView dateView = (TextView) fragmentView.findViewById(R.id.detailDate);
        titleView.setText(title);
        summaryView.setText(summary);
        dateView.setText(date);
        return fragmentView;
    }

}
