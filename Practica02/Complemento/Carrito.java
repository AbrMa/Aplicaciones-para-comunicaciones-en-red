package Complemento;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

import Cliente.Cliente;

public class Carrito extends JFrame {
    private static final long serialVersionUID = 4L;  
    private JPanel jpPrincipal;
    private JPanel jpBotones;
    private JButton comprar;
    private JButton regresar;
    private JButton remover;
    private JLabel costo;
    private double precio = 0;
    private Cliente Cliente;
    private final int PUERTO = 3000;
    private final String HOST = "127.0.0.1";
    private ArrayList<Producto> Carrito;
    private DefaultTableModel modelo;
    private JTable tablaProductos;

    public Carrito() {
        setTitle("Carrito");
        setBounds(260, 660, 400, 400);
        setResizable(false);

        jpPrincipal = new JPanel();
        jpBotones = new JPanel();
        comprar = new JButton("Comprar");
        regresar = new JButton("Regresar");
        remover = new JButton("Eliminar");
        costo = new JLabel("Costo Total: $");
        Carrito = new ArrayList<>();setVisible(true);
        tablaProductos = new JTable();
        modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.addColumn("Id");
        modelo.addColumn("Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");

        regresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            } 
        });

        comprar.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                Cliente = new Cliente(PUERTO, HOST);
                Cliente.hacerCompra(Carrito);
                Carrito.clear();
                modelo.setRowCount(0);
                costo.setText("Costo Total: $");
                setVisible(false);
            }
        });

        remover.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idFila = Integer.parseInt(JOptionPane.showInputDialog(jpPrincipal, "Eliminar por ID"));
                double precioRestar = (double) modelo.getValueAt(idFila - 1, 3);
                costo.setText("Costo Total: $" + (precio - precioRestar));
                Carrito.remove(idFila - 1);
                modelo.removeRow(idFila - 1);
            }
        });

        jpPrincipal.setLayout(new BorderLayout(1, 1));

        jpBotones.add(comprar);
        jpBotones.add(regresar);
        jpBotones.add(remover);
        jpPrincipal.add(costo, BorderLayout.NORTH);
        jpPrincipal.add(new JScrollPane(tablaProductos, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        jpPrincipal.add(jpBotones, BorderLayout.SOUTH);
        add(jpPrincipal);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
    }

    public void crearCarrito(Producto p, int i) {
       modelo.addRow(new Object[]{i, p.getNombre(), p.getCantidad(), (p.getCantidad() * p.getPrecio())});
       calcularPrecio(p);
       Carrito.add(p);
    }

    private void calcularPrecio(Producto producto) {
        precio += (producto.getPrecio() * producto.getCantidad());
        costo.setText("Costo Total: $" + precio);
    }

}