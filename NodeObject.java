import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NodeObject implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public int nodeId;
	
	public int minPerActive;
	public int maxPerActive;
	public int minSendDelay;
	public int snapshotDelay;
	public int maxNumber;
	
	public int messageSent;
	
	public boolean isActive;
	public boolean isTerminated;
	
    public int nodeRandomNumber;
	
    public NodeDetails nodeDetails;						// Information about Node's host name & port number
	public NeighborObject neighborObject;				// Has information about all the neighbors
	
	public HashMap<Integer, NodeDetails> nodeHashMap;	// Has information of all the nodes, used to make connections
	
	public HashMap<InetAddress, Socket> socketObjMap = new HashMap<InetAddress, Socket>();
	public HashMap<InetAddress, Socket> reverseSocketObjMap = new HashMap<InetAddress, Socket>();
	
	public int totalNodes;
	static public int[] clock;
	
	public boolean initiateCLProtocol;
	public HashMap<Socket, ObjectOutputStream> socketOutputStream = new HashMap<Socket, ObjectOutputStream>();
	
	public NodeObject(int nodeId, String configFileName) 
	{
        Random r = new Random();
        
		this.nodeId = nodeId;
		
		ConfigReader.initConfig(configFileName);
		this.totalNodes = ConfigReader.totalNodes;
		this.minPerActive = ConfigReader.minPerActive;
		this.maxPerActive = ConfigReader.maxPerActive;
		this.minSendDelay = ConfigReader.minSendDelay;
		this.snapshotDelay = ConfigReader.snapshotDelay;
		this.maxNumber = ConfigReader.maxNumber;
        this.messageSent = 0;
        this.isTerminated = false;
        
        if(nodeId == 0)
        {
        	this.initiateCLProtocol = true;
        }
        else
        {
        	this.initiateCLProtocol = false;
        }
        	
        
        
        clock = new int[this.totalNodes];
        for(int i =0;i<this.totalNodes;i++)
        {
        	clock[i]= 0;
        }
        
		this.nodeHashMap = ConfigReader.nodeMap;
        this.nodeDetails = nodeHashMap.get(nodeId);
        this.neighborObject = ConfigReader.neighborMap.get(nodeId);
        
        this.nodeRandomNumber = r.nextInt(100);
        
        if(neighborObject == null)
        	this.neighborObject = new NeighborObject();	
        if(neighborObject.neighbors == null)
        	this.neighborObject.neighbors = new ArrayList<Integer>();
    }
}