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

public class Server {                                                   
    private int pto;
    private ServerSocket ss;                    
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectOutputStream oos;

    public Server(int pto) {                                            
        this.pto = pto;
    }

    public ServerSocket getSocket() {                                   
        return this.ss;
    }

    public void startConnection() {                                     
        try {
            this.ss = new ServerSocket(this.pto);
            System.out.println("Servidor iniciado, Puerto: " + ss.getLocalPort() + "\nEsperando cliente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endConnection(){                                        
        try {
            System.out.println("Cerrando servidor, Puerto: " + ss.getLocalPort() );
            this.ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDirectory(Socket cl, Directory file){               
        try {
            this.oos = new ObjectOutputStream(cl.getOutputStream());
            this.oos.writeObject(file);
            this.oos.flush();
            this.oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int receiveAction(Socket cl){                                
        try {
            this.dis = new DataInputStream(cl.getInputStream());
            int option = dis.readInt();
            return option;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void uploadFile(String path){                              
        try {
            System.out.println("\nSubir un archivo al Drive(Servidor)");        
            Socket cl = ss.accept();
            this.dis = new DataInputStream(cl.getInputStream());
            String name = dis.readUTF();
            long size = dis.readLong();
            
            if(name.equals("") && size == 0){
                System.out.println("Operacion Cancelada");        
                return;
            }

            System.out.println("Archivo: " + name + "\nLongitud: " + size + "\nDireccion: " + cl.getInetAddress() + ":" + cl.getLocalPort());
            
            String save = path + "/" + name;
            this.dos = new DataOutputStream(new FileOutputStream(save));
            long aux = 0;
            int por = 0;
            int n = 0;
            
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

            File f = new File(save);
            ZipUtils unzipFile = new ZipUtils(f.getAbsolutePath());
            unzipFile.unzipFile(path);
            f.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveFileName(){                                        
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

    public void sendConfirm(boolean val){                                        
        try {
            Socket cl = ss.accept();
            this.dos = new DataOutputStream(cl.getOutputStream());
            this.dos.writeBoolean(val);
            this.dos.flush();
            this.dos.close();
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String file, String nameFolder){
        try {
            Socket cl = ss.accept();
            String name = "";
            String path = "";
            long size = 0;

            File f = new File(file);
            
            if(!f.exists()){
                System.out.println("No existe ese archivo");
                this.dos = new DataOutputStream (cl.getOutputStream());
                dos.writeUTF("");
                dos.flush();
                dos.writeLong(0);
                dos.flush();
                dos.close();
                cl.close();
                return;
            }
                
            String OUTPUT_ZIP_FILE = "Folder-" + nameFolder + "-" + LocalDateTime.now() + ".zip";

            if(f.isDirectory()){
                System.out.println(f.getAbsolutePath());
                ZipUtils zipDirectory = new ZipUtils(f.getAbsolutePath());
                zipDirectory.generateFileList(new File(f.getAbsolutePath()));
                zipDirectory.zipIt(OUTPUT_ZIP_FILE);
                
                name = "Upload-" + new File(OUTPUT_ZIP_FILE).getName();
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

            f = new File(OUTPUT_ZIP_FILE);
            f.delete();

            dis.close();
            dos.close();
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
