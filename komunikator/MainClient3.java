package komunikator;


import java.io.IOException;

public class MainClient3 {
		public static void main(String[] arg) throws IOException, InterruptedException{
		
			Thread t = new Thread(){
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
				t.start();
			}
}
	

