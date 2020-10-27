package Servidor;

import Directory.Directory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
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
    static Server driveServer;
    static DefaultTreeModel model;
    static DefaultMutableTreeNode root;
    static DataInputStream dis;
    static DataOutputStream dos;

    public static void getPath(File f, DefaultTreeModel model, DefaultMutableTreeNode root) { 
        File directorys[] = f.listFiles();

        for (int i = 0; i < directorys.length; ++i) {
            DefaultMutableTreeNode folder = new DefaultMutableTreeNode(directorys[i].getName());
            model.insertNodeInto(folder, root, i);

            if (directorys[i].isDirectory()) {
                System.out.println(f.getName() + "/" + directorys[i].getName());
                getPath(directorys[i], model, folder);
            } else {
                System.out.println(f.getName() + " -> Archivo: " + directorys[i].getName());
            }
        }
    }

    public static void makeDirectory(String newDirectory) { 
        System.out.println("Crear un nuevo directorio");
        File pos = new File(path + "/" + newDirectory);

        if (!pos.exists()) {
            try {
                if (pos.mkdirs()){
                    System.out.println("Directorio creado");
                    System.out.println("Pos: " + path + "\nCarpeta: " + newDirectory + "\nPath: " + pos.getAbsolutePath());
                }
                else
                    System.out.println("Error, directorio no creado");
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        } else{
            System.out.println("Directorio ya existente con ese nombre");
            //  Enviar retroalimentacion
        }
    }

    public static boolean removeFile(File file) { 
        System.out.println(file.getAbsolutePath());

        if (file.exists()) {
            System.out.println("Archivo Encontrado");

            if (file.isDirectory()) {
                File items[] = file.listFiles();

                for (int i = 0; i < items.length; i++) {
                    if (items[i].isDirectory())
                        removeFile(items[i]);
                    else {
                        items[i].delete();
                        System.out.println("Archivo: " + file.getPath() + " borrado");
                    }
                }

                file.delete();
                System.out.println("Archivo: " + file.getPath() + " borrado");
            } else {
                file.delete();
                System.out.println("Archivo: " + file.getPath() + " borrado");
            }
        } else {
            System.out.println("Archivo no encontrado");
            return false;
        }
        return true;
    }

    public static void doTreeDirectory(DefaultMutableTreeNode r, DefaultTreeModel m, JTree t) {
        root = new DefaultMutableTreeNode("Drive");
        model = new DefaultTreeModel(root);
        tree = new JTree(root);
    }

    public static void sendTreeDirectory(Server driveServer) throws IOException {
        directory = new Directory(tree);
        cl = driveServer.getSocket().accept();
        driveServer.sendDirectory(cl, directory);
    }

    public static void main(String[] args) {
        path = "Drive";                                             
        doTreeDirectory(root, model, tree);                               
        driveServer = new Server(3000);   

        try {       
            driveServer.startConnection();         
            
            while(true){
                int closeClient = 0;  
                file = new File(path);
                
                System.out.println("\nArchivos en Drive(Servidor)");
                getPath(file, model, root);
                sendTreeDirectory(driveServer);
                System.out.println("\nDirectorio Enviado (jtree) \nConexion: " + driveServer.getSocket().getInetAddress() + "\n");
                cl.close();

                for(;;){
                    doTreeDirectory(null, null, null);     
                    cl = driveServer.getSocket().accept();
                    System.out.println("\nEsperando instrucciones");
                    opt = driveServer.receiveAction(cl);

                    switch(opt){        
                        case 1:     //  Subir archivo
                            String dir = driveServer.receiveFileName();
                            driveServer.uploadFile(path + dir);
                                
                            doTreeDirectory(root, model, tree); 
                            System.out.println("\nArchivos en Drive(Servidor)");                              
                            getPath(file, model, root);
                            sendTreeDirectory(driveServer);
                            break;
                        case 2:     //  Hacer directorio
                            String newDirectory = driveServer.receiveFileName();
                            makeDirectory(newDirectory);    
                                
                            doTreeDirectory(root, model, tree);   
                            System.out.println("\nArchivos en Drive(Servidor)");                            
                            getPath(file, model, root);
                            sendTreeDirectory(driveServer);
                            break;

                        case 3:     //  Eliminar archivo
                            nameFile = driveServer.receiveFileName();
                            String pathFile = path + '/' + nameFile;
                            File fileDelete = new File(pathFile);
                                
                            System.out.println("\nEliminar archivo");
                            
                            if(removeFile(fileDelete))
                                driveServer.sendConfirm(true);
                            else
                                driveServer.sendConfirm(false);
                                
                            doTreeDirectory(root, model, tree);     
                            System.out.println("\nArchivos en Drive(Servidor)");                          
                            getPath(file, model, root);
                            sendTreeDirectory(driveServer);
                            break;

                        case 4:     //  Descargar archivo
                            System.out.println("\nDescargar archivo");
                            String fileDownload = driveServer.receiveFileName();
                            String foundFile = path + "/" + fileDownload;
                            driveServer.sendFile(foundFile, fileDownload);
                              
                            doTreeDirectory(root, model, tree);   
                            System.out.println("\nArchivos en Drive(Servidor)");                            
                            getPath(file, model, root);
                            sendTreeDirectory(driveServer);
                            break;
                            
                        case 5:     //  Terminar conexion 
                            closeClient = 1;
                            cl.close();
                            doTreeDirectory(root, model, tree);   
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