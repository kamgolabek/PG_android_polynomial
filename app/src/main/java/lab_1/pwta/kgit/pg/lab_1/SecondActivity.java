package lab_1.pwta.kgit.pg.lab_1;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends Activity {


    double a = 0;
    double b = 0;
    double c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle bndl = getIntent().getExtras();
        a = bndl.getDouble("a");
        b = bndl.getDouble("b");
        c = bndl.getDouble("c");

        System.out.println("got params: " + a + "," + b + "," + c);


        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        final CustomView cv = findViewById(R.id.customView);
        cv.setPolynomialValues(a, b, c);
        cv.drawParabola(miejscaZeroweX1(), miejscaZeroweX2(), w1(), w2());

        Button btnSaveImage = findViewById(R.id.btnSaveImage);
        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomView cv = findViewById(R.id.customView);
                System.out.println("save image to: " + saveToInternalStorage(getBitmapFromView(cv)));
            }
        });

    }

    private String saveToInternalStorage(Bitmap bitmapImage) {

        FileOutputStream fos = null;
        try {
            File fex2 = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            fex2 = new File(fex2, "SCREEN_LAB1.jpg");

            System.out.println("file path: " + fex2.getAbsolutePath());
            fos = new FileOutputStream(fex2);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(this, "Image saved in : " + fex2.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public void finishActivity() {
        Intent data = new Intent();
        data.putExtra("x0", miejscaZeroweX1());
        data.putExtra("x1", miejscaZeroweX2());
        setResult(Activity.RESULT_OK, data);
        finish();
    }


    public double w1() {
        return -(b / (2 * a));
    }

    public double w2() {
        return -(delta() / (4 * a));
    }

    public double delta() {
        return (b * b) - (4 * a * c);
    }

    public double miejscaZeroweX1() {
        return (-b - Math.sqrt(delta())) / (2 * a);
    }


    public double miejscaZeroweX2() {
        return (-b + Math.sqrt(delta())) / (2 * a);
    }
}
