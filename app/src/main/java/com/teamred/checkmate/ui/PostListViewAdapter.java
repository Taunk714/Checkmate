package com.teamred.checkmate.ui;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.Post;
import com.teamred.checkmate.ui.notes.PostFragment;
import com.teamred.checkmate.ui.notes.PostListViewModel;
import com.teamred.checkmate.util.DateUtil;

import java.util.List;

/**
 * display the posts list.
 */
public class PostListViewAdapter extends BaseAdapter {

    private Post[] postList;
    private PostListViewModel postListViewModel;

    Context context;   //Creating a reference to our context object, so we only have to get it once.  Context enables access to application specific resources.

    public PostListViewAdapter(Context context, Post[] postList, PostListViewModel postListViewModel) {
        this.postList = postList;
        this.context = context;
        this.postListViewModel = postListViewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;

        if (convertView == null){  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.post_listview_row, parent, false);
        }
        else
        {
            row = convertView;
        }

        // fill the view
        TextView postTitle = (TextView) row.findViewById(R.id.listview_group_name);
        TextView postAuthor = (TextView) row.findViewById(R.id.listview_group_creator);
        TextView postDate = row.findViewById(R.id.post_date);
        TextView postTags = row.findViewById(R.id.tags_tv);
        TextView postContent = row.findViewById(R.id.post_content);
//        TextView postNumber = row.findViewById(R.id.post_number);
        postContent.setText(postList[position].getContent());
        postAuthor.setText(postList[position].getAuthor());
        postDate.setText(DateUtil.getSimpleDateString(postList[position].getCreateDate()));
        postTitle.setText(postList[position].getTitle());
        postTags.setText(parseTags(postList[position].getTags()));
        
//        postNumber.setText(postList[position].getNumber().toString());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(this, )
                Log.d("Post", "Clicked position " + position);

                postListViewModel.select(position);

                Fragment postFragment = new PostFragment();

                FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.navigation_host, postFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return row;

    }

    @Override
    public int getCount() {
        return postList.length;
    }

    @Override
    public Object getItem(int position) {
        return postList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Tags a list of tags and returns a string of them comma delimited
     * @param tags
     * @return
     */
    public String parseTags(List<String> tags) {
        String commaDelimited = String.join(", ", tags);
        return commaDelimited;
    }

}
