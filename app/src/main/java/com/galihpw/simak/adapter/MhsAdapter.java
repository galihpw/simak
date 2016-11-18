package com.galihpw.simak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galihpw.simak.Mhs;
import com.galihpw.simak.R;

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
}
