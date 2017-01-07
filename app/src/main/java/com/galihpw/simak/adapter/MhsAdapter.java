package com.galihpw.simak.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.galihpw.simak.Mhs;
import com.galihpw.simak.R;
import com.galihpw.simak.config.Config;

/**
 * Created by ACER on 17/09/2016.
 */
public class MhsAdapter extends BaseAdapter {

    private Context mContext;

    private Mhs[] mMhs;

    public MhsAdapter(Context Context, Mhs[] Mhs){
        mContext = Context;
        mMhs = Mhs;
    }

    @Override
    public int getCount() {
        return mMhs.length;
    }

    @Override
    public Object getItem(int position) {
        return mMhs[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mhs, null);
            viewHolder = new ViewHolder();
            viewHolder.foto = (ImageView)convertView.findViewById(R.id.foto);
            viewHolder.nama = (TextView)convertView.findViewById(R.id.nama);
            viewHolder.nim = (TextView)convertView.findViewById(R.id.nim);
            viewHolder.kelas = (TextView)convertView.findViewById(R.id.kelas);
            viewHolder.noKontak = (TextView)convertView.findViewById(R.id.noKontak);
            viewHolder.alamat = (TextView)convertView.findViewById(R.id.alamat);
            viewHolder.bintang = (TextView)convertView.findViewById(R.id.jBintang);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Mhs mhs = mMhs[position];
        getPhoto(mhs, viewHolder);
        viewHolder.nama.setText(mhs.getNama() + "");
        viewHolder.nim.setText(mhs.getNim() + "");
        viewHolder.bintang.setText(mhs.getBintang() + "");

        return convertView;
    }

    private static class ViewHolder{
        ImageView foto;
        TextView nama;
        TextView nim;
        TextView kelas;
        TextView noKontak;
        TextView alamat;
        TextView bintang;
    }

    private void getPhoto(Mhs dataMhs, final ViewHolder v){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        String url = ""+ Config.URL+"photo/"+dataMhs.getNim()+".png";

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        v.foto.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        v.foto.setImageResource(R.drawable.default_profile);
                        v.foto.setColorFilter(filter);
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }
}
