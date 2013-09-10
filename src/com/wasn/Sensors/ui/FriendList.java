package com.wasn.Sensors.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import com.wasn.Sensors.R;
import com.wasn.Sensors.pojo.User;

import java.util.ArrayList;

/**
 * Display friend list/ Fragment
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class FriendList extends Fragment {
    // use to populate list
    private ListView friendListView;
    private ArrayList<User> userList;
    private FriendListAdapter adapter;

    // to handle empty view
    private ViewStub emptyView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        populateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.friend_list_layout, null);
        init(root);

        return root;
    }

    /**
     * Initialize activity components
     *  1. Initialize layout components
     *  2. Initialize list
     */
    private void init(View view) {
        initUI(view);
    }

    /**
     * Initialize UI components
     */
    private void initUI(View view) {
        friendListView = (ListView)view.findViewById(R.id.friend_list_layout_friend_list);

        // add header and footer for list
        View headerView = View.inflate(this.getActivity(), R.layout.list_header, null);
        View footerView = View.inflate(this.getActivity(), R.layout.list_header, null);
        friendListView.addHeaderView(headerView);
        friendListView.addFooterView(footerView);
    }

    /**
     * Create sensor list
     */
    private void populateList() {
        userList = new ArrayList<User>();

        userList.add(new User("eranga", "erangaeb@gmail.com"));
        userList.add(new User("pagero", "pagero@gmail.com"));
        userList.add(new User("test", "test@gmail.com"));
        userList.add(new User("herath", "herath@gmail.com"));
        userList.add(new User("vijith", "vijith@gmail.com"));

        // construct list adapter
        adapter = new FriendListAdapter(FriendList.this.getActivity(), userList);
        friendListView.setAdapter(adapter);
    }
}
