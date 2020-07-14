package in.technostack.projects.converterforall;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.facebook.ads.*;
import in.technostack.projects.converterforall.Classes.SharedPreferencesHelper;
import in.technostack.projects.converterforall.Classes.ThemeHelper;
import in.technostack.projects.converterforall.Dialogs.MyDialogs;
import in.technostack.projects.converterforall.FragmentActivities.BmiCalculate;
import in.technostack.projects.converterforall.FragmentActivities.Calculator;
import in.technostack.projects.converterforall.FragmentActivities.ConvertValues;
import in.technostack.projects.converterforall.FragmentActivities.Home;
import in.technostack.projects.converterforall.FragmentActivities.PercentageCalculate;
import in.technostack.projects.converterforall.FragmentActivities.dateCalculate;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    Fragment fr;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bundle bundle;
    LinearLayout cl;
    int id;
    int count=0;
    private AdView mAdView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeTitle("Converter");
        cl=findViewById(R.id.fragments);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        float scale = getResources().getDisplayMetrics().density;
        if (width>1440 && width<2048){
            cl.setPadding((int) (100*scale + 0.5f),0,(int) (100*scale + 0.5f),0);
        }
        else if(width>2048 && width<2560){
            cl.setPadding((int) (150*scale + 0.5f),0,(int) (150*scale + 0.5f),0);
        }
        else if (width>=2560){
            cl.setPadding((int) (200*scale + 0.5f),0,(int) (200*scale + 0.5f),0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        /*mAdView = new com.facebook.ads.AdView(this, "225154511577364_225155771577238", AdSize.BANNER_HEIGHT_50);
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
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.technoOne);
        // Add the ad view to your activity layout
        adContainer.addView(mAdView);
        // Request an ad
        AdSettings.addTestDevice("fbb3f244-7ea9-4122-ab5c-d2f68f3e5d1b");
        mAdView.loadAd(loadAdConfig);*/
        int count= SharedPreferencesHelper.getInt("reviewAppHome", 0, "reviewAppHome", Context.MODE_PRIVATE, this);
        count++;
        int favorites=SharedPreferencesHelper.getInt("favoritesCnt",0, "reviewAppHome", Context.MODE_PRIVATE, this);
        if (favorites<2){
            favorites++;
            showDialog(getResources().getString(R.string.newfeature),getResources().getString(R.string.featuredetails),"",false);
            SharedPreferencesHelper.putInt("favoritesCnt", favorites, "reviewAppHome", Context.MODE_PRIVATE, this);
        }
        else {
            if (count<5) {
                SharedPreferencesHelper.putInt("reviewAppHome", count, "reviewAppHome", Context.MODE_PRIVATE, this);
                if (SharedPreferencesHelper.getBoolean("isFirstPro", true, "isFirstPro", Context.MODE_PRIVATE, this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showDialog(getResources().getString(R.string.getProTitle), getResources().getString(R.string.pro), "auto",false);
                        }
                    }, 1000);
                }
            }
            else
            {
                if (!SharedPreferencesHelper.getBoolean("shown",false, "isFirstPro", Context.MODE_PRIVATE, this) || !SharedPreferencesHelper.getBoolean("alreadyCLicked",false, "isFirstPro", Context.MODE_PRIVATE, this))
                {
                    SharedPreferencesHelper.putInt("reviewAppHome", 0, "reviewAppHome", Context.MODE_PRIVATE, this);
                    SharedPreferencesHelper.putBoolean("shown", true, "isFirstPro", Context.MODE_PRIVATE, this);
                    if (!SharedPreferencesHelper.getBoolean("alreadyClicked",false, "isFirstPro", Context.MODE_PRIVATE, this))
                        showDialog(getResources().getString(R.string.rateUsTitle), getString(R.string.rating), "", true);
                }

            }
        }
        if(fr!=null){
            fragmentTransaction.replace(R.id.fragments,fr).commit();
        }
        else {
            fragmentManager=getFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fr=new Home();
            fragmentTransaction.replace(R.id.fragments,fr).commit();
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ThemeHelper.applyTheme(SharedPreferencesHelper.getString("themeSelected", "default", "themePrefer", MODE_PRIVATE, getBaseContext()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static void rating(Context context,String Name){
        try
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Name)));
        }
        catch (Exception e)
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+Name)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
       if (id == R.id.feedback) {
            intent=new Intent(this,HelpActivity.class);
            startActivity(intent);
        }
        else if (id==R.id.aboutus){
            intent=new Intent(this,AboutUsActivity.class);
            startActivity(intent);
        }
        else if (id==R.id.rate_us){
            rating(this,"in.technostack.projects.converterforall");
        }
        else if (id==R.id.share){
            shareApp(this);
        }
        else if (id==R.id.pro){
           showDialog(getResources().getString(R.string.getProTitle),getResources().getString(R.string.pro),"user",false);
        }
       else if (id==R.id.settings){
           intent=new Intent(this,SettingsActivity.class);
           startActivity(intent);
       }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        id = item.getItemId();
        bundle=new Bundle();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,android.R.animator.fade_in,android.R.animator.fade_out);
        switch (id){
            case R.id.home:
                    fr = new Home();
                    fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                    break;
            case R.id.cdate:
                    fr = new dateCalculate();
                    fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                    break;
            case R.id.percent:
                    fr = new PercentageCalculate();
                    fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                    break;
            case R.id.pro:
                    showDialog(getResources().getString(R.string.rateUsTitle),getResources().getString(R.string.pro),"user",false);
                    break;
            case R.id.calculator:
                fr = new Calculator();
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;
            case R.id.bmi:
                fr=new BmiCalculate();
                fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                break;
            case R.id.privacy:
                openSite(this);
                break;
            case R.id.settings:
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                    fr= new ConvertValues();
                    bundle.putInt("case",id);
                    fr.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragments, fr).addToBackStack("tag").commit();
                    break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public static void shareApp(Context context) {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.checkApp) + " https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public void showDialog(String title,String message,String WHO,boolean rate) {
        FragmentManager fm = getFragmentManager();
        MyDialogs dd=new MyDialogs();
        dd.sTitle=title;
        dd.message=message;
        dd.WHO=WHO;
        dd.rating=rate;
        try {
            dd.show(fm,"");
        }
        catch (Exception e){}
    }

    public void setScreenName(String screenName){
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, screenName);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
        mFirebaseAnalytics.setCurrentScreen(this, screenName+"Class", null);
    }

    /*public void canChangeOrientation(boolean value){
        if (value)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }*/

    public static void openSite(Context context){
        try
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://technostack.in/privacy-policy")));
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
