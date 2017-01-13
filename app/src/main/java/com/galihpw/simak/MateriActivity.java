package com.galihpw.simak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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

public class MateriActivity extends AppCompatActivity {

    Calendar calendar;
    EditText etMateri, edtMateri;
    TextView tvHariTglMateri, vMatkulMateri, vKodeMatkulMateri;
    String dayName, sMatkulMateri, sKodeMatkulMateri, tampungMateri;
    ListView listMateri;
    ProgressDialog loading;
    Dialog dia;
    ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materi);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listMateri = (ListView) findViewById(R.id.listviewMateri);

        Intent intent = getIntent();
        sMatkulMateri = intent.getStringExtra(MainActivity.MAIN_MESSAGE2);
        sKodeMatkulMateri = intent.getStringExtra(MainActivity.MAIN_MESSAGE3);

        vMatkulMateri = (TextView) findViewById(R.id.tvMatkulMateri);
        vKodeMatkulMateri = (TextView) findViewById(R.id.tvKodeMatkulMateri);

        etMateri = (EditText) findViewById(R.id.edtMateri);

        tvHariTglMateri = (TextView) findViewById(R.id.tvHariTglMateri);
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
        tvHariTglMateri.setText("" + dayName + ", " + currentDate + "");
        vMatkulMateri.setText(sMatkulMateri);
        vKodeMatkulMateri.setText("(" + sKodeMatkulMateri + ")");

        getMateri();

        Button bTambahMateri = (Button) findViewById(R.id.bTambahMateri);
        bTambahMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertupdateMateri(0);
            }
        });
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

    private void getMateri(){
        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url_gMateri = Config.URL + "getMateri.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gMateri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSONForum(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MateriActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_KODE_MATKUL, sKodeMatkulMateri);
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
            final String[] isiMateri = new String[result.length()];
            final int[] pertemuan = new int[result.length()];
            for(int i = 0;i < result.length();i++){
                JSONObject Data = result.getJSONObject(i);
                isiMateri[i] = Data.getString(Config.KEY_ISI_MATERI);
                pertemuan[i] = Integer.valueOf(Data.getString(Config.KEY_PERTEMUAN));

                items.add("Materi Pertemuan Ke-" + pertemuan[i]);
                listMateri.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        dialogMateri(pertemuan[position], isiMateri[position]);
                    }
                });
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        listMateri.setAdapter(adapter);
        listMateri.setClickable(true);
    }

    private void insertupdateMateri(final int status) {
        String url_iMateri = null;

        if(status == 0){
            loading = ProgressDialog.show(this, "Please wait...", "Publishing...", false, false);
            url_iMateri = Config.URL + "insertMateri.php";
        }else if(status == 1){
            loading = ProgressDialog.show(this, "Please wait...", "Updating...", false, false);
            url_iMateri = Config.URL + "updateMateri.php";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_iMateri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.LOGIN_SUCCESS);

                    if (success == 1) {
                        loading.dismiss();

                        etMateri.setText("");
                        items.clear();
                        getMateri();
                        //If the server response is success
                        //Displaying an message on toast
                        Toast.makeText(MateriActivity.this, "Success", Toast.LENGTH_LONG).show();
                    } else {
                        loading.dismiss();
                        //If the server response is not success
                        //Displaying an error message on toast
                        Toast.makeText(MateriActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MateriActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                if(status == 0) {
                    params.put(Config.KEY_ISI_MATERI, "" + etMateri.getText().toString());
                    params.put(Config.KEY_KODE_MATKUL, "" + sKodeMatkulMateri);
                }else if(status == 1){
                    params.put(Config.KEY_ISI_MATERI, "" + tampungMateri);
                    params.put(Config.KEY_ISI_UPDATE_MATERI, "" + edtMateri.getText().toString());
                    params.put(Config.KEY_KODE_MATKUL, "" + sKodeMatkulMateri);
                }
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void dialogMateri(int i, final String materi) {
        dia = new Dialog(MateriActivity.this);
        dia.setContentView(R.layout.dialog_materi);
        dia.setTitle("Materi Pertemuan Ke-" + i);
        dia.setCancelable(true);

        edtMateri = (EditText) dia.findViewById(R.id.edtMateri);
        edtMateri.setText(materi);
        tampungMateri = materi;

        Button beMateri = (Button) dia.findViewById(R.id.bhMateri);
        beMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insertupdateMateri(1);
                dia.dismiss();
            }
        });

        dia.show();
    }
}
