package com.shift.timer;

import android.content.SharedPreferences;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by roy on 5/27/2017.
 */
public class SpContract {
    private static final String TAG = "SpContract";

    private static final String KEY_WORKPLACE_ID = "pref_workplace_id";

    private SharedPreferences sp;

    @Inject
    public SpContract(SharedPreferences sp) {
        this.sp = sp;

        Timber.tag(TAG);
    }

    public int getWorkplaceId() {
        return sp.getInt(KEY_WORKPLACE_ID, DEFAULT_WORKPLACE_ID);
    }

    public void setWorkplaceId(int id) {
        sp.edit().putInt(KEY_WORKPLACE_ID, id).apply();
    }

    public static int DEFAULT_WORKPLACE_ID = -1;

}
