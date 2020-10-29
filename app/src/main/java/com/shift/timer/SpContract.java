package com.shift.timer;

import android.content.SharedPreferences;

import com.tfcporciuncula.flow.FlowSharedPreferences;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.MutableStateFlow;
import timber.log.Timber;

/**
 * Created by roy on 5/27/2017.
 */
public class SpContract {
    private static final String TAG = "SpContract";

    private static final String KEY_AUTH_TOKEN = "pref_auth";
    private static final String KEY_RESTAURANT_ID = "pref_restaurant_id";
    private static final String KEY_WORKPLACE_ID = "pref_workplace_id";
    private static final String PREF_STORE_ID = "pref_store_id";
    private static final String PREF_CURRENT_STORE_ID = "pref_current_store_id";
    private static final String PREF_CURRENT_VENDOR_ID = "pref_current_vendor_id";
    private static final String KEY_PREF_PHONE = "pref_phone";
    private static final String PREF_USER_NAME = "pref_username";
    private static final String PREF_USER_ID = "pref_user_id";
    private static final String PREF_EMAIL_ADDRESS = "pref_email_address";
    private static final String PREF_REFERRAL_CODE = "pref_referral_code";
    private static final String PREF_RESTAURANT_NAME = "pref_restaurant_name";
    private static final String KEY_PREF_ORDER_ID = "pref_order_id";
    private static final String KEY_PREF_VERSION_ID = "pref_version_code";
    private static final String PREF_ORDER_GUIDE_LAST_MODIFIED_INT = "pref_order_guide_last_modified_int";
    private static final String PREF_ORDER_DETAILS_LAST_MODIFIED_INT = "pref_order_details_last_modified_int";
    private static final String PREF_LAST_TIME_FETCHED_ORDER_DETAILS = "pref_last_time_fetched_order_details";
    private static final String PREF_LAST_TIME_FETCHED_CATEGORIES = "pref_last_time_fetched_categories";
    private static final String PREF_SORT_TYPE = "pref_sort_type";
    private static final String PREF_LAST_FETCHED_COWORKERS = "pref_last_fetched_coworkers";
    private static final String PREF_LAST_TIME_FETCHED_RESTAURANT = "pref_last_time_fetched_restaurant";
    private static final String PREF_SHOW_REPLACEMENT_POLICY_MESSAGE = "pref_show_replacement_policy_message";
    private static final String PREF_SHOW_UPDATES_TO_CART_MESSAGE = "pref_show_updates_to_cart_message";
    private static final String KEY_PREF_DISTINCT_ID = "pref_distinct_id";
    private static final String KEY_PREF_FIRST_RUN = "pref_first_run";
    private static final String PREF_ORDER_RATING = "pref_order_rating";
    private static final String PREF_IN_APP_MESSAGE_ID = "pref_in_app_message_id";
    private static final String PREF_VERSION_CODE = "pref_version_code";
    private static final String PREF_BUSINESS_TYPE = "business_type";
    private static final String PREF_SHOW_ADD_ADDRESS_SUGGESTION = "pref_show_add_address_suggestion";
    private static final String PREF_SUID = "pref_suid";

    public static final int NO_MESSAGE_ID = -1;

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
