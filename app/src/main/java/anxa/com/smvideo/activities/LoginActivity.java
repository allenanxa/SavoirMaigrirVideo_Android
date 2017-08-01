package anxa.com.smvideo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;
import anxa.com.smvideo.connection.ApiCaller;
import anxa.com.smvideo.connection.http.AsyncResponse;
import anxa.com.smvideo.contracts.DietProfilesDataContract;
import anxa.com.smvideo.contracts.LoginContract;
import anxa.com.smvideo.contracts.UserDataResponseContract;
import anxa.com.smvideo.util.AppUtil;

/**
 * Created by aprilanxa on 27/07/2017.
 */

public class LoginActivity extends Activity{

    private LoginContract loginContract;
    private EditText email_et, password_et;

    private ApiCaller apiCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        loginContract = new LoginContract();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        email_et = (EditText)findViewById(R.id.login_email_et);
        password_et = (EditText)findViewById(R.id.login_password_et);

    }

    public void goBackToLandingPage(View view){
        onBackPressed();
    }

    public void validateLogin(View view){
//        goToAccountLandingPage();

        if (validateLogin())
            loginToAPI();
    }

    private void loginToAPI(){
        loginContract.Email = email_et.getText().toString();
        loginContract.Password = password_et.getText().toString();
        loginContract.Check_npna = false;

        apiCaller = new ApiCaller();

        apiCaller.PostLogin(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if (output != null) {
                    UserDataResponseContract c = (UserDataResponseContract) output;

                    if (c != null) {
                        if(c.Message.equalsIgnoreCase("Failed")){
                            displayToastMessage(getString(R.string.ALERTMESSAGE_LOGIN_FAILED));
                        }else {
                            System.out.println("PostLogin: " + c.Data.Email);
                            ApplicationData.getInstance().userDataContract = c.Data;
                            ApplicationData.getInstance().regId = c.Data.Id;

                            goToAccountLandingPage();
                        }
                    }else{
                        displayToastMessage(getString(R.string.ALERTMESSAGE_LOGIN_FAILED));
                    }
                }

            }
        }, loginContract);

    }

    private void goToAccountLandingPage(){
        ApplicationData.getInstance().accountType = "account";
        Intent mainIntent = new Intent(this, LandingPageAccountActivity.class);
        startActivity(mainIntent);
    }

    private Boolean validateLogin() {
        if (email_et.getText() == null || email_et.getText().length() <= 0) {
            displayToastMessage(getString(R.string.ALERTMESSAGE_LOGIN_EMPTY));
            return false;
        } else if (!AppUtil.isEmail(email_et.getText().toString())) {
            displayToastMessage(getString(R.string.ALERTMESSAGE_LOGIN_EMAIL_ERROR));
            return false;
        } else if (password_et.getText() == null || password_et.getText().length() <= 0) {
            displayToastMessage(getString(R.string.ALERTMESSAGE_LOGIN_FAILED));
            return false;
        } else {
            return true;
        }
    }

    public void displayToastMessage(final String message) {
        final Context context = this;
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast m = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                m.show();

            }
        });
    }
}
