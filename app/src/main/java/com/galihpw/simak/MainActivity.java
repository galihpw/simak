package com.galihpw.simak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galihpw.simak.adapter.MhsAdapter;
import com.galihpw.simak.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Mhs[] mMhs;
    Dialog dia;
    int jbin[];
    String nip, sNama;
    ProgressDialog loading;
    Calendar calendar;
    TextView vHariTgl;
    int n;
    //int n;

    //konstanta, supaya bisa membedakan antar message
    public final static String EXTRA_MESSAGE = "com.galihpw.NIP";

    private static String url_gDosen 	 = Config.URL + "getDosen.php";
    private static String url_gAllMhs 	 = Config.URL + "getAllMhs.php";
    private static String url_uBintang 	 = Config.URL + "updateBintang.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vHariTgl = (TextView) findViewById(R.id.vHariTgl);

        calendar = Calendar.getInstance();
        //date format is:  "Date-Month-Year Hour:Minutes am/pm"
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); //Date
        String currentDate = sdf.format(calendar.getTime());

        //Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayName = sdf_.format(date);
        vHariTgl.setText("" + dayName + ", " + currentDate + "");

        //get data
        getData();
        getDataMhs();

        //ambil intent
        Intent intent2 = getIntent();
        //ambil datanya
        nip = intent2.getStringExtra(LoginActivity.EXTRA_MESSAGE);
    }

    //method untuk membuat halaman menjadi dialog
    public void cobaCustomDialog(final Mhs dataMhs){
        dia = new Dialog(MainActivity.this);
        dia.setContentView(R.layout.profil);

        TextView nama = (TextView) dia.findViewById(R.id.nama);
        TextView nim = (TextView) dia.findViewById(R.id.nim);
        TextView kelas = (TextView) dia.findViewById(R.id.kelas);
        TextView noKontak = (TextView) dia.findViewById(R.id.noKontak);
        TextView alamat = (TextView) dia.findViewById(R.id.alamat);
        TextView bintang = (TextView) dia.findViewById(R.id.jBintang);

        nama.setText(": " + dataMhs.getNama());
        nim.setText(": " + dataMhs.getNim());
        kelas.setText(": " + dataMhs.getKelas());
        noKontak.setText(": " + dataMhs.getNoKontak());
        alamat.setText(": " + dataMhs.getAlamat());
        bintang.setText(": " + dataMhs.getBintang());

        dia.setTitle("PROFIL");
        dia.setCancelable(true);
        dia.show();

        //memanggil button but yang ada pada dialog
        Button but = (Button) dia.findViewById(R.id.okecoy);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss(); //keluar dialog
            }
        });

        Button bin = (Button) dia.findViewById(R.id.bTambah);
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = dataMhs.getBintang();
                n = Integer.valueOf(m);
                n++;

                updateBintang(dataMhs);

                dia.dismiss();
            }
        });
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.NIP_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });

            alertDialogBuilder.setNegativeButton("No",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                logout();

                return true;
            case R.id.forum:
                Intent intent2 = new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent2);

                finish();
                return true;
            case R.id.profile:
                //Starting profile activity
                Intent intent = new Intent(MainActivity.this, ProfileDosen.class);
                intent.putExtra(EXTRA_MESSAGE, nip);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getData() {
        loading = ProgressDialog.show(this,"Please wait...","Getting Data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gDosen, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MainActivity.this,"No Connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_NIP, nip);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject Data = result.getJSONObject(0);
            sNama = Data.getString(Config.KEY_NAMA_DOSEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MenuView.ItemView menuTitle = ((MenuView.ItemView) findViewById(R.id.profile));
        menuTitle.setTitle(sNama);
    }

    // untuk menampilkan semua data pada gridview
    private void getDataMhs(){
        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_gAllMhs, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                GridView gridview = (GridView) findViewById(R.id.gridview);
                mMhs = new Mhs[15];
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject Data = response.getJSONObject(i);
                        Mhs data = new Mhs("" + Data.getString(Config.KEY_NAMA_MHS), "" + Data.getString(Config.KEY_NIM), "" + Data.getString(Config.KEY_KELAS), "" + Data.getString(Config.KEY_KONTAK_MHS), "" + Data.getString(Config.KEY_ALAMAT_MHS), "" + Data.getInt(Config.KEY_BINTANG));
                        mMhs[i] = data;

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Mhs dataMhs = mMhs[position];
                                cobaCustomDialog(dataMhs);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                MhsAdapter adapter = new MhsAdapter(MainActivity.this, mMhs);
                gridview.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MainActivity.this,"No Connection",Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jArr);
    }

    private void updateBintang(final Mhs dataMhs) {
        loading = ProgressDialog.show(this,"Please wait...","Updating Data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uBintang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                    loading.dismiss();
                    getDataMhs();
                    //If the server response is success
                    //Displaying an message on toast
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                }else{
                    loading.dismiss();
                    getDataMhs();
                    //If the server response is not success
                    //Displaying an error message on toast
                    Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MainActivity.this,"No Connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_NIM, dataMhs.getNim());
                params.put(Config.KEY_BINTANG, "" + n);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
