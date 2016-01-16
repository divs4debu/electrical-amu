package amu.electrical.deptt.fragment;

import amu.electrical.deptt.MainActivity;
import amu.electrical.deptt.R;
import amu.electrical.deptt.messages.Message;
import amu.electrical.deptt.messages.MessageManager;
import amu.electrical.deptt.utils.MessageAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MessageFragment extends Fragment {

    private ArrayList<Message> list;
    private MessageAdapter mAdapter;
    private MessageManager messageManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler);
        v.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageManager.clear();
                refresh();
            }
        });
        setupRecyclerView(rv);

        return v;
    }

    public void setupRecyclerView(RecyclerView rv) {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        messageManager = new MessageManager(getContext());
        list = (ArrayList<Message>) messageManager.getMessageDump().getMessages();
        mAdapter = new MessageAdapter(getContext(), (ArrayList) list);
        rv.setAdapter(mAdapter);
    }

    public void refresh() {
        list = (ArrayList<Message>) messageManager.getMessageDump().getMessages();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                ((MainActivity) getActivity()).closeNavDrawer();
            }

        }, 0);
    }


}
