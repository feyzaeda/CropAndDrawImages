package com.example.cropanddrawimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

public class CropActivity extends AppCompatActivity {

    private Button buttonGoruntuEkle,buttonBack;
    private ImageView imgGoruntu;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        buttonGoruntuEkle = findViewById(R.id.buttonGoruntuEkleCrop);
        buttonBack = findViewById(R.id.buttonBackParentActivity);
        imgGoruntu = findViewById(R.id.imageViewGoruntuCrop);

        buttonGoruntuEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(CropActivity.this);


            }
        });






    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){

                uri = result.getUri();
                imgGoruntu.setImageURI(uri);
                buttonBack.setVisibility(View.VISIBLE);
                buttonBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        String strUri = uri.toString();
                        returnIntent.putExtra("uri",strUri);
                        setResult(RESULT_OK,returnIntent);
                        finish();
                    }
                });



            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "Eror is:" + e, Toast.LENGTH_LONG).show();

            }

        }
    }



}
