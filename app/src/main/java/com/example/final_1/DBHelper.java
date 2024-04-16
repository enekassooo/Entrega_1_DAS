package com.example.final_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // Definición de constantes para el nombre de la base de datos, la versión y los nombres de las tablas y columnas
    public static final String DATABASE_NAME = "productos.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_PRODUCTOS = "productos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_COSTE = "coste";
    public static final String COLUMN_CANTIDAD = "cantidad";

    // Constructor de la clase DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método onCreate: se ejecuta al crear la base de datos por primera vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla "productos" con las columnas definidas
        String CREATE_TABLE_PRODUCTOS = "CREATE TABLE " + TABLE_PRODUCTOS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NOMBRE + " TEXT," +
                COLUMN_COSTE + " DOUBLE," +
                COLUMN_CANTIDAD + " INTEGER" +
                ")";
        db.execSQL(CREATE_TABLE_PRODUCTOS);
    }

    // Método onUpgrade: se ejecuta al actualizar la versión de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla existente si existe y crear una nueva tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }

    // Método para agregar un nuevo producto a la base de datos
    public long nuevoProducto(String nombre, String coste, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_COSTE, coste);
        values.put(COLUMN_CANTIDAD, cantidad);
        long id = db.insert(TABLE_PRODUCTOS, null, values);
        return id;
    }

    // Método para eliminar un producto de la base de datos por su ID
    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTOS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)});
    }

    // Método para obtener todos los productos de la base de datos
    public List<Producto> getAllTasks() {
        List<Producto> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
                    int columnIndexNombre = cursor.getColumnIndex(COLUMN_NOMBRE);
                    int columnIndexCoste = cursor.getColumnIndex(COLUMN_COSTE);
                    int columnIndexCant = cursor.getColumnIndex(COLUMN_CANTIDAD);

                    do {
                        // Obtener datos de cada columna del cursor y crear un objeto Producto
                        long id = cursor.getInt(columnIndexId);
                        String nombre = cursor.getString(columnIndexNombre);
                        String coste = cursor.getString(columnIndexCoste);
                        int cant = cursor.getInt(columnIndexCant);
                        Producto producto = new Producto(id, nombre, coste, cant);
                        productList.add(producto);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return productList;
    }
}

