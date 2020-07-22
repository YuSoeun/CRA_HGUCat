package com.example.CRA_HGUCat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class home extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1234;
    private FirebaseAuth nAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(provider).build(),
                RC_SIGN_IN
        );
    }

    @Override
    public void onStart()
    {
        super.onStart();
        nAuth = FirebaseAuth.getInstance();

        nAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser currentUser = nAuth.getCurrentUser();
                            updateUI(currentUser);
                        }
                        else
                        {
                            updateUI(null);
                            Toast.makeText(home.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    String name = null;

    private void updateUI(FirebaseUser user)
    {
        if(user != null)
        {
            name = user.getDisplayName();
        }
        else
        {
            finish();
        }
    }


    @Override
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
    }

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
