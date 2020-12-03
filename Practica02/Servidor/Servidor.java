package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Complemento.*;

public class Servidor {
    private int pto;
    private ServerSocket servidor;
    private Socket cliente;
    private Producto[] productos;

    public Servidor(int pto, Producto[] productos) {
        this.pto = pto;
        this.productos = productos;
    }

    public int getPto() {
        return pto;
    }

    private void mandarCatologo() {
        try {
            ObjectOutput oos = new ObjectOutputStream(cliente.getOutputStream());
            oos.writeObject(productos);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generarTicket(ArrayList<Producto> pr){
        double precio = 0;
        for(Producto p: pr){
            precio += p.getPrecio() * p.getCantidad();
        }

        try {
            Ticket t = new Ticket(pr, precio);    
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
            oos.writeObject(t);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarExistencias(ArrayList<Producto> pr){
        for(Producto p: pr){
            int n = p.getId() - 1;
            productos[n].setExistencias(productos[n].getExistencias() - p.getCantidad());
        }

        generarTicket(pr);

        System.out.println("Articulos actualizados");
        
        for(Producto p: productos)
            System.out.println(p.getId() + " " + p.getNombre() + " : " + p.getExistencias() + " Restante");
    }

    public void online(){
        try {
            this.servidor = new ServerSocket(this.pto);
            System.out.println("Servidor Iniciado");
            
            for( ; ; ){
                cliente = servidor.accept();   
                ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
                Lista l = (Lista) ois.readObject();
                int opt = l.getOpt();
                
                if(opt == 1)
                    mandarCatologo();
                else
                    actualizarExistencias(l.getProductos());
                
                ois.close();
                cliente.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

}
