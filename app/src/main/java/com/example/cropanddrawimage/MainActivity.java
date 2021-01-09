package com.example.cropanddrawimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private Button buttonCallImage, buttonSave;
    private ImageView imageViewGelenImage;
    static final int request = 1;
    private Uri imgUri;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private int px,py;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCallImage = findViewById(R.id.buttonCallImage);
        buttonSave = findViewById(R.id.buttonSave);
        imageViewGelenImage = findViewById(R.id.imageViewGelenImage);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);

        buttonCallImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCallImage = new Intent(MainActivity.this,CropActivity.class);
                startActivityForResult(intentCallImage,request);
            }
        });

        imageViewGelenImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        px = x;
                        py = y;
                        drawImage((ImageView) v, bitmap, px, py, x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        drawImage((ImageView) v, bitmap, px, py, x, y);
                        px = x;
                        py = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        drawImage((ImageView) v, bitmap, px, py, x, y);
                        break;
                }

                return true;
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void drawImage(ImageView imgView, Bitmap bitmap,float x0,float y0,float x, float y){
        if(x<0 || y<0 || x > imgView.getWidth() || y > imgView.getHeight()){
            //outside ImageView
            return;
        }else{

            float ratioWidth = (float)bitmap.getWidth()/(float)imgView.getWidth();
            float ratioHeight = (float)bitmap.getHeight()/(float)imgView.getHeight();

            canvas.drawLine(
                    x0 * ratioWidth,
                    y0 * ratioHeight,
                    x * ratioWidth,
                    y * ratioHeight,
                    paint);
            imageViewGelenImage.invalidate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap tempbitmap;

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case request:
                    buttonCallImage.setVisibility(View.GONE);
                    buttonSave.setVisibility(View.VISIBLE);


                    String strUri = data.getStringExtra("uri");
                    imgUri = Uri.parse(strUri);

                    try {
                        tempbitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));


                        Bitmap.Config config;
                        if (tempbitmap.getConfig() != null){
                            config = tempbitmap.getConfig();

                        }else {
                            config = Bitmap.Config.ARGB_8888;
                        }

                        bitmap = Bitmap.createBitmap(tempbitmap.getWidth(), tempbitmap.getHeight(), config);

                        canvas = new Canvas(bitmap);
                        canvas.drawBitmap(tempbitmap, 0, 0, null);
                        imageViewGelenImage.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }



            }
        }

    }
}
