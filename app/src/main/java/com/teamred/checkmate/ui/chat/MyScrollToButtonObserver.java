package com.teamred.checkmate.ui.chat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class MyScrollToButtonObserver<T extends FirebaseRecyclerAdapter> extends RecyclerView.AdapterDataObserver {
    private RecyclerView recycler;
    private T adapter;
    private LinearLayoutManager manager;

    public MyScrollToButtonObserver(RecyclerView recycler, T adapter, LinearLayoutManager manager) {
        this.recycler = recycler;
        this.adapter = adapter;
        this.manager = manager;
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        int count = adapter.getItemCount();
        int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
        // If the recycler view is initially being loaded or the
        // user is at the bottom of the list, scroll to the bottom
        // of the list to show the newly added message.
        boolean loading = lastVisiblePosition == -1;
        boolean atBottom = positionStart >= count - 1 && lastVisiblePosition == positionStart - 1;
        if (loading || atBottom) {
            recycler.scrollToPosition(positionStart);
        }
    }


}
