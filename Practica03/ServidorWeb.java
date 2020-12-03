import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorWeb
{
	public static final int PUERTO=8000;
	ServerSocket ss;
		
		class Manejador extends Thread
		{
			protected Socket socket;
			protected PrintWriter pw;
			protected BufferedOutputStream bos;
			protected BufferedReader br;
			protected String FileName;
			
			public Manejador(Socket _socket) throws Exception
			{
				this.socket=_socket;
			}
			
			public void run()
			{
				try{
					br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
					bos=new BufferedOutputStream(socket.getOutputStream());
					pw=new PrintWriter(new OutputStreamWriter(bos));
					String line=br.readLine();
					if(line==null)
					{
						pw.print("<html><head><title>Servidor WEB");
						pw.print("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
						pw.print("</body></html>");
						socket.close();
						return;
					}
					System.out.println("\n\n________________________________________________");
					System.out.println("Server: LopezMeza Server/1.0");
					System.out.println("Cliente Conectado desde: "+socket.getInetAddress());
					System.out.println("Fecha: "+java.time.LocalDate.now()+"\nHora: "+java.time.LocalTime.now()); 
					System.out.println("Por el puerto: "+socket.getPort());					
					if(line.toUpperCase().startsWith("GET"))
					{
						if(line.indexOf("?")==-1)
						{
							getArch(line);
							if(FileName.compareTo("")==0)
								FileName = "index.htm";
							
							System.out.println("Metodo: GET");
							System.out.println("Recurso solicitado: " + FileName);
							File f = new File(FileName);
							if(!(f.exists() && !f.isDirectory())) { 
							    System.out.println("El archivo no existe");
							    notFound404(FileName);
							    System.out.println("HTTP/1.0 404 Not Found");
							}
							else {
								System.out.println("HTTP/1.0 200 ok");
								System.out.println("El archivo existe");
								System.out.println("Content-Length: " + f.length());
								System.out.println("Content-Type: " + contentType(FileName));
								SendA(FileName);
							}
						}
					else {
						StringTokenizer tokens=new StringTokenizer(line,"?");
						String req_a=tokens.nextToken();
						String req=tokens.nextToken();
						System.out.println("Metodo: GET");
						System.out.println("Token1: "+req_a);
						System.out.println("Token2: "+req);
						pw.println("HTTP/1.0 200 Okay");
						pw.flush();
						pw.println();
						pw.flush();
						pw.print("<html><head><title>SERVIDOR WEB");
						pw.flush();
						pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
						pw.flush();
						pw.print("<h3><b>"+req+"</b></h3>");
						pw.flush();
						pw.print("</center></body></html>");
						pw.flush();
					}

					}
					else if(line.toUpperCase().startsWith("POST"))
					{
						
						String req = "";

						String params = "";
						while(params != null) {
							params = br.readLine();
							//saber si se ha terminado de leer el encabezado
							if (params.endsWith("--") || params.startsWith("Content-Length: 0")) {
								break;
							}
							else if (params.startsWith("Content-Disposition:")){

								String current = params.substring(params.indexOf("\""), params.lastIndexOf("\""));
								current = current.substring(1);
								req += current + "=";
								params = br.readLine();
								params = br.readLine();
								req += params + "\t";
							}
						}
						System.out.println("Metodo: POST");
						System.out.println("Datos recibidos: " + req);
						pw.println("HTTP/1.0 200 Okay");
						pw.flush();
						pw.println();
						pw.flush();
						pw.print("<html><head><title>SERVIDOR WEB");
						pw.flush();
						pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
						pw.flush();
						pw.print("<h3><b>"+req+"</b></h3>");
						pw.flush();
						pw.print("</center></body></html>");
						pw.flush();
					}
					else if(line.toUpperCase().startsWith("HEAD"))
					{
						System.out.println(line);
						System.out.println("Metodo: HEAD");
						String req = "";
						String params = "";
						pw.println("HTTP/1.0 200 Okay");
						pw.flush();
						pw.println();
						pw.flush();
						
						if(line.indexOf("?")==-1)
						{
							getArch(line);
							if(FileName.compareTo("")==0)
								FileName = "index.htm";
							System.out.println("avr " + FileName);
							System.out.println("Recurso solicitado: " + FileName);
							File f = new File(FileName);
							if(!(f.exists() && !f.isDirectory())) { 
							    System.out.println("El archivo no existe");
							    System.out.println("HTTP/1.0 404 Not Found");
							}
							else {
								System.out.println("HTTP/1.0 200 ok");
								System.out.println("El archivo existe");
								System.out.println("Content-Length: " + f.length());
								System.out.println("Content-Type: " + contentType(FileName));
								pw.print("Content-Type: " + contentType(FileName));
								pw.flush();
							}
						}
						/*
						while(params != null) 
						{
							params = br.readLine();
							//saber si se ha terminado de leer el encabezado
							if (params.endsWith("--") || params.startsWith("Content-Length: 0")) {
								break;
							}
							else if (params.startsWith("Content-Disposition:"))
							{

								String current = params.substring(params.indexOf("\""), params.lastIndexOf("\""));
								current = current.substring(1);
								req += current + "=";
								params = br.readLine();
								params = br.readLine();
								req = params;
								System.out.println("Recurso solicitado: " + req);
								File f = new File(req);
								if(!(f.exists() && !f.isDirectory())) { 
								    System.out.println("El archivo no existe");
								    notFound404(req);
								    notFound404(FileName);
								}
								else {
									System.out.println("El archivo existe");
									pw.println("HTTP/1.0 200 Okay");
								}
							}
						}
						*/
						notFound404(FileName);
						pw.print("Connection: Closed");
						pw.flush();
					}
					else if(line.toUpperCase().startsWith("DELETE"))
					{
						System.out.println(line);
						System.out.println("Metodo: DELETE");
						String req = "";
						String params = "";
						if(line.indexOf("?")==-1)
						{
							getArch(line);
							if(FileName.compareTo("")==0)
								FileName = "index.htm";
							System.out.println("avr " + FileName);
							System.out.println("Recurso solicitado: " + FileName);
							File f = new File(FileName);
							if(!(f.exists() && !f.isDirectory())) { 
							    System.out.println("El archivo no existe");
							    System.out.println("HTTP/1.0 404 Not Found");
							    notFound404(FileName);
							    pw.flush();
							}
							else {
								pw.println("HTTP/1.0 200 Okay");
								pw.flush();
								pw.println();
								pw.flush();
								System.out.println("HTTP/1.0 200 ok");
								System.out.println("El archivo existe");
								System.out.println("Content-Length: " + f.length());
								System.out.println("Content-Type: " + contentType(FileName));
								f.delete();
								pw.print("<html><head><title>SERVIDOR WEB");
								pw.flush();
								pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Archivo eliminado</br></h1>");
								pw.flush();
								pw.print("<h3><b>"+ FileName +"</b></h3>");
								pw.flush();
								pw.print("</center></body></html>");
								pw.flush();	

							}
						}
						pw.flush();
					}
					else
					{
						pw.println("HTTP/1.0 501 Not Implemented");
						pw.println();
					}
					pw.flush();
					bos.flush();
					System.out.println("________________________________________________\n\n");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				try{
					socket.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			public void getArch(String line)
			{
				int i;
				int f;
				if(line.toUpperCase().startsWith("GET") || line.toUpperCase().startsWith("HEAD") || line.toUpperCase().startsWith("DELETE"))
				{
					i=line.indexOf("/");
					f=line.indexOf(" ",i);
					FileName=line.substring(i+1,f);
				}
			}
			public void SendA(String fileName,Socket sc)
			{
				//System.out.println(fileName);
				int fSize = 0;
				byte[] buffer = new byte[4096];
				try{
					DataOutputStream out =new DataOutputStream(sc.getOutputStream());
					
					//sendHeader();
					FileInputStream f = new FileInputStream(fileName);
					int x = 0;
					while((x = f.read(buffer))>0)
					{
				//		System.out.println(x);
						out.write(buffer,0,x);
					}
					out.flush();
					f.close();
				}catch(FileNotFoundException e){
					//msg.printErr("Transaction::sendResponse():1", "El archivo no existe: " + fileName);
				}catch(IOException e){
		//			System.out.println(e.getMessage());
					//msg.printErr("Transaction::sendResponse():2", "Error en la lectura del archivo: " + fileName);
				}
				
			}
			public void notFound404(String fileName) {
						pw.println("HTTP/1.0 404 Not Found");
						pw.flush();
						pw.println();
						pw.flush();
						pw.print("<html><head><title>SERVIDOR WEB");
						pw.flush();
						pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Error 404: archivo no encontrado</br></h1>");
						pw.flush();
						pw.print("<h3><b>"+  fileName +"</b></h3>");
						pw.flush();
						pw.print("</center></body></html>");
						pw.flush();	
			}
			public String contentType(String fileName) {
				int i = fileName.lastIndexOf(".");
				String type = fileName.substring(i);
				if (type.startsWith(".htm")|| type.startsWith(".html")) // htm html
					type = "text/html";
				else if (type.startsWith(".pdf")) // pdf
					type = "application/pdf";
				else if (type.startsWith(".jpeg") || type.startsWith(".jpg"))// jpg
					type = "image/jpeg";
				return type;
			}
			public void SendA(String arg) 
			{
				//Checar extensión del archivo
				String extension = "";
				int indexPunto = 0;
				for (int i = arg.length() - 1; i >= 0; i--) {
					if (arg.charAt(i) == '.') {
						indexPunto = i; 
						break;
					}
				}
				for (int i = indexPunto; i < arg.length(); i++) {
					extension += arg.charAt(i);
				}

				try{
					 int b_leidos=0;
					 BufferedInputStream bis2=new BufferedInputStream(new FileInputStream(arg));
                     byte[] buf=new byte[1024];
                     int tam_bloque=0;
                     if(bis2.available()>=1024)
                     {
                        tam_bloque=1024;
                     }
                     else
                     {
                        bis2.available();
                     }
			
                     int tam_archivo=bis2.available();
		     /***********************************************/
				String sb = "";
				sb = sb+"HTTP/1.0 200 ok\n";
			        sb = sb +"Server: Axel Server/1.0 \n";
				sb = sb +"Date: " + new Date()+" \n";
				if (extension == "htm" || extension == "html") { // htm html
					sb = sb +"Content-Type: text/html \n";
					System.out.println("Content-Type: text/html");
				}
				else if (extension == "pdf") { // pdf
					sb = sb +"Content-Type: application/pdf \n";
					System.out.println("Content-Type: application/pdf");
				}
				else if (extension == "jpeg" || extension == "jpg") {// jpg
					sb = sb +"Content-Type: image/jpeg \n";
					System.out.println("Content-Type: image/jpeg");
				}
				sb = sb +"Content-Length: "+tam_archivo+" \n";
				sb = sb +"\n";
				bos.write(sb.getBytes());
				bos.flush();

				//out.println("HTTP/1.0 200 ok");
				//out.println("Server: Axel Server/1.0");
				//out.println("Date: " + new Date());
				//out.println("Content-Type: text/html");
				//out.println("Content-Length: " + mifichero.length());
				//out.println("\n");

		     /***********************************************/
			
                     while((b_leidos=bis2.read(buf,0,buf.length))!=-1)
                     {
                        bos.write(buf,0,b_leidos);
                        
                        
                     }
                     bos.flush();
                     bis2.close();
                     
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
				
			}
		}
		public ServidorWeb() throws Exception
		{
			System.out.println("[SERVIDOR INICIADO]");
			this.ss=new ServerSocket(PUERTO);
			System.out.println("[SERVIDOR INICIADO] OK");
			System.out.println("[ESPERANDO CONEX CLIENTE]");
			for(;;)
			{
				Socket accept=ss.accept();
				new Manejador(accept).start();
			}
		}
		
		
		
		public static void main(String[] args) throws Exception{
			ServidorWeb sWEB=new ServidorWeb();
		}
	
}