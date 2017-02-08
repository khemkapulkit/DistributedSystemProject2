import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;


public class SpanningTree {
	
	 public static HashMap<Integer, NeighborObject> SpanningTree = new HashMap<Integer, NeighborObject>();
	 public static HashMap<NeighborObject, Socket> nodeSocketMap = new HashMap<NeighborObject, Socket>();
	 
	 public static void ConstructSpanningTree(HashMap<Integer, NeighborObject> neighborMap, int N)
	 {
		
	    boolean[][] adj = new boolean[N][N];
	    boolean[][] tree = new boolean[N][N];
	    for(int i=0;i<N;i++)
	    {
	    	for(int j=0;j<N;j++)
		    {
	    		adj[i][j] = false;
	    		tree[i][j] = false;
		    }
	    }
	    
	    for(int i=0;i<N;i++)
	    {
	    	NeighborObject n = neighborMap.get(i);
	    	Iterator<Integer> iterator = n.neighbors.iterator();
	    	while(iterator.hasNext())
	    	{
	    		int next = iterator.next();
	    		adj[i][next]=true;
	    		adj[next][i]=true;
	    	}	    	
	    }
	    boolean[] done = new boolean[N];
	    for(int i=0;i<N;i++)
	    {
	    	done[i]= false;
	    	
	    }
	    done[0]= true;
	 
    	for(int k =0;k<N;k++)
    	{
    		for(int l=0;l<N;l++){
				if ((adj[k][l]== true))
				{
					if (!done[k] && done[l])
					{	    						
    					tree[k][l]= true;
    					tree[l][k]= true;
    					done[k] = true;
					}
					if (!done[l] && done[k])
					{	    						
    					tree[k][l]= true;
    					tree[l][k]= true;
    					done[l] = true;
					}
				}
    		}
    	}
	    	
	   for(int p=0;p<N;p++){
	    	NeighborObject NObj = new NeighborObject();
	    	for(int q=0;q<N;q++){
	    		if(tree[p][q])
	    		{
	    			NObj.neighbors.add(q);
	    		}	    		
	    	}
	    	SpanningTree.put(p, NObj);
	    	
	    }
	   
	   System.out.println("Spanning Tree is - ");
	    for(int m =0;m<N;m++)
	    {
	    	NeighborObject NObj = SpanningTree.get(m);
	    	Iterator<Integer> iterator1 = NObj.neighbors.iterator();
	    	System.out.print(m +"- ");
	    	while(iterator1.hasNext())
	    	{
	    		System.out.print(iterator1.next()+" ");
	    	}
	    	System.out.println();
	    }
	 }
	 public static boolean areAllTrue(boolean[] array)
	 {
	     for(boolean b : array) if(!b) return false;
	     return true;
	 }
	 
	 
}
