package com.vbllopes.adminappherdeirosdofuturo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainEventActivity extends Activity {


    private DatabaseReference mDatabaseRef;

    public static final String FB_DATABASE_PATH = "event";
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    EditText eNome;
    Button bData;
    TextView tvData;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);
        data = null;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        eNome = (EditText) findViewById(R.id.eNome);
        bData = (Button) findViewById(R.id.btEscolher);
        tvData = (TextView) findViewById(R.id.tvData);
        //Colocar o DatePicker dentro do Botão
        bData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainEventActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth, mDataSetListener, dia,mes,ano);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        mDataSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                data = Integer.toString(dayOfMonth) + "/" + Integer.toString(month+1) + "/" +
                        Integer.toString(year);
                tvData.setText(getResources().getString(R.string.eDataEvento) + data);
            }
        };

    }





    public void btCadastrarEvento_onClick(View v){
        if(eNome != null && data != null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.eventoEnviando), Toast.LENGTH_SHORT).show();
            EventoUpload evento = new EventoUpload(eNome.getText().toString(), data);

            String uploadId = mDatabaseRef.push().getKey();
            mDatabaseRef.child(uploadId).setValue(evento);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.eventoEnviadoSucesso), Toast.LENGTH_SHORT).show();


        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.erroEvento), Toast.LENGTH_SHORT).show();
        }

    }
}
