package lab_1.pwta.kgit.pg.lab_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int WIELOMIAN_LICZ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button liczBtn = findViewById(R.id.btnLicz);

        liczBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obliczWielomian();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WIELOMIAN_LICZ) {
            if (resultCode == RESULT_OK) {
                Double x0 = data.getDoubleExtra("x0", -1);
                Double x1 = data.getDoubleExtra("x1", -1);
                System.out.println("got result: " + x0 + " , " + x1);
                Toast.makeText(this, "Miejsca zerowe. x0: " + x0 + " ,  x1: " + x1, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void obliczWielomian() {
        EditText a = findViewById(R.id.nmbrA);
        EditText b = findViewById(R.id.nmbrB);
        EditText c = findViewById(R.id.nmbrC);


        Intent intent = new Intent(this, SecondActivity.class);
        Bundle bndl = new Bundle();
        bndl.putDouble("a", Double.valueOf(a.getText().toString()));
        bndl.putDouble("b", Double.valueOf(b.getText().toString()));
        bndl.putDouble("c", Double.valueOf(c.getText().toString()));
        intent.putExtras(bndl);
        startActivityForResult(intent, WIELOMIAN_LICZ);
    }
}
