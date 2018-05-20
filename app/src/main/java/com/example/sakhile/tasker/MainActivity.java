package com.example.sakhile.tasker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import com.mongodb.client.*;
import com.mongodb.*;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    TextView signUp;
    EditText username;
    EditText phoneNumber;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp=(TextView)findViewById(R.id.sign_up_btn);

        username= (EditText)findViewById(R.id.username_edt);
        phoneNumber= (EditText)findViewById(R.id.phone_edt);
        signIn= (Button)findViewById(R.id.sign_in_btn);
    }

    public void btnClick_SignUp(View view){

        Intent i= new Intent(this, SignUpActivity.class);
        finish();
        startActivity(i);
    }

    public void btnClick_SignIn(View view){
        try{

            if(isNetworkAvailable()){

                new signIn().execute("");
            }
            else{

                //if not available, add a Snackbar to display there's no connection#
                Snackbar.make(view, "Connection failed. Check your Internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }catch(Exception ex){
            ex.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    //culoLam

    private class signIn extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        public String doInBackground(String... param){


            try{
                MongoClientURI uri  = new MongoClientURI("mongodb://Sakhispot:sAkh1spot@ds259079.mlab.com:59079/services");
                MongoClient client = new MongoClient(uri);

                MongoDatabase db = client.getDatabase(uri.getDatabase());
                MongoCollection<Document> collection = db
                        .getCollection("users");

                Document document=null;
                document=collection.find(Filters.eq("username", username.getText().toString())).first();

                if(document==null){
                    return "Incorrect username or phone number";
                }else{
                    return "found";
                }

            }catch (Exception e){
                e.printStackTrace();

                return "Server not responding";
            }

        }

        @Override
        public void onPostExecute(String result) {
            progressDialog.dismiss();

            if(result.equals("found")){
                Intent intent= new Intent(MainActivity.this, Main2Activity.class);
                finish();
                startActivity(intent);
            }
            else if(result.equals("Incorrect username or phone number")){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(result)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(result)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Sign In",
                    "Signing in.....");
        }
    }
}
