package in.technostack.projects.converterforall.Dialogs;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import java.util.Calendar;
import in.technostack.projects.converterforall.R;

@TargetApi(Build.VERSION_CODES.N)
public class MarshmallowDatePickerDialog extends DatePickerDialog {
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    private final DatePicker mDatePicker;

    private OnDateSetListener mDateSetListener;

    public MarshmallowDatePickerDialog(@NonNull Context context,
                                       @Nullable OnDateSetListener listener,
                                       int year, int month, int dayOfMonth) {
        this(context, 0, listener, null, year, month, dayOfMonth);
    }

    private MarshmallowDatePickerDialog(@NonNull Context context, @StyleRes int themeResId,
                                        @Nullable OnDateSetListener listener,
                                        @Nullable Calendar calendar, int year,
                                        int monthOfYear, int dayOfMonth) {
        super(context, resolveDialogTheme(context, themeResId));

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.old_date_picker_dialog, null);
        setView(view);
        setButton(BUTTON_POSITIVE, getContext().getString(android.R.string.ok), this);
        setButton(BUTTON_NEGATIVE, getContext().getString(android.R.string.cancel), this);
        if (calendar != null) {
            year = calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        mDateSetListener = listener;
    }

    static @StyleRes
    int resolveDialogTheme(@NonNull Context context, @StyleRes int themeResId) {
        if (themeResId == 0) {
            final TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.datePickerDialogTheme, outValue, true);
            return outValue.resourceId;
        } else {
            return themeResId;
        }
    }

    @Override
    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
        mDatePicker.init(year, month, dayOfMonth, this);
    }
    public void setOnDateSetListener(@Nullable OnDateSetListener listener) {
        mDateSetListener = listener;
    }

    @Override
    public void onClick(@NonNull DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mDateSetListener != null) {
                    mDatePicker.clearFocus();
                    mDateSetListener.onDateSet(mDatePicker, mDatePicker.getYear(),
                            mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }
    @NonNull
    public DatePicker getDatePicker() {
        return mDatePicker;
    }
    public void updateDate(int year, int month, int dayOfMonth) {
        mDatePicker.updateDate(year, month, dayOfMonth);
    }

    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int year = savedInstanceState.getInt(YEAR);
        final int month = savedInstanceState.getInt(MONTH);
        final int day = savedInstanceState.getInt(DAY);
        mDatePicker.init(year, month, day, this);
    }
}
