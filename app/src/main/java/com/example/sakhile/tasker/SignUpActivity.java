package com.example.sakhile.tasker;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;

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

        //if available
        new submitRecords().execute("");

        /*if not available, add a Snackbar to display there's no connection#
        Tutorial @https://www.journaldev.com/10324/android-snackbar-example-tutorial*/

    }


    //sAkh1spot dLam1ni
    private class submitRecords extends AsyncTask<String, Void, String>{


        @Override
        public String doInBackground(String... param){

            MongoClientURI uri  = new MongoClientURI("mongodb://Sakhispot:sAkh1spot@ds259079.mlab.com:59079/services");
            MongoClient client = new MongoClient(uri);

            MongoDatabase db = client.getDatabase(uri.getDatabase());

            MongoCollection<BasicDBObject> collection = db.getCollection("users", BasicDBObject.class);
            BasicDBObject userInfo= new BasicDBObject();

            userInfo.put("name", name.getText().toString());
            userInfo.put("phone", phoneNumber.getText().toString());
            userInfo.put("username", username.getText().toString());
AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Account added. You can SignIn now")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent i= new Intent(SignUpActivity.this, MainActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            collection.insertOne(userInfo);

            return "Details inserted";

        }

        @Override
        public void onPostExecute(String result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Account added. You can SignIn now")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent i= new Intent(SignUpActivity.this, MainActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }


}
