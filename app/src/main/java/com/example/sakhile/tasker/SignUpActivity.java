package com.example.sakhile.tasker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import android.support.design.widget.Snackbar;
import java.net.UnknownHostException;

public class SignUpActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText name;
    EditText phoneNumber;
    EditText username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar= (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Sign up");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name= (EditText)findViewById(R.id.name_edt);
        phoneNumber= (EditText)findViewById(R.id.phone_edt);
        username= (EditText)findViewById(R.id.username_edt);


    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i= new Intent(this, MainActivity.class);
                finish();
                startActivity(i);
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnClick_SignUp(View view){

        /*check the device for internet connection
        https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
        * */

        try {

            if (isNetworkAvailable()) {
                new submitRecords().execute("");
            } else{
                //if not available, add a Snackbar to display there's no connection#
                Snackbar.make(view, "Connection failed. Check your Internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }catch(Exception e) {
            e.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage("Error occurred while checking connection")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private class submitRecords extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;

        @Override
        public String doInBackground(String... param){


           try{


               MongoClientURI uri  = new MongoClientURI("mongodb://Sakhispot:sAkh1spot@ds259079.mlab.com:59079/services");

               MongoClient client = new MongoClient(uri);

               MongoDatabase db = client.getDatabase(uri.getDatabase());

               MongoCollection<BasicDBObject> collection = db.getCollection("users", BasicDBObject.class);
               BasicDBObject userInfo= new BasicDBObject();

               userInfo.put("name", name.getText().toString());
               userInfo.put("phone", phoneNumber.getText().toString());
               userInfo.put("username", username.getText().toString());

               collection.insertOne(userInfo);


               return "User signed up. You can sign in now";

           }catch (Exception e){
               e.printStackTrace();

               return "Server not responding";
           }
        }

        @Override
        public void onPostExecute(String result) {
            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage(result)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Sign Up",
                    "Signing up.....");
        }
    }
}
