package Servidor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Complemento.Inventario;
import Complemento.Producto;

public class InterfazServidor {
    private JButton jbCargarProductos;
    private JButton jbOnline;
    private JLabel jlServidor;
    private Producto[] productos = new Producto[20];
    private Inventario inventario = new Inventario(productos);
    private final int pto = 3000;
    private Servidor servidor;

    public InterfazServidor(){
        JFrame jf = new JFrame("Servidor");
        jf.setBounds(50, 50, 200, 150);
        jf.setResizable(false);
        
        jlServidor = new JLabel("Encender Servidor");
        jlServidor.setFont(new java.awt.Font("Dialog", 1, 15));
        jlServidor.setBounds(20, 10, 160, 20);
        jf.add(jlServidor);

        jbCargarProductos = new JButton("Cargar Productos");
        jbCargarProductos.setBounds(10, 40, 160, 20);
        jbCargarProductos.setVisible(true);
        jbCargarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productos = inventario.inciarInvetario();
                jbOnline.setVisible(true);
                jbCargarProductos.setVisible(false);
                JOptionPane.showMessageDialog(jf,"Productos cargados");  
            }
        });
        jf.add(jbCargarProductos);
        
        jbOnline = new JButton("Online");
        jbOnline.setBounds(10, 80, 160, 20);
        jbOnline.setVisible(false);
        jbOnline.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    servidor = new Servidor(pto, productos);
                    servidor.online();   
                    JOptionPane.showMessageDialog(jf,"Servidor iniciado, puerto: " + servidor.getPto());  
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(jf,"Error!");  
                }
            }
        });
        jf.add(jbOnline);

        jf.setLayout(null);   
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        jf.setVisible(true);  
    }

    public static void main(String[] args) {
        new InterfazServidor();
    }

}
