/*
 * Copyright (C) 2010-12  Ciaran Gultnieks, ciaran@ciarang.com
 * Copyright (C) 2013-2017  Peter Serwylo <peter@serwylo.com>
 * Copyright (C) 2013-2016  Daniel Martí <mvdan@mvdan.cc>
 * Copyright (C) 2014-2018  Hans-Christoph Steiner <hans@eds.org>
 * Copyright (C) 2018  Senecto Limited
 * Copyright (C) 2019 Andreas Redmer <ar-gdroid@abga.be>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.gdroid.gdroid.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * Handles shared preferences for G-Droid, looking after the names of
 * preferences, default values and caching. Needs to be setup in the GDroidApp
 * (using {@link Pref#setup(android.content.Context)} before it gets
 * accessed via the {@link org.gdroid.gdroid.Pref#get()}
 * singleton method.
 */
public class Pref {
    private static final String TAG = "Pref";

    private final SharedPreferences preferences;
    private static Pref instance;

    private Pref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static final String PREF_AUTO_DOWNLOAD_INSTALL_UPDATES = "updateAutoDownload";
    public static final String PREF_SEND_CRASH_REPORTS = "send_crash_reports";

    // private properties are non-user visible settings
    private static final String PREF_LAST_UPDATE_CHECK = "lastUpdateCheck";
    private static final String PREF_LAST_FDROID_ETAG = "last_fdroid_etag";
    private static final String PREF_LAST_GDROID_ETAG = "last_gdroid_etag";
    private static final String PREF_CACHED_COMMENTED_APPS = "cached_commented_apps";

    private static final int DEFAULT_LAST_UPDATE_CHECK = -1;

    public boolean sendCrashReports() {
        return preferences.getBoolean(PREF_SEND_CRASH_REPORTS, true);
    }

    public long getLastUpdateCheck() {
        return preferences.getLong(PREF_LAST_UPDATE_CHECK, DEFAULT_LAST_UPDATE_CHECK);
    }

    public void setLastUpdateCheck(long lastUpdateCheck) {
        preferences.edit().putLong(PREF_LAST_UPDATE_CHECK, lastUpdateCheck).apply();
    }

    public void resetLastUpdateCheck() {
        setLastUpdateCheck(DEFAULT_LAST_UPDATE_CHECK);
    }

    public boolean isIndexNeverUpdated() {
        return getLastUpdateCheck() == DEFAULT_LAST_UPDATE_CHECK;
    }

    public boolean isAutoDownloadEnabled() {
        return preferences.getBoolean(PREF_AUTO_DOWNLOAD_INSTALL_UPDATES, false);
    }

    public String getLastFDroidEtag() {
        return preferences.getString(PREF_LAST_FDROID_ETAG, "");
    }

    public void setLastFDroidEtag(String lastFDroidEtag) {
        preferences.edit().putString(PREF_LAST_FDROID_ETAG, lastFDroidEtag).apply();
    }

    public String getLastGDroidEtag() {
        return preferences.getString(PREF_LAST_GDROID_ETAG, "");
    }

    public void setLastGDroidEtag(String lastGDroidEtag) {
        preferences.edit().putString(PREF_LAST_GDROID_ETAG, lastGDroidEtag).apply();
    }

    public String getLastCommentedApps() {
        return preferences.getString(PREF_CACHED_COMMENTED_APPS, "");
    }

    public void setLastCommentedApps(String lastCommentedApps) {
        preferences.edit().putString(PREF_CACHED_COMMENTED_APPS, lastCommentedApps).apply();
    }

    /**
     * Needs to be setup before anything else tries to access it.
     */
    public static void setup(Context context) {
        if (instance != null) {
            final String error = "Attempted to reinitialize preferences after it " +
                    "has already been initialized in GDroidApp";
            Log.e(TAG, error);
            throw new RuntimeException(error);
        }
        instance = new Pref(context);
    }

    public static Pref get() {
        if (instance == null) {
            final String error = "Attempted to access preferences before it " +
                    "has been initialized in GDroidApp";
            Log.e(TAG, error);
            throw new RuntimeException(error);
        }
        return instance;
    }


}
