package com.vrlcrypt.arkmonitor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.MainActivity;
import com.vrlcrypt.arkmonitor.adapters.DelegatesAdapter;
import com.vrlcrypt.arkmonitor.models.Delegate;
import com.vrlcrypt.arkmonitor.models.Settings;
import com.vrlcrypt.arkmonitor.services.ArkService;
import com.vrlcrypt.arkmonitor.services.RequestListener;
import com.vrlcrypt.arkmonitor.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DelegatesFragment extends Fragment implements RequestListener<List<Delegate>>{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Delegate> mDelegates = new ArrayList<>();
    private DelegatesAdapter mDelegatesAdapter;

    public DelegatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delegates, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDelegatesAdapter = new DelegatesAdapter(getContext(), mDelegates);

        RecyclerView rvDelegates = (RecyclerView) view.findViewById(R.id.rvDelegates);

        rvDelegates.setAdapter(mDelegatesAdapter);
        rvDelegates.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.delegates_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        if (Utils.isOnline(getActivity())) {
            loadDelegates();
        } else {
            Utils.showMessage(getResources().getString(R.string.internet_off), view);
        }
    }

    @Override
    public void onDestroy() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideLoadingIndicatorView();
        }
        super.onDestroy();
    }

    private void loadDelegates() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showLoadingIndicatorView();
        }

        refreshContent();
    }

    @Override
    public void onFailure(final Exception e) {
        if (!isAdded()) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());

                mSwipeRefreshLayout.setRefreshing(false);

                MainActivity activity = (MainActivity)getActivity();
                if (activity != null) {
                    activity.hideLoadingIndicatorView();
                }
            }
        });
    }

    @Override
    public void onResponse(final List<Delegate> delegates) {
        if (!isAdded()) {
            return;
        }

        mDelegates.addAll(delegates);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mDelegates.size() > 51) {
                    Collections.sort(mDelegates);
                    mDelegatesAdapter.setDelegates(mDelegates);
                    mDelegatesAdapter.notifyDataSetChanged();

                    MainActivity activity = (MainActivity) getActivity();
                    if (activity != null) {
                        activity.hideLoadingIndicatorView();
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void refreshContent() {
        Settings settings = Utils.getSettings(getActivity());

        mDelegates.clear();

        ArkService.getInstance().requestActiveDelegates(settings, this);
        ArkService.getInstance().requestStandyByDelegates(settings, this);
    }

}
