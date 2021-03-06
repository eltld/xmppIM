/**
 * 2015-2-28
 * 2015-2-28
 */
package com.quinn.xmpp.ui.messages;


import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.quinn.xmpp.R;
import com.quinn.xmpp.ui.contacts.ContactsAdapter;
import com.quinn.xmpp.ui.contacts.ContactsDataItem;
import com.quinn.xmpp.ui.main.MainActivity;
import com.quinn.xmpp.ui.widget.SimpleDividerItemDecoration;
import com.quinn.xmpp.util.DisplayUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Quinn
 * @date 2015-2-28
 * 
 * @description A Fragment to show the latest message(chatting to somebody, subscription to add friend,etc.)
 */
public class MessagesFragment extends Fragment{

	
	
	
	@InjectView(R.id.messages_recycle_view)
	RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private MainActivity mActivity;
	private ArrayList<MessagesDataItem> contactDataItems;
	
	//
	private int dividerHeight;
	private int dividerColor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contactDataItems = new ArrayList<MessagesDataItem>();
		/* test data */
		for (int i = 0; i < 40; i++) {
			contactDataItems.add(new MessagesDataItem(null, "周杰伦","挺妈妈的话，啦啦啦啦啦啦啦","昨天20:38"));
		}
		/* test data */
		
		dividerHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.recyclerView_small_divider);
		dividerColor = mActivity.getResources().getColor(R.color.color_gray);
	
		
		//??? difference
//		System.out.println("hehe = 1 " + mActivity.getResources().getDimensionPixelSize(R.dimen.recyclerView_small_divider));
//		System.out.println("hehe = 2 " + mActivity.getResources().getDimension(R.dimen.recyclerView_small_divider));
//		System.out.println("hehe = 3 " + DisplayUtils.dip2px(mActivity, 1));
		mAdapter = new MessagesAdapter(mActivity,contactDataItems);

	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
		setRetainInstance(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_messages, container,
				false);
		ButterKnife.inject(this, view);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
				mActivity.getApplicationContext(),dividerColor,dividerHeight
	        ));
		return view;
	}

	
}
