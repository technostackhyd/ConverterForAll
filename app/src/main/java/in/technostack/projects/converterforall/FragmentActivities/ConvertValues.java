package in.technostack.projects.converterforall.FragmentActivities;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;*/
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import in.technostack.projects.converterforall.DataModels.CurrencyModel;
import in.technostack.projects.converterforall.DataModels.ValueObject;
import in.technostack.projects.converterforall.Database.CurrencyRates;
import in.technostack.projects.converterforall.Database.CurrencyReaderHelper;
import in.technostack.projects.converterforall.Dialogs.MyDialogs;
import in.technostack.projects.converterforall.Dialogs.SelectCurrencyDialog;
import in.technostack.projects.converterforall.ViewAdapters.CurrArrayAdapter;
import in.technostack.projects.converterforall.ViewAdapters.ListViewAdapter;
import in.technostack.projects.converterforall.MainActivity;
import in.technostack.projects.converterforall.R;
import in.technostack.projects.converterforall.SearchableSpinner;
import in.technostack.projects.converterforall.Values;
import com.facebook.ads.*;

public class ConvertValues extends Fragment {
    @Nullable
    View view;
    TextView eqabt;
    AutoCompleteTextView val1;
    AutoCompleteTextView val2;
    SearchableSpinner list1;
    SearchableSpinner list2;
    ListView lv;
    ArrayList<String> list;
    ArrayList<CurrencyModel> currList;
    ArrayList<Integer> currOther=new ArrayList<>();
    int listSize=0;
    int v=1;
    int id;
    ArrayList<ArrayList<ValueObject>> values;
    ArrayList<ArrayList<Double>> sValues;
    ConnectivityManager conMgr;
    BroadcastReceiver rec;
    IntentFilter intentFilter;
    CurrencyReaderHelper mDbHelper;
    TextView rateTime;
    boolean currentState;
    String test="";
    String Title;
    ImageButton swap;
    boolean inUpRes;
    int selection1=0;
    int selection2=1;
    FillOthers task;
    private AdView mAdView;
    LinearLayout linearLayout;
    String activeConvert="";
    static{
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.convert,container,false);
        mDbHelper=new CurrencyReaderHelper(getActivity().getApplicationContext());
        rateTime= view.findViewById(R.id.rateTime);
        linearLayout=view.findViewById(R.id.convertLL1);
        setHasOptionsMenu(true);
        int maxLength = 15;
        final InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        val1= view.findViewById(R.id.value1);
        val2=view.findViewById(R.id.value2);
        list1= view.findViewById(R.id.list1);
        list2= view.findViewById(R.id.list2);
        lv=view.findViewById(R.id.others);
        eqabt= view.findViewById(R.id.eqabt);
        swap=view.findViewById(R.id.swap);
        currentState=checkConnection();
        val1.requestFocus();

        id=this.getArguments().getInt("case");
            if (id==R.id.curr){
                boolean b=isCurrAvailable();
                if (!b) {
                    rateTime.setText(getResources().getString(R.string.noCurrSave));
                    insertUpdateCurrency(1);
                }
                else {
                    setTime();
                }
            }
            else {
                rateTime.setVisibility(View.GONE);
            }
        if (id==R.id.temp){
            val1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            val2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        getValues(id);
        if (id!=R.id.curr) {
            list1.setCurr(false);
            list2.setCurr(false);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            list1.setAdapter(adapter);
            list2.setAdapter(adapter);
        }
        else {
            list1.setCurr(true);
            list2.setCurr(true);
            CurrArrayAdapter adapter = new CurrArrayAdapter(getActivity(), R.layout.currency_list_item, currList);
            list1.setAdapter(adapter);
            list2.setAdapter(adapter);
            if (isCurrAvailable()) {
                    setSelectionPref();
            }
        }
        if (!isPrefAvailable())
        {
            insertConvertPref();
        }
        else {
            setConvertPref(activeConvert);
        }
        list1.setSelection(selection1);
        list2.setSelection(selection2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            eqabt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_up_black_24dp,0);
        }
        eqabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayout.getVisibility()==View.VISIBLE){
                    linearLayout.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        eqabt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_down_black_24dp,0);
                    }
                }
                else {
                    linearLayout.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        eqabt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_up_black_24dp,0);
                    }
                }
            }
        });
        if (Title!=null)
        {
            list1.setTitle(Title);
            list2.setTitle(Title);
        }
        val1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v=1;
            }
        });
        val2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v=2;
            }
        });
        val1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    v=1;
                    val1.setTypeface(null, Typeface.BOLD);
                    val2.setTypeface(null, Typeface.NORMAL);
                }

            }
        });
        val2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    v=2;
                    val2.setTypeface(null, Typeface.BOLD);
                    val1.setTypeface(null, Typeface.NORMAL);
                }

            }
        });

        val1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (v==1){
                    val1.setFilters(fArray);
                    val2.setFilters(new InputFilter[] {});
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int pos1=list1.getSelectedItemPosition();
                int pos2=list2.getSelectedItemPosition();
                BigDecimal text_data=BigDecimal.ZERO;
                BigDecimal text_result;
                BigDecimal set_text;
                if (v==1 && id!=R.id.curr){
                    if(id == R.id.len){
                        if(pos1==7 && pos2==6){
                            if(!val1.getText().toString().isEmpty()){
                                String val=val1.getText().toString();
                                if(val.length()>1)
                                {
                                    text_data=new BigDecimal(val);
                                    set_text= text_data.setScale(1, RoundingMode.FLOOR);
                                    text_result= text_data.subtract(text_data.setScale(0, RoundingMode.FLOOR)).movePointRight(text_data.scale());
                                    if(text_result.intValue()>=12){
                                        val1.removeTextChangedListener(this);
                                        val1.setText(set_text.toString());
                                        val1.setSelection(val1.getText().length());
                                        val1.addTextChangedListener(this);
                                        showDialog(getResources().getString(R.string.note), getResources().getString(R.string.inchFeet));
                                    }
                                }
                            }
                        }
                    }
                    callMethod(1);
                }
                else if (v==1 && id==R.id.curr){
                    if (isCurrAvailable())
                    {
                        if (!val1.getText().toString().isEmpty())
                            calculateCurrency(val1,((CurrencyModel)list1.getSelectedItem()).getCurrName().toString().substring(0,3),val2,((CurrencyModel)list2.getSelectedItem()).getCurrName().toString().substring(0,3));
                        else
                            val2.setText("");
                    }
                }
                if (val1.getText().toString().isEmpty() || val2.getText().toString().isEmpty()){
                    eqabt.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        val2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (v==2) {
                    val2.setFilters(fArray);
                    val1.setFilters(new InputFilter[]{});
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int pos1=list1.getSelectedItemPosition();
                int pos2=list2.getSelectedItemPosition();
                BigDecimal text_data;
                BigDecimal text_result;
                BigDecimal set_text;
                if (v==2 && id!=R.id.curr) {
                    if(id == R.id.len){
                        if(pos1==6 && pos2==7){
                            if(!val2.getText().toString().isEmpty()){
                                String val=val2.getText().toString();
                                if(val.length()>1)
                                {
                                    text_data=new BigDecimal(val);
                                    set_text= text_data.setScale(1, RoundingMode.FLOOR);
                                    text_result= text_data.subtract(text_data.setScale(0, RoundingMode.FLOOR)).movePointRight(text_data.scale());
                                    if(text_result.intValue()>=12){
                                        val2.removeTextChangedListener(this);
                                        val2.setText(set_text.toString());
                                        val2.setSelection(val2.getText().length());
                                        val2.addTextChangedListener(this);
                                        showDialog(getResources().getString(R.string.note),getResources().getString(R.string.inchFeet));
                                    }
                                }
                            }
                        }
                    }
                    callMethod(2);
                }

                else if (id == R.id.curr && v == 2) {
                    if (isCurrAvailable())
                    {
                        if (!val2.getText().toString().isEmpty())
                            calculateCurrency(val2,((CurrencyModel)list2.getSelectedItem()).getCurrName().toString().substring(0,3),val1,((CurrencyModel)list1.getSelectedItem()).getCurrName().toString().substring(0,3));
                        else
                            val1.setText("");
                    }
                }
                if (val1.getText().toString().isEmpty() || val2.getText().toString().isEmpty()){
                    eqabt.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        list1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!val1.getText().toString().isEmpty() || !val2.getText().toString().isEmpty()){
                    if (id!=R.id.curr) {
                        callMethod(v);
                    }
                    else if (id==R.id.curr){
                        if (isCurrAvailable())
                        {
                            if (v==1){
                                if (!val1.getText().toString().isEmpty())
                                   calculateCurrency(val1,((CurrencyModel)list1.getSelectedItem()).getCurrName().toString().substring(0,3),val2,((CurrencyModel)list2.getSelectedItem()).getCurrName().toString().substring(0,3));

                            }
                            else if (v==2)
                            {
                                if (!val2.getText().toString().isEmpty())
                                    calculateCurrency(val2,((CurrencyModel)list2.getSelectedItem()).getCurrName().toString().substring(0,3),val1,((CurrencyModel)list1.getSelectedItem()).getCurrName().toString().substring(0,3));
                            }
                        }

                    }
                }
                else
                {
                    eqabt.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        list2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!val1.getText().toString().isEmpty() && !val2.getText().toString().isEmpty()) {
                    if (id!=R.id.curr) {
                        callMethod(v);
                    }
                    else if (id==R.id.curr){
                        if (isCurrAvailable())
                        {
                            if (v==1){
                                if (!val1.getText().toString().isEmpty())
                                    calculateCurrency(val1,((CurrencyModel)list1.getSelectedItem()).getCurrName().toString().substring(0,3),val2,((CurrencyModel)list2.getSelectedItem()).getCurrName().toString().substring(0,3));

                            }
                            else if (v==2)
                            {
                                if (!val2.getText().toString().isEmpty())
                                    calculateCurrency(val2,((CurrencyModel)list2.getSelectedItem()).getCurrName().toString().substring(0,3),val1,((CurrencyModel)list1.getSelectedItem()).getCurrName().toString().substring(0,3));
                            }
                        }
                    }
                }
                else
                {
                    eqabt.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        rec=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (id == R.id.curr) {
                    if (!checkConnection()==currentState){
                        if (checkConnection()) {
                            currentState=true;
                            if (!isCurrAvailable())
                                insertUpdateCurrency(1);
                        }
                        else {
                            currentState=false;
                        }
                    }
                }


            }
        };

        getActivity().registerReceiver(rec, intentFilter);
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int l1=list1.getSelectedItemPosition();
                        int l2=list2.getSelectedItemPosition();
                        list1.setSelection(l2);
                        list2.setSelection(l1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                swap.startAnimation(animation);
            }
        });
        /*mAdView = new com.facebook.ads.AdView(getActivity(), "225154511577364_435910703835076", AdSize.RECTANGLE_HEIGHT_250);
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        AdView.AdViewLoadConfig loadAdConfig = mAdView.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();
        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) view.findViewById(R.id.technoTwo);
        // Add the ad view to your activity layout
        adContainer.addView(mAdView);
        // Request an ad
        //AdSettings.addTestDevice("fbb3f244-7ea9-4122-ab5c-d2f68f3e5d1b");
        mAdView.loadAd(loadAdConfig);*/
        setScreen(activeConvert);
        return view;
    }

    //Method to set currency rate time
    private void setTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        dateFormat.setTimeZone(TimeZone.getDefault());
        String time=getTimeStamp();
        Calendar oldDate=Calendar.getInstance();
        oldDate.setTimeInMillis(Long.parseLong(time)*1000);
        String d1=dateFormat.format(oldDate.getTime());
        rateTime.setText(getResources().getString(R.string.refreshCurr1)+d1+getResources().getString(R.string.refreshCurr2));
    }

    //Method to get Required values in list
    private void getValues(int id){
        Values obj=new Values();
        switch (id){
            case R.id.angle:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Angle));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.angleArray)));
                listSize=list.size();
                activeConvert="angle";
                break;
            case R.id.area:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Area));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.areaArray)));
                listSize=list.size();
                values=obj.createAreaList();
                activeConvert="area";
                break;
            case R.id.data:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Data));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dataArray)));
                listSize=list.size();
                sValues=obj.createDataList();
                activeConvert="data";
                break;
            case R.id.len:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Length));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lengthArray)));
                listSize=list.size();
                values=obj.createLengthList();
                activeConvert="length";
                break;
            case R.id.power:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Power));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.powerArray)));
                listSize=list.size();
                values=obj.createPowerList();
                activeConvert="power";
                break;
            case R.id.pressure:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Pressure));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.pressureArray)));
                listSize=list.size();
                sValues=obj.createPressureList();
                activeConvert="pressure";
                break;
            case R.id.frequency:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Frequency));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.frequencyArray)));
                listSize=list.size();
                values=obj.createFreqList();
                activeConvert="Frequency";
                break;
            case R.id.speed:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Speed));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.speedArray)));
                listSize=list.size();
                values=obj.createSpeedList();
                activeConvert="speed";
                break;
            case R.id.datarate:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.DataTransRate));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.datarateArray)));
                listSize=list.size();
                sValues=obj.createDataTransList();
                activeConvert="Data Rate";
                break;
            case R.id.temp:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Temperature));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tempArray)));
                listSize=list.size();
                activeConvert="temperature";
                break;
            case R.id.time:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Time));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.timeArray)));
                listSize=list.size();
                values=obj.createTimeList();
                activeConvert="time";
                break;
            case R.id.vol:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Volume));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.VolumeArray)));
                listSize=list.size();
                values=obj.createVolumeList();
                activeConvert="volume";
                break;
            case R.id.wt:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.WeightMass));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.weightArray)));
                listSize=list.size();
                values=obj.createWeightList();
                activeConvert="weight";
                break;
            case R.id.energy:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Energy));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.energyArray)));
                listSize=list.size();
                values=obj.createEnergyList();
                activeConvert="energy";
                break;
            case R.id.curr:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Currency));
                currList=obj.getCurrency();
                listSize=currList.size();
                activeConvert="currency";
                Title="Select Currency";
                break;
            case R.id.density:
                ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.densityTitle));
                list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.densityArray)));
                listSize=list.size();
                sValues=obj.createDensityList();
                activeConvert="density";
        }
    }

    private void callMethod(int i){
        switch (id){
            case R.id.temp:
                convertTemp(i);
                break;
            case R.id.angle:
                convertAngle(i);
                break;
            case R.id.data:
                convertData(i);
                break;
            case R.id.datarate:
                convertData(i);
                break;
            default:
                if (i==1)
                    convertValues(val1,list1,val2,list2);
                else
                    convertValues(val2,list2,val1,list1);
                break;
        }
    }

    //Following two methods are to convert All Other Values
    private void convertValues(AutoCompleteTextView v1,SearchableSpinner l1,AutoCompleteTextView v2,SearchableSpinner l2){
        int position1;
        int position2;
        long base_value;
        long dec_value;
        long total_value;
        BigDecimal base_dec=BigDecimal.ZERO;
        BigDecimal bg1;
        BigDecimal bg2;
        BigDecimal bg3;
        MathContext mc=new MathContext(100);

        if (!v1.getText().toString().isEmpty()){
            try
            {
                position1=l1.getSelectedItemPosition();
                position2=l2.getSelectedItemPosition();
                if (id==R.id.density || id==R.id.pressure){
                    bg1=new BigDecimal(v1.getText().toString().replace(",",""));
                    bg2=new BigDecimal(sValues.get(0).get(position1).toString());
                    bg3=new BigDecimal(sValues.get(0).get(position2).toString());
                }
                else {
                    bg1=new BigDecimal(v1.getText().toString().replace(",",""));
                    bg2=new BigDecimal(values.get(position1).get(position2).getBase().toString());
                    bg3=new BigDecimal(values.get(position1).get(position2).getValue().toString());
                }

                if(id==R.id.len){
                    if(position1==7 && position2==6){
                        base_value=bg1.longValue();
                        BigDecimal r = bg1.subtract(bg1.setScale(0, RoundingMode.FLOOR)).movePointRight(bg1.scale());
                        dec_value=r.longValue()%12;
                        total_value=base_value*12+dec_value;
                        bg3=BigDecimal.valueOf(total_value);
                    }
                    else if(position1==6 && position2==7){
                        base_value=bg1.longValue()/12;
                        dec_value=bg1.longValue()%12;
                        if(dec_value<10)
                        {
                            base_dec=BigDecimal.valueOf(dec_value).divide(BigDecimal.valueOf(10));
                            base_dec=base_dec.add(BigDecimal.valueOf(base_value));
                            bg3=base_dec;
                        }
                        else if(dec_value==10){
                            base_dec = BigDecimal.valueOf(dec_value).divide(BigDecimal.valueOf(100));
                            base_dec=base_dec.add(BigDecimal.valueOf(base_value));
                            bg3=base_dec;
                            bg3=bg3.setScale(2,BigDecimal.ROUND_HALF_UP);
                        }
                        else if (dec_value==11)
                        {
                            base_dec = BigDecimal.valueOf(dec_value).divide(BigDecimal.valueOf(100));
                            base_dec=base_dec.add(BigDecimal.valueOf(base_value));
                            bg3=base_dec;
                        }
                    }
                    else{

                        bg3=bg3.multiply(bg1,mc);
                        bg3=bg3.divide(bg2,mc);
                    }
                }
                else{

                    bg3=bg3.multiply(bg1,mc);
                    bg3=bg3.divide(bg2,mc);
                }
                DecimalFormat format = new DecimalFormat("#######.################");
                BigDecimal result=setPrecision(bg3);
                if(id!=R.id.energy) {
                    if (result == null){
                        if(id==R.id.len && position1==6 && position2==7 )
                        {
                            v2.setText(bg3.toPlainString());
                        }
                        else{
                            v2.setText(format.format(bg3));
                        }
                    }
                    else
                    {
                            v2.setText(format.format(result));
                    }
                }
                else
                    v2.setText(bg3.toString());
                if (id==R.id.density || id==R.id.pressure){

                }
                else {
                    task=new FillOthers(position1,position2,bg1);
                    task.execute();
                }

            }
            catch (Exception ex){
                Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_LONG).show();
            }
        }
        else {
            v2.setText("");
            eqabt.setVisibility(View.GONE);
            lv.setVisibility(View.GONE);
        }
    }


    //Class which calculates other values
    private class FillOthers extends AsyncTask<Void,Void,String>{
        int p1;
        int p2;
        BigDecimal value;
        ListViewAdapter lva;
        public FillOthers(int p11,int p22,BigDecimal valuee){
            this.p1=p11;
            this.p2=p22;
            this.value=valuee;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            if (s.equals("s")) {
                if (lv != null) {

                            eqabt.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.VISIBLE);
                            lv.setAdapter(lva);


                }
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                int i=0;
                int j=0;
                int size;
                long base_value;
                long dec_value;
                long total_value;
                BigDecimal base_dec=BigDecimal.ZERO;
                MathContext mc=new MathContext(100, RoundingMode.DOWN);
                if (p1==p2)
                    size=listSize-1;
                else
                    size=listSize-2;
                String[] names=new String[size];
                String[] otherValues=new String[size];
                while(i<size){
                    if (p1!=j && p2!=j)
                    {
                        BigDecimal bg1=value;
                        BigDecimal bg2=new BigDecimal(values.get(p1).get(j).getBase().toString());
                        BigDecimal bg3=new BigDecimal(values.get(p1).get(j).getValue().toString());
                        if(p1==7 && j==6){
                            base_value=bg1.longValue();
                            BigDecimal r = bg1.subtract(bg1.setScale(0, RoundingMode.FLOOR)).movePointRight(bg1.scale());
                            dec_value=r.longValue()%12;
                            total_value=base_value*12+dec_value;
                            bg3=BigDecimal.valueOf(total_value);
                        }
                        else if(p1==6 && j==7){

                            base_value=bg1.longValue()/12;
                            dec_value=bg1.longValue()%12;
                            if(dec_value<10)
                            {
                                base_dec=BigDecimal.valueOf(dec_value).divide(BigDecimal.valueOf(10));
                                base_dec=base_dec.add(BigDecimal.valueOf(base_value));
                                bg3=base_dec;
                            }
                            else if(dec_value==10){
                                base_dec = BigDecimal.valueOf(dec_value).divide(BigDecimal.valueOf(100));
                                base_dec=base_dec.add(BigDecimal.valueOf(base_value));
                                bg3=base_dec;
                                bg3=bg3.setScale(2,BigDecimal.ROUND_HALF_UP);
                            }
                            else if (dec_value==11)
                            {
                                base_dec = BigDecimal.valueOf(dec_value).divide(BigDecimal.valueOf(100));
                                base_dec=base_dec.add(BigDecimal.valueOf(base_value));
                                bg3=base_dec;
                            }
                        }
                        else{

                            bg3=bg3.multiply(bg1,mc);
                            bg3=bg3.divide(bg2,mc);
                        }

                        DecimalFormat format=new DecimalFormat("#,###,###.###############");
                        names[i]=list.get(j);
                        BigDecimal result=setPrecision(bg3);
                        if (id!=R.id.energy) {
                            if (result == null){
                                if (id == R.id.len && p1 == 6 && j == 7) {
                                    otherValues[i] =bg3.toPlainString();
                                } else {
                                    otherValues[i] = format.format(bg3);
                                }
                            }
                            else
                                otherValues[i] = format.format(result);
                        }
                        else
                            otherValues[i] = bg3.toString();
                        i++;
                    }
                    j++;

                }
                lva=new ListViewAdapter(getActivity(),names,otherValues);
                return "s";
            }
            catch (Exception e){
                return "f";
            }
        }
    }

    //Following two methods are to convert Angle
    private void convertAngle(int i) {
        Double value;
        int position1;
        int position2;
        if (i == 1) {
            if (!val1.getText().toString().isEmpty()) {
                try {
                    value = Double.parseDouble(val1.getText().toString());
                    position1 = list1.getSelectedItemPosition();
                    position2 = list2.getSelectedItemPosition();
                    convert(position1,position2,value,i);
                } catch (Exception ex) {

                }
            }
            else {
                val2.setText("");
            }

        } else if (i == 2) {
            if (!val2.getText().toString().isEmpty()) {
                try {
                    value = Double.parseDouble(val2.getText().toString());
                    position1 = list2.getSelectedItemPosition();
                    position2 = list1.getSelectedItemPosition();
                    convert(position1,position2,value,i);
                } catch (Exception ex) {

                }
            } else {
                val1.setText("");
            }
        }
    }
    private void convert(int p1,int p2,Double value,int j){
        try{
        Double deg;
        Double rad;
        Double grad;
        int size;
        if (p1==p2)
            size=2;
        else
            size=1;
        String[] names=new String[size];
        String[] values=new String[size];
        DecimalFormat format = new DecimalFormat("###############.###############");
            switch (p1)
            {
                case 0:
                    rad= Math.toRadians(value);
                    grad=1.11111111*value;
                    if (p2==1){
                        if (j==1){
                            val2.setText(format.format(rad));
                            names[0]="Gradiant";
                            values[0]=format.format(grad);
                        }
                        else if (j==2){
                            val1.setText(format.format(rad));
                            names[0]="Gradiant";
                            values[0]=format.format(grad);
                        }
                    }
                    else if (p2==2){
                        if (j==1){
                            val2.setText(format.format(grad));
                            names[0]="Radiant";
                            values[0]=format.format(rad);
                        }
                        else {
                            val1.setText(format.format(grad));
                            names[0]="Radiant";
                            values[0]=format.format(rad);
                        }
                    }
                    else if (p2==0){
                        if (j==1){
                            val2.setText(val1.getText());
                        }
                        else
                            val1.setText(val2.getText());

                        names[0]="Radiant";
                        values[0]=format.format(rad);
                        names[1]="Gradiant";
                        values[1]=format.format(grad);
                    }
                    break;
                case 1:
                    deg= Math.toDegrees(value);
                    grad=1.11111111*deg;
                    if (p2==0){
                        if (j==1){
                            val2.setText(format.format(deg));
                            names[0]="Gradiant";
                            values[0]=format.format(grad);
                        }
                        else if (j==2){
                            val1.setText(format.format(deg));
                            names[0]="Gradiant";
                            values[0]=format.format(grad);
                        }
                    }
                    else if (p2==2){
                        if (j==1){
                            val2.setText(format.format(grad));
                            names[0]="Degree";
                            values[0]=format.format(deg);
                        }
                        else {
                            val1.setText(format.format(grad));
                            names[0]="Degree";
                            values[0]=format.format(deg);
                        }
                    }
                    else if (p2==1){
                        if (j==1){
                            val2.setText(val1.getText());
                        }
                        else
                            val1.setText(val2.getText());

                        names[0]="Degree";
                        values[0]=format.format(deg);
                        names[1]="Gradiant";
                        values[1]=format.format(grad);
                    }
                    break;
                case 2:
                    deg=0.9*value;
                    rad=Math.toRadians(deg);
                    if (p2==0){
                        if (j==1){
                            val2.setText(format.format(deg));
                            names[0]="Radiant";
                            values[0]=format.format(rad);
                        }
                        else if (j==2){
                            val1.setText(format.format(deg));
                            names[0]="Radiant";
                            values[0]=format.format(rad);
                        }
                    }
                    else if (p2==1){
                        if (j==1){
                            val2.setText(format.format(rad));
                            names[0]="Degree";
                            values[0]=format.format(deg);
                        }
                        else {
                            val1.setText(format.format(rad));
                            names[0]="Degree";
                            values[0]=format.format(deg);
                        }
                    }
                    else if (p2==2){
                        if (j==1){
                            val2.setText(val1.getText());
                        }
                        else
                            val1.setText(val2.getText());

                        names[0]="Degree";
                        values[0]=format.format(deg);
                        names[1]="Radiant";
                        values[1]=format.format(rad);
                    }
                    break;
            }
            ListViewAdapter lva=new ListViewAdapter(getActivity(),names,values);
            if(lv!=null){
                eqabt.setVisibility(View.VISIBLE);
                lv.setVisibility(View.VISIBLE);
                lv.setAdapter(lva);
            }
        }catch (Exception e){

        }

    }

    //Following two methods are to convert Data
    private void convertData(int i){
        Double value;
        int position1;
        int position2;
        BigDecimal bg1;
        BigDecimal bg2;
        MathContext c=new MathContext(100);
        if (i==1){
            if (!val1.getText().toString().isEmpty()){
                try{
                    value=Double.parseDouble(val1.getText().toString());
                    position1=list1.getSelectedItemPosition();
                    position2=list2.getSelectedItemPosition();
                    bg1=new BigDecimal(value.toString());
                    bg2=new BigDecimal(sValues.get(position1).get(position2).toString());
                    bg2=bg1.multiply(bg2,c);
                    DecimalFormat format=new DecimalFormat("###############.###############");
                    val2.setText(format.format(bg2));
                    fillOtherData(position1,position2,value);
                }
                catch (Exception ex){

                }
            }
            else {
                val2.setText("");
            }

        }
        else if (i==2){
            if (!val2.getText().toString().isEmpty()){
                try{
                    value=Double.parseDouble(val2.getText().toString());
                    position1=list2.getSelectedItemPosition();
                    position2=list1.getSelectedItemPosition();
                    bg1=new BigDecimal(value.toString());
                    bg2=new BigDecimal(sValues.get(position1).get(position2).toString());
                    bg2=bg1.multiply(bg2,c);
                    DecimalFormat format=new DecimalFormat("###############.###############");
                    val1.setText(format.format(bg2));
                    fillOtherData(position1,position2,value);
                }
                catch (Exception ex){
                }
            }
            else {
                val1.setText("");
            }

        }
    }
    private void fillOtherData(int p1,int p2,Double value){
        try{
            int i=0;
            int j=0;
            int size;
            if (p1==p2)
                size=listSize-1;
            else
                size=listSize-2;
            String[] names=new String[size];
            String[] otherValues=new String[size];
            MathContext c=new MathContext(100);
            while(i<size){
                if (p1!=j && p2!=j)
                {
                    BigDecimal bg1=new BigDecimal(value.toString());
                    BigDecimal bg2=new BigDecimal(sValues.get(p1).get(j).toString());
                    bg2=bg1.multiply(bg2,c);
                    DecimalFormat format=new DecimalFormat("###############.###############");
                    names[i]=list.get(j);
                    otherValues[i]=format.format(bg2);
                    i++;
                }
                j++;

            }
            ListViewAdapter lva=new ListViewAdapter(getActivity(),names,otherValues);
            if(lv!=null){

                eqabt.setVisibility(View.VISIBLE);
                lv.setVisibility(View.VISIBLE);
                lv.setAdapter(lva);
            }

        }
        catch (Exception e){
        }

    }

    //Following two methods are to convert Temperature
    private void convertTemp(int i) {
        Double value;
        int position1;
        int position2;
        if (i == 1) {
            if (!val1.getText().toString().isEmpty()) {
                try {
                    value = Double.parseDouble(val1.getText().toString());
                    position1 = list1.getSelectedItemPosition();
                    position2 = list2.getSelectedItemPosition();
                    Tconvert(position1,position2,value,i);
                } catch (Exception ex) {

                }
            }
            else {
                val2.setText("");
            }

        } else if (i == 2) {
            if (!val2.getText().toString().isEmpty()) {
                try {
                    value = Double.parseDouble(val2.getText().toString());
                    position1 = list2.getSelectedItemPosition();
                    position2 = list1.getSelectedItemPosition();
                    Tconvert(position1,position2,value,i);
                } catch (Exception ex) {

                }
            } else {
                val1.setText("");
            }
        }
    }
    private void Tconvert(int p1,int p2,Double value,int j){
        Double Cel;
        Double Far;
        Double Kel;
        int size;
        if (p1==p2)
            size=2;
        else
            size=1;
        String[] names=new String[size];
        String[] values=new String[size];
        DecimalFormat format = new DecimalFormat("###############.###############");
        switch (p1)
        {
            case 0:
                Far= (value*1.8)+32;
                Kel=value+273.15;
                if (p2==1){
                    if (j==1){
                        val2.setText(format.format(Far));
                        names[0]="Kelvin";
                        values[0]=format.format(Kel);
                    }
                    else if (j==2){
                        val1.setText(format.format(Far));
                        names[0]="Kelvin";
                        values[0]=format.format(Kel);
                    }
                }
                else if (p2==2){
                    if (j==1){
                        val2.setText(format.format(Kel));
                        names[0]="Fahrenheit";
                        values[0]=format.format(Far);
                    }
                    else {
                        val1.setText(format.format(Kel));
                        names[0]="Fahrenheit";
                        values[0]=format.format(Far);
                    }
                }
                else if (p2==0){
                    if (j==1){
                        val2.setText(val1.getText());
                    }
                    else
                        val1.setText(val2.getText());

                    names[0]="Fahrenheit";
                    values[0]=format.format(Far);
                    names[1]="Kelvin";
                    values[1]=format.format(Kel);
                }
                break;
            case 1:
                Cel= (value-32)/1.8;
                Kel=(value + 459.67)*1.8;
                if (p2==0){
                    if (j==1){
                        val2.setText(format.format(Cel));
                        names[0]="Kelvin";
                        values[0]=format.format(Kel);
                    }
                    else if (j==2){
                        val1.setText(format.format(Cel));
                        names[0]="Kelvin";
                        values[0]=format.format(Kel);
                    }
                }
                else if (p2==2){
                    if (j==1){
                        val2.setText(format.format(Kel));
                        names[0]="Celsius";
                        values[0]=format.format(Cel);
                    }
                    else {
                        val1.setText(format.format(Kel));
                        names[0]="Celsius";
                        values[0]=format.format(Cel);
                    }
                }
                else if (p2==1){
                    if (j==1){
                        val2.setText(val1.getText());
                    }
                    else
                        val1.setText(val2.getText());

                    names[0]="Celsius";
                    values[0]=format.format(Cel);
                    names[1]="Kelvin";
                    values[1]=format.format(Kel);
                }
                break;
            case 2:
                Cel=value  - 273.15;
                Far=(value / 1.8) - 459.67;
                if (p2==0){
                    if (j==1){
                        val2.setText(format.format(Cel));
                        names[0]="Fahrenheit";
                        values[0]=format.format(Far);
                    }
                    else if (j==2){
                        val1.setText(format.format(Cel));
                        names[0]="Fahrenheit";
                        values[0]=format.format(Far);
                    }
                }
                else if (p2==1){
                    if (j==1){
                        val2.setText(format.format(Far));
                        names[0]="Celsius";
                        values[0]=format.format(Cel);
                    }
                    else {
                        val1.setText(format.format(Far));
                        names[0]="Celsius";
                        values[0]=format.format(Cel);
                    }
                }
                else if (p2==2){
                    if (j==1){
                        val2.setText(val1.getText());
                    }
                    else
                        val1.setText(val2.getText());
                    names[0]="Celsius";
                    values[0]=format.format(Cel);
                    names[1]="Fahrenheit";
                    values[1]=format.format(Far);
                }
                break;
        }
        ListViewAdapter lva=new ListViewAdapter(getActivity(),names,values);
        if(lv!=null){
            eqabt.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            lv.setAdapter(lva);
        }
    }

    //Connection
    private boolean checkConnection(){
        try {
            conMgr = (ConnectivityManager) getActivity().getSystemService (Context.CONNECTIVITY_SERVICE);
            return conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected();
        }
        catch (Exception e){

        }
        return false;
    }

    //i=1 insert, i=2 update
    private boolean insertUpdateCurrency(final int i){
        final String getUrl ="http://currencies.technostack.in/get_currencies.php";
        final String updateUrl="http://currencies.technostack.in/update_currency.php";
        final String TAG_SUCCESS = "success";
        final String TAG_RATES="rates";
        final MyDialogs dd=new MyDialogs();
        FragmentManager fm = getFragmentManager();
        if (checkConnection()){
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url;
            if (i==1)
                url =getUrl;
            else
                url=updateUrl;

            if (i==1)
                dd.message="insert";
            else
                dd.message="update";
            dd.sTitle="hide";
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                dd.dismiss();
                                int res=response.getInt(TAG_SUCCESS);
                                if (res==1){
                                    JSONArray rates = response.getJSONArray(TAG_RATES);
                                    if (i==1) {

                                        inUpRes=insertCurrency(rates);
                                        if (inUpRes){
                                            showDialog(getResources().getString(R.string.success),getResources().getString(R.string.currencyFetchSuccess));
                                            setTime();
                                        }
                                        else {
                                            showDialog(getResources().getString(R.string.fail),getResources().getString(R.string.currencyFetchFail));
                                        }
                                    }
                                    else if(i==2) {
                                        inUpRes = updateCurrency(rates);
                                        if (inUpRes){
                                            showDialog(getResources().getString(R.string.success),getResources().getString(R.string.currencyUpdateSuccess));
                                            setTime();
                                        }

                                        else {
                                            showDialog(getResources().getString(R.string.fail),getResources().getString(R.string.currencyUpdateFail));
                                        }


                                    }
                                }
                            }
                            catch (Exception e){
                                showDialog(getResources().getString(R.string.fail),getResources().getString(R.string.someError));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    dd.dismiss();
                    showDialog(getResources().getString(R.string.fail),getResources().getString(R.string.someError)+err.toString());
                }
            });
            dd.show(fm,"dd");
            int timout=10000;
            RetryPolicy policy=new DefaultRetryPolicy(timout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

            return inUpRes;
        }
        else {
            showDialog(getResources().getString(R.string.alert),getResources().getString(R.string.getLatestCurrency));
        }
        return inUpRes;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(rec);
        updateConvertPref(activeConvert,list1.getSelectedItemPosition(),list2.getSelectedItemPosition());
        if(mAdView!=null){
         mAdView.destroy();
        }
        super.onDestroy();
    }

    private boolean insertCurrency(JSONArray rates){
        final String TAG_CURR = "currency";
        final String TAG_RATE = "rate";
        final String TAG_TIME = "timestamp";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < rates.length(); i++) {
            try {
                JSONObject c = rates.getJSONObject(i);
                String curr = c.getString(TAG_CURR);
                String price = c.getString(TAG_RATE);
                String timeStamp = c.getString(TAG_TIME);
                ContentValues values = new ContentValues();
                values.put(CurrencyRates.CurrencyEntry.COLUMN_NAME_VALUE, price);
                values.put(CurrencyRates.CurrencyEntry.COLUMN_NAME_TIME, timeStamp);
                values.put(CurrencyRates.CurrencyEntry.COLUMN_NAME_TITLE, curr);
                db.insert(CurrencyRates.CurrencyEntry.TABLE_NAME, null, values);
            }
            catch (Exception e){
                db.endTransaction();
                return false;
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return true;

    }

    private boolean updateCurrency(JSONArray rates){
        final String TAG_CURR = "currency";
        final String TAG_RATE = "rate";
        final String TAG_TIME = "timestamp";
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < rates.length(); i++) {
            try {
                JSONObject c = rates.getJSONObject(i);
                String curr = c.getString(TAG_CURR);
                String price = c.getString(TAG_RATE);
                String timeStamp = c.getString(TAG_TIME);
                ContentValues values = new ContentValues();
                values.put(CurrencyRates.CurrencyEntry.COLUMN_NAME_VALUE, price);
                values.put(CurrencyRates.CurrencyEntry.COLUMN_NAME_TIME, timeStamp);
                String selection = CurrencyRates.CurrencyEntry.COLUMN_NAME_TITLE + " LIKE ?";
                String[] selectionArgs = { curr };
                db.update(
                        CurrencyRates.CurrencyEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            }
            catch (Exception e){
                db.endTransaction();
                return false;
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return true;
    }

    private void currencyUpdateClick(){
        if (isCurrAvailable()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getDefault());
            String oldTime = getTimeStamp();
            Calendar oldDate=Calendar.getInstance();
            oldDate.setTimeInMillis(Long.parseLong(oldTime)*1000);
            Calendar newDate=Calendar.getInstance();
            newDate.setTimeInMillis(newDate.getTimeInMillis());
            Date start =oldDate.getTime();
            Date end =newDate.getTime();
            Double diff=getDifference(start,end);
            if (diff<=1 && diff>=0){
                showDialog(getResources().getString(R.string.note),getResources().getString(R.string.alreadyLatest));
            }
            else if(diff>1) {
                insertUpdateCurrency(2);
            }
            else if (diff<0){
                showDialog(getResources().getString(R.string.note),getResources().getString(R.string.devicePastDate));
            }
        }
        else {
            insertUpdateCurrency(1);
        }
    }

    private void calculateCurrency(AutoCompleteTextView v1,String l1,AutoCompleteTextView v2,String l2){
        try {
            if (!v1.getText().toString().isEmpty()) {
                DecimalFormat format = new DecimalFormat("#######.####");
                String value1 = v1.getText().toString();
                String one = "1";
                BigDecimal oneB = new BigDecimal(one);
                BigDecimal b1 = new BigDecimal(value1);
                BigDecimal c1 = new BigDecimal(getValues(l1));
                BigDecimal c2 = new BigDecimal(getValues(l2));
                c1 = oneB.divide(c1, 100, RoundingMode.HALF_EVEN);
                c2 = c2.multiply(c1);
                c2 = c2.multiply(b1);
                BigDecimal f = setPrecision(c2);
                if (f != null)
                    v2.setText(format.format(f));
                else
                    v2.setText(format.format(c2));
                if (currOther != null && currOther.size() > 0)
                    fillOtherCurrency(v1, l1, v2);
            }
        }
        catch (Exception e){

        }

    }

    private void fillOtherCurrency(AutoCompleteTextView v1,String l1,AutoCompleteTextView v2){
            int size=currOther.size();
        String[] names = new String[size];
        String[] otherValues = new String[size];
        int j=0;
        if (!v1.getText().toString().isEmpty()) {
            for (int i = 0; i < size; i++) {
                CurrencyModel curr = (CurrencyModel) list2.getItemAtPosition(currOther.get(i));
                String l2 = curr.getCurrName();
                DecimalFormat format = new DecimalFormat("#######.####");
                String value1 = v1.getText().toString();
                String one = "1";
                BigDecimal oneB = new BigDecimal(one);
                BigDecimal b1 = new BigDecimal(value1);
                BigDecimal c1 = new BigDecimal(getValues(l1));
                BigDecimal c2 = new BigDecimal(getValues(l2.substring(0, 3)));
                c1 = oneB.divide(c1, 100, RoundingMode.HALF_EVEN);
                c2 = c2.multiply(c1);
                c2 = c2.multiply(b1);
                BigDecimal f = setPrecision(c2);
                names[j] = l2;
                if (f != null)
                    otherValues[j] = format.format(f);
                else
                    otherValues[j] = format.format(c2);
                j++;
            }
            ListViewAdapter lva = new ListViewAdapter(getActivity(), names, otherValues);
            if (lv != null) {

                eqabt.setVisibility(View.VISIBLE);
                lv.setVisibility(View.VISIBLE);
                lv.setAdapter(lva);

            }
        }
    }

    private String getTimeStamp(){
        String timestamp="";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                CurrencyRates.CurrencyEntry.COLUMN_NAME_TIME
        };
        String selection = CurrencyRates.CurrencyEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "AED" };
        Cursor cursor = db.query(
                CurrencyRates.CurrencyEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        while (cursor.moveToNext()){
            timestamp = cursor.getString(
                    cursor.getColumnIndexOrThrow(CurrencyRates.CurrencyEntry.COLUMN_NAME_TIME));
        }
        cursor.close();
        return timestamp;
    }

    private boolean isCurrAvailable(){
        try{
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            long cnt  = DatabaseUtils.queryNumEntries(db, CurrencyRates.CurrencyEntry.TABLE_NAME);

            if (cnt>0 && cnt==168) {
                test=cnt+"";
                db.close();
                return true;
            }
            else {
                db.execSQL("delete from "+ CurrencyRates.CurrencyEntry.TABLE_NAME);
                db.close();
                return false;
            }

        }
        catch (Exception e){
            return false;
        }

    }

    private boolean isPrefAvailable(){
        try{
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            long cnt  = DatabaseUtils.queryNumEntries(db, CurrencyRates.UserPreferences.TABLE_NAME);
            if (cnt>0) {
                db.close();
                return true;
            }
            else {
                db.execSQL("delete from "+ CurrencyRates.UserPreferences.TABLE_NAME);
                db.close();
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    private boolean insertConvertPref(){
        SQLiteDatabase db=null;
        try {
            db=mDbHelper.getWritableDatabase();
            Values obj = new Values();
            ArrayList<String> convertArray=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.ConvertArray)));
            db.beginTransaction();
            for (int i = 0; i < convertArray.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(CurrencyRates.UserPreferences.COLUMN_NAME_CONVERT, convertArray.get(i));
                values.put(CurrencyRates.UserPreferences.COLUMN_NAME_VALUE1, 0);
                values.put(CurrencyRates.UserPreferences.COLUMN_NAME_VALUE2, 1);
                long id=db.insert(CurrencyRates.UserPreferences.TABLE_NAME, null, values);

            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
            }
            catch (Exception e){
                return false;
            }


    }

    private boolean updateConvertPref(String conName,int val1,int val2){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(CurrencyRates.UserPreferences.COLUMN_NAME_VALUE1, val1);
            values.put(CurrencyRates.UserPreferences.COLUMN_NAME_VALUE2, val2);
            String selection = CurrencyRates.UserPreferences.COLUMN_NAME_CONVERT + " LIKE ?";
            String[] selectionArgs = {conName};
            int i=db.update(
                    CurrencyRates.UserPreferences.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            if (i>0){
                return true;

            }
            else {
                values.put(CurrencyRates.UserPreferences.COLUMN_NAME_CONVERT, activeConvert);
                long a=db.insert(CurrencyRates.UserPreferences.TABLE_NAME, null, values);
                if (a>0)
                    return true;
                else
                    return false;
            }

        }
        catch (Exception e){
            return false;
        }
    }

    private BigDecimal setPrecision(BigDecimal bigDecimal) {
        DecimalFormat format=new DecimalFormat("###############.###############");
        String bigDecimalString = format.format(bigDecimal);
        int lastIndex=bigDecimalString.lastIndexOf(".");
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int precision = Integer.parseInt(sharedPreferences.getString("precisionPref", "3"));
        Log.d("Precision", ""+precision);
        if (lastIndex==-1)
            return null;
        else {
            String stringAfterDot = bigDecimalString.substring(bigDecimalString.lastIndexOf(".") + 1);
            if (stringAfterDot.length()>=precision){
                String zeros="";
                int index=0;
                for(int i=0;i<stringAfterDot.length();i++){
                    if('0'==stringAfterDot.charAt(i)) {
                        zeros=zeros+"0";
                        index=i;
                    }
                    else{
                        break;
                    }
                }
                String numberString=stringAfterDot.substring(index);
                Double tempDiv = 1.0;
                if (numberString.length()<=precision)
                    return null;
                for (int i = 0; i <= numberString.length(); i++) {
                    if (i >= 0 && i<=precision)
                        continue;
                    tempDiv = tempDiv * 10;
                }
                BigDecimal div = new BigDecimal(tempDiv.toString());
                BigDecimal last = new BigDecimal(numberString);
                last = last.divide(div);
                Long precisionValue = Math.round(last.doubleValue());
                BigDecimal ff=null;
                if (precisionValue==10000 && zeros.length()==0){
                        ff = new BigDecimal("1");
                }
                else {
                    ff = new BigDecimal("0."+zeros+precisionValue.toString());
                }
                BigDecimal f2 = new BigDecimal(bigDecimal.toBigInteger());
                f2 = f2.add(ff);
                return f2;
            }
            else
                return null;
        }


    }

    private String getValues(String curr){
        String currValue="1";
        try{
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                CurrencyRates.CurrencyEntry.COLUMN_NAME_VALUE
        };
        String selection = CurrencyRates.CurrencyEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { curr };
        Cursor cursor = db.query(
                CurrencyRates.CurrencyEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        while (cursor.moveToNext()){
            currValue = cursor.getString(
                    cursor.getColumnIndexOrThrow(CurrencyRates.CurrencyEntry.COLUMN_NAME_VALUE));
        }
        cursor.close();
        return currValue;
        }
        catch (Exception e){
            return currValue;
        }
    }

    public Double getDifference(Date startDate, Date endDate){
        long different = endDate.getTime() - startDate.getTime();
        Double secondsInMilli = 1000.0;
        Double minutesInMilli = secondsInMilli * 60;
        Double hoursInMilli = minutesInMilli * 60;
        Double daysInMilli = hoursInMilli * 24;
        Double elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;
        Double elapsedHours = different / hoursInMilli;
        //different = different % hoursInMilli;
        Double elapsedMinutes = different / minutesInMilli;
        //different = different % minutesInMilli;
        Double elapsedSeconds = different / secondsInMilli;
        return elapsedHours;
    }

    public void showDialog(String title,String message) {
        FragmentManager fm = getFragmentManager();
        MyDialogs dd=new MyDialogs();
        dd.sTitle=title;
        dd.message=message;
        dd.show(fm,"");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==2018){
            String res = data.getStringExtra("Result");
            if (res.equals("success")) {
                showDialog(getResources().getString(R.string.success),getResources().getString(R.string.preferenceSaved));
                setSelectionPref();
                AutoCompleteTextView v1;
                String l1;
                if (val1.hasFocus()){
                   v1 =val1;
                   l1=list1.getSelectedItem().toString().substring(0,3);
                }
                else {
                    v1=val2;
                    l1=list1.getSelectedItem().toString().substring(0,3);
                }
                fillOtherCurrency(v1,l1,v1);
            }
            else if (res.equals("fail"))
                Toast.makeText(getActivity(),getResources().getString(R.string.someError),Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onPause() {
        updateConvertPref(activeConvert,list1.getSelectedItemPosition(),list2.getSelectedItemPosition());
        super.onPause();
    }

    private long Count(String TableName) {
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            long cnt = DatabaseUtils.queryNumEntries(db, TableName);
            db.close();
            return cnt;
        }
        catch (Exception e){
            return 0;
        }
    }

    private Cursor getPref(String TableName, String ColumnName){
        try{
            if (Count(TableName)>0) {
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select " + ColumnName + " FROM " + TableName, null);
                return cursor;
            }
        }
        catch (Exception e){
            return null;
        }
        return null;
    }

    private void setConvertPref(String con) {
        int v1=0;
        int v2=0;
        try{
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String[] projection = {
                    CurrencyRates.UserPreferences.COLUMN_NAME_VALUE1,CurrencyRates.UserPreferences.COLUMN_NAME_VALUE2
            };
            String selection = CurrencyRates.UserPreferences.COLUMN_NAME_CONVERT + " = ?";
            String[] selectionArgs = { con };
            Cursor cursor = db.query(
                    CurrencyRates.UserPreferences.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()){
                v1 = cursor.getInt(
                        cursor.getColumnIndexOrThrow(CurrencyRates.UserPreferences.COLUMN_NAME_VALUE1));
                v2 = cursor.getInt(
                        cursor.getColumnIndexOrThrow(CurrencyRates.UserPreferences.COLUMN_NAME_VALUE2));
            }
            selection1=v1;
            selection2=v2;
            cursor.close();
        }
        catch (Exception e){

        }
    }

    private void setSelectionPref(){
            Cursor cursor1=getPref(CurrencyRates.UserCurrPrefrences.TABLE_NAME,CurrencyRates.UserCurrPrefrences.COLUMN_OTHER_CURRENCY);
            if (cursor1!=null && cursor1.moveToFirst()){
                try {
                    String currValue1;
                    currOther.clear();
                    do {
                        currValue1 = cursor1.getString(
                                cursor1.getColumnIndexOrThrow(CurrencyRates.UserCurrPrefrences.COLUMN_OTHER_CURRENCY));
                        currOther.add(Integer.parseInt(currValue1));
                    }while (cursor1.moveToNext());
                    cursor1.close();
                }
                catch (Exception e){
                }
            }
            else {
                SharedPreferences preferences=getActivity().getSharedPreferences("SetSelMyPrefcCurr",Context.MODE_PRIVATE);
                if (preferences.getBoolean("isFirst",true)) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isFirst", false);
                    editor.commit();
                    showPrefDialog(getResources().getString(R.string.currencyPref), 10, 2018);
                }
            }
    }

    private void showPrefDialog(String DTitle,int num,int CODE){
        SelectCurrencyDialog obj=new SelectCurrencyDialog();
        FragmentManager fm=getFragmentManager();
        obj.NUMBER=num;
        obj.Title=DTitle;
        obj.setTargetFragment(this,CODE);
        obj.show(fm,"Show");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        try
        {
            if (id==R.id.curr) {
                inflater.inflate(R.menu.currency_menu, menu);
                super.onCreateOptionsMenu(menu, inflater);
            }
        }
        catch (Exception e){
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.currSet){
            showPrefDialog(getResources().getString(R.string.currencyFreq),10,2018);
        }
        else if (item.getItemId()==R.id.updateCurrency){
            currencyUpdateClick();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setScreen(String screen){
        ((MainActivity) getActivity()).setScreenName(screen);
    }
}






