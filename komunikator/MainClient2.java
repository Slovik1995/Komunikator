package komunikator;

import java.io.IOException;

public class MainClient2 {
	public static void main(String[] arg) throws IOException{
		Thread t = new Thread(){
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
		t.start();
	}
}
