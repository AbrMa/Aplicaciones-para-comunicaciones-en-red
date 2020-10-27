package Cliente;

import Directory.Directory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class userInterfaz {
    String host = "localhost";
    String SelectedPath;
    int pto = 3000;
    JFrame f;
    JTree jt;

    private JTextField jtf;
    private JButton upload;
    private JButton createDirectory;
    private JLabel jl;

    private JScrollPane jsp;
    private JTree tree;
    private JButton deleteFile;
    private JButton download;

    public userInterfaz(){
        initComponents();
    }

    public void initComponents(){
        JFrame f = new JFrame("Interfaz Cliente");
        f.setBounds(500, 90, 635, 650);
        f.setResizable(false);
        
        
        Client cl = new Client("localhost", 3000);
        Directory dir = cl.receiveDirectory();
        
        jl = new JLabel();
        jl.setFont(new java.awt.Font("Dialog", 1, 15));
        jl.setText("Archivos Servidor");
        jl.setBounds(10, 10, 610, 20);
        f.add(jl);

        jtf = new JTextField();
        jtf.setBounds(10, 585, 617, 20);
        jtf.setText(null);
        f.add(jtf);

        upload = new JButton();
        upload.setText("Subir Archivo");
        upload.setBounds(10, 560, 150, 20);
        upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadAction();
            }
        });
        f.add(upload);

        createDirectory = new JButton();
        createDirectory.setText("Directorio Nvo");
        createDirectory.setBounds(165, 560, 150, 20);
        createDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDirectoryAction();
            }
        });
        f.add(createDirectory);

        deleteFile = new JButton();
        deleteFile.setText("Borrar Archivo");
        deleteFile.setBounds(320, 560, 150, 20);
        deleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFileAction();
            }
        });
        f.add(deleteFile);

        download = new JButton();
        download.setText("Descargar");
        download.setBounds(475, 560, 150, 20);
        download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadAction();
            }
        });
        f.add(download);

        
        tree = new JTree();
        tree = dir.getDirectory();
        tree.setEditable(true);
        tree.setBounds(10, 30, 610, 500);
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();

                    if (node == null)   return;

                    Object nodeObject = node.getUserObject();
                    System.out.println("Selected node : " + nodeObject);
                }
            }
        });
        f.add(tree);

        jsp = new JScrollPane(tree);
        jsp.setViewportView(tree);
        jsp.setBounds(10, 30, 610, 500);
        jsp.setVisible(true);
        f.add(jsp);     

        f.setLayout(null);  
        f.setVisible(true); 

        f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Client cl = new Client(host, pto);
                cl.sendAction(5);
                System.exit(0);
            }
        });
    
    }

    public void uploadAction(){
        Client cl = new Client(host, pto);
        cl.sendAction(1);
        String inDir = jtf.getText();

        if(inDir.equals("")){
            inDir = "/";
        }
        else{
            inDir = "/" + inDir;
        }
            
        cl.sendDir(inDir);
        cl.uploadFile();
        
        Directory dir = cl.receiveDirectory();
        JTree jt = dir.getDirectory();
        jsp.setViewportView(jt);
        jsp.updateUI();
        jtf.setText("");
    }

    public void createDirectoryAction(){
        String nameDir = jtf.getText();
        
        if (!(nameDir.equals(""))){
            Client cl = new Client(host, pto);        
            cl.sendAction(2);
            cl.sendDir(nameDir);
            Directory dir = cl.receiveDirectory();
            JTree jt = dir.getDirectory();
            jsp.setViewportView(jt);
            jsp.updateUI();
            jtf.setText("");
        }
        else{
            System.out.println("Crear Directorio: Error, ingrese el nombre del directorio"); 
            JOptionPane.showMessageDialog(null, "Error! Ingrese el nombre del directorio");
        }       
    }

    public void deleteFileAction(){
        String nameFile = jtf.getText();
        
        if(!(nameFile.equals(""))){
            Client cl = new Client(host, pto);
            cl.sendAction(3);
            cl.sendDir(nameFile);

            if(cl.receiveConfirm()){
                JOptionPane.showMessageDialog(null, "Archivo Eliminado");
            }
            else{
                JOptionPane.showMessageDialog(null, "Archivo no existente");
            }

            Directory dir = cl.receiveDirectory();
            JTree jt = dir.getDirectory();
            jsp.setViewportView(jt);
            jsp.updateUI();
            jtf.setText("");
        }
        else{
            System.out.println("Eliminar Archivo: Error, ingrese el nombre del Archivo"); 
            JOptionPane.showMessageDialog(null, "Error! Ingrese el nombre del Archivo");
        }  
    }

    public void downloadAction(){
        String nameFile = jtf.getText();

        if (!(nameFile.equals(""))){
            Client cl = new Client(host, pto);
            cl.sendAction(4);
            cl.sendDir(nameFile);
            
            if(cl.reciveFile())
                JOptionPane.showMessageDialog(null, "Archivo Descargado");    
            else
                JOptionPane.showMessageDialog(null, "Error! Archivo no existente");


            Directory dir = cl.receiveDirectory();
            JTree jt = dir.getDirectory();
            jsp.setViewportView(jt);
            jsp.updateUI();
            jtf.setText("");
        }
        else{
            System.out.println("Descargar Archivo: Error, ingrese el nombre del Archivo"); 
            JOptionPane.showMessageDialog(null, "Error! Ingrese el nombre del Archivo");
        }  
    }

    public static void main(String[] args) {
        userInterfaz UI = new userInterfaz();
    }

}
