import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Client {
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE;                //capitalized message read from the server
	BufferedReader br;
	String clientName;
	int portNo;
	
	public Client(String clientName, int portNo) {
		this.clientName = clientName;
		this.portNo = portNo;
	}

	
	void run()
	{
		try{
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", 8000);
			System.out.println("Connected to localhost in port 8000");
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			//get Input from standard input
			br = new BufferedReader(new InputStreamReader(System.in));
			/*while(true)
			{
				//System.out.print("Hello, please input a sentence: ");
				message = bufferedReader.readLine();
				//Send the sentence to the server
				sendMessage(message);
				//Receive the upperCase sentence from the server
				MESSAGE = (String)in.readObject();
				//show the message to the user
				System.out.println("Receive message: " + MESSAGE);
				
			}*/
			
			final Thread sendThread = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					System.out.println("in send thread");
					
						while(true)
						{
							sendThreadMethod();
						}	
					
				}
				
			});
			sendThread.start();
			final Thread receiveThread = new Thread(new Runnable(){

				@Override
				public void run() {
					System.out.println("in receive thread");
				
						while(true){
							receiveThreadMethod();
						}
					
					// TODO Auto-generated method stub
					
				}
				
			});
			receiveThread.start();
			
			TimeUnit.MINUTES.sleep(2);
			
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		
		catch(IOException ioException){
			ioException.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			//Close connections
			try{
				
				System.out.println("finally/....");
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	//send a message to the output stream
	
	private void sendThreadMethod(){
		try{
			
			message = br.readLine();
			//System.out.println(message);
			//Send the sentence to the server
			
			out.writeObject(message);
			//System.out.println("sdfsdf");
			out.flush();
			//sendMessage(message);
			
		}
		catch(Exception e){
			
		}
	}
	
	private void receiveThreadMethod(){
		try{
			
			 String msg = (String)in.readObject();
			 System.out.println("Message from server: "+msg);
			 
				//MESSAGE = (String)in.readObject();
				
		}
		catch(Exception e){
			
		}
	}
	
	void sendMessage(String msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
			//System.out.println("Send message: " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//main method
	public static void main(String args[])
	{
		Client client = new Client(args[0],Integer.parseInt(args[1]));
		client.run();
	}

}