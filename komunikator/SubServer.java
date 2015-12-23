package komunikator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class SubServer implements Runnable{
	PrintWriter out;
	BufferedReader in;
    Socket sock;
    String name, surname,nick;
    LinkedList<User> list=null;
    User user;
    
    static LinkedList<String> waitingMessages = new LinkedList<String>();
    
	public SubServer(Socket s, LinkedList<User> lista){
		sock=s;
		list=lista;
	}
	
	private void checkWaitingMessages(User u)
	{
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(u.getSocket().getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> it = waitingMessages.iterator();
		while(it.hasNext())
		{	
			String s = it.next();
			if(s.startsWith(u.getNick()+":") || (s.startsWith(u.getName()+" "+u.getSurname())))
			{
				String data[] = s.split(":");
				s=s.substring(s.indexOf(":")+1);
				 send(s,pw);
			}
		}
	}
	
	public void run(){
		 try {
			out = new PrintWriter(sock.getOutputStream(), true);
			  in = new BufferedReader( new InputStreamReader(sock.getInputStream()));
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registerNewClient(in, out);
		
		listenForCommands(in, out);
		 
	}
private String search(String name, String surname) throws ClassNotFoundException, SQLException{
	Connection con=null;
		Scanner scan = new Scanner(System.in);
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/telefony","root","");
		PreparedStatement statement;
		ResultSet result;
				statement = con.prepareStatement("select * from contacts where name='"+name+"' and surname='"+surname+"'");
				result = statement.executeQuery();
				String s=null;
				
				while(result.next()){
					s=result.getString(3);
				}
			
		return s;
	}
private void removeFaultyMessages() throws ClassNotFoundException, SQLException{
	Connection con=null;
	Class.forName("com.mysql.jdbc.Driver");
	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/telefony","root","");
	PreparedStatement statement;
	ResultSet result;
	String[] data;
	for(int i=0; i<waitingMessages.size(); i++){
		data = waitingMessages.get(i).split(":");
			statement = con.prepareStatement("select * from contacts where number='"+data[0]+"'");
			result = statement.executeQuery();
			String s=null;
			if(!result.next()){
				waitingMessages.remove(i);
				i--;
			}
	}
}

private void login(String line, PrintWriter out){
	String[] dane = line.split(":");
	name=dane[1];
	surname=dane[2];
	user = new User(dane[1],dane[2],sock, dane[3]);
	list.add(user);
	line="$LOGGED IN.";
	send(line, out);
	checkWaitingMessages(user);
}

private void send(String line, PrintWriter out){
	char[] b = Hex.encodeHex(line.getBytes());
	line = new String(b);
	out.println(line);
	out.flush();
}

private void registerNewClient(BufferedReader in, PrintWriter out){
	 String line="$ACCEPTED";
	 send(line,out);
	 line="";
	try {
		line=in.readLine();
		line = new String(Hex.decodeHex(line.toCharArray()), StandardCharsets.UTF_8);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch (DecoderException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 
	if(line.startsWith("$LOGIN:"))
	{
		login(line,out);
	}
}

private void listenForCommands(BufferedReader in, PrintWriter out){
	String line="";
	 while(true){
	   try{
		   if(in.ready())
		   {
			   line=in.readLine();
			   line = new String(Hex.decodeHex(line.toCharArray()), StandardCharsets.UTF_8);
			   if(line.startsWith("$LOGOUT"))
			   {
				   logout(line);
				   break;
			   }
			   else if(line.startsWith("$FINDCONTACT:"))
			   {
				   findContact(line);
			   }
			   else
			   {   
				   String[] data = line.split(":");
				   Iterator<User> it = list.iterator();
				   boolean logged = false;
				   while(it.hasNext())
				   {
					   User user = it.next();
					   if(user.getNick().equals(data[0]) || (user.getName()+" "+user.getSurname()).equals(data[0]))
					   {
					   		line=line.substring(line.indexOf(":")+1);
					   		PrintWriter pw = new PrintWriter(user.getSocket().getOutputStream(), true);
					   		line=name+":"+surname+":"+line;
							   send(line,pw);
					   		logged=true;
					   		break;
					   }
				   }
				   if(logged==false)
				   {
					   line=line.substring(line.indexOf(":")+1);
					   waitingMessages.add(data[0]+":"+name+":"+surname+":"+line);
					   if(waitingMessages.size()%50==0)
					   {
						   removeFaultyMessages();
					   }
				   }
			   }
		   }
		
	   }catch(Exception e){
		   
	   }
	}
}
private void logout(String line){
	line="logged out.";
	   send(line,out);
	   list.remove(user);
}
private void findContact(String line){
	String data[] = line.split(":");
	   String number=null;
	try {
		number = search(data[1],data[2]);
	} catch (ClassNotFoundException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   if(number==null)
	   {
		   line="$NOMATCHINGCONTACT";
		   send(line,out);
	   }
	   else
	   {
		   send(number,out);
	   }
}
}

