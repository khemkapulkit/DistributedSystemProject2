import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import org.omg.CORBA.portable.OutputStream;

public class ClientConnection implements Runnable 
{
    private Message message;
    public NodeDetails nodeDetailsObj;
    public NodeDetails serverNodeDetailsObj;
    
    public NodeObject nodeObj;
    
    public Socket soc;
    
    
    public ClientConnection(Socket soc,Message message)//NodeDetails nodeDetailsObj, ApplicationMessage message, NodeDetails serverNode) 
    {
    	/*this.nodeDetailsObj = nodeDetailsObj;
    	this.message = message;
    	this.serverNodeDetailsObj= serverNode;*/
    	//this.nodeObj = nodeObject;
    	this.message = message;
    	this.soc = soc;
    	
    }

    public void run() 
    {
    	/*try 
        {
    		Thread.sleep(3000);
    	} 
        catch (InterruptedException e) 
        {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}*/
    	/*Socket clientSocket;
        ObjectOutputStream outputStream;
		try {
			
			InetAddress inetServer = InetAddress.getByName(serverNodeDetailsObj.nodeHostName);
			InetAddress inetClient = InetAddress.getByName(nodeDetailsObj.nodeHostName);
			System.out.println("~~~~~~~~~~~~inetServer" + inetServer + inetServer.getHostAddress() + inetServer.getHostName() );
			System.out.println("~~~~~~~~~~~~inetClient" + inetClient + inetClient.getHostAddress() + inetClient.getHostName() );
			
			clientSocket = new Socket(inetClient, nodeDetailsObj.nodePortNumber);
			
			System.out.println("~~~~~~~~~~~~clientSocket" + clientSocket.getPort() +"~~~"+ clientSocket.getInetAddress() );
			
				//new Socket(nodeDetailsObj.nodeHostName,nodeDetailsObj.nodePortNumber, inetServer, serverNodeDetailsObj.nodePortNumber);
					//Socket(nodeDetailsObj.nodeHostName, nodeDetailsObj.nodePortNumber, serverNodeDetailsObj.nodeHostName, serverNodeDetailsObj.nodePortNumber);
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(message);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	try {
			
				//ObjectOutputStream outputStream = null;// = new ObjectOutputStream(null);
				//System.out.println("~~~~~~~~~~~~~Socket" + soc);
				
				//System.out.println("~~~~~~~~~~~~~~Writting Msg with type - " +message.type);
	    						
				if(soc != null)
				{
					//System.out.println("Writting to Socket - " + soc.getInetAddress() + " port # "+soc.getLocalPort() +" is connected - " +soc.isConnected() + " is closed - " + soc.isClosed());
					//System.out.println("~~~~~~~~~~~~~SocketInetAddress" + soc.getInetAddress());
					ObjectOutputStream outputStream = new ObjectOutputStream(soc.getOutputStream());
					outputStream.writeObject(message);
		            //outputStream.flush();
		            //outputStream.reset();
				}
			
						
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//System.out.println("~~~~~~~~~soc" + soc + soc.isClosed() + soc.isConnected());
			//e1.printStackTrace();
		}
    }
}
