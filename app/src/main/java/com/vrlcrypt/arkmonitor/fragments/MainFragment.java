package com.vrlcrypt.arkmonitor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrlcrypt.arkmonitor.MainActivity;
import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.models.Account;
import com.vrlcrypt.arkmonitor.models.Block;
import com.vrlcrypt.arkmonitor.models.Delegate;
import com.vrlcrypt.arkmonitor.models.Forging;
import com.vrlcrypt.arkmonitor.models.PeerVersion;
import com.vrlcrypt.arkmonitor.models.Settings;
import com.vrlcrypt.arkmonitor.models.Status;
import com.vrlcrypt.arkmonitor.models.Ticker;
import com.vrlcrypt.arkmonitor.services.ExchangeService;
import com.vrlcrypt.arkmonitor.services.ArkService;
import com.vrlcrypt.arkmonitor.services.RequestListener;
import com.vrlcrypt.arkmonitor.utils.Utils;

public class MainFragment extends Fragment {

    private TextView usernameTextview;
    private TextView addressTextview;
    private TextView balanceTextview;
    private TextView balanceBtcEquivalentTextview;
    private TextView balanceUsdEquivalentTextview;
    private TextView balanceEurEquivalentTextview;
    private TextView rankTextview;
    private TextView productivityTextview;
    private TextView forgedMissedBlocksTextview;
    private TextView feesTextview;
    private TextView rewardsTextview;
    private TextView forgedTextview;
    private TextView versionTextview;
    private TextView blocksTextview;
    private TextView heightTextview;
    private TextView lastBlockForgedTextView;
    private TextView delegateApprovalTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private double balance = -1;
    private double arkBTCValue = -1;
    private double bitcoinUSDValue = -1;
    private double bitcoinEURValue = -1;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTextview = (TextView) view.findViewById(R.id.account_username);
        addressTextview = (TextView) view.findViewById(R.id.account_address);
        balanceTextview = (TextView) view.findViewById(R.id.account_balance);
        balanceBtcEquivalentTextview = (TextView) view.findViewById(R.id.balance_btc_equivalent);
        balanceUsdEquivalentTextview = (TextView) view.findViewById(R.id.balance_usd_equivalent);
        balanceEurEquivalentTextview = (TextView) view.findViewById(R.id.balance_eur_equivalent);
        rankTextview = (TextView) view.findViewById(R.id.delegate_rank);
        productivityTextview = (TextView) view.findViewById(R.id.delegate_productivity);
        forgedMissedBlocksTextview = (TextView) view.findViewById(R.id.delegate_forged_missed_blocks);
        feesTextview = (TextView) view.findViewById(R.id.forgin_fees);
        rewardsTextview = (TextView) view.findViewById(R.id.forging_rewards);
        forgedTextview = (TextView) view.findViewById(R.id.forgin_forged);
        versionTextview = (TextView) view.findViewById(R.id.peer_version);
        blocksTextview = (TextView) view.findViewById(R.id.sync_blocks);
        heightTextview = (TextView) view.findViewById(R.id.sync_height);
        lastBlockForgedTextView = (TextView) view.findViewById(R.id.delegate_last_block_forged);
        delegateApprovalTextView = (TextView) view.findViewById(R.id.delegate_approval);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRequests();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Utils.isOnline(getActivity())) {
            showLoadingIndicatorView();
            loadRequests();
        } else {
            Utils.showMessage(getResources().getString(R.string.internet_off), getView());
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

    private void loadRequests() {
        loadDelegate();
        loadPeerVersion();
        loadStatus();
        loadLastForgedBlock();
        loadTicker();
    }

    private void loadLastForgedBlock() {
        Settings settings = Utils.getSettings(getActivity());

        ArkService.getInstance().requestLastBlockForged(settings, new RequestListener<Block>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(final Block block) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        if (block != null && block.getTimestamp() > 0) {
                            lastBlockForgedTextView.setText(Utils.getTimeAgo(block.getTimestamp()));
                        } else {
                            lastBlockForgedTextView.setText(R.string.not_forging);
                        }
                    }
                });
            }
        });
    }

    private void loadDelegate() {
        Settings settings = Utils.getSettings(getActivity());

        if (Utils.validateUsername(settings.getUsername())) {
            usernameTextview.setText(settings.getUsername());
        }

        ArkService.getInstance().requestDelegate(settings, new RequestListener<Delegate>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(final Delegate delegate) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        String status = delegate.getRate() <= 51 ? getString(R.string.active) : getString(R.string.standby);

                        rankTextview.setText(getString(R.string.ranking_status_value,
                                String.valueOf(delegate.getRate()),
                                status));

                        String productivity = delegate.getProductivity() + getString(R.string.percent_symbol);
                        productivityTextview.setText(productivity);

                        Long producedBlocks = delegate.getProducedblocks();
                        Long missedblocks = delegate.getMissedblocks();

                        forgedMissedBlocksTextview.setText(getString(R.string.forged_missed_value,
                                String.valueOf(producedBlocks),
                                String.valueOf(missedblocks)));

                        String approval = delegate.getApproval() + getString(R.string.percent_symbol);
                        delegateApprovalTextView.setText(approval);

                        loadForging();
                        loadAccount();
                    }
                });
            }
        });
    }

    private void loadPeerVersion() {
        Settings settings = Utils.getSettings(getActivity());

        ArkService.getInstance().requestPeerVersion(settings, new RequestListener<PeerVersion>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(final PeerVersion peerVersion) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        versionTextview.setText(peerVersion.getVersion());
                    }
                });
            }
        });
    }

    private void loadStatus() {
        Settings settings = Utils.getSettings(getActivity());

        ArkService.getInstance().requestStatus(settings, new RequestListener<Status>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(final Status status) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        heightTextview.setText(String.valueOf(status.getHeight()));
                        blocksTextview.setText(String.valueOf(status.getBlocks()));
                    }
                });
            }
        });
    }

    private void loadForging() {
        Settings settings = Utils.getSettings(getActivity());

        ArkService.getInstance().requestForging(settings, new RequestListener<Forging>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(final Forging forging) {
                if (!isAdded()) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        String fees = Utils.formatDecimal(forging.getFees());
                        String rewards = String.valueOf(Utils.convertToArkBase(forging.getRewards()));
                        String forged = Utils.formatDecimal(forging.getForged());

                        feesTextview.setText(fees);
                        rewardsTextview.setText(rewards);
                        forgedTextview.setText(forged);
                    }
                });
            }
        });
    }

    private void loadAccount() {
        final Settings settings = Utils.getSettings(getActivity());

        ArkService.getInstance().requestAccount(settings, new RequestListener<Account>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.balance = -1;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(final Account account) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.balance = account.getBalance();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        addressTextview.setText(account.getAddress());
                        balanceTextview.setText(Utils.formatDecimal(MainFragment.this.balance));

                        calculateEquivalentInBitcoinUSDandEUR();
                    }
                });
            }
        });
    }

    private void loadTicker() {
        ExchangeService.getInstance().requestTicker(new RequestListener<Ticker>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.arkBTCValue = -1;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);
                        balanceBtcEquivalentTextview.setText(getString(R.string.undefined));

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(final Ticker ticker) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.arkBTCValue = ticker.getLast();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        calculateEquivalentInBitcoinUSDandEUR();
                    }
                });
            }
        });


        ExchangeService.getInstance().requestBitcoinUSDTicker(new RequestListener<Ticker>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.bitcoinUSDValue = -1;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);
                        balanceUsdEquivalentTextview.setText(getString(R.string.undefined));

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(Ticker ticker) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.bitcoinUSDValue = ticker.getLast();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        calculateEquivalentInBitcoinUSDandEUR();
                    }
                });
            }
        });


        ExchangeService.getInstance().requestBitcoinEURTicker(new RequestListener<Ticker>() {
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.bitcoinEURValue = -1;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);
                        balanceEurEquivalentTextview.setText(getString(R.string.undefined));

                        Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                    }
                });
            }

            @Override
            public void onResponse(Ticker ticker) {
                if (!isAdded()) {
                    return;
                }

                MainFragment.this.bitcoinEURValue = ticker.getLast();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicatorView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        calculateEquivalentInBitcoinUSDandEUR();
                    }
                });
            }
        });
    }

    private void showLoadingIndicatorView() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showLoadingIndicatorView();
        }
    }

    private void hideLoadingIndicatorView() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideLoadingIndicatorView();
        }
    }

    private void calculateEquivalentInBitcoinUSDandEUR(){
        if (MainFragment.this != null && MainFragment.this.balance > 0 && MainFragment.this.arkBTCValue > 0) {
            double balanceBtcEquivalent = MainFragment.this.balance * MainFragment.this.arkBTCValue;
            balanceBtcEquivalentTextview.setText(Utils.formatDecimal(balanceBtcEquivalent));

            if (MainFragment.this.bitcoinUSDValue > 0) {
                double balanceUSDEquivalent = balanceBtcEquivalent * MainFragment.this.bitcoinUSDValue;
                balanceUsdEquivalentTextview.setText(Utils.formatDecimal(balanceUSDEquivalent));
            }

            if (MainFragment.this.bitcoinEURValue > 0) {
                double balanceUSDEquivalent = balanceBtcEquivalent * MainFragment.this.bitcoinEURValue;
                balanceEurEquivalentTextview.setText(Utils.formatDecimal(balanceUSDEquivalent));
            }
        }

    }
}
