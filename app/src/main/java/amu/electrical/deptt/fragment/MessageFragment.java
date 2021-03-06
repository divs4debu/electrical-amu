package amu.electrical.deptt.fragment;

import amu.electrical.deptt.MainActivity;
import amu.electrical.deptt.R;
import amu.electrical.deptt.messages.Message;
import amu.electrical.deptt.messages.MessageManager;
import amu.electrical.deptt.utils.Colors;
import amu.electrical.deptt.utils.MessageAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class MessageFragment extends Fragment {

    private ArrayList<Message> list;
    private MessageAdapter mAdapter;
    private MessageManager messageManager;

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
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(), "Clear Messages", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Colors.tintFab(fab, getActivity());
        setupRecyclerView(rv);
        init();

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
        final ArrayList<Message> temp = (ArrayList<Message>) list.clone();
        messageManager.clear();
        list.clear();
        mAdapter.notifyDataSetChanged();
        Snackbar sb = Snackbar.make(v, temp.size() + " messages deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Message m : temp) {
                            list.add(list.size(), m);
                            messageManager.saveMessage(m);
                            mAdapter.notifyItemInserted(list.size());
                        }
                        init();
                    }
                });
        sb.show();
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
