import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Project2 
{

 public static void main(String[] args) 
 {
	PrintWriter writer = null;
	String configFileName = null;
	String configFileNameWOExt = null;
	
	if(args.length == 0) 
    {
		System.exit(0);
    }
	
	/*
	 * 	Store passed argument as nodeId
	 */
    int nodeId = Integer.parseInt(args[0]);
    configFileName = args[1];
    
    if (configFileName.indexOf(".") > 0)
    	configFileNameWOExt = configFileName.substring(0, configFileName.lastIndexOf("."));
    
    /*
	 * 	Create instance of NodeObject class. This will store all the details for this particular node
	 */
    NodeObject nodeObj = new NodeObject(nodeId,configFileName);
    
    if(nodeId == 0)
    {
    	nodeObj.isActive = true;
    }
    
    System.out.println("Node Id - " + nodeObj.nodeId );
    System.out.println("Node Host Name - " + nodeObj.nodeDetails.nodeHostName );
    System.out.println("Node Port Number - " + nodeObj.nodeDetails.nodePortNumber );
    System.out.println("Node Neighbors - " + nodeObj.neighborObject.neighbors );
    /*System.out.println("Node minPerActive - " + nodeObj.minPerActive );
    System.out.println("Node maxPerActive - " + nodeObj.maxPerActive );
    System.out.println("Node minSendDelay - " + nodeObj.minSendDelay );
    System.out.println("Node snapshotDelay - " + nodeObj.snapshotDelay );
    System.out.println("Node maxNumber - " + nodeObj.maxNumber );
    System.out.println("Node Random Number - " + nodeObj.nodeRandomNumber );
    System.out.println("Node nodeHashMap - " + nodeObj.nodeHashMap );*/
    
    /*
     * Create new instance of 'ServerConnection' class with node specific details
     * When instance is created, run this connection as new thread. 
     */
    ServerConnection nodeAsServer;
    Thread nodeAsServerThread = null; 
	try {
		nodeAsServer = new ServerConnection(nodeObj,configFileName);
		nodeAsServerThread = new Thread(nodeAsServer);
		nodeAsServerThread.start();
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
    
    try 
    {
		Thread.sleep(1000);
	} 
    catch (InterruptedException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    /*
     * Create output file for this node....If file alreday exits it will over-write that file.
     * Add details about the node into the file.
     */
/*    try {
		writer = new PrintWriter(configFileNameWOExt+"-mrk140230-"+nodeId+".out", "UTF-8");
		writer.println("Net ID: mrk140230");//+ConfigReader.netId);
		writer.println("Node ID: "+nodeId);
		writer.println("Listening on "+nodeObj.nodeDetails.nodeHostName+":"+nodeObj.nodeDetails.nodePortNumber);
		writer.println("Random number: "+nodeObj.nodeRandomNumber);
		writer.close();
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
*/    
	
	/*
	 * Iterate all the available tokens starting from current node with current node as source.
	 */
	
	/*if(nodeObj.tokens.tokensList.size() == 0)
    {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(configFileNameWOExt+"-mrk140230-"+nodeId+".out", true)));
			System.out.println("All tokens received");
	        writer.println("All tokens received");
	        writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }*/
    for (Integer neighborNodeId : nodeObj.neighborObject.neighbors) 
    {
    	//System.out.println("Host Node - " + nodeId+ ". Connecting to neighbor - " + neighborNodeId);
    	/*
    	 * Then, create new client connection with next node.
    	 * And start it as new thread. 
    	 */
    	NodeDetails nextNodeObj = nodeObj.nodeHashMap.get(neighborNodeId);
    	Message messageToPass = new Message(nodeObj.nodeId, "Connect","Connecting now");
    	NodeObject.clock[nodeObj.nodeId]++;
		//System.out.print(" at Node - "+ nodeObj.nodeId+ " clock = " );
		//for(int i : NodeObject.clock ){  System.out.print(i +"  ");	}
		//System.out.println();
		messageToPass.ts = NodeObject.clock;
        ClientConnection clientConnect = null;
        Socket clientSocket;
		try {
			InetAddress inetClient = InetAddress.getByName(nextNodeObj.nodeHostName);
			//System.out.print(" inetClient" + inetClient );
			//clientSocket = new Socket(nextNodeObj.nodeHostName, nextNodeObj.nodePortNumber);
			clientSocket = new Socket(inetClient, nextNodeObj.nodePortNumber);
			nodeObj.socketObjMap.put(clientSocket.getInetAddress(), clientSocket);
			
			clientConnect = new ClientConnection(clientSocket,messageToPass);
			new Thread(clientConnect).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
    if(nodeId != 0)
    {
	    Message messageToPass = new Message(nodeObj.nodeId, "Connect","Connecting now");
		NodeObject.clock[nodeObj.nodeId]++;
		messageToPass.ts = NodeObject.clock;
	    ClientConnection clientConnect = null;
	    Socket clientSocket;
		try {
			InetAddress inetClient = InetAddress.getByName(nodeObj.nodeHashMap.get(0).nodeHostName);
			//System.out.print(" inetClient" + inetClient );
			//clientSocket = new Socket(nextNodeObj.nodeHostName, nextNodeObj.nodePortNumber);
			clientSocket = new Socket(inetClient, nodeObj.nodeHashMap.get(0).nodePortNumber);
			nodeObj.socketObjMap.put(clientSocket.getInetAddress(), clientSocket);
			
			clientConnect = new ClientConnection(clientSocket,messageToPass);
			new Thread(clientConnect).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    //System.out.println("~~~~~~~~~~~Socket Map for Node " + nodeObj.nodeId  + nodeObj.socketObjMap);
    	
    
    /*for (Integer neighborNodeId : nodeObj.neighborObject.neighbors) 
    {
    	 if(nodeObj.nodeId == 0)
    	 {
	    	Message messageToPass1 = new Message(nodeObj.nodeId, "Marker","C & L");
			messageToPass1.ts = NodeObject.clock;                	
			InetAddress inetClient;
			try {
				inetClient = InetAddress.getByName(nodeObj.nodeHashMap.get(neighborNodeId).nodeHostName);
				//ClientConnection clientTokenProcesser = new ClientConnection(nodeObj.socketObjMap.get(inetClient),messageToPass1);
				//new Thread(clientTokenProcesser).start();
				System.out.println("Sending Marker Message to - " + neighborNodeId + " from Node - "+ nodeObj.nodeId );
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	 }
    }*/
    
    try 
    {
		Thread.sleep(22000);
	} 
    catch (InterruptedException e) 
    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    /*if(nodeObj.nodeId == 0)
    {
    	nodeObj.isTerminated = true;
    	for(int nodeIdToTerminate : nodeObj.neighborObject.neighbors)
    	{
    		Message messageToPass1 = new Message(nodeObj.nodeId, "Finish","Terminate MAP Protocol");
    		messageToPass1.ts = NodeObject.clock;                	
    		InetAddress inetClient;
			try {
				inetClient = InetAddress.getByName(nodeObj.nodeHashMap.get(nodeIdToTerminate).nodeHostName);
				ClientConnection clientTokenProcesser = new ClientConnection(nodeObj.socketObjMap.get(inetClient),messageToPass1);
	    		new Thread(clientTokenProcesser).start();
	    		System.out.println("Sending Finish Message to - " + nodeIdToTerminate + " from Node - "+ nodeObj.nodeId );
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }*/
    
    
 }
}
