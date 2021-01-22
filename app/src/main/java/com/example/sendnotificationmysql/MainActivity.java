package com.example.sendnotificationmysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sendnotificationmysql.GridViewUtils.Adapter.gridAdapterTotal;
import com.example.sendnotificationmysql.GridViewUtils.Model.gridModelNews;
import com.example.sendnotificationmysql.GridViewUtils.Model.gridModelTotal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //here i am setting the total and the name of the category
    GridView gridView;
    gridAdapterTotal adapter;
    List<gridModelTotal> list;
    gridModelTotal item;

    //php fetch from the db
    private String url = "http://192.168.43.70/banksys";
    private String url_file = "/unread.php";
    RequestQueue requestQueue;
    boolean toastShown = false;
    //notification
    NotificationManager NM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        gridView = findViewById(R.id.gridView);

        adapter = new gridAdapterTotal(list, getApplicationContext());

        gridView.setAdapter(adapter);

        //volley requestQueue
        requestQueue = Volley.newRequestQueue(getApplication());
        getUrlValue();


    }

    private void getUrlValue() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + url_file,
                response -> {

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("result");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String getTitle = object.getString("name");
                            String getCount = object.getString("unread");
                            String getId = object.getString("id");

                            item = new gridModelTotal(getTitle.toUpperCase() + ".", getCount.toUpperCase() + ".", getId);
                            list.add(item);

                            //send notification
                            JSONArray jsonArrayArticles = new JSONArray(object.getString("articles"));
                            JSONArray jsonArrayUnread = new JSONArray(object.getString("unread_ids"));


                            for (int j = 0; j < jsonArrayArticles.length(); j++) {

                                JSONObject objectArticles = jsonArrayArticles.getJSONObject(j);

                                for (int k = 0; k < jsonArrayUnread.length(); k++) {


                                    System.out.println(jsonArrayUnread.get(k).toString());

                                    String getDescription = objectArticles.getString("description");

                                    if (objectArticles.getString("id").equals(jsonArrayUnread.get(k).toString())) {

                                        NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        Notification notify = new Notification.Builder(getApplicationContext())
                                                .setContentTitle(getTitle).
                                                        setContentText(getDescription)
                                                .setPriority(Notification.PRIORITY_HIGH)
                                                .setSmallIcon(R.drawable.notifications).build();

                                        notify.defaults |= Notification.DEFAULT_SOUND;

                                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                                        //create random number to allow multiple show of the notification
                                        NM.notify(Integer.parseInt(jsonArrayUnread.get(k).toString()), notify);
                                    }

                                }

                            }
                            //send notification

                        }

                        adapter.notifyDataSetChanged();

                        gridView.setOnItemClickListener((parent, view, position, id) -> {

                            String getId = list.get(position).getId();

                            Intent intent = new Intent(getApplication(), ShowNew.class);
                            intent.putExtra("id", getId);
                            startActivity(intent);

                        });


                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                },
                error -> {

                    if (toastShown == false) {
                        toastShown = true;
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //try to reload until it gets the data
                    getUrlValue();

                });
        requestQueue.add(stringRequest);
    }


}
