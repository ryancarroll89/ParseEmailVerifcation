package com.example.parseemailverifcation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity
{
    private EditText edtLoginUsername, edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
    }

    public void loginIsPressed(View btnView)
    {
        ParseUser.logInInBackground(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback()
        {
            @Override
            public void done(ParseUser parseUser, ParseException e)
            {
                if (parseUser != null)
                {
                    if (parseUser.getBoolean("emailVerified"))
                    {
                        alertDisplayer("Login Successful", "Welcome " + parseUser.getUsername(), false);
                    }
                    else
                    {
                        ParseUser.logOut();
                        alertDisplayer("Login fail", "please verify email " + parseUser.getUsername(), true);
                    }
                }
                else
                {
                    ParseUser.logOut();
                    alertDisplayer("Login Failed", e.getMessage() + " Please Try Again", true);
                }
            }
        });
    }

    private void alertDisplayer(String title, String message, final boolean error)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                        if (!error)
                        {
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}