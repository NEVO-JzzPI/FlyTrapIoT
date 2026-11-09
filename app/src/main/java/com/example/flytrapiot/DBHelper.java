package com.example.flytrapiot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "flytrap.db";
    private static final int VERSION_BASE_DATOS = 1;

    DBHelper(Context contexto)
    {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String crearTabla = "CREATE TABLE trampas (\n" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                " codigo_sector TEXT NOT NULL,\n" +
                " tipo_trampa TEXT NOT NULL,\n" +
                " nivel_captura INTEGER NOT NULL,\n" +
                " observaciones TEXT )";
        db.execSQL(crearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version_vieja, int version_nueva)
    {
        db.execSQL("DROP TABLE IF EXISTS flytrap");
        onCreate(db);
    }

}
