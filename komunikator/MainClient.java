package komunikator;

import java.io.IOException;
import java.sql.SQLException;

public class MainClient {
	public static void main(String[] arg) throws IOException, ClassNotFoundException, SQLException{
		
		Thread t = new Thread(){
			public void run(){
		
			Client c1=null;
			try {
				c1 = new Client("Piotr", "Rosa", 4444,"PR");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			};
			
			t.start();
		
	Thread t2 = new Thread(){
		 Client c2=null;
		public void run(){
	try {
		c2 = new Client("Jan", "Jaworz",4444, "JJ");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	};
	t2.start();
	Thread t3 = new Thread(){
		public void run(){
	
		Client c1=null;
		try {
			c1 = new Client("Michal", "Nowak", 4444,"MN");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		};			
		t3.start();
	}
	
	
	
}
