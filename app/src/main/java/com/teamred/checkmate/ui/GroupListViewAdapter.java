package com.teamred.checkmate.ui;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.util.DateUtil;

public class GroupListViewAdapter extends BaseAdapter {

    private Group[] groupList;

    Context context;   //Creating a reference to our context object, so we only have to get it once.  Context enables access to application specific resources.

    public GroupListViewAdapter( Context context, Group[] groupList) {
        this.groupList = groupList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row;

        if (convertView == null){  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  //Inflater's are awesome, they convert xml to Java Objects!
            row = inflater.inflate(R.layout.group_listview_row, parent, false);
        }
        else
        {
            row = convertView;
        }

        // fill the view
        TextView groupName = (TextView) row.findViewById(R.id.listview_group_name);
        TextView groupCreator = (TextView) row.findViewById(R.id.listview_group_creator);
//        TextView noteDate = row.findViewById(R.id.note_date);
        TextView groupDescription = row.findViewById(R.id.listview_group_description);
//        TextView noteNumber = row.findViewById(R.id.note_number);
        groupDescription.setText(Html.fromHtml(groupList[position].getDescription()));
        groupCreator.setText(Html.fromHtml(groupList[position].getCreator()));
//        noteDate.setText(DateUtil.getSimpleDateString(groupList[position].getCreateDate()));
        groupName.setText(Html.fromHtml(groupList[position].getGroupName()));
//        noteNumber.setText(groupList[position].getNumber().toString());

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
        return groupList.length;
    }

    @Override
    public Object getItem(int position) {
        return groupList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
