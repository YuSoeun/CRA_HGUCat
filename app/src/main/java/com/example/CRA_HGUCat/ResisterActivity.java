package com.example.CRA_HGUCat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class ResisterActivity extends AppCompatActivity {

    TextInputEditText[] profile = new TextInputEditText[4];
    FirebaseAuth firebaseAuth;
    String HandongEmail;
    private DocumentReference docRef;
    boolean ableNickname = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resister);

        profile[0] = findViewById(R.id.HandongEmail);
        profile[1] = findViewById(R.id.loginPassword);
        profile[2] = findViewById(R.id.loginRePassword);
        profile[3] = findViewById(R.id.nickname);

        profile[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = profile[1].getText().toString();
                boolean onAlpha = false, onNumber = false;
                for(int i = 0; i < password.length(); i++)
                {
                    if(Character.isDigit(password.charAt(i)) && !onNumber)
                    {
                        onNumber = true;
                    }
                    else if(Character.isLetter(password.charAt(i)) && !onAlpha)
                    {
                        onAlpha = true;
                    }

                    if(onNumber && onAlpha)
                    {
                        break;
                    }
                }

                TextInputLayout passwordLayout = findViewById(R.id.loginPasswordLayout);

                if(!onNumber || !onAlpha)
                {
                    passwordLayout.setError("반드시 알파벳(a ~ z)과 숫자(0 ~ 9)를 사용해주세요!");
                    profile[2].setEnabled(false);
                }
                else if(password.length() > 15 || password.length() < 6)
                {
                    passwordLayout.setError("비밀번호의 길이가 6 ~ 15자이어야 합니다!");
                    profile[2].setEnabled(false);
                }
                else
                {
                    passwordLayout.setErrorEnabled(false);
                    passwordLayout.setHelperTextEnabled(false);
                    profile[2].setEnabled(true);
                }
            }
        });

        profile[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = profile[1].getText().toString();
                String repassword = profile[2].getText().toString();
                TextInputLayout rePasswordLayout = findViewById(R.id.loginRePasswordLayout);
                if(password.equals(repassword))
                {
                    rePasswordLayout.setErrorEnabled(false);
                    profile[3].setEnabled(true);
                }
                else
                {
                    rePasswordLayout.setError("다시 입력한 비밀번호가 비밀번호와 다릅니다!");
                    profile[3].setEnabled(false);
                }
            }
        });

        profile[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String nickname = profile[3].getText().toString();
                if(nickname.isEmpty())
                {
                    ableNickname = false;
                }
                else
                {
                    boolean fillSpace = true;
                    for(int i = 0; i < nickname.length(); i++)
                    {
                        if(nickname.charAt(i) != ' ')
                        {
                            fillSpace = false;
                            break;
                        }
                    }

                    if(fillSpace) ableNickname = false;
                    else ableNickname = true;
                }

                if(ableNickname &&
                        profile[1].getText().toString().equals(profile[2].getText().toString()) &&
                        !findViewById(R.id.HandongAuthBtn).isEnabled())
                {
                    findViewById(R.id.registerBtn).setEnabled(true);
                }
                else
                {
                    findViewById(R.id.registerBtn).setEnabled(false);
                }
            }
        });
    }

    public void callEmail(View v)
    {
        final String HandongMail = profile[0].getText().toString().replace(" ", "").replace("\n", "");
        if (!HandongMail.contains("@") && !HandongMail.contains(".")) {
            Toast.makeText(this, "이메일의 형태로 입력해주세요!", Toast.LENGTH_SHORT).show();
            profile[0].setText("");
        } else if (HandongMail.length() != "00000000@handong.edu".length()) {
            Toast.makeText(this, "오탈자가 있는지 확인해주세요!", Toast.LENGTH_SHORT).show();
        } else if (!HandongMail.substring(HandongMail.length() - "@handong.edu".length()).equals("@handong.edu")) {
            Toast.makeText(this, "한동대학교 이메일이 맞는지 확인해주세요!", Toast.LENGTH_SHORT).show();
        } else {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists() && document.getData().containsKey(HandongMail))
                            Toast.makeText(ResisterActivity.this, "이미 가입된 학번입니다.", Toast.LENGTH_SHORT).show();
                        else
                        {
                            HandongEmail = HandongMail;
                            findViewById(R.id.HandongAuthBtn).setEnabled(false);
                            profile[1].setEnabled(true);
                            Toast.makeText(ResisterActivity.this, "중복되지 않은 학번입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public void AskResister(View v) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(HandongEmail, profile[1].getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
                                            Map<String, Object> SaveData = new HashMap<>();
                                            SaveData.put(HandongEmail, profile[3].getText().toString());
                                            docRef.set(SaveData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.ResisterActivityLayout),
                                                            "한동대 이메일에서 링크를 클릭하여 인증\n해주셔야 로그인이 가능합니다.",
                                                            Snackbar.LENGTH_INDEFINITE);
                                                    snackbar.setAction("확인", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            snackbar.dismiss();
                                                            finish();
                                                        }
                                                    });
                                                    snackbar.show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    });
                        } else {
                            Toast.makeText(ResisterActivity.this, "가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void ReturnLogin(View v)
    {
        finish();
    }
}