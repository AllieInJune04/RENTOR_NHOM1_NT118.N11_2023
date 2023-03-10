package com.example.carrenting.Service.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {

    private FirebaseUser user;
    private EditText currentPass, newPass, confirmPass;
    private String current, newPassword, confirmNew;
    private Button btnContinue;
    private Boolean isValid = true;

    private void init()
    {
        currentPass = findViewById(R.id.input_password);
        newPass = findViewById(R.id.input_new_password);
        confirmPass = findViewById(R.id.reinput_new_password);
        btnContinue = findViewById(R.id.btn_continue);

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void getPass()
    {
        current = currentPass.getText().toString();
        newPassword = newPass.getText().toString();
        confirmNew = confirmPass.getText().toString();
        checkPassword();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        init();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPass();

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), current);

                if(isValid)
                {
                    user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // M???t kh???u hi???n t???i ???? ???????c x??c th???c th??nh c??ng
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(UpdatePassword.this, "Thay ?????i m???t kh???u th??nh c??ng",Toast.LENGTH_LONG).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(UpdatePassword.this, "???? x???y ra l???i trong qu?? tr??nh thay ?????i m???t kh???u",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(UpdatePassword.this, "M???t kh???u hi???n t???i kh??ng ch??nh x??c",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }
            }
        });
    }

    private void checkPassword() {

        if (!newPassword.equals(confirmNew))
        {
            Toast.makeText(this,"M???t kh???u kh??ng kh???p, m???i nh???p l???i",Toast.LENGTH_LONG).show();
            newPass.setText("");
            confirmPass.setText("");
            isValid = false;
        }
        else if(newPassword.isEmpty())
        {
            Toast.makeText(this,"Vui l??ng nh???p m???t kh???u",Toast.LENGTH_LONG).show();
            newPass.setText("");
            confirmPass.setText("");
            isValid = false;
        }
    }
}