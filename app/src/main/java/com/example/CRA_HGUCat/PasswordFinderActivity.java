package com.example.CRA_HGUCat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PasswordFinderActivity extends AppCompatActivity {

    TextInputEditText resettingPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_finder);

        resettingPassword = findViewById(R.id.PasswordResetting);
    }

    public void ChangePasswordCall(View v)
    {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().getData().containsKey(resettingPassword.getText().toString()))
                    {

                    }
                    else
                    {
                        Snackbar.make(findViewById(R.id.PasswordFinderLayout),"등록되지 않은 학번입니다.\n 다시 확인해주세요.",Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}