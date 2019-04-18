package com.example.imagevolly;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private Button Choosebt,Uploadbt;
    private ImageView imageView;
    private EditText name;
    private int IMAGE_REQUEST=1;
    private Bitmap bitmap;
    private String upload_url="http://192.168.1.8/updateinfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Choosebt=(Button)findViewById(R.id.choosebtn);
        Uploadbt=(Button)findViewById(R.id.uploadbtn);
        imageView=(ImageView) findViewById(R.id.imageView);
        name=(EditText)findViewById(R.id.name);
        Choosebt.setOnClickListener(this);
        Uploadbt.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.choosebtn:
               selectimage();
               break;
           case R.id.uploadbtn:
               uploadimage();
               break;
       }
    }
    private void selectimage(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private void uploadimage(){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, upload_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =new JSONObject(response);

                            Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                            imageView.setImageResource(0);
                            imageView.setVisibility(View.GONE);
                            name.setText("");
                            name.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("name",name.getText().toString().trim());
                params.put("image",imagetostring(bitmap));

                return params;
            }
        };
        MySingleTon.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    private  String imagetostring(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbyte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte,Base64.DEFAULT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK &&data !=null){
            Uri path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
