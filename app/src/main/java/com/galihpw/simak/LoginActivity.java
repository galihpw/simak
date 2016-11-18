package com.galihpw.simak;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galihpw.simak.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GalihPW on 23/09/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    //Defining views
    private EditText editTextNip;
    private EditText editTextPassword;
    private Button buttonLogin;
    public ProgressDialog progressDialog;
    String nipp, nip;

    private static String url_login 	 = Config.URL + "login.php";

    //konstanta, supaya bisa membedakan antar message
    public final static String EXTRA_MESSAGE = "com.galihpw.NIP";

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Initializing views
        editTextNip = (EditText) findViewById(R.id.nip);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.login_btn);

        //Adding click listener
        buttonLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        nipp = sharedPreferences.getString(Config.NIP_SHARED_PREF, nip);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(EXTRA_MESSAGE, nipp);
            startActivity(intent);

            finish();
        }
    }

    private void login(){
        //Getting values from edit texts
        nip = editTextNip.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.NIP_SHARED_PREF, nip);

                            //Saving values to editor
                            editor.commit();

                            progressDialog.dismiss();

                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, nip);
                            startActivity(intent);

                            finish();
                        }else{
                            progressDialog.dismiss();

                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        progressDialog.dismiss();

                        Toast.makeText(LoginActivity.this, "No Connection", Toast.LENGTH_LONG).show();
                        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);*/
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_NIP, nip);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Hide:
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }

        //Loading
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Calling the login function
        login();

    }

    public boolean validate() {
        boolean valid = true;

        String nip = editTextNip.getText().toString();
        String password = editTextPassword.getText().toString();

        if (nip.isEmpty()) {
            editTextNip.setError("Masukkan NIP");
            valid = false;
        } else {
            editTextNip.setError(null);
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Masukkan Password");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }
}
