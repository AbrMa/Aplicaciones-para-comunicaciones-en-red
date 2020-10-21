package Servidor;

import Directory.Directory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class Drive {
    static File file;
    static JTree tree;
    static int opt;
    static long sizeFile;
    static Socket cl;
    static String nameFile, path;
    static Directory directory;
    static Server form1Server;
    static DefaultTreeModel model;
    static DefaultMutableTreeNode root;
    static DataInputStream dis;
    static DataOutputStream dos;

    public static void getPath(File f, DefaultTreeModel model, DefaultMutableTreeNode root){        //  Crear ruta 
        File directorys[] = f.listFiles();

        for(int i = 0; i < directorys.length; ++i){
            DefaultMutableTreeNode folder = new DefaultMutableTreeNode(directorys[i].getName());
            model.insertNodeInto(folder , root, i);
            
            if(directorys[i].isDirectory()){
                System.out.println("Directorio -> " + directorys[i].getName());
                getPath(directorys[i], model, folder);
            }else{
                System.out.println( "Archivo: " + directorys[i].getName());
            }
        }
    }

    public static void makeDirectory(String newDirectory){                                          //  Crea un directorio
        File pos = new File(path + "/" + newDirectory);
        System.out.println("Pos: " + path + "\nCarpeta: " + newDirectory + "\nPath: " + pos.getAbsolutePath());

        if(!pos.exists()) {
            try {
                if(pos.mkdirs())
                    System.out.println("Carpeta creada");
                else 
                    System.out.println("No se creo la carpeta");
            }catch(SecurityException se) { 
                se.printStackTrace(); 
            }
        }
        else
            System.out.println("El directorio ya existe");
    }

    public static boolean removeFile(File file){                                                       //  Eliminar un archivo
        System.out.println(file.getAbsolutePath());
        
        if(file.exists()){
            System.out.println("Archivo Encontrado");
        
            if (file.isDirectory()){
                System.out.println("Carpeta");
                File items[] = file.listFiles();
                
                if(items.length > 0){
                    for(int i = 0 ;i < items.length;i++){
                        if(items[i].isDirectory())
                            removeFile(items[i]);
                        else{
                            items[i].delete();
                            System.out.println("Archivo: " + file.getPath() + " borrado");
                        }
                    }
                }              
                file.delete();
                System.out.println("Archivo: " + file.getPath() + " borrado");
                
            }
            else{
                file.delete();
                System.out.println("Archivo: " + file.getPath() + " borrado");
            }         
        }
        else{
            System.out.println("Archivo no encontrado");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        path = "Drive";                                             //  Armar el arbol
        root = new DefaultMutableTreeNode("Drive");
        model = new DefaultTreeModel(root);
        tree = new JTree(root);                                   

        try {   
            form1Server = new Server(3000);
            form1Server.startConnection();         
            int closeClient;         
            while(true){
                closeClient = 0;
                file = new File(path);
                getPath(file, model, root);
                directory = new Directory(tree);
                cl = form1Server.getSocket().accept();
                form1Server.SendDirectory(cl, directory);
                System.out.println("Directorio Enviado (jtree)");
                cl.close();

                for(;;){
                    root =  null;
                    model = null;
                    tree = null;
                    cl = form1Server.getSocket().accept();
                    opt = form1Server.receiveAction(cl);

                    switch(opt){        
                        case 1:     //  Subir archivo
                                String dir = form1Server.receiveFileName();
                                
                                if(!dir.equals(""))
                                    form1Server.uploadFile(path + dir);
                                
                                
                                root = new DefaultMutableTreeNode("Drive");
                                model = new DefaultTreeModel(root);
                                tree = new JTree(root);
                            
                                getPath(file, model, root);
                                directory = new Directory(tree);
                                cl = form1Server.getSocket().accept();
                                form1Server.SendDirectory(cl, directory);
                            break;

                        case 2:     //  Hacer directorio
                                String newDirectory = form1Server.receiveFileName();
                                makeDirectory(newDirectory);    
                                
                                root = new DefaultMutableTreeNode("Drive");
                                model = new DefaultTreeModel(root);
                                tree = new JTree(root);    
                                
                                getPath(file, model, root);
                                directory = new Directory( tree );
                                cl = form1Server.getSocket().accept();
                                form1Server.SendDirectory(cl, directory);
                            break;

                        case 3:     //  Eliminar archivo
                                nameFile = form1Server.receiveFileName();
                                String pathFile = path + '/' + nameFile;
                                File fileDelete = new File(pathFile);
                                
                                removeFile(fileDelete);
                                
                                root = new DefaultMutableTreeNode("Drive");
                                model = new DefaultTreeModel(root);
                                tree = new JTree(root);    

                                getPath(file, model, root);
                                directory = new Directory(tree);
                                cl = form1Server.getSocket().accept();
                                form1Server.SendDirectory(cl, directory);
                            break;

                        case 4:     //  Descargar archivo
                                String fileDownload = form1Server.receiveFileName();
                                String foundFile = path + "/" + fileDownload;
                                form1Server.getFile(foundFile, fileDownload);
                              
                                root = new DefaultMutableTreeNode("Drive");
                                model = new DefaultTreeModel(root);
                                tree = new JTree(root);    
                                getPath(file, model, root);
                                
                                directory = new Directory(tree);
                                cl = form1Server.getSocket().accept();
                                form1Server.SendDirectory(cl, directory);
                            break;
                            
                        case 5:     //  Salir
                                cl.close();
                                closeClient = 1;
                            break;
                    }
                    
                    if(closeClient == 1)
                        break;
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}