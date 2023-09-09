import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread{
	// private variables for the server thread 
	private PrintWriter pw;
	private PrintStream ps;
	private BufferedReader br;
	private Server server;
	private Socket socket;	
	
	public ServerThread(Socket s, Server server) {
		try {
			// to do --> store them somewhere, you will need them later 
			this.server = server;
			ps = new PrintStream(new BufferedOutputStream(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));	
			
			// to do --> complete the implementation for the constructor
			// to do --> start socket connection
			socket = s;
			this.start();
			
			
			
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}

	// to do --> what method are we missing? Implement the missing method 
	public void send404(PrintStream out)
	{
		try {
			//trim chops off whitespace from start and end
			//don't need to trim from filePath bc is guaranteed to be proper format since asked Java to give it
			FileReader fr = new FileReader("404.html");
			
			// to do --> read in file through input stream
			BufferedReader reader = new BufferedReader(fr);
			
			//create the response header based on the filetype
			String responseHeader = "HTTP/1.1 200 OK\r\n" + 
					"Content-Type: " + "text/html" + "\r\n\r\n";	
			
			out.print(responseHeader);
			
			int content;
			
			// to do --> then pass the stream of the image from the input stream to the print stream byte by byte				
			// to do --> while loop to write out bytes
			while ((content = reader.read()) != -1) {
			    out.write(content);
			}
			
		}	
		catch (IOException ioe) {
		
			System.out.println("ioe in ServerThread fileIO " + ioe.getMessage());
		}
	}
		
	public void run() {
		try {
			//grab the request from the server
			String header = br.readLine();
			
			//find the name of the file requested
			String fileName = "";
			if (header != null && !header.equals("")) {
				fileName = header.substring(header.indexOf("GET /") + 5, header.indexOf("HTTP"));
				System.out.println(fileName);
			}
			
			//assign filetype for http response
			String fileType = "";	
			
			if (fileName.indexOf(".html") != -1) {
				fileType = "text/html";
			} else if (fileName.indexOf(".jpg") != -1)
			{
			// to do --> add sections for image/jpg. image/png, text/css
				fileType = "image/jpg";
			
			}
			else if (fileName.indexOf(".png") != -1)
			{
			// to do --> add sections for image/jpg. image/png, text/css
				fileType = "image/png";
			
			}
			else if (fileName.indexOf(".css") != -1)
			{
			// to do --> add sections for image/jpg. image/png, text/css
				fileType = "text/css";
			
			}
	
			//create the response header based on the filetype
			String responseHeader = "HTTP/1.1 200 OK\r\n" + 
					"Content-Type: " + fileType + "\r\n\r\n";	
			
			//handle files as byte streams (bc using normal streams)
			String filePath = new File("").getAbsolutePath();	
			//wrap the socket output stream in a print stream (like did in constructor, but here just want to do it again for some reason)
			PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
			try {
				//trim chops off whitespace from start and end
				//don't need to trim from filePath bc is guaranteed to be proper format since asked Java to give it
				InputStream fileIn = new FileInputStream(filePath + "/" + fileName.trim());
				
				// to do --> read in file through input stream
				BufferedInputStream reader = new BufferedInputStream(fileIn);
				
				// to do --> first print the header
				out.print(responseHeader);
				
				int content;
				
				// to do --> then pass the stream of the image from the input stream to the print stream byte by byte				
				// to do --> while loop to write out bytes
				while ((content = reader.read()) != -1) {
				    out.write(content);
				}
				
			} catch(FileNotFoundException e) {
				// to do --> If file wasn't found, display 404 error
				send404(out);
			}
			catch (IOException ioe) {
			
				System.out.println("ioe in ServerThread fileIO " + ioe.getMessage());
			} 
			finally
			{
				out.flush();
				out.close();
			}
		}
		catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());	
		}
	}
}
