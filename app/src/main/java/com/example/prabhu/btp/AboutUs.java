package com.example.prabhu.btp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends Activity {
TextView textView;
    String about_us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        about_us="Mentor:\nProf. Rajendra Sahu\n\nGroup Members:\nB.Srikanth\nG.Prabhu Teja\nL.Bhargavi\n\nInstitute:\nABV-IIITM,Gwalior";
        textView=(TextView)findViewById(R.id.textView);
        textView.setText(about_us);
    }
}
