package Complemento;

import java.io.Serializable;

public class Producto implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private double precio;
    private int existencias;
    private String descripcion;
    private String imagen;
    private int cantidad;

    public Producto(int id, String nombre, double precio, int existencias, String descripcion, String imagen){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.cantidad = 0;
    }

    public void setId(int id) { this.id = id;   }
    public void setNombre(String nombre) {  this.nombre = nombre;}
    public void setPrecio(double precio) {  this.precio = precio;}
    public void setExistencias(int existencias) {   this.existencias = existencias;}
    public void setDescripcion(String descripcion) {    this.descripcion = descripcion;}
    public void setImagen(String imagen) {  this.imagen = imagen;}
    public void setCantidad(int cantidad) { this.cantidad = cantidad;}

    public int getId() {    return id;}
    public String getNombre() { return nombre;}
    public double getPrecio() { return precio;}
    public int getExistencias() {   return existencias;}
    public String getDescripcion() {    return descripcion;}
    public String getImagen() { return imagen;}
    public int getCantidad() {  return cantidad;}

}
