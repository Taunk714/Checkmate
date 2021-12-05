package com.teamred.checkmate.ui;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.Note;
import com.teamred.checkmate.util.DateUtil;

/**
 * display the notes list.
 */
public class NoteListViewAdapter extends BaseAdapter {

    private Note[] noteList;

    Context context;   //Creating a reference to our context object, so we only have to get it once.  Context enables access to application specific resources.

    public NoteListViewAdapter( Context context, Note[] noteList) {
        this.noteList = noteList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row;

        if (convertView == null){  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.note_listview_row, parent, false);
        }
        else
        {
            row = convertView;
        }

        // fill the view
        TextView noteTitle = (TextView) row.findViewById(R.id.listview_group_name);
        TextView noteAuthor = (TextView) row.findViewById(R.id.listview_group_creator);
        TextView noteDate = row.findViewById(R.id.note_date);
        TextView noteContent = row.findViewById(R.id.listview_group_description);
//        TextView noteNumber = row.findViewById(R.id.note_number);
        noteContent.setText(Html.fromHtml(noteList[position].getContent()));
        noteAuthor.setText(Html.fromHtml(noteList[position].getAuthor()));
        noteDate.setText(DateUtil.getSimpleDateString(noteList[position].getCreateDate()));
        noteTitle.setText(Html.fromHtml(noteList[position].getTitle()));
//        noteNumber.setText(noteList[position].getNumber().toString());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(this, )
            }
        });

        return row;

    }

    @Override
    public int getCount() {
        return noteList.length;
    }

    @Override
    public Object getItem(int position) {
        return noteList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
