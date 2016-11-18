package com.galihpw.simak;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galihpw.simak.adapter.TpkAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ListView listTopik;
    EditText judul, deskripsi;
    TpkAdapter Adapter;
    SwipeRefreshLayout swipe;
    List<Topik> itemList = new ArrayList<Topik>();
    Dialog dia;
    public final static String EXTRA_MESSAGE = "com.galih.simak";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listTopik = (ListView) findViewById(R.id.lvTopik);
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        /*for(int i = 0; i < 15; i++){
            Topik data = new Topik(" ", " ", "  " + i);
            mTopik[i] = data;

            listTopik.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Topik dataMhs = mTopik[position];
                    Intent intent = new Intent(ForumActivity.this, IsiTopik.class);
                    intent.putExtra(EXTRA_MESSAGE,position);
                    startActivity(intent);
                }
            });
        }*/

        //set Adapter ke listview
        Adapter = new TpkAdapter(ForumActivity.this, itemList);
        listTopik.setAdapter(Adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           Adapter.notifyDataSetChanged();
                           //callVolley();
                       }
                   }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahTopik();
                Toast.makeText(ForumActivity.this, "Tambah Topik", Toast.LENGTH_SHORT).show();
            }
        });

        listTopik.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Topik dataMhs = mTopik[position];
                Intent intent = new Intent(ForumActivity.this, IsiTopik.class);
                intent.putExtra(EXTRA_MESSAGE,position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        Adapter.notifyDataSetChanged();
        //callVolley();
    }

    //Dialog untuk menambah Topik
    public void tambahTopik(){
        dia = new Dialog(ForumActivity.this);
        dia.setContentView(R.layout.tambah_topik);
        dia.setTitle("Tambah Topik");
        dia.setCancelable(false);
        dia.show();

        judul = (EditText) dia.findViewById(R.id.jdlTopik);
        deskripsi = (EditText) dia.findViewById(R.id.deskTopik);

        //memanggil button Simpan yang ada pada dialog
        Button buat = (Button) dia.findViewById(R.id.btnBuat);
        buat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itemList.clear();
                Adapter.notifyDataSetChanged();
                swipe.setRefreshing(true);

                //String desk = deskTopik.getText().toString();
                Toast.makeText(ForumActivity.this, judul.getText().toString(), Toast.LENGTH_SHORT).show();

                Topik data = new Topik();

                data.setJudul(judul.getText().toString());
                data.setDeskripsi(deskripsi.getText().toString());
                data.setPenanya("PENANYA");

                //menambah data ke array
                itemList.add(data);

                // notifikasi adanya perubahan data pada adapter
                Adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);

                dia.dismiss();
            }
        });

        //memanggil button Batal yang ada pada dialog
        Button batal = (Button) dia.findViewById(R.id.btnBatal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss(); //keluar dialog
            }
        });
    }

}