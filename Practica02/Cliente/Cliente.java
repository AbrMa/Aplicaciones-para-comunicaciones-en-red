package Cliente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Complemento.InterfazTicket;
import Complemento.Lista;
import Complemento.Producto;
import Complemento.Ticket;

public class Cliente {
    private int puerto;
    private String host;
    private Producto[] Productos;
    private Socket cliente;
    private InterfazTicket intTicket;

    public Cliente(int puerto, String host) {
        this.puerto = puerto;
        this.host = host;
    }

    public Producto[] recibirCatalogo() {
        try {
            cliente = new Socket(this.host, this.puerto);
            Lista l = new Lista(1, null);
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
            oos.writeObject(l);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            Productos = (Producto[]) ois.readObject();
            ois.close();
            oos.close();

            for(Producto p : Productos)
                System.out.println(p.getNombre() + "," + p.getExistencias());

            return Productos;
        } catch(Exception e) { 
            e.printStackTrace(); 
            return null;
        }
    }

    public void hacerCompra(ArrayList<Producto> productosFinal) {
        try {
            cliente = new Socket(this.host, this.puerto);
            Lista l = new Lista(2, productosFinal);
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());

            oos.writeObject(l);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            Ticket t = (Ticket) ois.readObject();


            ois.close();
            oos.close();
            cliente.close();

            intTicket = new InterfazTicket();
            intTicket.crear(t);
            intTicket.setVisible(true);
        } catch(Exception e) { e.printStackTrace(); }
    }


}