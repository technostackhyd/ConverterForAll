package in.technostack.projects.converterforall;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences=getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        if (preferences.getBoolean("isFirst",true))
        {
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isFirst",false);
            editor.apply();
            setContentView(R.layout.splash_screen);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

}
