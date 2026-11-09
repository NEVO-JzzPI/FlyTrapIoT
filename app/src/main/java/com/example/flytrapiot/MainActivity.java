package com.example.flytrapiot;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText input_codSector, input_TipoTrampa,input_NivelCapt,input_Observaciones;
    Button btn_crear;
    ListView lista_trampas;


    List<String> datos_trampas = new ArrayList<>();
    List<Integer> lista_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_crear), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        input_codSector = findViewById(R.id.input_codSector);
        input_TipoTrampa = findViewById(R.id.input_TipoTrampa);
        input_NivelCapt = findViewById(R.id.input_NivelCapt);
        input_Observaciones = findViewById(R.id.input_Observaciones);
        lista_trampas = findViewById(R.id.lista_trampas);

        btn_crear = findViewById(R.id.btn_crear);


        cargarTrampas();

        btn_crear.setOnClickListener(v -> {
            if(NivelCaptura()){
                crearTrampas();
            }
            else
            {
                Toast.makeText(this, "Ingresa un dato valido", Toast.LENGTH_SHORT).show();
            }

        });
/*        lista_trampas.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id){
                //al hacer click en un elemento de la lista, buscamos ese elmento en la lista de id y lo enviamos a detalle activity

                Integer id_actual = lista_id.get(posicion);

                Intent intent_detalle = new Intent(MainActivity.this, DetalleActivity.class);
                intent_detalle.putExtra("id_actual", id_actual);
                startActivity(intent_detalle);
            }
        });

 */
    }
    public boolean NivelCaptura(){
        String texto = input_NivelCapt.getText().toString().trim();

        if(texto.isEmpty()) {
            input_NivelCapt.setError("Debes ingresar un valor");
            return false;
        }
        int nivelUsuario;
        try {
            nivelUsuario = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            input_NivelCapt.setError("Ingresa un número válido");
            return false;
        }
        if(nivelUsuario >= 1 && nivelUsuario <= 5){
            return true;
        }
        else{
            input_NivelCapt.setError("Solo ingresa numeros del 1 al 5");
            return false;
        }
    }
    public void cargarTrampas()
    {
        DBHelper admin = new DBHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        datos_trampas = new ArrayList<>();
        lista_id = new ArrayList<Integer>();

        //CONSULTAR DATOS A LA BASE DE DATOS
        Cursor fila = db.rawQuery("SELECT * FROM trampas", null);

        //validar que tenga por lo menos un dato
        if (fila.moveToFirst())
        {
            //tiene como minimo un dato
            do {
                //obtener los datos de la fila
                int id_actual = fila.getInt(0);
                String codigo_sector = fila.getString(1);
                String tipo_trampa = fila.getString(2);
                String nivel_captura = fila.getString(3);
                String observaciones = fila.getString(4);


                String trampa_actual = "# "+id_actual+" codigo sector: "+codigo_sector+" Tipo Trampa: "+tipo_trampa +" Nivel Captura: "+ nivel_captura+ " Observaciones: "+ observaciones;
                datos_trampas.add(trampa_actual);
                lista_id.add(id_actual);

            } while (fila.moveToNext());
        }
        else
        {
            //si no retorna nada de la base de datos
            datos_trampas.add("No existen contactos en la base de datos");
        }

        //adapter
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,datos_trampas );
        lista_trampas.setAdapter(adapter);
    }

    public void crearTrampas() {
        String codigo_actual = String.valueOf(input_codSector.getText());
        String tipo_actual = String.valueOf(input_TipoTrampa.getText());
        String nivel_actual = String.valueOf(input_NivelCapt.getText());
        String observacion_actual = String.valueOf(input_Observaciones.getText());

        if (codigo_actual.isEmpty() == false & tipo_actual.isEmpty() == false & nivel_actual.isEmpty() == false & observacion_actual.isEmpty() == false) {
            //si nombre tiene valor y telefono tiene valor entonces agregamos
            DBHelper admin = new DBHelper(this);
            SQLiteDatabase db = admin.getWritableDatabase();

            ContentValues nuevo_dato = new ContentValues();
            nuevo_dato.put("codigo_sector", codigo_actual);
            nuevo_dato.put("tipo_trampa", tipo_actual);
            nuevo_dato.put("nivel_captura", Integer.parseInt(nivel_actual.trim()));
            nuevo_dato.put("observaciones", observacion_actual);

            long resultado = db.insert("trampas", null, nuevo_dato);

            //reiniciar los inputs
            input_codSector.setText("");
            input_TipoTrampa.setText("");
            input_NivelCapt.setText("");
            input_Observaciones.setText("");

            db.close();

            cargarTrampas();

        } else {
            Toast.makeText(this, "Ingrese todos los valores", Toast.LENGTH_LONG).show();
        }
    }
}