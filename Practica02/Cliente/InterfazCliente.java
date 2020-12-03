package Cliente;

import javax.swing.*;

import Complemento.Carrito;
import Complemento.Producto;

import java.awt.*;
import java.awt.event.*;

public class InterfazCliente extends JFrame{
    private static final long serialVersionUID = 1L;
    
    private JPanel panelPrincipal;
    private JPanel panelInferior;
    private JPanel panelSuperior;
    private JPanel panelCentral;
    private JPanel panelCentralCentral;
    private JButton botonSiguiente;
    private JButton botonAnterior;
    private JButton agregarCarrito;
    private JButton verCarrito;
    private JButton pedirCatalogo;
    private JLabel cliente;
    private JLabel id;
    private final int PUERTO = 3000;
    private final String HOST = "127.0.0.1";
    private Cliente miCliente;
    private Producto[] misProductos;
    private JLabel imagen, nombre, precio, existencia, descripcion;
    private int contador = 0;
    private int i = 0;
    private Carrito miCarrito;
    private JTextField texto;
    private int cantidad;


    public InterfazCliente() {
        setBounds(260, 50, 400, 500);
        setTitle("Articulos de limpieza");
        setResizable(false);

        panelPrincipal = new JPanel();
        panelInferior = new JPanel();
        panelSuperior = new JPanel();
        panelCentral = new JPanel();
        panelCentralCentral = new JPanel();
        botonAnterior = new JButton("<<");
        botonSiguiente = new JButton(">>");
        agregarCarrito = new JButton("Agregar al carrito");
        agregarCarrito.setVisible(false);
        verCarrito = new JButton("Ver carrito");
        verCarrito.setVisible(false);
        pedirCatalogo = new JButton("Ver Productos");
        cliente = new JLabel("Productos");
        imagen = new JLabel("");
        nombre = new JLabel("");
        id = new JLabel("");
        precio = new JLabel("$");
        existencia = new JLabel("");
        descripcion = new JLabel("");
        texto = new JTextField(4);
        miCarrito = new Carrito();
        i = 0;

        pedirCatalogo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pedirCatalogo.setVisible(false);
                verCarrito.setVisible(true);
                agregarCarrito.setVisible(true);
                miCliente = new Cliente(PUERTO, HOST);
                misProductos = miCliente.recibirCatalogo();
                botonAnterior.setEnabled(false);
                dibujo(misProductos[0]);
            }
        });

        botonSiguiente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(contador == 19)
                    botonSiguiente.setEnabled(false);
                else {
                    dibujo(misProductos[++contador]);
                    botonSiguiente.setEnabled(true);
                    botonAnterior.setEnabled(true);
                }
            }
        });

        botonAnterior.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (contador > 0) {
                    botonAnterior.setEnabled(true);
                    botonSiguiente.setEnabled(true);
                    dibujo(misProductos[--contador]);
                } else if(contador == 19){
                    botonSiguiente.setEnabled(false);
                } else {
                    botonAnterior.setEnabled(false);
                }
            }
        });

        agregarCarrito.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(texto.getText().equals("")){
                    JOptionPane.showMessageDialog(InterfazCliente.this, "Ingrese cantidad");
                }
                else {
                cantidad = Integer.parseInt(texto.getText());
                int ids = Integer.parseInt(id.getText().substring(4));
                if(cantidad > misProductos[ids - 1].getExistencias()) {
                    JOptionPane.showMessageDialog(InterfazCliente.this, "No hay suficiente cantidad");
                } else if(cantidad <= 0) {
                    JOptionPane.showMessageDialog(InterfazCliente.this, "Cantidad No Valida");
                }
                else {
                    misProductos[ids - 1].setExistencias(misProductos[ids - 1].getExistencias() - cantidad);
                    misProductos[ids - 1].setCantidad(misProductos[ids - 1].getCantidad() + cantidad);
                    existencia.setText("Existencias: " + misProductos[ids - 1].getExistencias());
                    miCarrito.crearCarrito(misProductos[ids - 1], ++i);
                }
                texto.setText("");
            }
        }
        });

        verCarrito.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                miCarrito.setVisible(true);
            }
        });

        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelCentral.setLayout(new BorderLayout(5, 5));
        panelCentralCentral.setLayout(new BoxLayout(this.panelCentralCentral, BoxLayout.Y_AXIS));

        panelCentralCentral.add(id);
        panelCentralCentral.add(nombre);
        panelCentralCentral.add(imagen);
        panelCentralCentral.add(precio);
        panelCentralCentral.add(existencia);
        panelCentralCentral.add(descripcion);

        panelCentralCentral.add(new JLabel("Cantidad"));
        panelCentralCentral.add(texto);
        
        //panelCentral.add(imagen, BorderLayout.NORTH);
        panelCentral.add(panelCentralCentral, BorderLayout.NORTH);        
        panelInferior.add(botonAnterior, BorderLayout.WEST);
        panelInferior.add(pedirCatalogo, BorderLayout.CENTER);
        panelInferior.add(agregarCarrito, BorderLayout.CENTER);
        panelInferior.add(verCarrito, BorderLayout.CENTER);
        panelInferior.add(botonSiguiente, BorderLayout.EAST);
        panelSuperior.add(cliente);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        add(panelPrincipal);



        setDefaultCloseOperation(3);
        setVisible(true);
    }

    private void dibujo(Producto producto) {
        ImageIcon imageIcon = new ImageIcon("Complemento/Imagenes/" + producto.getImagen());
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        imagen.setIcon(imageIcon);

        id.setText("ID: " + producto.getId());
        nombre.setText("Nombre: " + producto.getNombre());
        precio.setText("$" + producto.getPrecio());
        existencia.setText("Existencias: " + producto.getExistencias());
        descripcion.setText("Descripcion: " + producto.getDescripcion());
    }

    public static void main(String[] args) {
        new InterfazCliente();
    }

}