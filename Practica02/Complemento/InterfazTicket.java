package Complemento;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.awt.*;

public class InterfazTicket extends JFrame {
    private static final long serialVersionUID = 11L;

    private JPanel panelPrincipal;
    private JPanel panelInferior;
    private JPanel panelCostoFinal;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton aceptar;
    private JLabel costo;
    private JLabel fecha;

    public InterfazTicket() {
        setTitle("Ticket");
        setBounds(670, 50, 500, 500);
        setResizable(false);
        setVisible(true);

        costo = new JLabel("Costo Final: $");
        fecha = new JLabel("Fecha - " + LocalDateTime.now());

        aceptar = new JButton("Aceptar");
        aceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modelo.setRowCount(0);
                costo.setText("Costo Final: $");
                setVisible(false);
            }
        });

        tabla = new JTable();
        
        modelo = (DefaultTableModel) tabla.getModel();
        modelo.addColumn("Articulo");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");

        panelInferior = new JPanel();
        panelInferior.add(aceptar);

        panelCostoFinal = new JPanel();
        panelCostoFinal.setLayout(new BorderLayout(3, 3));
        panelCostoFinal.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panelCostoFinal.add(costo, BorderLayout.SOUTH);
        panelCostoFinal.add(fecha, BorderLayout.SOUTH);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(5, 5));
        panelPrincipal.add(panelCostoFinal, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        add(panelPrincipal);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void crear(Ticket t) { 
        ArrayList <Producto> miProducto = new ArrayList<>();
        miProducto = t.getProductos();
        for(Producto p : miProducto) {
            modelo.addRow(new Object[]{p.getNombre(), p.getCantidad(), p.getPrecio()});
        }

        costo.setText("Costo Final: $" + t.getPrecio());
    }

}