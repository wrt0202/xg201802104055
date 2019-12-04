package util;

public class IdService {
	private static int nextId=1000;
	public synchronized static int getId(){
		return nextId++;
	}		
}
