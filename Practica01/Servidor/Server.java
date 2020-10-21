package Servidor;

import Directory.Directory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class Server {                                                   //  Servidor
    private int pto;
    private ServerSocket ss;                    
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectOutputStream oos;

    public Server(int pto) {                                            // Constructor
        this.pto = pto;
    }

    public ServerSocket getSocket() {                                   //  Retornar serverSocket
        return this.ss;
    }

    public void startConnection() {                                     //    Inicia la conexion 
        try {
            this.ss = new ServerSocket(this.pto);
            System.out.println("Servidor iniciado en el puerto " + ss.getLocalPort() + ", Esperando cliente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endConnection(){                                        //  Termina la conexion
        try {
            System.out.println("Cerrando servidor en el puerto " + ss.getLocalPort() );
            this.ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendDirectory(Socket cl, Directory file){               //  Envia un Jtree
        try {
            this.oos = new ObjectOutputStream(cl.getOutputStream());
            this.oos.writeObject(file);
            this.oos.flush();
            this.oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int receiveAction(Socket cl){                                //  Recibe la instruccion a realizar
        try {
            this.dis = new DataInputStream(cl.getInputStream());
            int option = dis.readInt();
            return option;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String uploadFile(String path){                              //  Actualiza el Drive
        try {
            Socket cl = ss.accept();
            this.dis = new DataInputStream(cl.getInputStream());
            String name = dis.readUTF();
            long size = dis.readLong();

            System.out.println("Archivo: " + name + "\nLongitud: " + size + "\nDireccion: " + cl.getInetAddress() + ":" + cl.getLocalPort());
            
            String save = path + "/" + name;
            this.dos = new DataOutputStream(new FileOutputStream(save));
            long aux = 0;
            int n = 0, por = 0;
            
            while(aux < size){
                byte[] b = new byte[3000];
                n = dis.read(b);
                dos.write(b, 0, n);
                dos.flush();
                aux += n;
                por = (int)((aux*100)/size);
                System.out.print("\rPorcentaje recibido" + por + "%");
            }
            
            dos.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String receiveFileName(){                                        //  Recibe el nombre de un archivo 
        try {
            Socket cl = ss.accept();
            this.dis = new DataInputStream(cl.getInputStream());
            String name = dis.readUTF();
            dis.close();
            cl.close();
            return name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void getFile(String file, String nameFolder){                                           //  Enviar un archivo
        try {
            Socket cl = ss.accept();
            String name = "";
            String path = "";
            long size = 0;

            File f = new File(file);

            if(f.isDirectory()){
                String OUTPUT_ZIP_FILE = "Folder-" + nameFolder + LocalDateTime.now() + ".zip";
                System.out.println(f.getAbsolutePath());
                ZipUtils zipDirectory = new ZipUtils(f.getAbsolutePath());
                zipDirectory.generateFileList(new File(f.getAbsolutePath()));
                zipDirectory.zipIt(OUTPUT_ZIP_FILE);
                name = "Download-" + new File(OUTPUT_ZIP_FILE).getName();
                size = new File(OUTPUT_ZIP_FILE).length();
                path = new File(OUTPUT_ZIP_FILE).getAbsolutePath();
            }else{
                name = f.getName();
                size = f.length();
                path = f.getAbsolutePath();
            }

            System.out.println("Archivo: " + name + "\nLongitud: " + size + "\nDireccion: " + path);

            this.dos = new DataOutputStream (cl.getOutputStream());
            this.dis = new DataInputStream(new FileInputStream(path));
            dos.writeUTF(name);
            dos.flush();
            dos.writeLong(size);
            dos.flush();
            long aux = 0;
            int n = 0, por = 0;

            while(aux < size){
                byte[] b = new byte[3000];
                n = dis.read(b);
                dos.write(b,0,n);
                dos.flush();
                aux += n;
                por = (int)((aux*100)/size);
                System.out.print("\rPorcentaje enviado "+ por +" %");
            }

            dis.close();
            dos.close();
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
