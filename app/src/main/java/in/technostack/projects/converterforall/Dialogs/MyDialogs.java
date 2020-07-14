package in.technostack.projects.converterforall.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import in.technostack.projects.converterforall.R;

public class MyDialogs extends DialogFragment {
    public String sTitle="";
    public String message="";
    private String positive="Ok";
    private String negative="";
    public String WHO="Ok";
    public boolean swap=false;
    public boolean rating=false;
    String URL1="market://details?id=in.technostack.projects.converter";
    String URL2="https://play.google.com/store/apps/details?id=in.technostack.projects.converter";
    String URL3="market://details?id=in.technostack.projects.converterforall";
    String URL4="https://play.google.com/store/apps/details?id=in.technostack.projects.converterforall";
    View view;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Nullable
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        LayoutInflater inflater= getActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.dialogs,null);
        TextView title= view.findViewById(R.id.title);
        TextView content= view.findViewById(R.id.content);
        TextView txtView= view.findViewById(R.id.loadText);
        ProgressBar bar= view.findViewById(R.id.pBar);
        final CheckBox dontShow=view.findViewById(R.id.dontShow);
        positive=getResources().getString(R.string.ok);
        preferences=getActivity().getSharedPreferences("reviewAppHome",Context.MODE_PRIVATE);
        editor=preferences.edit();
        if (sTitle.equals("hide")) {
            if (message.equalsIgnoreCase("insert")){
                txtView.setText(getResources().getString(R.string.fetchRates));
            }
            else
                txtView.setText(getResources().getString(R.string.updaterates));
            title.setVisibility(View.GONE);
            content.setVisibility(View.GONE);
            setCancelable(false);
            txtView.setVisibility(View.VISIBLE);
            bar.setVisibility(View.VISIBLE);
        }
        else {
            title.setVisibility(View.VISIBLE);
            if (sTitle.contains("Pro Version")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.star_50, 0);
                }
                positive=getResources().getString(R.string.buynow);
                negative=getResources().getString(R.string.later);
                if (WHO.equals("auto"))
                dontShow.setVisibility(View.VISIBLE);
                builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dontShow.isChecked()) {
                            final SharedPreferences preferences = getActivity().getSharedPreferences("isFirstPro", Context.MODE_PRIVATE);
                            if (preferences.getBoolean("isFirstPro", true)) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isFirstPro", false);
                                editor.apply();
                            }
                        }
                    }
                });
            }
            else if (sTitle.contains(getResources().getString(R.string.noplaystore))) {
                positive=getResources().getString(R.string.download);
                negative=getResources().getString(R.string.close);
            }
            else if (swap){
                builder.setNegativeButton(getResources().getString(R.string.swapdates), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(1,"swap");
                    }
                });
            }
            else if (rating){
                    positive=getResources().getString(R.string.ratenow);
                if (!preferences.getBoolean("rateNowClicked",false)) {
                    builder.setNegativeButton(getResources().getString(R.string.later), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
                else {
                    builder.setNegativeButton(getResources().getString(R.string.alreadyReview), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editor.putBoolean("alreadyClicked",true);
                            editor.apply();
                        }
                    });
                }
            }
            content.setVisibility(View.VISIBLE);
            txtView.setVisibility(View.GONE);
            bar.setVisibility(View.GONE);
            title.setText(sTitle);
            content.setText(message);
            builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (dontShow.isChecked()){
                        final SharedPreferences preferences = getActivity().getSharedPreferences("isFirstPro", Context.MODE_PRIVATE);
                        if (preferences.getBoolean("isFirstPro", true)) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isFirstPro", false);
                            editor.commit();
                        }
                    }
                        if (positive.equals("Buy Now")){
                            try
                            {
                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL1)));
                            }
                            catch (Exception e)
                            {
                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL2)));
                            }
                        }
                        else if(positive.equals("Rate Now")){
                            try
                            {
                                editor.putBoolean("rateNowClicked",true);
                                editor.commit();
                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL3)));
                            }
                            catch (Exception e)
                            {
                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL4)));
                            }
                        }
                        else if(positive.equals(getResources().getString(R.string.download))){
                            try {
                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL3)));
                            }
                            catch (Exception e){
                                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL4)));
                            }
                        }
                }
            });
        }
        builder.setView(view);
        return builder.create();
    }
    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_bg));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textColor));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.textColor));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.textColor));
    }
    private void sendResult(int REQUEST_CODE, String RESULT) {
        Intent intent = new Intent();
        intent.putExtra("Result",RESULT);
        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);
    }
}
