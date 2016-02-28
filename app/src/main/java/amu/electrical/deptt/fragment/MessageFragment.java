package amu.electrical.deptt.fragment;

import amu.electrical.deptt.MainActivity;
import amu.electrical.deptt.R;
import amu.electrical.deptt.messages.Message;
import amu.electrical.deptt.messages.MessageDump;
import amu.electrical.deptt.messages.MessageManager;
import amu.electrical.deptt.utils.Colors;
import amu.electrical.deptt.utils.MessageAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MessageFragment extends Fragment {

    private ArrayList<Message> list;
    private MessageAdapter mAdapter;
    private MessageManager messageManager;
    BroadcastReceiver messageInsert = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MessageDump.TAG)) {
                inserted();
                Log.d("Broadcast", "Received");
            }
        }
    };
    private FloatingActionButton fab;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        v = inflater.inflate(R.layout.fragment_messages, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        Colors.tintFab(fab, getActivity());
        setupRecyclerView(rv);
        init();

        getActivity().registerReceiver(messageInsert, new IntentFilter(MessageDump.TAG));
        return v;
    }

    public void setupRecyclerView(RecyclerView rv) {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        messageManager = new MessageManager(getContext());
        list = messageManager.getMessageDump().getMessages();

        mAdapter = new MessageAdapter(getContext(), (ArrayList) list);
        rv.setAdapter(mAdapter);
    }

    public void clear() {
        messageManager.clear();
        list.clear();
        mAdapter.notifyDataSetChanged();
        init();
    }

    public void inserted() {
        list = messageManager.getMessages();
        mAdapter.notifyDataSetChanged();
    }

    public void init() {
        if (list.size() > 0) {
            v.findViewById(R.id.no_message).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.no_message).setVisibility(View.VISIBLE);
            fab.hide();
        }
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
