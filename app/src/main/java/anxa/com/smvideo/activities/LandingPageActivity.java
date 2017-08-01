package anxa.com.smvideo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import anxa.com.smvideo.ApplicationData;
import anxa.com.smvideo.R;

/**
 * Created by aprilanxa on 31/05/2017.
 */

public class LandingPageActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_page_free);

        ApplicationData.getInstance().showLandingPage = false;

        //initialize on click (transfer inside api call if there will be one in the future
        ((Button) findViewById(R.id.LandingDiscoverButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.LandingRecettesButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.LandingBilanButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.LandingTemoignagesButton)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.LandingImage1)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.LandingImage2)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.LandingImage3)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.LandingImage4)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.LandingDiscoverButton || v.getId() == R.id.LandingImage1) {
            GoToDiscoverPage();
        }
        if (v.getId() == R.id.LandingRecettesButton || v.getId() == R.id.LandingImage4) {
            GoToRecettesPage();
        }
        if (v.getId() == R.id.LandingBilanButton || v.getId() == R.id.LandingImage2) {
            GoToBilanPage();
        }
        if (v.getId() == R.id.LandingTemoignagesButton || v.getId() == R.id.LandingImage3) {
            GoToTemoignagesPage();
        }
    }

    private void GoToDiscoverPage() {
        ApplicationData.getInstance().selectedFragment = ApplicationData.SelectedFragment.Decouvir;
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainIntent);

    }

    private void GoToRecettesPage() {
      ApplicationData.getInstance().selectedFragment = ApplicationData.SelectedFragment.Recettes;
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainIntent);




    }

    private void GoToBilanPage() {
        ApplicationData.getInstance().selectedFragment = ApplicationData.SelectedFragment.Bilan;
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainIntent);
    }

    private void GoToTemoignagesPage() {
        ApplicationData.getInstance().selectedFragment = ApplicationData.SelectedFragment.Temoignages;
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainIntent);
    }

}
