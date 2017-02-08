import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MessageProcesser implements Runnable 
{
    
	
	public NodeObject nodeObject;
	public Socket clientSocket;
    
    
    public MessageProcesser(NodeObject nodeObject, Socket clientSocket) 
    {
    	this.nodeObject = nodeObject;
    	this.clientSocket = clientSocket;
    	
    }

    public void run() 
    {
    	int cntTemp = 0; 
    	try 
        {
    		Thread.sleep(4000);
    	} 
        catch (InterruptedException e) 
        {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	while(true) 
    	{
    		cntTemp++;
    		try 
    		{
    			/*
            	 *  Accept client connection on previously opened serverSocket
            	 */
    			//System.out.println("Host Node - "+ nodeObject.nodeId +" is - " + nodeObject.isActive);
    			
    			
    			
    			//System.out.println("Host Node - "+ nodeObject.nodeId +" clientSocket " + clientSocket.getInetAddress() + " --- " + clientSocket.getPort());
    			
    			//System.out.println("Host Node - "+ nodeObject.nodeId +" clientSocket is  - " + clientSocket.getLocalPort() + clientSocket.getPort() + clientSocket.getInetAddress() +clientSocket.isClosed());
    			
    			/*
            	 * Read passed token from clientSocket
            	 */
    			//System.out.println("~~~~~~~~~~~~~~clientSocket" + clientSocket);
    			//System.out.println("~~~~~~~~~~~~~~clientSocket.getInputStream()" + clientSocket.getInputStream().toString());
    			ObjectInputStream inputToken = null;
    			if(inputToken == null)
    			{
    				inputToken = new ObjectInputStream(clientSocket.getInputStream());
    			}
    			
                Message message = (Message) inputToken.readObject();
                
                if((message != null && message.type != null && message.type.equals("Finish")))
                {
                	nodeObject.isTerminated = true;
                	System.out.println("Host Node - "+ nodeObject.nodeId +" Received message with type - " + message.type + " - " + message.message + " from -" + message.senderId) ;
                	for(int nodeIdToTerminate : nodeObject.neighborObject.neighbors)
                	{
                		Message messageToPass1 = new Message(nodeObject.nodeId, "Finish","Terminate MAP Protocol");
                		messageToPass1.ts = NodeObject.clock;
                		SendMessage msgSender = new SendMessage(messageToPass1, nodeObject, nodeIdToTerminate);
            			new Thread(msgSender).start();
            			
                		/*Message messageToPass1 = new Message(nodeObject.nodeId, "Finish","Terminate MAP Protocol");
                		messageToPass1.ts = NodeObject.clock;                	
                		InetAddress inetClient;
            			try {
            				inetClient = InetAddress.getByName(nodeObject.nodeHashMap.get(nodeIdToTerminate).nodeHostName);
            				ClientConnection clientTokenProcesser = new ClientConnection(nodeObject.socketObjMap.get(inetClient),messageToPass1);
            	    		new Thread(clientTokenProcesser).start();
            	    		System.out.println("Sending Finish Message to - " + nodeIdToTerminate + " from Node - "+ nodeObject.nodeId );
            			} catch (UnknownHostException e) {
            				// TODO Auto-generated catch block
            				//e.printStackTrace();
            			}*/
                	}
                		
                	return;
                }
                
                System.out.println("Host Node - "+ nodeObject.nodeId +" Received message with type - " + message.type + " - " + message.message + " from -" + message.senderId) ;
                //if((message != null && message.type != null && message.type.equals("Converge")))
                if((message != null && message.type != null && message.type == "Converge" ))// && !nodeObject.isTerminated)
                {
                	System.out.println("Converge Received Message from - " + message.senderId + " at Node - "+ nodeObject.nodeId +" of type - " + message.type );
                	
                	if(nodeObject.nodeId == 0)
                	{
                		System.out.println("Recieved Converge Message from Node - "+ message.NodeObject.nodeId );
                	}
                	else
                	{
                		ArrayList<Integer> treeneighbors = SpanningTree.SpanningTree.get(nodeObject.nodeId).neighbors;
                		for(int neighborNodeId : nodeObject.neighborObject.neighbors)
                    	{
                    		if(message.senderId != neighborNodeId)
                    		{
                    			System.out.println("passing Converge Message to - " + 0 + " from Node - "+ nodeObject.nodeId );
                    			Message messageToPass1 = new Message(nodeObject.nodeId, "Converge","Converge at Node 0");
                    			messageToPass1.NodeObject = nodeObject;
                    			SendMessage msgSender = new SendMessage(messageToPass1, nodeObject, neighborNodeId);
                    			new Thread(msgSender).start();
                    		}        	            		
                        }
                	}
                }
                
                if( ((message != null && message.type != null && message.type.equals("Application")) || nodeObject.isActive) && !nodeObject.isTerminated)
                {
                	if(message != null)
                	{
                		//System.out.println("Received Message from - " + message.senderId + " at Node - "+ nodeObject.nodeId +" of type - " + message.type );
                		//System.out.println("message.ts - " + message.ts);
                		
                		if(message.ts != null)
                		{
                			//for(int i : message.ts ){  System.out.print(i +"  ");	}
                			
                			for(int i=0; i<message.ts.length;i++)
                    		{
                    			NodeObject.clock[i] = Math.max(NodeObject.clock[i], message.ts[i]);
                    		}	
                		}	
                		
                		NodeObject.clock[nodeObject.nodeId]++;
                		/*System.out.print(" at Node - "+ nodeObject.nodeId+ " clock = " );
                		for(int i : NodeObject.clock ){  System.out.print(i +"  ");	}
                		System.out.println();*/
                	}
                
                	if(nodeObject.messageSent < nodeObject.maxNumber)
                    {
                    	nodeObject.isActive = true;
                    	System.out.println("Making Node - " + nodeObject.nodeId + " active");
                    }
                
                    while(nodeObject.isActive)
                    {
                    	Random r = new Random();
                    	int noOfMessagesToBeSent = nodeObject.minPerActive + r.nextInt(nodeObject.maxPerActive - nodeObject.minPerActive +1);
                    	
                    	//System.out.println("Host Node - "+ nodeObject.nodeId +" is - " + nodeObject.isActive + " in while");
                    	//System.out.println("Host Node - "+ nodeObject.nodeId +" is sending - " + noOfMessagesToBeSent + " messages");
                    	
                    	for(int cnt=0 ; cnt < noOfMessagesToBeSent ; cnt++)
                    		//nodeObject.messageSent < nodeObject.maxPerActive; nodeObject.messageSent++)
                        {
                    		nodeObject.messageSent++;
                    		
                    		if(nodeObject.messageSent <= nodeObject.maxNumber)
                    		{
                    			System.out.println("# of Messages sent - " + (nodeObject.messageSent) + " By Host - "+ nodeObject.nodeId);
                    			int randomNeighbor = r.nextInt(nodeObject.neighborObject.neighbors.size());
                    			
                    			//System.out.println("# of Messages sending neighbor size - " + nodeObject.neighborObject.neighbors.size());
                    			//System.out.println("# of Messages sending neighbor - " + nodeObject.neighborObject.neighbors);
                    			//System.out.println("# of Messages sending randomNeighbor - " + randomNeighbor);
                    			
                    			int neighborId = nodeObject.neighborObject.neighbors.get(randomNeighbor);
                    			
                    			Message messageToPass = new Message(nodeObject.nodeId, "Application","MAP Protocol");
                    			NodeObject.clock[nodeObject.nodeId]++;
                    			messageToPass.ts = NodeObject.clock;
                    			SendMessage msgSender = new SendMessage(messageToPass, nodeObject, neighborId);
                    			new Thread(msgSender).start();
                    			System.out.print(" at Node - "+ nodeObject.nodeId+ " clock = " );
	                    		for(int i : NodeObject.clock ){  System.out.print(i +"  ");	}
	                    		System.out.println();
	                    		/*System.out.println("Sending Message to - " + neighborId + " from Node - "+ nodeObject.nodeId );
	                    		NodeDetails neighborNodeObj = nodeObject.nodeHashMap.get(neighborId);
	                    		Message messageToPass = new Message(nodeObject.nodeId, "Application","MAP Protocol");
	                    		NodeObject.clock[nodeObject.nodeId]++;
	                    		System.out.print(" at Node - "+ nodeObject.nodeId+ " clock = " );
	                    		for(int i : NodeObject.clock ){  System.out.print(i +"  ");	}
	                    		System.out.println();
	                    		messageToPass.ts = NodeObject.clock;
	                    		//System.out.println("~~~~~~~~~~Sending Msg  - " + messageToPass.type + "~~~~~~~~~~" + messageToPass.message);
	                    		//System.out.println("Neighbor Socket - " + nodeObject.socketObjMap.get(neighborId).getInetAddress() + " port # "+nodeObject.socketObjMap.get(neighborId).getPort() +" is connected - " +nodeObject.socketObjMap.get(neighborId).isConnected() + " is closed - " + nodeObject.socketObjMap.get(neighborId).isClosed());
	                    		InetAddress inetClient = InetAddress.getByName(nodeObject.nodeHashMap.get(neighborId).nodeHostName);
	                    		//System.out.println("~~~~~~~inetClient" + inetClient);
	                    		ClientConnection clientTokenProcesser = new ClientConnection(nodeObject.socketObjMap.get(inetClient),messageToPass);
	                    		new Thread(clientTokenProcesser).start();*/
	                    		/*Socket soc = nodeObject.socketObjMap.get(inetClient);
	    	            		ObjectOutputStream outputStream =  nodeObject.socketOutputStream.get(soc);
	    	            		if(outputStream == null)
	    	            		{
	    	            			System.out.println("Creating new stream for Application" + nodeObject.nodeId + "~~~" + soc);
	    	            			outputStream = new ObjectOutputStream(soc.getOutputStream()); 
	    	            		}
	    	            		nodeObject.socketOutputStream.put(soc, outputStream);
	    	            		outputStream.writeObject(messageToPass);*/
	    						//outputStream.flush();
	                    		
	                            
	                            try 
	                            {
	                        		Thread.sleep(nodeObject.minSendDelay);
	                        	} 
	                            catch (InterruptedException e) 
	                            {
	                        		// TODO Auto-generated catch block
	                        		e.printStackTrace();
	                        	}	
                    		}
                    		else
                    			break;
                    		
                        }
                    	nodeObject.isActive = false;
                    }
                }
                if(((message != null && message.type != null && message.type.equals("Marker")) || nodeObject.initiateCLProtocol) && !nodeObject.isTerminated)
                {
                	//System.out.println("Received Message from - " + message.senderId + " at Node - "+ nodeObject.nodeId +" of type - " + message.type );
                	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ServerConnection.filename , true)));
                	for(int k=0;k<nodeObject.totalNodes;k++)
                	{
                		out.print(NodeObject.clock[k]+"  ");
                	}
                    out.println();
                    out.close();
                	if(nodeObject.nodeId != 0)
            		{
                    	ArrayList<Integer> treeneighbors = SpanningTree.SpanningTree.get(nodeObject.nodeId).neighbors;
                    	for (Integer neighborNodeId : treeneighbors) 
                        {
                    		System.out.println("passing Converge Message to - " + neighborNodeId + " from Node - "+ nodeObject.nodeId );
                			Message messageToPass = new Message(nodeObject.nodeId, "Converge","Converge at Node 0");
                			messageToPass.NodeObject = nodeObject; 
                			SendMessage msgSender = new SendMessage(messageToPass, nodeObject, neighborNodeId);
                			new Thread(msgSender).start();
                		}
            		}
                	ArrayList<Integer> neighbors = nodeObject.neighborObject.neighbors;
                	Iterator<Integer> i = neighbors.iterator();
                	Thread.sleep(nodeObject.snapshotDelay);
                	for(int nodeIdToTerminate : nodeObject.neighborObject.neighbors)
                	{
                		int neighborId= i.next();
                		Message messageToPass = new Message(nodeObject.nodeId, "Marker","C & L");
                		
                		//System.out.println("Sending Marker Message to - " + neighborId + " from Node - "+ nodeObject.nodeId );
                		SendMessage msgSender = new SendMessage(messageToPass, nodeObject, neighborId);
            			new Thread(msgSender).start();
                	}
                	//while(i.hasNext())
                	//{
                		
                		
            			/*Message messageToPass = new Message(nodeObject.nodeId, "Marker","C & L");
	            		messageToPass.ts = NodeObject.clock;                	
	            		InetAddress inetClient;
	            		System.out.println("Sending Marker Message to - " + neighborId + " from Node - "+ nodeObject.nodeId );
	            		inetClient = InetAddress.getByName(nodeObject.nodeHashMap.get(neighborId).nodeHostName);
	            		//InetAddress inetClient = InetAddress.getByName(nodeObject.nodeHashMap.get(neighborId).nodeHostName);
	            		ClientConnection clientTokenProcesser = new ClientConnection(nodeObject.socketObjMap.get(inetClient),messageToPass);
	            		new Thread(clientTokenProcesser).start();*/
	            	//}                    
                }
                
                
                //nodeObject.isActive = true;
                
            } 
    		catch (Exception e) 
    		{
                //System.out.println(" Error in node - " + nodeObject.nodeId);
    			//e.printStackTrace();
            }
        }
    }
}
