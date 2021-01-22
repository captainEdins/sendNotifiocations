package com.example.sendnotificationmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sendnotificationmysql.GridViewUtils.Adapter.gridAdapterNews;
import com.example.sendnotificationmysql.GridViewUtils.Adapter.gridAdapterTotal;
import com.example.sendnotificationmysql.GridViewUtils.Model.gridModelNews;
import com.example.sendnotificationmysql.GridViewUtils.Model.gridModelTotal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ShowNew extends AppCompatActivity {


    //here i am setting the total and the name of the category
    GridView gridView;
    gridAdapterNews adapter;
    List<gridModelNews> list;
    gridModelNews item;

    //php fetch from the db
    private String url = "http://192.168.43.70/banksys";
    private String url_file = "/unread.php";
    RequestQueue requestQueue;

    boolean toastShown = false;

    //to avoid multiple insert of data
    String check = "null";
    String checkStatus = "null";
    int checkCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_new);

        list = new ArrayList<>();

        gridView = findViewById(R.id.gridViewNews);

        adapter = new gridAdapterNews(list, getApplicationContext());

        gridView.setAdapter(adapter);

        //volley requestQueue
        requestQueue = Volley.newRequestQueue(getApplication());
        getUrlValue();

    }

    private void getUrlValue() {

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String resultId = id;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + url_file,
                response -> {

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("result");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            JSONArray jsonArrayArticles = new JSONArray(object.getString("articles"));
                            JSONArray jsonArrayUnread = new JSONArray(object.getString("unread_ids"));


                                for (int j = 0; j < jsonArrayArticles.length(); j++) {

                                    JSONObject objectArticles = jsonArrayArticles.getJSONObject(j);

                                    for (int k = 0; k < jsonArrayUnread.length(); k++) {


                                    System.out.println(jsonArrayUnread.get(k).toString());

                                    String getTitle = objectArticles.getString("name");
                                    String getDescription = objectArticles.getString("description");
                                    String getId = objectArticles.getString("category_id");


                                    item = new gridModelNews();

                                    if (resultId.equals(getId)) {

                                        if (objectArticles.getString("id").equals(jsonArrayUnread.get(k).toString())){

                                            item.setDescription(getDescription + ".");
                                            item.setRead("false");
                                            item.setName(getTitle.toUpperCase() + ".");
                                            list.add(item);
                                            checkStatus = "false";
                                            check = objectArticles.getString("id");

                                        }else if(!(objectArticles.getString("id").equals(jsonArrayUnread.get(k).toString()))){

                                            if (checkStatus.equals("false") && check.equals(objectArticles.getString("id"))){

                                            }else if(checkStatus.equals("true") && check.equals(objectArticles.getString("id")) && k == jsonArrayUnread.length() - 1){

                                                item.setDescription(getDescription + ".");
                                                item.setRead("true");
                                                item.setName(getTitle.toUpperCase() + ".");
                                                list.add(item);

                                            }else{

                                                checkStatus = "true";
                                                check = objectArticles.getString("id");

                                            }


                                        }


                                    }

                                }

                            }

                        }

                        adapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        Toast.makeText(ShowNew.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                },
                error -> {

                    if (toastShown == false) {
                        toastShown = true;
                        Toast.makeText(ShowNew.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //try to reload until it gets the data
                    getUrlValue();

                });
        requestQueue.add(stringRequest);
    }

}
