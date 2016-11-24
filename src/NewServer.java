import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class NewServer implements Runnable {
	 protected static int   serverPort   = 1234;
	    protected ServerSocket serverSocket = null;
	    protected boolean      isStopped    = false;
	    protected Thread       runningThread= null;
	    private BufferedReader br;
	    public NewServer(int port){
	        this.serverPort = port;
	    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(!isStopped()){
            Socket clientSocket = null;
            final InputStream input;  //= clientSocket.getInputStream();
            OutputStream output;
            final BufferedReader br;//= clientSocket.getOutputStream();
            try {
                clientSocket = this.serverSocket.accept();
                input  = clientSocket.getInputStream();
                output = clientSocket.getOutputStream();
                br = new BufferedReader(new InputStreamReader(System.in));
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            
            new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						System.out.println(input.read());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}})
            .start();
        }
        System.out.println("Server Stopped.") ;
	}
	private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port: "+this.serverPort
            		, e);
        }
    }
    public static void main(String[] args) {
    	NewServer server = new NewServer(serverPort);
    	new Thread(server).start();

    	try {
    	    Thread.sleep(20 * 1000);
    	} catch (InterruptedException e) {
    	    e.printStackTrace();
    	}
	}

}
