package th.co.thinknet.atit.tnfingerscanviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

import th.co.thinknet.atit.tnfingerscanviewer.R;

public class SplashScreenActivity extends Activity {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        UserData userData = UserData.getInstance();
        userData.initialize(getApplicationContext());

        if(userData.getDataModel() == null){
            //go to login page
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    gotoLogin();
                }
            }, 5000);
        }
        else{
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    gotoMain();
                }
            }, 5000);
        }
    }

    public void gotoLogin(){
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }

    public void gotoMain(){
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }

}
