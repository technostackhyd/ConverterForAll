package in.technostack.projects.converterforall.FragmentActivities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.app.Fragment;
import androidx.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import in.technostack.projects.converterforall.DataModels.DateObj;
import in.technostack.projects.converterforall.Dialogs.Dialogs;
import in.technostack.projects.converterforall.Dialogs.MyDialogs;
import in.technostack.projects.converterforall.MainActivity;
import in.technostack.projects.converterforall.R;

public class dateCalculate extends Fragment{
    @Nullable
    View dateView;
    TextView date1;
    TextView date2;
    TextView date3;
    AutoCompleteTextView years;
    AutoCompleteTextView months;
    AutoCompleteTextView days;
    DatePickerDialog datePickerDialog;
    TextView lYear,lMonth,lDate;
    RadioButton add;
    RadioButton sub;
    TextView addSubRes,diffDays;
    ImageView fromDate,toDate,addSubDate;
    ImageView diffHelp,addSubHelp;
    final String HELP="Help";
    private Calendar selectedDate;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dateView=inflater.inflate(R.layout.date_calculate,container,false);
        diffHelp= dateView.findViewById(R.id.diff);
        addSubHelp= dateView.findViewById(R.id.addSub);
        date1= dateView.findViewById(R.id.editText3);
        date2= dateView.findViewById(R.id.editText4);
        date3=dateView.findViewById(R.id.date3);
        years= dateView.findViewById(R.id.yearsAdd);
        months= dateView.findViewById(R.id.monthsAdd);
        days= dateView.findViewById(R.id.daysAdd);
        add= dateView.findViewById(R.id.addR);
        sub= dateView.findViewById(R.id.subR);
        addSubRes= dateView.findViewById(R.id.addRes);
        lYear= dateView.findViewById(R.id.yrView);
        lMonth= dateView.findViewById(R.id.mmView);
        lDate= dateView.findViewById(R.id.ddView);
        fromDate= dateView.findViewById(R.id.fromDate);
        toDate= dateView.findViewById(R.id.toDate);
        addSubDate= dateView.findViewById(R.id.addSubDate);
        diffDays=dateView.findViewById(R.id.diffDays);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        date1.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        date2.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        date3.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        add.setChecked(true);
        addSubRes.setText(getResources().getString(R.string.SeeResult));
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setDate(date1);
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(date2);
            }
        });
        addSubDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear;
                int mMonth;
                int mDay;
                if (date3.getText().toString().isEmpty()) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    String curSetDate=date3.getText().toString();
                    String currArray[]=curSetDate.split("/");
                    mDay=Integer.parseInt(currArray[0]);
                    mMonth=Integer.parseInt(currArray[1])-1;
                    mYear=Integer.parseInt(currArray[2]);
                }
                DatePickerDialog d = Dialogs.createDatePicker(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date3.setText(dayOfMonth + "/"
                                + (month + 1) + "/" + year);
                        calculate(1,0,0,0);
                    }
                }, mYear, mMonth, mDay);
                d.show();
                d.getWindow().setBackgroundDrawableResource(R.color.colorHomeGridBack);
                d.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textColor));
                d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.textColor));
            }
        });
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(date1);
            }
        });
        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(date2);
            }
        });
        date3.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View view) {
                int mYear;
                int mMonth;
                int mDay;
                if (date3.getText().toString().isEmpty()) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    String curSetDate=date3.getText().toString();
                    String currArray[]=curSetDate.split("/");
                    mDay=Integer.parseInt(currArray[0]);
                    mMonth=Integer.parseInt(currArray[1])-1;
                    mYear=Integer.parseInt(currArray[2]);
                }
                DatePickerDialog d = Dialogs.createDatePicker(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date3.setText(dayOfMonth + "/"
                                + (month + 1) + "/" + year);
                        calculate(1,0,0,0);
                    }
                }, mYear, mMonth, mDay);
                d.show();
                d.getWindow().setBackgroundDrawableResource(R.color.colorHomeGridBack);
                d.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textColor));
                d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.textColor));
            }
        });
        add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                addSubTextChange();
            }
        });
        sub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                addSubTextChange();
            }
        });
        years.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addSubTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        months.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addSubTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        days.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addSubTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final FragmentManager fm = getFragmentManager();
        diffHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getResources().getString(R.string.help),getResources().getString(R.string.diff),false,false);
            }
        });
        addSubHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getResources().getString(R.string.help),getResources().getString(R.string.addSub),false,false);
            }
        });
        date3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addSubTextChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.dateCal));
        ((MainActivity) getActivity()).setScreenName(getResources().getString(R.string.dateCal));
        return dateView;
    }

    public void calculate(int a,int y,int m,int d){
        if (a==0){

            if (!date1.getText().toString().isEmpty() && !date2.getText().toString().isEmpty()){
                java.text.DateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                Date frmDate;
                Date toDate;
                try {
                    frmDate=format.parse(date1.getText().toString());
                    toDate=format.parse(date2.getText().toString());
                    DateObj res=calculateAge(frmDate,toDate);
                    if (!date1.getText().toString().equals(date2.getText().toString())){
                        if(res.getYears()==0 && res.getMonths()==0 && res.getDays()==0){
                            diffDays.setText("0");
                            lYear.setText("0");
                            lMonth.setText("0");
                            lDate.setText("0");
                            showDialog(getResources().getString(R.string.alert), getResources().getString(R.string.dateAlert),true,false);
                        }
                        else {
                            Double days=calculateDays(frmDate,toDate);
                            String dayStr=String.format("%d",Math.round(days));
                            diffDays.setText(dayStr);
                            lYear.setText(res.getYears()+"");
                            lMonth.setText(res.getMonths()+"");
                            lDate.setText(res.getDays()+"");
                        }
                    }
                    else {
                        lYear.setText(res.getYears()+"");
                        lMonth.setText(res.getMonths()+"");
                        lDate.setText(res.getDays()+"");
                        diffDays.setText("0");
                    }

                } catch (ParseException e) {
                }
            }
            else
            {
                lYear.setText("0");
                lMonth.setText("0");
                lDate.setText("0");
                diffDays.setText("0");
            }
        }
        else if (a==1) {
                if (!date3.getText().toString().isEmpty()){
                    java.text.DateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                    Date date;
                    try {
                        date=format.parse(date3.getText().toString());
                        if (add.isChecked()) {
                            Calendar calendar=Calendar.getInstance();
                            calendar.setTimeInMillis(date.getTime());
                            calendar.add(Calendar.DAY_OF_MONTH,d);
                            calendar.add(Calendar.MONTH,m);
                            calendar.add(Calendar.YEAR,y);
                            java.text.DateFormat df=new SimpleDateFormat("dd MMM yyyy");
                            addSubRes.setText(df.format(calendar.getTime()).toString());
                        }
                        else if (sub.isChecked()){
                            Calendar calendar=Calendar.getInstance();
                            calendar.setTimeInMillis(date.getTime());
                            calendar.add(Calendar.DAY_OF_MONTH,-d);
                            calendar.add(Calendar.MONTH,-m);
                            calendar.add(Calendar.YEAR,-y);
                            java.text.DateFormat df=new SimpleDateFormat("dd MMM yyyy");
                            addSubRes.setText(df.format(calendar.getTime()).toString());
                        }
                    } catch (ParseException e) {
                    }

                }
                else {
                    addSubRes.setText(getResources().getString(R.string.selectDate));
                }
        }
    }
    private DateObj calculateAge(Date fromDate, Date toDate) {
        int years;
        int months;
        int days;
        Calendar fromDay = Calendar.getInstance();
        Calendar toDay = Calendar.getInstance();
        fromDay.setTimeInMillis(fromDate.getTime());
        toDay.setTimeInMillis(toDate.getTime());
        if (fromDay.getTimeInMillis()<toDay.getTimeInMillis()){
            //Get difference between years
            years = toDay.get(Calendar.YEAR) - fromDay.get(Calendar.YEAR);
            int toMonth = toDay.get(Calendar.MONTH) + 1;
            int fromMonth = fromDay.get(Calendar.MONTH) + 1;
            //Get difference between months
            months = toMonth - fromMonth;//2
            //if month difference is in negative then reduce years by one and calculate the number of months.
            if (months < 0)
            {
                years--;
                months = 12 - fromMonth + toMonth;
            }
            if ((toDay.getActualMaximum(Calendar.DAY_OF_MONTH)==29 && fromDay.getActualMaximum(Calendar.DAY_OF_MONTH)==28) || (toDay.getActualMaximum(Calendar.DAY_OF_MONTH)==28 && fromDay.getActualMaximum(Calendar.DAY_OF_MONTH)==29)){
                int t=toDay.get(Calendar.DATE);
                int f=fromDay.get(Calendar.DATE);
                if ((t==29 && f==28)|| (t==28 && f==29))
                    return new DateObj(0, months, years);
            }

            days=toDay.get(Calendar.DATE)-fromDay.get(Calendar.DATE);
            if (days<0){
                if (months==0){
                    years--;
                    months=11;
                }
                else
                months--;
                int today = toDay.get(Calendar.DAY_OF_MONTH);
                toDay.add(Calendar.MONTH, -1);
                days = toDay.getActualMaximum(Calendar.DAY_OF_MONTH) - fromDay.get(Calendar.DAY_OF_MONTH) + today;
            }
            return new DateObj(days, months, years);
        }
        else {
            return new DateObj (0,0,0);
        }

    }
    public void addSubTextChange(){
        int y=0;
        int m=0;
        int d=0;
        if (!years.getText().toString().isEmpty())
            y= Integer.parseInt(years.getText().toString());
        if (!months.getText().toString().isEmpty())
            m= Integer.parseInt(months.getText().toString());
        if (!days.getText().toString().isEmpty())
            d= Integer.parseInt(days.getText().toString());
        calculate(1,y,m,d);
    }

    private void setDate(final TextView date) {
        int mYear;
        int mMonth;
        int mDay;
        if (date.getText().toString().isEmpty()) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String curSetDate = date.getText().toString();
            String currArray[] = curSetDate.split("/");
            mDay = Integer.parseInt(currArray[0]);
            mMonth = Integer.parseInt(currArray[1]) - 1;
            mYear = Integer.parseInt(currArray[2]);
        }

        DatePickerDialog d = Dialogs.createDatePicker(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(dayOfMonth + "/"
                        + (month + 1) + "/" + year);
                calculate(0,0,0,0);
            }
        }, mYear, mMonth, mDay);
        d.show();
        d.getWindow().setBackgroundDrawableResource(R.color.colorHomeGridBack);
        d.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textColor));
        d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.textColor));
    }
    public void showDialog(String title,String message, boolean swap,boolean rate)
    {
        FragmentManager fm = getFragmentManager();
        MyDialogs dd=new MyDialogs();
        dd.sTitle=title;
        dd.message=message;
        dd.swap=swap;
        dd.rating=rate;
        dd.setTargetFragment(this,1);
        dd.show(fm,"");
    }


    public Double calculateDays(Date startDate, Date endDate)
    {
        long different = endDate.getTime() - startDate.getTime();
        Double secondsInMilli = 1000.0;
        Double minutesInMilli = secondsInMilli * 60;
        Double hoursInMilli = minutesInMilli * 60;
        Double daysInMilli = hoursInMilli * 24;
        Double elapsedDays = different / daysInMilli;
        return elapsedDays;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        String res=data.getStringExtra("Result");
        if (requestCode==1 && res.equals("swap")){
            String dateStr1= date1.getText().toString();
            String dateStr2= date2.getText().toString();
            date2.setText(dateStr1);
            date1.setText(dateStr2);
            calculate(0,0,0,0);
        }
    }
}

