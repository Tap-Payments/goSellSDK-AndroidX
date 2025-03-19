package company.tap.sample.activity;


import static java.security.AccessController.getContext;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import company.tap.sample.CustomTypefaceSpan;
import company.tap.sample.R;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(findViewById(R.id.settings_toolbar));
        setupActionBar();
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }



    private void setupActionBar() {
            ViewGroup rootView = (ViewGroup)findViewById(R.id.action_bar_root); //id from appcompat

        if (rootView != null) {
            View view = getLayoutInflater().inflate(R.layout.settings_toolbar, rootView, false);
            rootView.addView(view, 0);

            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_main);


            // sdk language change listener
               bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sdkLanguage)));

            // sdk mode change listener
               bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sdkMode)));

            // appearance mode change listener
               bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sdk_appearance_mode)));

            // transaction mode
               bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sdk_transaction_mode)));


            ListPreference currencyPreference = (ListPreference) findPreference(getString(R.string.key_sdk_transaction_currency));

            if (currencyPreference != null) {
                // Set Typeface for the selected currency in the summary
                applyCustomFontToPreference(currencyPreference);

                // Update the summary when a new value is selected
                currencyPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    applyCustomFontToPreference(currencyPreference);
                    return true;
                });
            }

            // transaction currency listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sdk_transaction_currency)));
//            // appearance header text color listener
//               bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_header_preference_color_key)));
//
//            // appearance header background color listener
//               bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_header_preference_background_color_key)));
//
//
//
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_card_input_fields_text_color_key)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_card_input_fields_invalid_text_color_key)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_card_input_fields_placeholder_text_color_key)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_card_input_fields_description_color_key)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_tap_button_disabled_background_color_key)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_tap_button_enabled_background_color_key)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_tap_button_disabled_titled_color_key)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.appearance_tap_button_enabled_titled_color_key)));
        }
        private void applyCustomFontToPreference(ListPreference preference) {
            // Get selected currency
            String selectedCurrency = preference.getValue();

            // Load custom font
            Typeface customFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/sar-Regular.otf");

            // Check if it's SAR currency
            if (selectedCurrency.equalsIgnoreCase("SR") ||
                    selectedCurrency.equalsIgnoreCase("SAR") ||
                    selectedCurrency.equals("ر.س")) {

                // Create a SpannableString with the custom font
                SpannableString spannable = new SpannableString("R");
                spannable.setSpan(new CustomTypefaceSpan("", customFont), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Delay the update using a Handler
                new android.os.Handler().postDelayed(() -> preference.setSummary(spannable), 1000);
            } else {
                // Default summary if not SAR
                new android.os.Handler().postDelayed(() -> preference.setSummary(preference.getEntry()), 1000);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));

    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            System.out.println(" &&&& " +preference.getKey() + " >>> "+ stringValue);

            SpannableString coloredText = new SpannableString(stringValue);

            if(preference instanceof com.tap.tapcolorskit.ColorPreferences){
//
//                Spannable summary = new SpannableString("Currently This Color");
//                summary.setSpan(new ForegroundColorSpan(Color.RED), 0, summary.length(), 0);

                preference.setSummary(stringValue);
            }

            else if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            }else if (preference instanceof EditTextPreference) {
                    preference.setSummary(stringValue);
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };




    public void back(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}