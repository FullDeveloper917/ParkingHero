package com.emraz.parkinghero.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.TypedValue;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.emraz.parkinghero.R;
import com.emraz.parkinghero.adapter.NewsAdapter;
import com.emraz.parkinghero.rest.model.NewsDoc;
import com.emraz.parkinghero.util.Constants;
import com.emraz.parkinghero.util.Log;
import com.emraz.parkinghero.util.VerticalSpaceItemDecoration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsActivity extends AppCompatActivity {


    RecyclerView recyclerViewNews;

    NewsAdapter mNewsAdapter;

    List<NewsDoc> newsDocList;

    ProgressDialog pDialog;


    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerViewNews = (RecyclerView) findViewById(R.id.recyclerViewNews);

        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerViewNews.setHasFixedSize(true);

        recyclerViewNews.addItemDecoration(new VerticalSpaceItemDecoration(dpToPx(8)));

        newsDocList = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(R.layout.item_news, this, newsDocList);

        recyclerViewNews.setAdapter(mNewsAdapter);

        setupActionBar();

        getNewsResponse();

    }

    private void setupActionBar() {

        // getSupportActionBar().setTitle("Confirmation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void getNewsResponse() {


        showProgressDialog("Loading....");

        String url = "https://b18e8d85-eafc-4e77-90fa-5fda27fc9e64-bluemix.cloudant.com/parking_news/_all_docs?include_docs=true&limit=5&descending=true";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response: " + response.toString());

                        hideProgressDialog();
                        displayResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "askingielyinedualiestion:10782ced98dfc2828d377b7db296e682d88b2d21";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }


    private void displayResponse(JSONObject jsonObject) {

        List<NewsDoc> dataList = getNewsFromJson(jsonObject);

        if (dataList != null) {
            if (dataList.size() > 0) {
                newsDocList.clear();
                newsDocList.addAll(dataList);
                mNewsAdapter.notifyDataSetChanged();
            }
        }

    }


    private List<NewsDoc> getNewsFromJson(JSONObject jsonObject) {

        List<NewsDoc> newsDocList = new ArrayList<>();

        if (jsonObject != null) {

            if (jsonObject.has(Constants.NODE_ROWS)) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray(Constants.NODE_ROWS);

                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonNews = jsonArray.getJSONObject(i);
                            if (jsonNews != null) {

                                if (jsonNews.has(Constants.NODE_DOC)) {
                                    JSONObject jsonDoc = jsonNews.getJSONObject(Constants.NODE_DOC);
                                    if (jsonDoc != null) {

                                        NewsDoc newsDoc = new NewsDoc();

                                        if (jsonDoc.has(Constants.NODE_DATE)) {
                                            newsDoc.setDate(jsonDoc.getString(Constants.NODE_DATE));
                                        }
                                        if (jsonDoc.has(Constants.NODE_TITLE)) {
                                            newsDoc.setTitle(jsonDoc.getString(Constants.NODE_TITLE));
                                        }
                                        if (jsonDoc.has(Constants.NODE_DESCRIPTION)) {
                                            newsDoc.setDescription(jsonDoc.getString(Constants.NODE_DESCRIPTION));
                                        }

                                        newsDocList.add(newsDoc);


                                    }
                                }

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


        return newsDocList;

    }


    private void showProgressDialog(String message) {
        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
        }

        pDialog.setMessage(message);
        pDialog.show();

    }

    private void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
