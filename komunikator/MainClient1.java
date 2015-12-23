package komunikator;


import java.io.IOException;

public class MainClient1 {
		public static void main(String[] arg) throws IOException, InterruptedException{
		
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
			
}
}

