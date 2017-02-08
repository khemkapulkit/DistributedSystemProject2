import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


public class SocketObjectStream {

	public HashMap<Socket, ObjectOutputStream> socketOutputStream = new HashMap<Socket, ObjectOutputStream>();
	
	public SocketObjectStream(Socket soc, ObjectOutputStream objOPS)
	{
		this.socketOutputStream.put(soc, objOPS);
	}
}
