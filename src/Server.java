import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Server {

	private static final int sPort = 8000;   //The server will be listening on this port number
	private static final ArrayList<Integer> clientNumbers=new ArrayList<Integer>();
	private static final ArrayList<ObjectOutputStream> clientStreams = new ArrayList<ObjectOutputStream>();
	public static void main(String[] args) throws Exception {
		System.out.println("The server is running."); 
			
        	ServerSocket listener = new ServerSocket(sPort);
		int clientNum = 1;
        	try {
            		while(true) {
            			clientNumbers.add(clientNum);
                		new Handler(listener.accept(),clientNum).start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
            			}
        	} finally {
            		listener.close();
        	} 
 
    	}

	/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	private static class Handler extends Thread {
        	private String message;    //message received from the client
        	private Socket connection;
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out;    //stream write to the socket
        	private int no;		//The index number of the client

        	public Handler(Socket connection, int no) {
            		this.connection = connection;
            		this.no = no;
            		//clientNames.add("client"+no);
        	}

        public void run() {
 		try{
			//initialize Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			clientStreams.add(out);
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			/*final Thread serverListener = new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true){
						serverReceiveMethod(in);
						
					}
				}
			});
			serverListener.start();*/
			TimeUnit.SECONDS.sleep(2);
			while(true){
				try{
					String message = (String)in.readObject();
					String[] typeOfTask = parseMessage(message);
					//System.out.println("size of aarray: "+clientStreams.size());
					if(typeOfTask[0].equals("broadcastmessage")){
						//System.out.println("broadcasting message....");
						messageSender(this.no,clientStreams,typeOfTask[1]);
					}
				}catch(Exception e){
					
				}
			}
			
		}
		catch(IOException | InterruptedException ioException){
			System.out.println("Main try Disconnect with Client " + no);
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				connection.close();
			}
			catch(IOException ioException){
				System.out.println("Closing connection Disconnect with Client " + no);
			}
		}
	}

	
	
		private void messageSender(int no,ArrayList<ObjectOutputStream> clientstreams, String message2) {
			System.out.println("Current port no: "+no);
			//clientStreams.remove(no-1);
			int index = no-1;
			for(ObjectOutputStream o: clientStreams){
				
					if(o!=clientStreams.get(index))
					sendMessage(o,message2);
			}
		}

		private void sendMessage(ObjectOutputStream out, String message2){
			try{
				//System.out.println("message: "+message2);
				out.writeObject(message2);
				out.flush();
			}catch(Exception e){
				
			}
		}
		

		private String[] parseMessage(String message){
			String[] strings = message.split(" ");
			String typeOfMessage = strings[0]+strings[1];
			strings = message.split("\"");
			String actualMessage = strings[1];
			return new String[]{typeOfMessage,actualMessage};
		}
		
		
		/*private void serverReceiveMethod(ObjectInputStream in){
			try{
				//System.out.println("server receieving...");
				message = (String)in.readObject();
				
				parseMessage(message);
				
				
				//System.out.println("Receive message: " + message + " from client " + no);
			}catch(Exception e){
				
			}
		}*/
		
		
    }
    	
    	//send a message to the output stream
    	public void sendMessage(ObjectOutputStream out,ArrayList<ObjectOutputStream> clientStreams,String msg)
    	{
    		try{
    			out.writeObject(msg);
    			out.flush();
    			//System.out.println("Send message: " + msg + " to Client " + no);
    		}
    		catch(IOException ioException){
    			ioException.printStackTrace();
    			}
    	}
    	

}