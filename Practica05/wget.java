import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class wget {

    static void findLinks(Set<String> links, String dirname, String parentName, String space, String path) {
        try {
            System.out.println(dirname);
            Reader r = null;
            URL pageURL = new URL(dirname);
            InputStream in = pageURL.openStream();
            downloadResorce(dirname, path, "index.html");
            
            r = new InputStreamReader(in);
            ParserDelegator hp = new ParserDelegator();
            hp.parse(r, new HTMLEditorKit.ParserCallback() {
                public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
                    
                    if (t == HTML.Tag.A) {
                        
                        Enumeration attrNames = a.getAttributeNames();
                        while (attrNames.hasMoreElements()) {
                            
                            Object key = attrNames.nextElement();
                            String actualName = (String) a.getAttribute(key);
                            String auxLink = makeLink(dirname, actualName);
                            
                            if ("href".equals(key.toString()) && linkViable(actualName, parentName) && !links.contains(auxLink)) {
                                
                                System.out.println(actualName);
                                links.add(auxLink);
                                System.out.println(space + auxLink);

                                if(isResource(actualName)){
                                    downloadResorce(auxLink, path, onlyName(actualName));
                                }else{
                                    System.out.println("Carpeta");
                                    File f = new File(path, actualName);
                                    f.mkdir();
                                
                                    findLinks(links, makeLink(dirname, actualName), dirname, space + "----|", path + actualName);
                                }
                            }
                        }
                    }
                }
            }, true);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void downloadResorce(String urlReource, String path, String filename){
        try{
            System.out.println("    Dirección: " + path + "\n    Descargar " + filename + "\n");
            BufferedInputStream in = new BufferedInputStream(new URL(urlReource).openStream());
            File f = new File(path + "/" + filename);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            
            fileOutputStream.close();
        } catch (IOException e) {
            // handle exception
        }
    }

    static String pDirname(String s) {
        for (int i = s.length() - 2; i > 0; i--) {
            if (s.charAt(i) == '/')
                return s.substring(0, i + 1);
        }
        return "";
    }

    static String pServer(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '/')
                return s.substring(0, i);
        }
        return "";
    }

    static boolean linkViable(String s, String a) {
        if(s.contains("//"))    return false;
        if(s.contains("@"))     return false;            
        if(s.contains("#"))     return false;
        if(s.contains("\\"))     return false;
        if(a.contains(s) || a.contains("/" + s))
            return false;
        if(s.charAt(0) == '#' || s.charAt(0) == '?')
            return false;
        return true;
    }

    static String makeLink(String s, String a){
        if(s.contains(a) || s.contains("/" + a))
            return "";
        if(a.charAt(0) == '/')
            return s + a.substring(1);
        else
            return s + a;
    }

    static boolean isResource(String s){
        if(s.contains("."))
            return true;
        return false;
    }

    static String actualPath(String[] s){
        String auxPath = "";
        
        for(int i = 2; i < s.length; ++i){
            auxPath += s[i] + "/";
            File file = new File(auxPath);
            file.mkdirs();
        }
        
        return auxPath;
    }

    static String onlyName(String s){
        if(s.contains("/")){
            if(s.charAt(0) == '/' && s.charAt(s.length()-1) == '/')
                return s.substring(1, s.length()-1);
            if(s.charAt(0) == '/')
                return s.substring(1);
            if(s.charAt(s.length()-1) == '/')
                return s.substring(0, s.length()-1);
        }
        return s;
    }

    public static void main(String[] args) {
        int page = 0;
        String dirname = "http://148.204.58.221/axel/aplicaciones/sockets/java/canales/";
        String parentName = pDirname(dirname);
        String[] server1 = dirname.split("/");
        String serverName = server1[0] + "//" + server1[2] + "/";
        String path = actualPath(server1);

        System.out.println("Enlace en donde se descargaran recursos: " + dirname);
        System.out.println("Dirección anterior: " + parentName);
        System.out.println("Dirección del servidor: " + serverName + "\n");

        Hashtable<Integer, String> allLinks = new Hashtable<>();
        Set<String> links = new HashSet<String>(); 

        links.add(dirname);
        links.add(parentName);
        links.add(serverName);
        allLinks.put(page, dirname);
        allLinks.put(page, parentName);
        allLinks.put(page, serverName);

        findLinks(links, dirname, parentName, "----|----|", path);
    }
    
}
