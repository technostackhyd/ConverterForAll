package in.technostack.projects.converterforall.Classes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import in.technostack.projects.converterforall.R;

public class MyPreferencesCategory extends PreferenceCategory {
    Context currContext;
    public MyPreferencesCategory(Context context) {
        super(context);
        currContext = context;
    }

    public MyPreferencesCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        currContext = context;
    }

    public MyPreferencesCategory(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        currContext = context;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView view = (TextView)holder.findViewById(android.R.id.title);
        view.setTextColor(currContext.getResources().getColor(R.color.textColor));
    }
}
