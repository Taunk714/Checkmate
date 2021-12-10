package com.teamred.checkmate.ui;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.teamred.checkmate.R;
import com.teamred.checkmate.data.LoginDataSource;
import com.teamred.checkmate.data.model.Post;
import com.teamred.checkmate.data.model.User;
import com.teamred.checkmate.ui.notes.PostFragment;
import com.teamred.checkmate.ui.notes.PostsViewModel;

import java.util.List;

/**
 * display the posts list.
 */
public class PostListViewAdapter extends BaseAdapter {

    private Post[] postList;
    private PostsViewModel postsViewModel;

    Context context;   //Creating a reference to our context object, so we only have to get it once.  Context enables access to application specific resources.

    public PostListViewAdapter(Context context, Post[] postList, PostsViewModel postsViewModel) {
        this.postList = postList;
        for (Post p: postList) {
            Log.i(TAG, "PostListViewAdapter: Post's subtopic: " + p.getSubtopic());
        }
        this.context = context;
        this.postsViewModel = postsViewModel;
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
        TextView postSubTopic = row.findViewById(R.id.postrowsubtopic_TV);
//        TextView postNumber = row.findViewById(R.id.post_number);
        postContent.setText(postList[position].getContent());
        LoginDataSource.getTargetUser(postList[position].getAuthor()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                postAuthor.setText(user.getUsername());
            }
        });

        postDate.setText(postList[position].getCreateDate().toString());
        postTitle.setText(postList[position].getPostTitle());
        postTags.setText(parseTags(postList[position].getTags()));
        Log.i(TAG, "getView: postlist position subtopic" + postList[position].getSubtopic());
        postSubTopic.setText(postList[position].getSubtopic());

//        postNumber.setText(postList[position].getNumber().toString());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(this, )
                Log.d("Post", "Clicked position " + position);

                postsViewModel.select(position);

                Fragment postFragment = new PostFragment();

                FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.navigation_host).getChildFragmentManager();

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
