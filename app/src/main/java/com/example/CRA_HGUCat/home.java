package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class home extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(provider).build(),
                RC_SIGN_IN
        );*/
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            }
            else
            {
                response.getError().getErrorCode();
            }
        }
    }*/

    public void StartList(View v)
    {
        Intent catlist = new Intent(this, catlist.class);
        startActivity(catlist);
    }

    public void StartAccount(View v)
    {
        Intent acc = new Intent(this, personal_account.class);
        startActivity(acc);
    }

    public void StartComu(View v)
    {
        Intent comu = new Intent(this, Comunity_main.class);
        startActivity(comu);
    }

    public void StartCamera(View v)
    {
        Intent cmr = new Intent(this,CaptureCat.class);
        startActivity(cmr);
    }

    /*
    public void Startfavo(View v)
    {
        Intent cmr = new Intent(this, favorite.class);
        startActivity(cmr);
    }
    */
}
