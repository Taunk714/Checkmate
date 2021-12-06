package com.teamred.checkmate.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.FireStoreDataSource;
import com.teamred.checkmate.data.model.Group;
import com.teamred.checkmate.ui.group.GroupDetailFragment;

public class GroupListViewAdapter extends BaseAdapter {

    private Group[] groupList;

    Context context;   //Creating a reference to our context object, so we only have to get it once.  Context enables access to application specific resources.

    FragmentManager fm = null;

    public GroupListViewAdapter(Context context, Group[] groupList, FragmentManager fm) {
        this.groupList = groupList;
        this.fm = fm;
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
        TextView groupDescription = row.findViewById(R.id.post_content);
//        TextView noteNumber = row.findViewById(R.id.note_number);
        groupDescription.setText(Html.fromHtml(groupList[position].getDescription()));
        groupCreator.setText(Html.fromHtml(groupList[position].getCreator()));
//        noteDate.setText(DateUtil.getSimpleDateString(groupList[position].getCreateDate()));
        groupName.setText(Html.fromHtml(groupList[position].getGroupName()));
//        noteNumber.setText(groupList[position].getNumber().toString());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("group", "search group click");
                groupList[position].addView();
                Group.update(groupList[position]);
                FragmentTransaction ft = fm.beginTransaction();
                GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("group", JSON.toJSONString(groupList[position]));
                groupDetailFragment.setArguments(bundle);
                ft.replace(R.id.navigation_host, groupDetailFragment)
                        .commit();
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

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }
}
