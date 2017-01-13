package com.galihpw.simak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
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
import com.galihpw.simak.adapter.AdapterKom;
import com.galihpw.simak.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.galihpw.simak.ForumActivity.FORUM_MESSAGE1;
import static com.galihpw.simak.ForumActivity.FORUM_MESSAGE2;
import static com.galihpw.simak.ForumActivity.FORUM_MESSAGE3;
import static com.galihpw.simak.ForumActivity.FORUM_MESSAGE4;
import static com.galihpw.simak.ForumActivity.FORUM_MESSAGE5;

/**
 * Created by Nada on 12/26/2016.
 */

public class IsiForum extends AppCompatActivity {

    String sJudulForum, sIsiForum, sNim, sIdTopik, sNama;
    TextView tvJudulForum, tvIsiForum, tvNamaForum, tvNamaKomentar, tvIsiKomentar;
    EditText edKomentar;
    Dialog dia;
    ProgressDialog loadingKomentar;

    ListView listKomentar;
    private Komentar[] mKomentar;

    private static String url_iKomentar = Config.URL + "insertKomentar.php";
    private static String url_gKomentar = Config.URL + "getKomentar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listtopik);

        tvJudulForum = (TextView) findViewById(R.id.judulforum);
        tvIsiForum = (TextView) findViewById(R.id.isiforum);
        tvNamaForum = (TextView) findViewById(R.id.namaforum);
        tvNamaKomentar = (TextView) findViewById(R.id.namakom);
        tvIsiKomentar = (TextView) findViewById(R.id.isikom);

        Intent intent = getIntent();
        sJudulForum = intent.getStringExtra(FORUM_MESSAGE1);
        sIsiForum = intent.getStringExtra(FORUM_MESSAGE2);
        sNim = intent.getStringExtra(FORUM_MESSAGE3);
        sIdTopik = intent.getStringExtra(FORUM_MESSAGE4);
        sNama = intent.getStringExtra(FORUM_MESSAGE5);

        tvJudulForum.setText(sJudulForum);
        tvIsiForum.setText(sIsiForum);
        tvNamaForum.setText(sNama);

        getDataKomentar();
    }

    private void getDataKomentar(){
        loadingKomentar = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gKomentar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingKomentar.dismiss();
                showJSONForum(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingKomentar.dismiss();
                Toast.makeText(IsiForum.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_ID_TOPIK, sIdTopik);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONForum(String response){
        listKomentar = (ListView) findViewById(R.id.listKomentar);
        try{
            JSONArray result = new JSONArray(response);
            mKomentar = new Komentar[result.length()];
            for(int i = 0;i < result.length();i++){
                JSONObject Data = result.getJSONObject(i);
                Komentar data = new Komentar("" + Data.getString(Config.KEY_NAMA_MHS), "" + Data.getString(Config.KEY_ISI_KOMENTAR));
                mKomentar[i] = data;
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

        AdapterKom adapter = new AdapterKom(IsiForum.this, mKomentar);
        listKomentar.setAdapter(adapter);
    }

}
