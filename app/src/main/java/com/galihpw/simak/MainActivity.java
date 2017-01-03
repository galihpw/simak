package com.galihpw.simak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
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
import com.android.volley.toolbox.ImageRequest;
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
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private Mhs[] mMhs;
    Dialog dia;
    String nip, sNama, dayName, kodeMatkul, namaMatkul, waktuMulai, waktuSelesai;
    ProgressDialog loading;
    Calendar calendar;
    TextView vHariTgl;
    int n, status;

    //konstanta, supaya bisa membedakan antar message
    public final static String MAIN_MESSAGE = "com.galihpw.NIP";
    public final static String MAIN_MESSAGE2 = "com.galihpw.Matkul";
    public final static String MAIN_MESSAGE3 = "com.galihpw.KoMatkul";
    public final static String MAIN_MESSAGE4 = "com.galihpw.dayName";

    private static String url_gDosen = Config.URL + "getDosen.php";
    private static String url_gAllMhs = Config.URL + "getAllMhs.php";
    private static String url_gJadwal = Config.URL + "getJadwal.php";
    private static String url_uBintang = Config.URL + "updateBintang.php";
    private static String url_uPertemuan = Config.URL + "updateMatkul.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vHariTgl = (TextView) findViewById(R.id.vHariTgl);

        calendar = Calendar.getInstance();
        //date format is:  "Date-Month-Year Hour:Minutes am/pm"
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US); //Date
        String currentDate = sdf.format(calendar.getTime());

        //Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE", Locale.US);
        Date date = new Date();
        dayName = sdf_.format(date);
        switch (dayName) {
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
        vHariTgl.setText("" + dayName + ", " + currentDate + "");

        //ambil intent
        Intent intent2 = getIntent();
        //ambil datanya
        nip = intent2.getStringExtra(LoginActivity.LOGIN_MESSAGE);
        status = Integer.valueOf(intent2.getStringExtra(LoginActivity.LOGIN_MESSAGE1));

        //get data
        loading = ProgressDialog.show(this, "Please wait...", "Getting Data1...", false, false);
        getJadwal();
    }

    //method untuk membuat halaman menjadi dialog
    public void cobaCustomDialog(final Mhs dataMhs) {
        dia = new Dialog(MainActivity.this);
        dia.setContentView(R.layout.profil);

        final CircleImageView foto = (CircleImageView) dia.findViewById(R.id.fotofil);
        TextView nama = (TextView) dia.findViewById(R.id.nama);
        TextView nim = (TextView) dia.findViewById(R.id.nim);
        TextView kelas = (TextView) dia.findViewById(R.id.kelas);
        TextView noKontak = (TextView) dia.findViewById(R.id.noKontak);
        TextView alamat = (TextView) dia.findViewById(R.id.alamat);
        TextView bintang = (TextView) dia.findViewById(R.id.jBintang);

        //Get Photo Mahasiswa
        String url = ""+ Config.URL+"photo/"+dataMhs.getNim()+".png";

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        foto.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        foto.setImageResource(R.drawable.default_profile);
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

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
    private void logout() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();

                return true;
            case R.id.profile:
                //Starting profile activity
                Intent intent = new Intent(MainActivity.this, ProfileDosen.class);
                intent.putExtra(MAIN_MESSAGE, nip);
                startActivity(intent);

                return true;
            case R.id.forum:
                Intent intent2 = new Intent(MainActivity.this, ForumActivity.class);
                intent2.putExtra(MAIN_MESSAGE2, namaMatkul);
                intent2.putExtra(MAIN_MESSAGE3, kodeMatkul);
                startActivity(intent2);

            return true;
            case R.id.absensi:
                Intent intent3 = new Intent(MainActivity.this, RekapAbsensi.class);
                intent3.putExtra(MAIN_MESSAGE3, kodeMatkul);
                intent3.putExtra(MAIN_MESSAGE4, dayName);
                startActivity(intent3);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getJadwal() {
        //loading = ProgressDialog.show(this, "Please wait...", "Getting Data1...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gJadwal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSONJadwal(response);
                getData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_NIP, nip);
                params.put(Config.KEY_HARI, dayName);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONJadwal(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject Data = result.getJSONObject(0);
            kodeMatkul = Data.getString(Config.KEY_KODE_MATKUL);
            namaMatkul = Data.getString(Config.KEY_NAMA_MATKUL);
            waktuMulai = Data.getString(Config.KEY_WAKTU_MULAI);
            waktuSelesai = Data.getString(Config.KEY_WAKTU_SELESAI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView namaMat = (TextView) findViewById(R.id.tvMatkul);
        namaMat.setText("Mata Kuliah : " + namaMatkul);

        TextView kodeMat = (TextView) findViewById(R.id.tvKode);
        kodeMat.setText("Kode : " + kodeMatkul);

        compareDates();
        //Toast.makeText(this, "Masuk->" + waktuMulai + " | Keluar->" + waktuSelesai, Toast.LENGTH_SHORT).show();
    }

    private void getData() {
        //loading = ProgressDialog.show(this,"Please wait...","Getting Data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gDosen, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSON(response);
                if(status == 0) {
                    updatePertemuan();
                }else{
                    getDataMhs();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
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

    private void showJSON(String response) {
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

    private void updatePertemuan() {
        //loading = ProgressDialog.show(this, "Please wait...", "Updating Data...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uPertemuan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.LOGIN_SUCCESS);

                    if (success == 1) {
                        //loading.dismiss();
                        getDataMhs();
                        //If the server response is success
                        //Displaying an message on toast
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                    } else {
                        //loading.dismiss();
                        //If the server response is not success
                        //Displaying an error message on toast
                        Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_KODE_MATKUL, "" + kodeMatkul);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDataMhs() {
        //loading = ProgressDialog.show(this,"Please wait...","Getting Data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gAllMhs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.v("tes", response);
                showJSONMhs(response);
                //getDataMhs();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_LONG).show();
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
        GridView gridview = (GridView) findViewById(R.id.gridview);
        try {
            JSONArray result = new JSONArray(response);
            mMhs = new Mhs[result.length()];

            TextView jmlhMhs = (TextView) findViewById(R.id.tvJmlhMhs);
            jmlhMhs.setText("Jumlah Mahasiswa : " + result.length());

            // Parsing json
            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                Mhs data = new Mhs("" + Data.getString(Config.KEY_NAMA_MHS), "" + Data.getString(Config.KEY_NIM), "" + Data.getString(Config.KEY_KELAS), "" + Data.getString(Config.KEY_KONTAK_MHS), "" + Data.getString(Config.KEY_ALAMAT_MHS), "" + Data.getInt(Config.KEY_BINTANG));
                mMhs[i] = data;


                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Mhs dataMhs = mMhs[position];
                        cobaCustomDialog(dataMhs);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MhsAdapter adapter = new MhsAdapter(MainActivity.this, mMhs);
        gridview.setAdapter(adapter);
        loading.dismiss();
    }

    private void updateBintang(final Mhs dataMhs) {
        loading = ProgressDialog.show(this, "Please wait...", "Updating Data...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uBintang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.LOGIN_SUCCESS);

                    if (success == 1) {
                        //loading.dismiss();
                        status = 1;
                        getDataMhs();
                        //If the server response is success
                        //Displaying an message on toast
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                    } else {
                        loading.dismiss();
                        //getDataMhs();
                        //If the server response is not success
                        //Displaying an error message on toast
                        Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_NIM, dataMhs.getNim());
                params.put(Config.KEY_KODE_MATKUL, kodeMatkul);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Compare Time
    public static final String inputFormat = "HH:mm";

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    private void compareDates(){
        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);

        Date date = parseDate(hour + ":" + minute);
        Date dateCompareOne = parseDate(waktuMulai);
        Date dateCompareTwo = parseDate(waktuSelesai);

        if(dateCompareOne.before(date) && dateCompareTwo.after(date)) {
            //want to do
            Log.v("tes","masuk masuk masuk");
        }
    }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }
}

    // untuk menampilkan semua data pada gridview
    /*private void getDataMhs(){
        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_gAllMhs, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                GridView gridview = (GridView) findViewById(R.id.gridview);
                mMhs = new Mhs[response.length()];
                Toast.makeText(MainActivity.this, "" + response.length(), Toast.LENGTH_SHORT).show();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_HARI, "Selasa");

                //returning parameter
                return params;
            }
        };

        // menambah request ke request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jArr);
    }*/
