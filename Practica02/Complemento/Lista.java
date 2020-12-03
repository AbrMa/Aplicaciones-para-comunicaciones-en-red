package Complemento;

import java.io.Serializable;
import java.util.ArrayList;

public class Lista implements Serializable{
    private static final long serialVersionUID = 1L;
    private int opt;
    private ArrayList<Producto> productos;

    public Lista(int opt, ArrayList<Producto> productos){
        this.opt = opt;
        this.productos = productos;
    }

    public int getOpt() {    return opt;}
    public ArrayList<Producto> getProductos() { return productos;}

    public void setOpt(int opt) { this.opt = opt;}
    public void setProductos(ArrayList<Producto> productos) {   this.productos = productos; }
    
}
