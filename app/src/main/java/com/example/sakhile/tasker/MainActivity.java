package com.example.sakhile.tasker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import com.mongodb.client.*;
import com.mongodb.*;
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
        new signIn().execute("");
    }

    private class signIn extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... param){

            MongoClientURI uri  = new MongoClientURI("mongodb://Sakhispot:sAkh1spot@ds259079.mlab.com:59079/services");
            MongoClient client = new MongoClient(uri);

            MongoDatabase db = client.getDatabase(uri.getDatabase());
            MongoCollection<Document> collection = db
                    .getCollection("users");

            List<Document> documents = (List<Document>) collection.find().into(
                    new ArrayList<Document>());

            for(Document document : documents){
                System.out.println(document);
            }
            return "";
        }
    }
}
