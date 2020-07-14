package in.technostack.projects.converterforall;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AboutUsActivity extends AppCompatActivity {

    public static String FACEBOOK_URL = "https://www.facebook.com/technostackhyd";
    public static String FACEBOOK_ID = "462920840567094";
    public static String FACEBOOK_PAGE_ID = "technostackhyd";
    public static String webURL = "http://www.technostack.in/";

    ImageView facebook,instagram,web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Info");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        facebook= (ImageView) findViewById(R.id.facebook);
        instagram=(ImageView) findViewById(R.id.instagram);
        web=(ImageView) findViewById(R.id.website);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFacebook(getApplicationContext());
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInstagram(getApplicationContext());
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite(getApplicationContext());
            }
        });
        FirebaseAnalytics.getInstance(this).setCurrentScreen(this,getResources().getString(R.string.info),null);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public static void getFacebook(Context context) {

        PackageManager packageManager = context.getPackageManager();
        String facebookUrl;
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                facebookUrl="fb://page/" + FACEBOOK_ID;
            } else { //older versions of fb app
                facebookUrl="fb://page/" + FACEBOOK_ID;
            }
            facebookIntent.setData(Uri.parse(facebookUrl));
            context.startActivity(facebookIntent);
        }
        catch (PackageManager.NameNotFoundException e) {
            facebookUrl=FACEBOOK_URL; //normal web url
            facebookIntent.setData(Uri.parse(facebookUrl));
            context.startActivity(facebookIntent);
        }
        catch (ActivityNotFoundException exc){
            Toast.makeText(context,context.getResources().getString(R.string.fbError),Toast.LENGTH_LONG).show();
        }
        catch (Exception exc){
            Toast.makeText(context,context.getResources().getString(R.string.fbError),Toast.LENGTH_LONG).show();
        }
    }

    public static void getInstagram(Context context){

        Intent instagramIntent;

        try {
                instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/technostack1"));
                instagramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                instagramIntent.setPackage("com.instagram.android");
                context.startActivity(instagramIntent);
        }
        catch (ActivityNotFoundException e) {
            instagramIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://instagram.com/technostack1"));
            instagramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(instagramIntent);
        }
    }

    public static void getWebsite(Context context){

        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        webIntent.setData(Uri.parse(webURL));
        context.startActivity(webIntent);

    }
}
