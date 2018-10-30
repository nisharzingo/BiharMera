package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tv.merabihar.app.merabihar.R;

public class LoginScreen extends AppCompatActivity {

    TextView mLoginAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_login_screen);

            mLoginAccount = (TextView)findViewById(R.id.loginAccount);

            mLoginAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent create = new Intent(LoginScreen.this,ProfileScreen.class);
                    startActivity(create);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
