package in.technostack.projects.converterforall.FragmentActivities;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdSize;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;*/
import com.facebook.ads.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import in.technostack.projects.converterforall.Dialogs.MyDialogs;
import in.technostack.projects.converterforall.MainActivity;
import in.technostack.projects.converterforall.R;
public class PercentageCalculate extends Fragment {
    View view;
    LinearLayout L11,L12,L21,L22,L31,L32,L41,L42;
    AutoCompleteTextView basicVal,percentVal,d_basicVal,d_percentVal,a_basicVal,a_percentVal,k_basicVal,k_Val;
    TextView result,d_result,a_result,k_result,kd_result;
    int v=1;
    int w=1;
    int x=1;
    int y=1;
    ImageView p,d,a,f;
    final String HELP="Help";
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.percentage_layout,container,false);
        p=view.findViewById(R.id.chelp);
        d=view.findViewById(R.id.dhelp);
        a=view.findViewById(R.id.ahelp);
        f=view.findViewById(R.id.fhelp);

        L11=view.findViewById(R.id.l11);
        L12=view.findViewById(R.id.l12);
        L21=view.findViewById(R.id.l21);
        L22=view.findViewById(R.id.l22);
        L31=view.findViewById(R.id.l31);
        L32=view.findViewById(R.id.l32);
        L41=view.findViewById(R.id.l41);
        L42=view.findViewById(R.id.l42);

        L12.setVisibility(View.GONE);
        L22.setVisibility(View.GONE);
        L32.setVisibility(View.GONE);
        L42.setVisibility(View.GONE);
        /*mAdView = new com.facebook.ads.AdView(getActivity(), "225154511577364_435912303834916", AdSize.RECTANGLE_HEIGHT_250);
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
        mAdView.loadAd(loadAdConfig);*/

        L11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(L12.getVisibility()==View.GONE)
                {
                    L12.setVisibility(View.VISIBLE);    
                }    
                else
                {
                    L12.setVisibility(View.GONE);
                }
                L22.setVisibility(View.GONE);
                L32.setVisibility(View.GONE);
                L42.setVisibility(View.GONE);
            }
        });

        L21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(L22.getVisibility()==View.GONE)
                {
                    L22.setVisibility(View.VISIBLE);
                }
                else
                {
                    L22.setVisibility(View.GONE);
                }
                L12.setVisibility(View.GONE);
                L32.setVisibility(View.GONE);
                L42.setVisibility(View.GONE);
            }
        });

        L31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(L32.getVisibility()==View.GONE)
                {
                    L32.setVisibility(View.VISIBLE);
                }
                else
                {
                    L32.setVisibility(View.GONE);
                }
                L12.setVisibility(View.GONE);
                L22.setVisibility(View.GONE);
                L42.setVisibility(View.GONE);
            }
        });

        L41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(L42.getVisibility()==View.GONE)
                {
                    L42.setVisibility(View.VISIBLE);
                }
                else
                {
                    L42.setVisibility(View.GONE);
                }
                L12.setVisibility(View.GONE);
                L22.setVisibility(View.GONE);
                L32.setVisibility(View.GONE);
            }
        });

        basicVal= view.findViewById(R.id.basicValue);basicVal.setTypeface(null, Typeface.BOLD);
        percentVal= view.findViewById(R.id.percentValue);percentVal.setTypeface(null,Typeface.NORMAL);

        d_basicVal= view.findViewById(R.id.d_basicValue);d_basicVal.setTypeface(null, Typeface.BOLD);
        d_percentVal= view.findViewById(R.id.d_percentValue);d_percentVal.setTypeface(null,Typeface.NORMAL);

        a_basicVal= view.findViewById(R.id.a_basicValue);a_basicVal.setTypeface(null, Typeface.BOLD);
        a_percentVal= view.findViewById(R.id.a_percentValue);a_percentVal.setTypeface(null,Typeface.NORMAL);

        k_basicVal= view.findViewById(R.id.k_basicValue);k_basicVal.setTypeface(null, Typeface.BOLD);
        k_Val= view.findViewById(R.id.k_Value);k_Val.setTypeface(null,Typeface.NORMAL);

        result= view.findViewById(R.id.perResult);
        d_result= view.findViewById(R.id.d_perResult);
        a_result= view.findViewById(R.id.a_perResult);
        k_result= view.findViewById(R.id.k_Result);
        kd_result=view.findViewById(R.id.kd_Result);



        basicVal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    v=1;
                    basicVal.setTypeface(null, Typeface.BOLD);
                    percentVal.setTypeface(null,Typeface.NORMAL);
                }
            }
        });
        percentVal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    v=2;
                    percentVal.setTypeface(null, Typeface.BOLD);
                    basicVal.setTypeface(null,Typeface.NORMAL);
                }
            }
        });
        d_basicVal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    w=1;
                    d_basicVal.setTypeface(null, Typeface.BOLD);
                    d_percentVal.setTypeface(null,Typeface.NORMAL);
                }
            }
        });
        d_percentVal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    w=2;
                    d_percentVal.setTypeface(null, Typeface.BOLD);
                    d_basicVal.setTypeface(null,Typeface.NORMAL);
                }
            }
        });

        a_basicVal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    x=1;
                    a_basicVal.setTypeface(null, Typeface.BOLD);
                    a_percentVal.setTypeface(null,Typeface.NORMAL);
                }
            }
        });
        a_percentVal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    x=2;
                    a_percentVal.setTypeface(null, Typeface.BOLD);
                    a_basicVal.setTypeface(null,Typeface.NORMAL);
                }
            }
        });

        k_basicVal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    y=1;
                    k_basicVal.setTypeface(null, Typeface.BOLD);
                    k_Val.setTypeface(null,Typeface.NORMAL);
                }
            }
        });
        k_Val.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    y=2;
                    k_Val.setTypeface(null, Typeface.BOLD);
                    k_basicVal.setTypeface(null,Typeface.NORMAL);
                }
            }
        });


        basicVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (v==1){
                        if (!basicVal.getText().toString().isEmpty() && !percentVal.getText().toString().isEmpty()){
                            calculatePercent(0);
                        }
                        else {
                            result.setText("0.0");
                        }
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        percentVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (v==2){
                    if (!basicVal.getText().toString().isEmpty() && !percentVal.getText().toString().isEmpty()){
                        calculatePercent(0);
                    }
                    else {
                        result.setText("0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        d_basicVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (w==1){
                    if (!d_basicVal.getText().toString().isEmpty() && !d_percentVal.getText().toString().isEmpty()){
                        calculatePercent(1);
                    }
                    else {
                        d_result.setText("0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        d_percentVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (w==2){
                    if (!d_basicVal.getText().toString().isEmpty() && !d_percentVal.getText().toString().isEmpty()){
                        calculatePercent(1);
                    }
                    else {
                        d_result.setText("0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        a_basicVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (x==1){
                    if (!a_basicVal.getText().toString().isEmpty() && !a_percentVal.getText().toString().isEmpty()){
                        calculatePercent(2);
                    }
                    else {
                        a_result.setText("0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        a_percentVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (x==2){
                    if (!a_basicVal.getText().toString().isEmpty() && !a_percentVal.getText().toString().isEmpty()){
                        calculatePercent(2);
                    }
                    else {
                        a_result.setText("0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        k_basicVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (y==1){
                    if (!k_basicVal.getText().toString().isEmpty() && !k_Val.getText().toString().isEmpty()){
                        calculatePercent(3);
                    }
                    else {
                        k_result.setText("0.0");
                        kd_result.setText("0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        k_Val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (y==2){
                    if (!k_basicVal.getText().toString().isEmpty() && !k_Val.getText().toString().isEmpty()){
                        calculatePercent(3);
                    }
                    else {
                        k_result.setText("0.0");
                        kd_result.setText("0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getResources().getString(R.string.help),getResources().getString(R.string.percent));
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getResources().getString(R.string.help),getResources().getString(R.string.discount));
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getResources().getString(R.string.help),getResources().getString(R.string.add));
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getResources().getString(R.string.help),getResources().getString(R.string.find));
            }
        });
        ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.Percentage));
        ((MainActivity) getActivity()).setScreenName(getResources().getString(R.string.Percentage));
        return view;
    }
    public void calculatePercent(int i){

        MathContext mc=new MathContext(100);
        BigDecimal value=null,percent=null,oValue=null;
        BigDecimal hundred=new BigDecimal("100");
        try {
            if (i==0){
                value=new BigDecimal(basicVal.getText().toString());
                percent=new BigDecimal(percentVal.getText().toString());
            }
            else if (i==1){
                value=new BigDecimal(d_basicVal.getText().toString());
                percent=new BigDecimal(d_percentVal.getText().toString());
                percent=hundred.subtract(percent,mc);
            }
            else if (i==2){
                value=new BigDecimal(a_basicVal.getText().toString());
                percent=new BigDecimal(a_percentVal.getText().toString());
                percent=hundred.add(percent,mc);
            }
            else if (i==3){
                value=new BigDecimal(k_basicVal.getText().toString());
                oValue=new BigDecimal(k_Val.getText().toString());
                percent=oValue.divide(value,mc).multiply(hundred,mc);
            }
            value=value.multiply(percent.divide(hundred,mc),mc);
        DecimalFormat format=new DecimalFormat("#,###,###.##");
        switch (i){
            case 0:
                result.setText(format.format(value));
                break;
            case 1:
                d_result.setText(format.format(value));
                break;
            case 2:
                a_result.setText(format.format(value));
                break;
            case 3:
                k_result.setText(k_Val.getText().toString()+" is "+format.format(percent)+getResources().getString(R.string.percentOf)+k_basicVal.getText().toString());
                kd_result.setText(getResources().getString(R.string.discountIs)+format.format(hundred.subtract(percent,mc))+"%");
                break;
        }
        }
        catch (Exception e){

        }

    }

    public void showDialog(String title,String message)
    {
        FragmentManager fm = getFragmentManager();
        MyDialogs dd=new MyDialogs();
        dd.sTitle=title;
        dd.message=message;
        dd.show(fm,"");
    }

    @Override
    public void onDestroy() {
        if(mAdView!=null){
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
