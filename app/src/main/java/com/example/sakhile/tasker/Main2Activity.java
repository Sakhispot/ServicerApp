package com.example.sakhile.tasker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.support.design.widget.CoordinatorLayout;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    Adapter adapter;
    private List<Services> servicesList = new ArrayList<>();
    public ProgressBar progressBar;
    Services service;
    EditText name;
    EditText location;
    EditText contact;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddServicesDialog();
            }
        });

        adapter= new Adapter(servicesList);
        recyclerView= (RecyclerView)findViewById(R.id.services_rv);
        coordinatorLayout= (CoordinatorLayout)findViewById(R.id.coodinator_layout);

        try{
            if (isNetworkAvailable()) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

                //set the adapter
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

                new ServiceLoader().execute("");

                ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
                new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

            } else{
                //if not available, add a Snackbar to display there's no connection#
                Snackbar.make(view, "Connection failed. Check your Internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }catch(Exception ex){
            ex.printStackTrace();

            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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

    public void showAddServicesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("Title");

        View viewInflated = LayoutInflater.from(Main2Activity.this).inflate(R.layout.services_dialog, null);
// Set up the input
        name = (EditText) viewInflated.findViewById(R.id.service_name_edt);
        location = (EditText) viewInflated.findViewById(R.id.service_location_edt);
        contact = (EditText) viewInflated.findViewById(R.id.contact_edt);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

// Set up the buttons
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter.MyViewHolder) {


            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());
            String servicename = ((TextView) recyclerView.findViewHolderForAdapterPosition(position-1).itemView.findViewById(R.id.name)).getText().toString();
            new deleteService().execute(servicename);

        }
    }

    private class submitRecords extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        public String doInBackground(String... param){


            try{


                MongoClientURI uri  = new MongoClientURI("mongodb://Sakhispot:sAkh1spot@ds259079.mlab.com:59079/services");

                MongoClient client = new MongoClient(uri);

                MongoDatabase db = client.getDatabase(uri.getDatabase());

                MongoCollection<BasicDBObject> collection = db.getCollection("services", BasicDBObject.class);
                BasicDBObject userInfo= new BasicDBObject();

                userInfo.put("service_name", name.getText().toString());
                userInfo.put("service_location", location.getText().toString());
                userInfo.put("contact", contact.getText().toString());

                collection.insertOne(userInfo);

                return "Service added";

            }catch (Exception e){
                e.printStackTrace();

                return "Server not responding";
            }
        }

        @Override
        public void onPostExecute(String result) {
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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
            progressDialog = ProgressDialog.show(Main2Activity.this,
                    "Service Info",
                    "Adding services.....");
        }
    }

    private class ServiceLoader extends AsyncTask<String, Void, String> {

        ProgressBar progressBar= (ProgressBar)findViewById(R.id.pb_loading_indicator);

        @Override
        public String doInBackground(String... param){


            try{
                MongoClientURI uri  = new MongoClientURI("mongodb://Sakhispot:sAkh1spot@ds259079.mlab.com:59079/services");
                MongoClient client = new MongoClient(uri);

                MongoDatabase db = client.getDatabase(uri.getDatabase());
                MongoCollection<Document> collection = db
                        .getCollection("services");

                FindIterable<Document> findIterable = collection.find();

                for(Document d  : findIterable){
                    service= new Services(d.getString("service_name"),
                            d.getString("service_location"), d.getString("contact"));
                    servicesList.add(service);

                }

                if(servicesList.isEmpty()){
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
            progressBar.setVisibility(View.INVISIBLE);

            if(result.equals("found")){
                adapter.notifyDataSetChanged();
            }
            else{


            }
        }


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private class deleteService extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        public String doInBackground(String... param){


            try{

                String serviceDeleted= param[0];

                MongoClientURI uri  = new MongoClientURI("mongodb://Sakhispot:sAkh1spot@ds259079.mlab.com:59079/services");

                MongoClient client = new MongoClient(uri);

                MongoDatabase db = client.getDatabase(uri.getDatabase());

                MongoCollection<BasicDBObject> collection = db.getCollection("services", BasicDBObject.class);
                Bson filter= new Document("service_name", serviceDeleted);
                collection.deleteOne(filter);


                return serviceDeleted;

            }catch (Exception e){
                e.printStackTrace();

                return "Server not responding";
            }
        }

        @Override
        public void onPostExecute(String result) {
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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
            progressDialog = ProgressDialog.show(Main2Activity.this,
                    "Service Info",
                    "Deleting services.....");
        }
    }

}
