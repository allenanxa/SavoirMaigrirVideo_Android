package anxa.com.smvideo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;

/**
 * Created by aprilanxa on 27/07/2017.
 */

public class MainLandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_main);
    }

    private void goToLoginPage(){
        Intent mainIntent = new Intent(this, LoginActivity.class);
        startActivity(mainIntent);
    }

    private void goToFreePage(){
        ApplicationData.getInstance().accountType = "free";
        Intent mainIntent = new Intent(this, LandingPageActivity.class);
        startActivity(mainIntent);
    }

    public void goToLoginPage(View view){
        goToLoginPage();
    }

    public void goToFreePart(View view){
        goToFreePage();
    }
}
