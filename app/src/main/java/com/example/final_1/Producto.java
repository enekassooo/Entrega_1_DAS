package com.example.final_1;

public class Producto {
    private long id;
    private String nombre;
    private String coste;
    private int cant;

    public Producto(long id, String nombre, String costeTotal, int transporte) {
        this.id = id;
        this.nombre = nombre;
        this.coste = coste;
        this.cant = transporte;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCoste() {
        return coste;
    }

    public void setCoste(String costeTotal) {
        this.coste = costeTotal;
    }

    public int getCantidad() {
        return cant;
    }

    public void setCantidad(int transporte) {
        this.cant = transporte;
    }
}
