package com.galihpw.simak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galihpw.simak.config.Config;
import com.galihpw.simak.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.galihpw.simak.config.Config.KEY_NIP;
import static com.galihpw.simak.config.Config.KEY_PHOTO;

public class ProfileDosen extends AppCompatActivity {

    CircleImageView imgProfile;
    TextView vNip, vNama, vAlamat, vKontak, vEmail, vFB, vTW;
    EditText edAlamat, edKontak, edEmail, edFB, edTW;
    String sNip, sNama, sAlamat, sKontak, sEmail, sFB, sTW, nip;

    static final int REQUEST_CAMERA = 0;
    static final int SELECT_FILE = 1;
    private String userChoosenTask;
    Bitmap bitmap;

    private static String url_gDosen 	 = Config.URL + "getDosen.php";
    private static String url_uDosen 	 = Config.URL + "updateDosen.php";
    private static String UPLOAD_URL     = Config.URL + "uploadPhotoDosen.php";
    ProgressDialog loading;

    String[] value = new String[]{
            "Choose from Gallery",
            "Take Photo",
            "Remove Profile Photo"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_dosen);

        //ambil intent
        Intent intent2 = getIntent();
        //ambil datanya
        nip = intent2.getStringExtra(MainActivity.MAIN_MESSAGE);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        vNip = (TextView) findViewById(R.id.view_nip);
        vNama = (TextView) findViewById(R.id.view_nama);
        vAlamat = (TextView) findViewById(R.id.view_alamat);
        vKontak = (TextView) findViewById(R.id.view_kontak);
        vEmail = (TextView) findViewById(R.id.view_email);
        vFB = (TextView) findViewById(R.id.view_facebook);
        vTW = (TextView) findViewById(R.id.view_twitter);

        edAlamat = (EditText) findViewById(R.id.edit_alamat);
        edKontak = (EditText) findViewById(R.id.edit_kontak);
        edEmail = (EditText) findViewById(R.id.edit_email);
        edFB = (EditText) findViewById(R.id.edit_facebook);
        edTW = (EditText) findViewById(R.id.edit_twitter);


        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgProfile.setImageResource(R.drawable.default_profile);

        loading = ProgressDialog.show(this,"Please wait...","Getting Data...",false,false);
        getPhoto();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhoto();
            }
        });

        Button editProf = (Button) findViewById(R.id.editProf);
        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProf();
            }
        });

        Button saveProf = (Button) findViewById(R.id.simpProf);
        saveProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProf();
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

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    public void dialogPhoto(){
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(ProfileDosen.this);

        alertdialogbuilder.setTitle("Profile Picture");

        alertdialogbuilder.setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
            boolean result=Utility.checkPermission(ProfileDosen.this);

            switch (value[item]) {
                case "Choose from Gallery":
                    Toast.makeText(ProfileDosen.this, "Select Photo", Toast.LENGTH_SHORT).show();
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                    break;
                case "Take Photo":
                    Toast.makeText(ProfileDosen.this, "Take Photo", Toast.LENGTH_SHORT).show();
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                    break;
                case "Remove Profile Photo":
                    Toast.makeText(ProfileDosen.this, "Photo Removed", Toast.LENGTH_SHORT).show();
                    imgProfile.setImageResource(R.drawable.default_profile);
                    break;
            }

            }
        });

        AlertDialog dialog = alertdialogbuilder.create();

        dialog.show();
    }

    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uploadPhoto();
        //imgProfile.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmap=null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        uploadPhoto();
        //imgProfile.setImageBitmap(bm);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadPhoto(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(ProfileDosen.this, s , Toast.LENGTH_LONG).show();
                        //Get photo from database
                        getPhoto();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(ProfileDosen.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String photo = getStringImage(bitmap);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_PHOTO, photo);
                params.put(KEY_NIP, nip);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getPhoto(){
        String url = ""+Config.URL+"photo/"+nip+".png";

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imgProfile.setImageBitmap(bitmap);
                        getData();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imgProfile.setImageResource(R.drawable.default_profile);
                        getData();
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gDosen, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileDosen.this,"No Connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(KEY_NIP, nip);

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
            sNip = Data.getString(KEY_NIP);
            sNama = Data.getString(Config.KEY_NAMA_DOSEN);
            sAlamat = Data.getString(Config.KEY_ALAMAT_DOSEN);
            sKontak = Data.getString(Config.KEY_KONTAK_DOSEN);
            sEmail = Data.getString(Config.KEY_EMAIL_DOSEN);
            sFB = Data.getString(Config.KEY_FB_DOSEN);
            sTW = Data.getString(Config.KEY_TW_DOSEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        vNip.setText(sNip);
        vNama.setText(sNama);
        vAlamat.setText(sAlamat);
        vKontak.setText(sKontak);
        vEmail.setText(sEmail);
        vFB.setText(sFB);
        vTW.setText(sTW);
    }

    public void editProf(){
        edAlamat.setText(vAlamat.getText().toString());
        edKontak.setText(vKontak.getText().toString());
        edEmail.setText(vEmail.getText().toString());
        edFB.setText(vFB.getText().toString());
        edTW.setText(vTW.getText().toString());

        switcher();
    }

    public void saveProf(){
        vAlamat.setText(edAlamat.getText().toString());
        vKontak.setText(edKontak.getText().toString());
        vEmail.setText(edEmail.getText().toString());
        vFB.setText(edFB.getText().toString());
        vTW.setText(edTW.getText().toString());

        updateData();
    }

    public void switcher(){
        ViewSwitcher viewSwitcher0 =   (ViewSwitcher)findViewById(R.id.alamat);
        ViewSwitcher viewSwitcher1 =   (ViewSwitcher)findViewById(R.id.noKontak);
        ViewSwitcher viewSwitcher2 =   (ViewSwitcher)findViewById(R.id.email);
        ViewSwitcher viewSwitcher3 =   (ViewSwitcher)findViewById(R.id.facebook);
        ViewSwitcher viewSwitcher4 =   (ViewSwitcher)findViewById(R.id.twitter);
        ViewSwitcher viewSwitcher5 =   (ViewSwitcher)findViewById(R.id.button);

        View myFirstView = findViewById(R.id.view_kontak);
        View mySecondView = findViewById(R.id.edit_kontak);

        // TODO Auto-generated method stub
        if (viewSwitcher1.getCurrentView() != myFirstView){

            viewSwitcher0.showPrevious();
            viewSwitcher1.showPrevious();
            viewSwitcher2.showPrevious();
            viewSwitcher3.showPrevious();
            viewSwitcher4.showPrevious();
            viewSwitcher5.showPrevious();
        } else if (viewSwitcher1.getCurrentView() != mySecondView){

            viewSwitcher0.showNext();
            viewSwitcher1.showNext();
            viewSwitcher2.showNext();
            viewSwitcher3.showNext();
            viewSwitcher4.showNext();
            viewSwitcher5.showNext();
        }
    }

    private void updateData() {
        loading = ProgressDialog.show(this,"Please wait...","Updating Data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uDosen, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.LOGIN_SUCCESS);

                    if (success == 1) {
                        loading.dismiss();
                        getData();
                        switcher();
                        //If the server response is success
                        //Displaying an message on toast
                        Toast.makeText(ProfileDosen.this, "Success", Toast.LENGTH_LONG).show();
                    } else {
                        loading.dismiss();
                        //getDataMhs();
                        //If the server response is not success
                        //Displaying an error message on toast
                        Toast.makeText(ProfileDosen.this, "Data not Updated", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ProfileDosen.this,"No Connection",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(KEY_NIP, nip);
                params.put(Config.KEY_ALAMAT_DOSEN, edAlamat.getText().toString());
                params.put(Config.KEY_KONTAK_DOSEN, edKontak.getText().toString());
                params.put(Config.KEY_EMAIL_DOSEN, edEmail.getText().toString());
                params.put(Config.KEY_FB_DOSEN, edFB.getText().toString());
                params.put(Config.KEY_TW_DOSEN, edTW.getText().toString());

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
