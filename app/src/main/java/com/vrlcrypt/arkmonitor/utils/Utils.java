package com.vrlcrypt.arkmonitor.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.vrlcrypt.arkmonitor.models.Server;
import com.vrlcrypt.arkmonitor.models.Settings;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern PATTERN_IP_ADDRESS = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final String ALARM_ATTR = "alarm_key";
    private static final int MAX_PORT_NUMBER = 65535;
    private static final String START_DATE = "21/03/2017 13:00:00";
    private static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
    private static final String TIME_ZONE = "UTC";

    private Utils() {
    }

    public static boolean validateArkAddress(final String arkAddress) {
        return arkAddress != null && arkAddress.length() > 0;
    }

    public static boolean validateIpAddress(final String ip) {
        return ip != null && PATTERN_IP_ADDRESS.matcher(ip).matches();
    }

    public static boolean validatePort(final int port) {
        return port > 0 && port <= MAX_PORT_NUMBER;
    }

    public static boolean validatePort(final String port) {
        return isInteger(port) && validatePort(Integer.valueOf(port));
    }

    private static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    private static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean saveSettings(Activity activity, Settings settings) {
        if (settings.getServer().isCustomServer()) {
            if (!validateIpAddress(settings.getIpAddress())) {
                return false;
            }

            if (!validatePort(settings.getPort())) {
                return false;
            }
        }

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        prefsEditor.putString(Settings.USERNAME_ATTR, settings.getUsername());
        prefsEditor.putString(Settings.ARK_ADDRESS_ATTR, settings.getArkAddress());
        prefsEditor.putString(Settings.PUBLIC_KEY_ATTR, settings.getPublicKey());
        prefsEditor.putString(Settings.IP_ATTR, settings.getIpAddress());
        prefsEditor.putInt(Settings.PORT_ATTR, settings.getPort());
        prefsEditor.putBoolean(Settings.SSL_ENABLED_ATTR, settings.getSslEnabled());
        prefsEditor.putInt(Settings.SERVER_ATTR, settings.getServer().getId());
        prefsEditor.putLong(Settings.NOTIFICATION_INTERVAL_ATTR, settings.getNotificationInterval());

        return prefsEditor.commit();
    }

    public static Settings getSettings(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Settings settings = new Settings();
        settings.setUsername(mPrefs.getString(Settings.USERNAME_ATTR, null));
        settings.setArkAddress(mPrefs.getString(Settings.ARK_ADDRESS_ATTR, null));
        settings.setPublicKey(mPrefs.getString(Settings.PUBLIC_KEY_ATTR, null));
        settings.setIpAddress(mPrefs.getString(Settings.IP_ATTR, null));
        settings.setPort(mPrefs.getInt(Settings.PORT_ATTR, -1));
        settings.setSslEnabled(mPrefs.getBoolean(Settings.SSL_ENABLED_ATTR, false));
        settings.setServerById(mPrefs.getInt(Settings.SERVER_ATTR, Server.ark1.getId()));
        settings.setNotificationInterval(mPrefs.getLong(Settings.NOTIFICATION_INTERVAL_ATTR, AlarmManager.INTERVAL_FIFTEEN_MINUTES));

        return settings;
    }

    public static void showMessage(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static String formatDecimal(double value) {
        double total = convertToArkBase(value);
        DecimalFormat df = new DecimalFormat("#0.00000000");
        return df.format(total);
    }

    public static double convertToArkBase(double value){
        return value * Math.pow(10, -8);
    }

    public static boolean validatePublicKey(String publicKey) {
        return publicKey != null && publicKey.length() > 0;
    }

    public static boolean validateUsername(String username) {
        return username != null && username.length() > 0 && username.length() <= 20;
    }


    public static CharSequence getTimeAgo(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH);
        sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

        Date d;
        try {
            d = sdf.parse(START_DATE);
        } catch (ParseException e) {
            return "";
        }

        long t = d.getTime() / 1000;

        Date timeStart = new Date((timestamp + t) * 1000);

        long now = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE)).getTimeInMillis();

        return DateUtils.getRelativeTimeSpanString(timeStart.getTime(), now, DateUtils.SECOND_IN_MILLIS);
    }

    public static long getTimeInMillisUntilNow(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE, Locale.ENGLISH);
        sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, TIME_ZONE));

        Date d;
        try {
            d = sdf.parse(START_DATE);
        } catch (ParseException e) {
            return -1;
        }

        long t = d.getTime() / 1000;

        Date timeStart = new Date((timestamp + t) * 1000);

        long now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

        return now - timeStart.getTime();
    }

    public static boolean alarmEnabled(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(ALARM_ATTR, false);
    }

    public static boolean enableAlarm(Context context, boolean enable){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(ALARM_ATTR, enable);
        return prefsEditor.commit();
    }
}