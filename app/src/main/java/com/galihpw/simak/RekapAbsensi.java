package com.galihpw.simak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galihpw.simak.adapter.MhsAdapter;
import com.galihpw.simak.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RekapAbsensi extends AppCompatActivity {

    public Mhs[] mMhs;
    public int absensi[];
    public int banyakMhs, no=0;


    String kodeMatkul, dayName;
    ProgressDialog loading;
    TableLayout stk1;

    private static String url_gAllMhs = Config.URL + "getAbsensiMhs.php";
    private static String url_gAbsensi = Config.URL + "getAbsensi.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_absensi);

        stk1 = (TableLayout) findViewById(R.id.tabel_rekap);

        //ambil intent
        Intent intent2 = getIntent();
        //ambil datanya
        kodeMatkul = intent2.getStringExtra(MainActivity.MAIN_MESSAGE3);
        dayName = intent2.getStringExtra(MainActivity.MAIN_MESSAGE4);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        getDataMhs();
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

    private void getDataMhs() {
        loading = ProgressDialog.show(this,"Please wait...","Getting Data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gAllMhs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSONMhs(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(RekapAbsensi.this, "No Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_HARI, dayName);
                params.put(Config.KEY_KODE_MATKUL, "" + kodeMatkul);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONMhs(String response) {
        try {
            JSONArray result = new JSONArray(response);
            mMhs = new Mhs[result.length()];
            banyakMhs = result.length();

            // Parsing json
            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                Mhs data = new Mhs("" + Data.getString(Config.KEY_NAMA_MHS), "" + Data.getString(Config.KEY_NIM), "" + Data.getString(Config.KEY_KELAS), "" + Data.getString(Config.KEY_KONTAK_MHS), "" + Data.getString(Config.KEY_ALAMAT_MHS), "" + Data.getInt(Config.KEY_BINTANG));
                mMhs[i] = data;

                getAbsensiMhs(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loading.dismiss();
    }

    private void getAbsensiMhs(final Mhs dataMhs) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gAbsensi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSONAbsen(response, dataMhs);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(RekapAbsensi.this, "No Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_NIM, dataMhs.getNim());
                params.put(Config.KEY_KODE_MATKUL, "" + kodeMatkul);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONAbsen(String response, Mhs dataMhs) {
        int jumlah=0;
        no++;

        // tabel absensi
        TableRow tbrow1 = new TableRow(this);
        TextView tv = new TextView(this);
        tv.setText(""+no);
        tv.setTypeface(Typeface.create("casual", Typeface.NORMAL));
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setGravity(Gravity.CENTER);
        tbrow1.addView(tv);
        TextView tvq = new TextView(this);
        tvq.setText(dataMhs.getNama());
        tvq.setTypeface(Typeface.create("casual", Typeface.NORMAL));
        tvq.setBackgroundResource(R.drawable.cell_shape);
        tvq.setGravity(Gravity.CENTER);
        tbrow1.addView(tvq);

        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (30 * scale + 0.5f);

        TableLayout pert = new TableLayout(this);
        TableRow tbrowP = new TableRow(this);

        try {
            JSONArray result = new JSONArray(response);
            absensi = new int[17];

            // inisiasi array
            for(int b = 0; b<16; b++) {
                absensi[b] = 0;
            }

            // Parsing json
            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                int x = Integer.valueOf(Data.getString(Config.KEY_PERTEMUAN));
                absensi[x-1] = x;
                jumlah++;

            }

            for(int b = 0; b<16; b++) {
                TextView tv1 = new TextView(this);
                tv1.setBackgroundResource(R.drawable.cell_shape);

                //int x = i - 1;
                if (absensi[b] != 0) {
                    tv1.setBackgroundResource(R.drawable.cell_shape_absen);
                }
                //tv1.setText("" + i);
                tv1.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tv1.setWidth(pixels);
                tv1.setGravity(Gravity.CENTER);
                tbrowP.addView(tv1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pert.addView(tbrowP);
        tbrow1.addView(pert);

        TextView tvj = new TextView(this);
        tvj.setText(""+jumlah);
        tvj.setTypeface(Typeface.create("casual", Typeface.NORMAL));
        tvj.setBackgroundResource(R.drawable.cell_shape);
        tvj.setGravity(Gravity.CENTER);
        tbrow1.addView(tvj);

        stk1.addView(tbrow1);
    }

    public void init(int j) {
        //for (int j = 0; j < banyakMhs; j++) {
            TableRow tbrow1 = new TableRow(this);
            TextView tv = new TextView(this);
            tv.setText("" + (j + 1));
            tv.setTypeface(Typeface.create("casual", Typeface.NORMAL));
            tv.setBackgroundResource(R.drawable.cell_shape);
            tv.setGravity(Gravity.CENTER);
            tbrow1.addView(tv);
            TextView tvq = new TextView(this);
            tvq.setText(mMhs[j].getNama());
            tvq.setTypeface(Typeface.create("casual", Typeface.NORMAL));
            tvq.setBackgroundResource(R.drawable.cell_shape);
            tvq.setGravity(Gravity.CENTER);
            tbrow1.addView(tvq);

            final float scale = this.getResources().getDisplayMetrics().density;
            int pixels = (int) (30 * scale + 0.5f);

            TableLayout pert = new TableLayout(this);
            TableRow tbrowP = new TableRow(this);
            for (int i = 1; i <= 16; i++) {
                TextView tv1 = new TextView(this);
                tv1.setBackgroundResource(R.drawable.cell_shape);

                int x = i - 1;
                if (absensi[x] == i) {
                    tv1.setBackgroundResource(R.drawable.cell_shape_absen);
                }
                //tv1.setText("" + i);
                tv1.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                tv1.setWidth(pixels);
                tv1.setGravity(Gravity.CENTER);
                tbrowP.addView(tv1);
            }
            pert.addView(tbrowP);
            tbrow1.addView(pert);

            TextView tvj = new TextView(this);
            tvj.setText("1");
            tvj.setTypeface(Typeface.create("casual", Typeface.NORMAL));
            tvj.setBackgroundResource(R.drawable.cell_shape);
            tvj.setGravity(Gravity.CENTER);
            tbrow1.addView(tvj);

            stk1.addView(tbrow1);
        }
    //}
}
