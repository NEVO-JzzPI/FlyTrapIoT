package com.example.flytrapiot;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditActivity extends AppCompatActivity {
    EditText input_codSector2, input_TipoTrampa2,input_NivelCapt2,input_Observaciones2;
    Button btn_editar;
    public int id_actual ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        input_codSector2 = findViewById(R.id.input_codSector2);
        input_TipoTrampa2 = findViewById(R.id.input_TipoTrampa2);
        input_NivelCapt2 = findViewById(R.id.input_NivelCapt2);
        input_Observaciones2 = findViewById(R.id.input_Observaciones2);
        btn_editar = findViewById(R.id.btn_editar);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarTrampa(id_actual);

        btn_editar.setOnClickListener(v -> {
            actualizarTrampa(id_actual);
        });
    }
    public void cargarTrampa(int id)
    {
        DBHelper admin = new DBHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor consulta = db.rawQuery("SELECT * FROM trampas WHERE id="+id, null);

        if(consulta.moveToFirst())
        {

            input_codSector2.setText(consulta.getString(1));
            input_TipoTrampa2.setText(consulta.getString(2));
            input_NivelCapt2.setText(consulta.getString(3));
            input_Observaciones2.setText(consulta.getString(4));
        }

        db.close();
        consulta.close();
    }

    public void actualizarTrampa(int id)
    {
        DBHelper admin = new DBHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        String codSector = input_codSector2.getText().toString();
        String tipoTrampa = input_TipoTrampa2.getText().toString();
        String nivelCapt = input_NivelCapt2.getText().toString();
        String observacion = input_Observaciones2.getText().toString();



        ContentValues valores = new ContentValues();
        valores.put("codigo_sector", codSector);
        valores.put("tipo_trampa", tipoTrampa);
        valores.put("nivel_captura", nivelCapt);
        valores.put("observaciones", observacion);

        db.update("trampas", valores, "id="+id, null);

        db.close();
        finish();
    }
}