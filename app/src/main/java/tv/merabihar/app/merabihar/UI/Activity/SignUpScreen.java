package tv.merabihar.app.merabihar.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import tv.merabihar.app.merabihar.R;

public class SignUpScreen extends AppCompatActivity {

    FrameLayout mCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_sign_up_screen);

            mCreateAccount = (FrameLayout)findViewById(R.id.createAccount);

            mCreateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent create = new Intent(SignUpScreen.this,ContentDetailScreen.class);
                    startActivity(create);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
