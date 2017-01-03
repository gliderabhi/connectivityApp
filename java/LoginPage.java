package com.anurag.rebel.customerappstart;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class LoginPage extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
         img=(ImageView)findViewById(R.id.imageView8);
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(LoginPage.this, LoginPageUser.class);
                    LoginPage.this.startActivity(mainIntent);
                    LoginPage.this.finish();
                }
        }, SPLASH_DISPLAY_LENGTH);

    }
}


