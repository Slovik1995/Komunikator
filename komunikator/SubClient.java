package komunikator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import javax.swing.*;
;
public class SubClient extends JFrame implements Runnable{
	Socket sock;
	String name, surname,nick,historyDirectory;
	PrintWriter out;
    BufferedReader in;
    BufferedReader stdIn;
    String myContacts;
    Scanner scaner;
    StringBuilder history = new StringBuilder();
    boolean condition=true;
    JTextArea text = new JTextArea();
    
    WindowAdapter exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
        	processInput("logout");
            }
     };
    
    
	public SubClient(Socket s,String n, String sur, String contacts,String nic, String hist){
		sock=s;
		name=n;
		surname=sur;
		myContacts=contacts;
		nick=nic;
		historyDirectory=hist;
		super.setSize(600,260);
		super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		super.setLayout(new FlowLayout());
		super.setTitle(name+" "+surname+" ("+nick+")");
		super.addWindowListener(exitListener);
		JPanel conversation = new JPanel();
		
		text.setSize(580,290);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		text.setEditable(false);
		text.setText("\n\n\n\n\n\n\n\n\n\n");
		conversation.add(text);
		conversation.setVisible(true);
		JTextField mymessage = new JTextField(50);
		mymessage.setEditable(true);
		StringBuilder historyText = new StringBuilder("\n\n\n\n\n\n\n\n\n\n");
		mymessage.addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar()=='\012'){
					int index = text.getText().indexOf("\n");
					if(mymessage.getText().equals("?help"))
					{
						int index2=0;
						for(int j=0; j<8; j++)
							index2 = text.getText().indexOf("\n", index2+1);
						text.setText(text.getText().substring(index2+1, text.getText().length()));
					}
					String str = processInput(mymessage.getText());
					if(str!=null)
					{
						text.setText(text.getText().substring(index+1,text.getText().length())+"\n"+str);
					}
					mymessage.setText("");
				}
			}
		});
		super.add(conversation);
		super.add(mymessage);
		super.setVisible(true);
		super.setResizable(false);
	}
	
	public void saveHistory(){
		try {
		String path = historyDirectory+System.currentTimeMillis()+".txt";
		File f = new File(path);
		BufferedWriter bw=null;
		
			bw = new BufferedWriter(new FileWriter(f));
		    Scanner scan = new Scanner(history.toString());
		    while(scan.hasNext())
		    {
		    	bw.write(scan.nextLine());
		    	bw.write(System.getProperty( "line.separator" ));
		    }
		    bw.flush();
		    bw.close();
		    scan.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("SAVING HISTORY TO FILE FAILED");
		}
	}
	public void run(){
		
		 try {
			out = new PrintWriter(sock.getOutputStream(), true);
			 in = new BufferedReader(new InputStreamReader(
			         sock.getInputStream()));
			 stdIn = new BufferedReader(new InputStreamReader(System.in));
		 } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		registerMe();
		String line;	
		Scanner scan = new Scanner(System.in);
		StringBuilder historyText = new StringBuilder();
		
		while(condition){
				try {
					if(in.ready())
					{
						line=in.readLine();
						try {
							
							line = new String(Hex.decodeHex(line.toCharArray()), StandardCharsets.UTF_8);
						} catch (DecoderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(line.equals("logged out."))
								break;
						String data[] = line.split(":");
						 line=line.substring(line.indexOf(":")+1);
						 line=line.substring(line.indexOf(":")+1);
						System.out.println(data[0]+" "+data[1]+": "+line);
						history.append(data[0]+" "+data[1]+": "+line+"\n");
						
						int index = text.getText().indexOf("\n");
						text.setText(text.getText().substring(
								index+1,text.getText().length())+"\n"+data[0]+
								" "+data[1]+": "+line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	////////////////////////////////////////////////////////////////////////////
public void addContact(String name, String surname, String number){
		
		String content=null;
		File f = new File(myContacts);
		Boolean exists = f.exists();
		if(exists){
		StringBuilder build = new StringBuilder();
		FileReader fr=null;
		try {
			fr = new FileReader(myContacts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String str;
		try {
			while((str=br.readLine())!=null){
				build.append(str);
				build.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		content = build.toString();
		}
		FileWriter fw=null;
		try {
			fw = new FileWriter(myContacts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		   try{
			   if(exists){
			   String[] tabl = content.split("#");
			   for(String s : tabl)
			   {
				   if(!s.equals("\n")&&(!s.equals("")))
				   {
					   bw.write(s);
					   bw.write("#");
					   bw.write(System.getProperty( "line.separator" ));
				   }
			   }
			   }
				bw.write(""+name+"@"+surname+"@"+number+"#");
				bw.write(System.getProperty( "line.separator" ));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		try {
			bw.flush();
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
}
	
	public String findContact(String name, String surname) throws IOException{
		String line="$FINDCONTACT:"+name+":"+surname;
		  send(line);
		String fromServer;
		while ((fromServer = in.readLine()) != null) {
			try {
				fromServer = new String(Hex.decodeHex(fromServer.toCharArray()), StandardCharsets.UTF_8);
			} catch (DecoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    if (fromServer.equals("$NOMATCHINGCONTACT"))
		    	return null;
		    else{
		    	
		    	return fromServer;
		    }   	
		}
		return null;	 
	}
	
	public String help(){
		return  "==============\n"+
				"?find name surname      - searching for the nick in the DB\n"+
				"?add name surname nick      - adding person to your contacts\n"+
				"PK: message     - the example of the message send to the person whose nick is PK\n"+
				"Name Surname: message     - you can also send messages in this way if there is only one such person in your contacts\n"+ 
				"other commands: logout\n"+
				"==============\n";
	}
	
	private void send(String line){
		 char[] b = Hex.encodeHex(line.getBytes());
			line = new String(b);
		out.println(line);
		out.flush();
	}
	private void registerMe(){
		String line="";
		try {
			line=in.readLine();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  try {
			line = new String(Hex.decodeHex(line.toCharArray()), StandardCharsets.UTF_8);
		} catch (DecoderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(line.startsWith("$ACCEPTED"))
		{
			line="$LOGIN:"+name+":"+surname+":"+nick;
			send(line);
			try {
				line=in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				line = new String(Hex.decodeHex(line.toCharArray()), StandardCharsets.UTF_8);
			} catch (DecoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(line.equals("$LOGGED IN."))
				System.out.println(name+" "+surname+" logged in.");
		}
	}
	
	private String processInput(String line){
		String line2="";	
		if(line.toLowerCase().equals("logout"))
			{
				line="$"+line.toUpperCase();
				send(line);
				saveHistory();
				System.out.println(name+" "+surname+" logged out.");
				condition=false;
				this.dispose();
				return name+" "+surname+" logged out.";
			}
			else if(line.startsWith("?"+"find "))
			{
				String data[] = line.split(" ");
				if(data.length!=3){
					System.out.println("ERROR IN QUERY.");
					return "ERROR IN QUERY.";
				}
				else{
					String contact="";
					try {
						contact = findContact(data[1], data[2]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(contact==null){
						System.out.println("THIS PERSON IS NOT REGISTERED IN DATABASE.");
						return "THIS PERSON IS NOT REGISTERED IN DATABASE.";
					}
					else{
						System.out.println("nick: "+contact);
						return "nick: "+contact;
					}
				}
			}
			else if(line.startsWith("?"+"add "))
			{
				String data[] = line.split(" ");
				if(data.length!=4){
					System.out.println("ERROR IN QUERY.");
					return "ERROR IN QUERY.";
				}
				else{
					addContact(data[1],data[2],data[3]);
					System.out.println(data[1]+" "+data[2]+" added to your contacts");
					return data[1]+" "+data[2]+" added to your contacts";
				}
			}
			else if(line.startsWith("?"+"help"))
			{
				String str = help();
				System.out.println(str);
				return str;
			}
			else
			{
				if(!line.equals("")&&(!line.startsWith("$"))&&(!line.startsWith(nick))){
					char[] b = Hex.encodeHex(line.getBytes());
					line2 = new String(b);
					out.println(line2);
					out.flush();
					
					String[] s = line.split(":");
					line=line.substring(line.indexOf(":")+1);
					history.append("Me to "+s[0]+": "+line+"\n");
					System.out.println("Me to "+s[0]+": "+line);
					return "Me to "+s[0]+": "+line;
				}
			}
		return null;
		}
}
