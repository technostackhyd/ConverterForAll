package in.technostack.projects.converterforall;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class HelpActivity extends AppCompatActivity {

    Button sendbtn;
    boolean check=false;
    EditText name,subject,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Feedback");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        sendbtn= (Button)findViewById(R.id.SendFeedback);
        name=(EditText) findViewById(R.id.feedbackName);
        subject=(EditText) findViewById(R.id.feedbackSubject);
        msg=(EditText) findViewById(R.id.feedbackMsg);
        sendbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                sendEmail();
            };
        });
        FirebaseAnalytics.getInstance(this).setCurrentScreen(this,getResources().getString(R.string.feedbackTitle),null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        if(check)
        {
            name.setText("");
            subject.setText("");
            msg.setText("");
            check=false;
        }
        super.onResume();
    }

    protected void sendEmail(){
        String TO = "technostackhyd@gmail.com";
        String Name=name.getText().toString();
        String Subject=subject.getText().toString();
        String Msg=msg.getText().toString();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+TO+"?subject="+Subject+"&body= Name: "+Name+"\n\nMessage: "+Msg));
        try {
            startActivity(emailIntent);
            check=true;
        } catch (android.content.ActivityNotFoundException ex) {
            check=false;
            Toast.makeText(this, getResources().getString(R.string.emailError), Toast.LENGTH_SHORT).show();
        }
    }
}
