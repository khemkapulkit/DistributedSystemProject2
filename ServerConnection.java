import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
	import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
	import java.net.ServerSocket;
	import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

public class ServerConnection implements Runnable 
{
	public PrintWriter writer = null;
	public NodeObject nodeObject;
	public HashMap<Integer, NodeDetails> nodeHashMap;
	public int serverPortNumber;
	public int randomNumber;
	public int tokensSize;
	public static String filename;
	
	public ServerConnection(NodeObject nodeObj, String configFileName) throws IOException 
	{
		String configFileNameWOExt = null;
		this.nodeObject = nodeObj;
		this.serverPortNumber = nodeObj.nodeDetails.nodePortNumber;
		this.randomNumber = nodeObj.nodeRandomNumber;
		this.nodeHashMap = nodeObj.nodeHashMap;
		//this.tokensSize = nodeObj.tokens.tokensList.size();
		//if (configFileName.indexOf(".") > 0)
	    //	configFileNameWOExt = configFileName.substring(0, configFileName.lastIndexOf("."));
		//this.writer = new PrintWriter(new BufferedWriter(new FileWriter(configFileNameWOExt+"-mrk140230-"+nodeObject.nodeId+".out", true)));
		if (configFileName.indexOf(".") > 0)
	    	configFileNameWOExt = configFileName.substring(0, configFileName.lastIndexOf("."));
		filename = configFileNameWOExt+"-"+nodeObject.nodeId+".out";
		writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		writer.close();
	}

    public void run() 
    {
        int tokensVisited = 0;
        try 
        {
        	/*
        	 *  Open ServerSocket on running node...
        	 */
        	InetAddress inetServer = InetAddress.getByName(nodeObject.nodeDetails.nodeHostName);
        	ServerSocket serverSocket = new ServerSocket(serverPortNumber, 0, inetServer);
        	//System.out.println("Server port for Node -" + nodeObject.nodeId + " is " + serverSocket.getLocalPort() + "~~~" + serverSocket.getInetAddress());
        	//System.out.println("Server port for Node -" + nodeObject.nodeId + " is " + serverSocket.getLocalSocketAddress() + "~~~" + serverSocket.getReuseAddress());
        	
        	try 
            {
        		Thread.sleep(1100);
        	} 
            catch (InterruptedException e) 
            {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        	for(int i=0; i < nodeObject.neighborObject.neighbors.size(); i++)
            {
				try{    	
					Socket clientSocket = serverSocket.accept();
					
					//nodeObject.socketObjMap.put(neighborNodeId, clientSocket);

	                //System.out.println("node " + nodeObject.nodeId + " accepted socket"+ clientSocket.getInetAddress() +"~~"+ clientSocket.getLocalPort() +"~~"+ clientSocket.getPort());
	                //System.out.println("node " + nodeObject.nodeId + " accepted socket"+ clientSocket.getRemoteSocketAddress() +"~~"+ clientSocket.getLocalSocketAddress() +"~~"+ clientSocket.getReuseAddress());
	                MessageProcesser messageProcesser = new MessageProcesser(nodeObject,clientSocket);
	        		new Thread(messageProcesser).start();
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
            }
        	
        	//System.out.println("Reverse Map - " + nodeObject.reverseSocketObjMap);
            /*while(true)
            {
            	Socket clientSocket = serverSocket.accept();
                System.out.println("node " + nodeObject.nodeId + " accepted socket");
                MessageProcesser messageProcesser = new MessageProcesser(nodeObject,clientSocket);
        		new Thread(messageProcesser).start();	
            }*/
            
    		
        } 
        catch (IOException e) 
        {
            System.out.println("Error in opening port: " +serverPortNumber+" on" + nodeObject.nodeDetails.nodeHostName);
        	e.printStackTrace();
        }
    }
}

