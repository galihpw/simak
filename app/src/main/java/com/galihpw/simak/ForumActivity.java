package com.galihpw.simak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galihpw.simak.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ForumActivity extends AppCompatActivity{

    Calendar calendar;
    TextView tvHariTglForum, vMatkulForum, vKodeMatkulForum;
    String dayName, sMatkulForum, sKodeMatkulForum;
    ProgressDialog loadingForum;

    public final static String FORUM_MESSAGE1 = "com.galihpw.judulforum";
    public final static String FORUM_MESSAGE2 = "com.galihpw.isiforum";
    public final static String FORUM_MESSAGE3 = "com.galihpw.nimforum";
    public final static String FORUM_MESSAGE4 = "com.galihpw.idtopik";
    public final static String FORUM_MESSAGE5 = "com.galihpw.nama";

    ListView listForum;
    ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private Topik[] mTopik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listForum = (ListView) findViewById(R.id.listviewForum);


        Intent intent = getIntent();
        sMatkulForum = intent.getStringExtra(MainActivity.MAIN_MESSAGE2);
        sKodeMatkulForum = intent.getStringExtra(MainActivity.MAIN_MESSAGE3);

        vMatkulForum = (TextView) findViewById(R.id.tvMatkulForum);
        vKodeMatkulForum = (TextView) findViewById(R.id.tvKodeMatkulForum);

        tvHariTglForum = (TextView) findViewById(R.id.tvHariTglForum);
        calendar = Calendar.getInstance();
        SimpleDateFormat adf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String currentDate = adf.format(calendar.getTime());

        SimpleDateFormat adf_ = new SimpleDateFormat("EEEE", Locale.US);
        Date date = new Date();
        dayName = adf_.format(date);
        switch(dayName){
            case "Monday":
                dayName = "Senin";
                break;
            case "Tuesday":
                dayName = "Selasa";
                break;
            case "Wednesday":
                dayName = "Rabu";
                break;
            case "Thursday":
                dayName = "Kamis";
                break;
            case "Friday":
                dayName = "Jumat";
                break;
            case "Saturday":
                dayName = "Sabtu";
                break;
            case "Sunday":
                dayName = "Minggu";
                break;
        }
        tvHariTglForum.setText("" + dayName + ", " + currentDate + "");
        vMatkulForum.setText(sMatkulForum);
        vKodeMatkulForum.setText("(" + sKodeMatkulForum + ")");

        getDataForum();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDataForum(){
        loadingForum = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url_gTopik = Config.URL + "getTopik.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gTopik, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingForum.dismiss();
                showJSONForum(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingForum.dismiss();
                Toast.makeText(ForumActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_KODE_MATKUL, sKodeMatkulForum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONForum(String response){
        try{
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            mTopik = new Topik[result.length()];
            for(int i = 0;i < result.length();i++){
                JSONObject Data = result.getJSONObject(i);
                Topik data = new Topik("" + Data.getString(Config.KEY_JUDUL), "" + Data.getString(Config.KEY_ISITOPIK));
                mTopik[i] = data;

                items.add("Cek: " + mTopik[i].getJudulForum());
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);

        listForum.setAdapter(adapter);
        listForum.setClickable(true);
        listForum.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ForumActivity.this , IsiForum.class );
                intent.putExtra(FORUM_MESSAGE1, "" + mTopik[position].getJudulForum());
                intent.putExtra(FORUM_MESSAGE2, "" + mTopik[position].getIsiForum());
                startActivity(intent);
            }
        });
    }

}