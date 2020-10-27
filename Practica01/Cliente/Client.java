package Cliente;

import Directory.Directory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;

public class Client {
    private int pto;
    private String host;
    private Socket cl;
    private JFileChooser jfc;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectInputStream ois;

    public Client(String host, int pto) { // Constructor
        this.host = host;
        this.pto = pto;
    }

    public Directory receiveDirectory() { // Recibir un directorio
        Directory d = new Directory(null);
        try {
            this.cl = new Socket(this.host, this.pto);
            this.ois = new ObjectInputStream(cl.getInputStream());
            Directory dir = (Directory) ois.readObject();
            this.ois.close();
            this.cl.close();
            return dir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public void sendAction(int opt){                                //  Envia una accion a realizar
        try {
            this.cl = new Socket(this.host, this.pto);
            this.dos = new DataOutputStream(cl.getOutputStream());
            this.dos.writeInt(opt);
            this.dos.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean receiveConfirm(){         
        boolean aux = false;
        try {
            this.cl = new Socket(this.host, this.pto);
            this.dis = new DataInputStream(cl.getInputStream());
            aux = dis.readBoolean();
            this.dos.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aux;
    }

    public void zipDir(String dir, ZipOutputStream zos){            // Comprimir directorio
        try {
            File zipD = new File(dir);
            String[] dirList = zipD.list();
            byte[] readBuffer = new byte[1024];
            int bytesIn = 0;

            for(int i = 0; i < dirList.length; ++i){            
                File f = new File(zipD, dirList[i]);            
                
                if(f.isDirectory()){                            
                    String filePath = f.getPath(); 
                    zipDir(filePath, zos);                      
                    continue; 
                } 
                
                FileInputStream fis = new FileInputStream(f);   
                ZipEntry anEntry = new ZipEntry(f.getPath()); 
                zos.putNextEntry(anEntry); 
                
                while((bytesIn = fis.read(readBuffer)) != -1) { 
                    zos.write(readBuffer, 0, bytesIn); 
                } 
                
                fis.close(); 
            }           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDir(String dir){                                        //  Enviar string
        try{
            this.cl = new Socket(this.host, this.pto);
            this.dos = new DataOutputStream(cl.getOutputStream());
            this.dos.writeUTF(dir);
            this.dos.flush();
            this.dos.close();
            this.cl.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public boolean reciveFile(){                                               //  Recibir un archivo
        try{
            this.cl = new Socket(this.host, this.pto);
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            String name = dis.readUTF();
            long size = dis.readLong();
            
            if(name.equals("") && size == 0){
                dis.close();
                cl.close();
                return false;
            }
                

            System.out.println("Archivo: " + name + "\nLongitud: " + size + "\nDireccion: " + cl.getInetAddress() + ":" + cl.getLocalPort());
            
            DataOutputStream dos = new DataOutputStream (new FileOutputStream(name));
            long rec = 0;
            int n = 0, por = 0;

            while( rec < size){
                byte[] b = new byte[3000]; 
                n = dis.read(b);
                dos.write(b,0,n);
                dos.flush();
                rec = rec +n;
                por = (int)((rec*100)/size);
                System.out.print("\rRecibe el " + por + " % del archivo");
            }

            File f = new File(""); 
            String newPath = f.getAbsolutePath();
            System.out.println("Archivo recibido y descargado en la carpeta" + newPath);
            dos.close();
            dis.close();
            cl.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public void uploadFile(){                                               //  Subir un archivo
        try{
            this.cl = new Socket(this.host, this.pto);
            this.jfc = new JFileChooser();
            this.jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int r = jfc.showOpenDialog(null);
            
            if (r == JFileChooser.APPROVE_OPTION){
                String name = "";
                String path = "";
                long size = 0;
                File f = jfc.getSelectedFile();

                String OUTPUT_ZIP_FILE = f.getName() + ".zip";

                if(f.isDirectory()){
                    System.out.println(f.getAbsolutePath());
                    ZipUtils zipDirectory = new ZipUtils(f.getAbsolutePath());
                    zipDirectory.generateFileList(new File(f.getAbsolutePath()));
                    zipDirectory.zipIt(OUTPUT_ZIP_FILE);

                    name = new File(OUTPUT_ZIP_FILE).getName();
                    size = new File(OUTPUT_ZIP_FILE).length();
                    path = new File(OUTPUT_ZIP_FILE).getAbsolutePath();
                }else{
                    name = f.getName();
                    size = f.length();
                    path = f.getAbsolutePath();
                }
                    
                this.dos = new DataOutputStream( cl.getOutputStream() );
                this.dis = new DataInputStream(new FileInputStream(path));
                this.dos.writeUTF(name);
                this.dos.flush();
                this.dos.writeLong(size);
                this.dos.flush();
                int n, por;
                long aux = 0;

                while(aux < size){
                    byte[] b = new byte[3000];
                    n = this.dis.read(b);
                    this.dos.write(b,0,n);
                    this.dos.flush();
                    aux += n;
                    por = (int)((aux*100)/size);
                    System.out.print("\rPorcentaje enviado " + por + "% \nDireccion: " + path);
                }

                f = new File(OUTPUT_ZIP_FILE);
                f.delete();

                this.dis.close();
                this.dos.close();
                this.cl.close();
            }
            else{
                System.out.println("Abortar operacion");
                this.dos = new DataOutputStream(cl.getOutputStream());
                this.dos.writeUTF("");
                this.dos.flush();
                this.dos.writeLong(0);
                this.dos.flush();
                this.dos.close();
                this.cl.close();
            }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

}
