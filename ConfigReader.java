import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ConfigReader 
{
	
	/*
	 * Read config file on each node and store config details such node details, available tokens for that node 
	 */
    public static HashMap<Integer, NodeDetails> nodeMap = new HashMap<Integer, NodeDetails>();
    public static HashMap<Integer, NeighborObject> neighborMap = new HashMap<Integer, NeighborObject>();
    public static int minPerActive;
    public static int maxPerActive;
    public static int minSendDelay;
    public static int snapshotDelay;
    public static int maxNumber;
    
    
    public static String netId;
	public static int totalNodes = 0;
    
	public static void initConfig(String configFileName) 
    {
    	File f = new File(configFileName);
    	BufferedReader br;
    	 
    	try 
    	{
    		
			br = new BufferedReader(new FileReader(f));
			String line = null;
			Integer count = 0;
			Integer srcNodeId = -1;
			
			while ((line = br.readLine()) != null) 
			{
				/*
				 * If line is empty or line is a comment in config file skip it
				 */
				if (line.length() == 0 ) {
					continue;
				}
				else if(line.startsWith("#") || line.trim().startsWith("#") ) {
					continue;
				}
				else
				{
					/*
					 * If it is first line without comments, It has information about netId and total nodes
					 */
					String[] lineSplit = line.split(" ");
					if(count == 0)
					{
						//System.out.println("~~~~~~~~~~~~~ Total Nodes and NetId" + line);
						Integer paramCount = 0;
						String totalNodeStr = null,minPerActiveStr = null,maxPerActiveStr = null,
						minSendDelayStr = null,snapshotDelayStr = null,maxNumberStr = null;
						
						for(String str : line.split(" "))
						{
							if(!str.isEmpty())
							{
								if(paramCount == 0)
								{
									totalNodeStr = str;
									paramCount++;
								}
								else if (paramCount == 1)
								{
									minPerActiveStr = str;
									paramCount++;
								}
								else if (paramCount == 2)
								{
									maxPerActiveStr = str;
									paramCount++;
								}
								else if (paramCount == 3)
								{
									minSendDelayStr = str;
									paramCount++;
								}
								else if (paramCount == 4)
								{
									snapshotDelayStr = str;
									paramCount++;
								}
								else if (paramCount == 5)
								{
									maxNumberStr = str;
									paramCount++;
								}
							}
						}
						totalNodes = Integer.parseInt(totalNodeStr.trim());
						
						minPerActive = Integer.parseInt(minPerActiveStr.trim());
						maxPerActive = Integer.parseInt(maxPerActiveStr.trim());
						minSendDelay = Integer.parseInt(minSendDelayStr.trim());
						snapshotDelay = Integer.parseInt(snapshotDelayStr.trim());
						maxNumber = Integer.parseInt(maxNumberStr.trim());
						
						count++;
						continue;
					}
					/*
					 * If it is line after first line and till the count of totalnodes
					 * i.e. line will have information about nodeId, Node HostName and Node PortNumber
					 */
					else if(count > 0 && count <= totalNodes)
					{
						//System.out.println("~~~~~~~~~~~~~ Node Cofigurations " + line);
						Integer paramCount = 0;
						String nodeIdStr = null,nodeNameStr = null,nodePortStr = null;
						for(String str : line.split(" "))
						{
							if(!str.isEmpty())
							{
								if(paramCount == 0)
								{
									nodeIdStr = str;
									paramCount++;
								}
								else if (paramCount == 1)
								{
									nodeNameStr = str;
									paramCount++;
								}
								else if (paramCount == 2)
								{
									nodePortStr = str;
									paramCount++;
								}
							}
						}
						NodeDetails info = new NodeDetails(nodeNameStr.trim(), Integer.parseInt(nodePortStr.trim()));
						nodeMap.put(Integer.parseInt(nodeIdStr.trim()), info);
						count++;
						continue;
					}
					/*
					 * Now all the remaining lines without comments will be token paths
					 * Store it as Map with NodeId as key and List of all token paths as value
					 */
					else
					{
						//System.out.println("~~~~~~~~~~~~~ " + line);
						srcNodeId++;
						
						ArrayList<Integer> neighbors = new ArrayList<Integer>();
						
						for(String str : lineSplit)
						{	
							if(str.contains("#"))
								break;
							else if(str.isEmpty() || str.trim().isEmpty())
								continue;	
							else

							{
								str = str.trim();
								neighbors.add(Integer.parseInt(str));
							}
						}
						
						NeighborObject neighborObj = new NeighborObject();
						neighborObj.neighbors = neighbors;
						neighborMap.put(srcNodeId,neighborObj);
						count++;
					}	
				}
			}
			br.close();
		} 
    	catch (FileNotFoundException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	SpanningTree.ConstructSpanningTree(neighborMap, totalNodes);
    	
    }
}
