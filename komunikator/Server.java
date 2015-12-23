package komunikator;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Server{
int host = 4444;

ServerSocket serverSocket;
PrintWriter out; 
BufferedReader in;

public Server() throws IOException, ClassNotFoundException, SQLException
{
	System.out.println("Server connected.");
	takePort(host);
	System.out.println("Server disconnected.");
}

LinkedList<User> lista = new LinkedList<User>();

public void takePort(int port) throws IOException, ClassNotFoundException, SQLException{
try {
    ServerSocket serverSocket = new ServerSocket(port);
    while(true){
    Socket clientSocket = serverSocket.accept();
    SubServer s = new SubServer(clientSocket,lista);
    Thread t = new Thread(s);
    t.start();
    }
}
catch(Exception e){
	e.printStackTrace();
} 
}

}

class KnockKnockProtocol{
	//int counter=0;
	
	public String processInput(String s){
	//	int c = counter++;
		return s;
	};
	
}
