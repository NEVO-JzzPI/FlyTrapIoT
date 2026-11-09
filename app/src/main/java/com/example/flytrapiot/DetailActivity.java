package com.example.flytrapiot;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {
    public TextView txt_id, txt_1, txt_2,txt_3,txt_4;
    public Button boton_edit, boton_eliminar;
    public int id_actual ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txt_1 = findViewById(R.id.txt_1);
        txt_id = findViewById(R.id.txt_id);
        txt_2 = findViewById(R.id.txt_2);
        txt_3 = findViewById(R.id.txt_3);
        txt_4 = findViewById(R.id.txt_4);

        boton_edit = findViewById(R.id.btn_edit);
        boton_eliminar = findViewById(R.id.btn_eliminar);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        boton_eliminar.setOnClickListener(v -> {
            eliminarTrampa(id_actual);
        });

        boton_edit.setOnClickListener(v -> {
            Intent editar_intent = new Intent(DetailActivity.this, EditActivity.class);
            editar_intent.putExtra("id_actual", id_actual);
            startActivity(editar_intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // se ejecuta cada vez que la pantalla vuelve a verse, asi al volver de EditActivity se ven los datos nuevos
        cargarTrampa(id_actual);
    }
    public void cargarTrampa(int id)
    {
        DBHelper admin = new DBHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor consulta = db.rawQuery("SELECT * FROM trampas WHERE id="+id, null);

        if(consulta.moveToFirst())
        {
            //existe valor
            txt_id.setText("# "+consulta.getInt(0));
            txt_1.setText("Codigo sector : "+consulta.getString(1));
            txt_2.setText("Tipo Trampa : "+consulta.getString(2));
            txt_3.setText("Nivel Trampa : "+consulta.getString(3));
            txt_4.setText("Observacion : "+consulta.getString(4));
        }


        db.close();
        consulta.close();
    }

    public void eliminarTrampa(int id)
    {
        DBHelper admin = new DBHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        db.delete("trampas", "id="+id, null);

        db.close();
        finish();
    }
}