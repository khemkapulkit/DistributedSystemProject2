import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

public class SendMessage implements Runnable 
{
    private Message message;
    public NodeDetails nodeDetailsObj;
    public NodeDetails serverNodeDetailsObj;
    
    public NodeObject nodeObject;
    public int neighborId;
    
    
    public SendMessage(Message message,NodeObject nodeObject, int neighborId) 
    {
    	this.nodeObject = nodeObject;
    	this.message = message;
    	this.neighborId = neighborId;
    }

    public void run() 
    {
    	NodeDetails neighborNodeObj = nodeObject.nodeHashMap.get(neighborId);
		InetAddress inetClient;
		try {
			inetClient = InetAddress.getByName(nodeObject.nodeHashMap.get(neighborId).nodeHostName);
			/*if(message.type == "Converge")
			{
				System.out.println("~~~~~~~~~~~~inetClient" + inetClient);
				System.out.println("~~~~~~~~~~~~socket" + nodeObject.socketObjMap.get(inetClient));
			}*/
			
			ClientConnection clientTokenProcesser = new ClientConnection(nodeObject.socketObjMap.get(inetClient),message);
			new Thread(clientTokenProcesser).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
