package com.vrlcrypt.arkmonitor.fragments;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.MainActivity;
import com.vrlcrypt.arkmonitor.models.Delegate;
import com.vrlcrypt.arkmonitor.models.Server;
import com.vrlcrypt.arkmonitor.models.Settings;
import com.vrlcrypt.arkmonitor.scheduler.ForgingAlarmReceiver;
import com.vrlcrypt.arkmonitor.services.ArkService;
import com.vrlcrypt.arkmonitor.services.RequestListener;
import com.vrlcrypt.arkmonitor.utils.Utils;

public class SettingsFragment extends Fragment implements OnClickListener, AdapterView.OnItemSelectedListener {

    private OnSavedSettingsListener mListener;
    private static final int INTERVAL_SEVEN_MINUTES_INDEX = 0;
    private static final int INTERVAL_FIFTEEN_MINUTES_INDEX = 1;
    private static final int INTERVAL_HALF_HOUR_INDEX = 2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = (Button) view.findViewById(R.id.save_btn);
        if (button != null) {
            button.setOnClickListener(this);
        }

        EditText editTextUsername = (EditText) view.findViewById(R.id.username);
        final EditText editTextIpAddress = (EditText) view.findViewById(R.id.ip_address);
        final EditText editTextPort = (EditText) view.findViewById(R.id.port);
        final CheckBox checkboxSslEnabled = (CheckBox) view.findViewById(R.id.ssl_enabled);
        final Spinner spinnerServer = (Spinner) view.findViewById(R.id.servers);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, Server.getServers());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServer.setAdapter(dataAdapter);
        spinnerServer.setOnItemSelectedListener(this);

        final View viewIpAddressContainer = view.findViewById(R.id.ip_address_container);
        final View viewPortContainer = view.findViewById(R.id.port_container);
        final View viewSSLEnabledContainer = view.findViewById(R.id.ssl_enabled_container);

        final View viewIpAddressLine = view.findViewById(R.id.ip_address_line);
        final View viewPortLine = view.findViewById(R.id.port_line);
        final View viewSSLEnabledLine = view.findViewById(R.id.ssl_enabled_line);

        final Spinner notificationIntervalSpinner = (Spinner) view.findViewById(R.id.notification_interval);

        final Settings settings = Utils.getSettings(getActivity());

        if (Utils.validateUsername(settings.getUsername())) {
            editTextUsername.setText(settings.getUsername());
        }

        if (Utils.validateIpAddress(settings.getIpAddress())) {
            editTextIpAddress.setText(settings.getIpAddress());
        }

        if (Utils.validatePort(settings.getPort())) {
            editTextPort.setText(String.valueOf(settings.getPort()));
        }

        checkboxSslEnabled.setChecked(settings.getSslEnabled());
        spinnerServer.setSelection(settings.getServer().getId());

        if (!settings.getServer().isCustomServer()) {
            editTextIpAddress.setEnabled(false);
            editTextPort.setEnabled(false);
            checkboxSslEnabled.setEnabled(false);

            viewIpAddressContainer.setVisibility(View.GONE);
            viewPortContainer.setVisibility(View.GONE);
            viewSSLEnabledContainer.setVisibility(View.GONE);

            viewIpAddressLine.setVisibility(View.GONE);
            viewPortLine.setVisibility(View.GONE);
            viewSSLEnabledLine.setVisibility(View.GONE);
        }

        long notificationInterval = settings.getNotificationInterval();
        if (notificationInterval == 420000L) {
            notificationIntervalSpinner.setSelection(INTERVAL_SEVEN_MINUTES_INDEX);
        } else if (notificationInterval == AlarmManager.INTERVAL_FIFTEEN_MINUTES) {
            notificationIntervalSpinner.setSelection(INTERVAL_FIFTEEN_MINUTES_INDEX);
        } else if (notificationInterval == AlarmManager.INTERVAL_HALF_HOUR) {
            notificationIntervalSpinner.setSelection(INTERVAL_HALF_HOUR_INDEX);
        }

        if (!Utils.isOnline(getActivity())) {
            Utils.showMessage(getResources().getString(R.string.internet_off), view);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        final View viewIpAddressContainer = getView().findViewById(R.id.ip_address_container);
        final View viewPortContainer = getView().findViewById(R.id.port_container);
        final View viewSSLEnabledContainer = getView().findViewById(R.id.ssl_enabled_container);

        final View viewIpAddressLine = getView().findViewById(R.id.ip_address_line);
        final View viewPortLine = getView().findViewById(R.id.port_line);
        final View viewSSLEnabledLine = getView().findViewById(R.id.ssl_enabled_line);

        EditText editTextIpAddress = (EditText) getView().findViewById(R.id.ip_address);
        EditText editTextPort = (EditText) getView().findViewById(R.id.port);
        CheckBox checkboxSslEnabled = (CheckBox) getView().findViewById(R.id.ssl_enabled);

        boolean isCustomServer = pos == Server.custom.getId();

        if (!isCustomServer) {
            editTextIpAddress.setText(R.string.empty);
            editTextPort.setText(R.string.empty);
            checkboxSslEnabled.setChecked(false);
        }

        viewIpAddressContainer.setVisibility(isCustomServer ? View.VISIBLE : View.GONE);
        viewPortContainer.setVisibility(isCustomServer ? View.VISIBLE : View.GONE);
        viewSSLEnabledContainer.setVisibility(isCustomServer ? View.VISIBLE : View.GONE);

        viewIpAddressLine.setVisibility(isCustomServer ? View.VISIBLE : View.GONE);
        viewPortLine.setVisibility(isCustomServer ? View.VISIBLE : View.GONE);
        viewSSLEnabledLine.setVisibility(isCustomServer ? View.VISIBLE : View.GONE);

        editTextIpAddress.setEnabled(isCustomServer);
        editTextPort.setEnabled(isCustomServer);
        checkboxSslEnabled.setEnabled(isCustomServer);
    }

    public void  onNothingSelected(AdapterView<?> var1) {

    }

    @Override
    public void onDestroy() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideLoadingIndicatorView();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(final View v) {
        View view = getView();

        Utils.hideSoftKeyboard(getActivity());

        EditText editTextUsername = (EditText) view.findViewById(R.id.username);
        EditText editTextIpAddress = (EditText) view.findViewById(R.id.ip_address);
        EditText editTextPort = (EditText) view.findViewById(R.id.port);
        CheckBox checkboxSslEnabled = (CheckBox) view.findViewById(R.id.ssl_enabled);
        Spinner spinnerServer = (Spinner) view.findViewById(R.id.servers);
        Spinner notificationIntervalSpinner = (Spinner) view.findViewById(R.id.notification_interval);

        String username = editTextUsername.getText().toString();
        String ipAddress = editTextIpAddress.getText().toString();
        String portStr = editTextPort.getText().toString();
        boolean sslEnabled = checkboxSslEnabled.isChecked();
        Server server = Server.fromId(spinnerServer.getSelectedItemPosition());
        int notificationIntervalSpinnerPosition = notificationIntervalSpinner.getSelectedItemPosition();

        if (!Utils.validateUsername(username)) {
            Snackbar.make(view, R.string.username_message, Snackbar.LENGTH_LONG).show();
            return;
        }

        if (server.isCustomServer()) {
            if (!Utils.validateIpAddress(ipAddress)) {
                Snackbar.make(view, R.string.ip_address_message, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (!Utils.validatePort(portStr)) {
                Snackbar.make(view, R.string.port_message, Snackbar.LENGTH_LONG).show();
                return;
            }
        }

        final Settings settings = Utils.getSettings(getActivity());
        settings.setUsername(username);

        if (server.isCustomServer()) {
            settings.setIpAddress(ipAddress);
            settings.setPort(Integer.valueOf(portStr));
            settings.setSslEnabled(sslEnabled);
        }

        settings.setServer(server);

        switch (notificationIntervalSpinnerPosition) {
            case INTERVAL_FIFTEEN_MINUTES_INDEX:
                settings.setNotificationInterval(AlarmManager.INTERVAL_FIFTEEN_MINUTES);
                break;
            case INTERVAL_HALF_HOUR_INDEX:
                settings.setNotificationInterval(AlarmManager.INTERVAL_HALF_HOUR);
                break;
            default:
                settings.setNotificationInterval(420000L);
                break;
        }

        if (Utils.saveSettings(getActivity(), settings)) {
            v.setEnabled(false);
            showLoadingIndicatorView();

            ArkService.getInstance().requestDelegate(settings, new RequestListener<Delegate>() {
                @Override
                public void onFailure(Exception e) {
                    if (!isAdded()) {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            v.setEnabled(true);

                            hideLoadingIndicatorView();

                            Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());
                        }
                    });
                }

                @Override
                public void onResponse(Delegate delegate) {
                    if (!isAdded()) {
                        return;
                    }

                    boolean alarmEnabled = Utils.alarmEnabled(getActivity());
                    ForgingAlarmReceiver alarm = new ForgingAlarmReceiver();

                    if (alarmEnabled) {
                        alarm.restartAlarm(getContext());
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoadingIndicatorView();
                        }
                    });

                    settings.setPublicKey(delegate.getPublicKey());
                    settings.setArkAddress(delegate.getAddress());
                    Utils.saveSettings(getActivity(), settings);

                    if (mListener != null) {
                        mListener.onSavedSettingsListener();
                    }
                }
            });


        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSavedSettingsListener) {
            mListener = (OnSavedSettingsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSavedSettingsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSavedSettingsListener {
        void onSavedSettingsListener();
    }


}