package komunikator;

import java.net.Socket;

public class User {
   private String name;
   private String surname;
   private String nick;
   private Socket socket;
   
public User(String n, String s, Socket so,String nic){
	name=n;
	surname=s;
	socket=so;
	nick=nic;
}
   
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getSurname() {
	return surname;
}
public void setSurname(String surname) {
	this.surname = surname;
}
public Socket getSocket() {
	return socket;
}
public void setSocket(Socket socket) {
	this.socket = socket;
}
public String getNick() {
	return nick;
}
public void setNick(String nic) {
	this.nick = nic;
}   
   
   
}
