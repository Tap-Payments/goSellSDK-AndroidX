package company.tap.sample;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomListPreference extends ListPreference {
    private Typeface customFont;

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        customFont = Typeface.createFromAsset(context.getAssets(), "fonts/sar-Regular.otf"); // Load font
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        // Set custom font to title and summary
        TextView title = view.findViewById(android.R.id.title);
        TextView summary = view.findViewById(android.R.id.summary);

        if (title != null) title.setTypeface(customFont);
        if (summary != null) summary.setTypeface(customFont);
    }
}

