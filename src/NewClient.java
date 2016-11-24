import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class NewClient implements Runnable {

	protected Socket clientSocket = null;
    protected String serverText   = null;
    private BufferedReader br;

    public NewClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
        //this.serverText   = serverText;
    }

    public void run() {
        while(true){
    	try {
        	br = new BufferedReader(new InputStreamReader(System.in));
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            String text = br.readLine();
            /*output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
this.serverText + " - " +
time +
"").getBytes());
      */    output.flush();output.write(text.getBytes());  
            /*output.close();
            input.close();*/
            //System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        	}
        }
    }
    
    public static void main(String[] args) throws IOException{
    	Socket clientSocket = new Socket("localhost",1234);
		NewClient client = new NewClient(clientSocket);
		client.run();
	}
}
