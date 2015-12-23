package komunikator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	String fromServer, fromUser;
	String hostName = "localhost";
	int portNumber;
	
	PrintWriter out=null;
	BufferedReader in=null;
	Socket mykkSocket=null;
	String myContacts=null, history=null;
	String name;
	String surname;
	String nick;

	public void logIn(){
		try {
			    mykkSocket = new Socket(hostName, portNumber);
			    SubClient c = new SubClient(mykkSocket,name,surname, myContacts,nick, history);
			    Thread t = new Thread(c);
			    t.start();
			}
		catch(Exception e){
			e.printStackTrace();
		}
	
	}
	public Client(String name, String surname, int portnr, String nic) throws IOException
	{
		myContacts="C:\\Users\\Jack\\Desktop\\czat\\ksiazkaTelefoniczna\\"+name+surname+".txt";
		history = "C:\\Users\\Jack\\Desktop\\czat\\"+name+surname;
		this.name=name;
		this.surname=surname;
		this.portNumber=portnr;
		this.nick=nic;
		logIn();
	}
	
}

