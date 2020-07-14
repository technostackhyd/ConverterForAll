package in.technostack.projects.converterforall.FragmentActivities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;*/
import com.facebook.ads.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import in.technostack.projects.converterforall.Dialogs.MyDialogs;
import in.technostack.projects.converterforall.MainActivity;
import in.technostack.projects.converterforall.R;
import in.technostack.projects.converterforall.SearchableSpinner;

public class BmiCalculate extends Fragment {
    private View bmiView;
    ImageView help;
    AutoCompleteTextView val1;
    AutoCompleteTextView val2;
    SearchableSpinner list1;
    SearchableSpinner list2;
    ArrayList<String> arrayList1;
    ArrayList<String> arrayList2;
    TextView result, link;
    private DecimalFormat decimalFormat;
    private AdView mAdView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        bmiView= inflater.inflate(R.layout.bmi, container,false);
        ((MainActivity) getActivity()).changeTitle(getResources().getString(R.string.bmi));
        ((MainActivity) getActivity()).setScreenName(getResources().getString(R.string.bmi));
        help=bmiView.findViewById(R.id.help);
        val1= bmiView.findViewById(R.id.value1);
        val2=bmiView.findViewById(R.id.value2);
        list1= bmiView.findViewById(R.id.list1);
        list2= bmiView.findViewById(R.id.list2);
        result=bmiView.findViewById(R.id.result);
        link=bmiView.findViewById(R.id.link);
        /*mAdView = new com.facebook.ads.AdView(getActivity(), "225154511577364_435911947168285", AdSize.RECTANGLE_HEIGHT_250);
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
        LinearLayout adContainer = (LinearLayout) bmiView.findViewById(R.id.technoTwo);
        // Add the ad view to your activity layout
        adContainer.addView(mAdView);
        // Request an ad
        mAdView.loadAd(loadAdConfig);*/
        arrayList1=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.bmiWeightArray)));
        arrayList2=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.bmiHeightArray)));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list1.setAdapter(adapter1);
        list2.setAdapter(adapter2);
        decimalFormat = new DecimalFormat("#.#");
        val1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    calculateBmi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        val2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    calculateBmi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        list1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    calculateBmi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        list2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    calculateBmi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             showDialog(getResources().getString(R.string.help),getResources().getString(R.string.bmiranges));
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.who.int"));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(i);
            }
        });
        return bmiView;
    }

    private void calculateBmi() {

        try {
            int pos1=list1.getSelectedItemPosition();
            int pos2=list2.getSelectedItemPosition();
            String weight=val1.getText()+"";
            String height=val2.getText()+"";
            if (weight.length()>0 && height.length()>0)
            {
                double weightVal=Double.parseDouble(weight);
                double heightVal=Double.parseDouble(height);
                double bmi=0.0;
                if (pos1==0 && pos2==0){
                    //kg & cm
                    heightVal=heightVal/100;
                    bmi=(weightVal/(heightVal*heightVal));
                }
                else if (pos1==0 && pos2==1){
                    //kg & inch (inch to cm)
                    heightVal=(heightVal*2.54)/100;
                    bmi=(weightVal/(heightVal*heightVal));

                }
                else if (pos1==1 && pos2==0){
                    //lb to cm (cm to inch)
                    heightVal=heightVal/2.54;
                    bmi=(weightVal/(heightVal*heightVal));
                    bmi=bmi*703;
                }
                else if (pos1==1 && pos2==1){
                    //lb to inch
                    bmi=(weightVal/(heightVal*heightVal));
                    bmi=bmi*703;
                }
                String bmiResult=decimalFormat.format(bmi);
                String bmigroup="";
                bmi=Double.parseDouble(bmiResult);
                if (bmi<18.5)
                    bmigroup="Underweight.";
                else if (bmi>=18.5 && bmi<=24.9)
                    bmigroup="Normal.";
                else if (bmi>=25.0 && bmi<=29.9)
                    bmigroup="Overweight.";
                else if (bmi>=30.0)
                    bmigroup="Obese.";
                result.setText(bmiResult+" - "+bmigroup);
            }
            else {
                result.setText("");
            }
        }
        catch (Exception e){

        }


    }

    public void showDialog(String title,String message) {
        FragmentManager fm = getFragmentManager();
        MyDialogs dd=new MyDialogs();
        dd.sTitle=title;
        dd.message=message;
        dd.show(fm,"");
    }

    @Override
    public void onDestroy() {
        if (mAdView!=null){
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
