package com.back4app.basicuserregistration;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.View;
//import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText et_username;
    EditText et_password;
    Button bt_login;
    Button bt_register;
    ProgressDialog progressDialog;
//    ProgressBar progressBar;
//    create an alert dialog to show the progressbar

    String user;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("znPJtKs3ulIypFkuVP9nVxUpaC47qijSyqe6BQ6C") //PASTE YOUR Back4App APPLICATION ID
                .clientKey("7m0FBbyeIuwZFpIP7ZUVRHmC3PvZt3XKHhW0Ogqs") //PASTE YOUR CLIENT KEY
                .server("https://parseapi.back4app.com/").build()
        );

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        bt_register = findViewById(R.id.bt_register);
        bt_login = findViewById(R.id.bt_login);

        progressDialog = new ProgressDialog(MainActivity.this);


        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Registering");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            user = (et_username.getText().toString());
                            pwd = (et_password.getText().toString());
                            userRegister(user, pwd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Login in");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            user = (et_username.getText().toString());
                            pwd = (et_password.getText().toString());
                            userLogin(user, pwd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


    }

    void alertDisplayer(final String title, final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        et_password.setText(""); TODO:encontrar outra solução p/ isso
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    void userRegister(final String username, final String password){
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    progressDialog.dismiss();
//                    et_username.setText(ParseUser.getCurrentUser().getUsername()); TODO:encontrar outra solução p/ isso

                    saveNewUser(username);
                } else {
                    progressDialog.dismiss();
                    alertDisplayer("Register Fail", e.getMessage()+" Please Try Again");
                }
            }
        });
    }

    void saveNewUser(final String username){
        ParseUser user = ParseUser.getCurrentUser();
        user.setUsername(username);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                alertDisplayer("Register Successful", "User: " + username);
            }
        });
    }

    void userLogin(final String username, final String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    progressDialog.dismiss();
                    alertDisplayer("Login Successful", "Welcome " + parseUser.getUsername());
                } else {
                    progressDialog.dismiss();
                    alertDisplayer("Login Failed", e.getMessage() + " Please Try Again");
                }
            }
        });
    }


}


















