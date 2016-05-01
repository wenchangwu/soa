package com.zookeeper;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Master implements Watcher{
	
	
	ZooKeeper zk;
	String hostport;
	Random random =new Random();
	static boolean isLeader=false;

	@Override
	public void process(WatchedEvent arg0) {
		System.out.println(arg0);
	}
	
	Master(String port){
		this.hostport=port;
	}
	
	void startZk(){
		try {
			zk=new ZooKeeper(hostport, 1500, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	void stopZk(){
		try {
			zk.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String serverId=Integer.toHexString(random.nextInt());
	
	void runForMaster(){
		while(true){
			try {
				zk.create("/Master", serverId.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				isLeader=true;
				break;
			} catch (KeeperException e) {
				isLeader=false;
				break;
			} catch (InterruptedException e) {
				isLeader=false;
				break;
			}
		}
	}
	
	 byte[] getData(String path,boolean watch,Stat stat){
		 return new byte[2];
	}
	 
	 boolean checkMaster(){
		 while(true){
			 Stat stat=new Stat();
			 byte data[];
			try {
				data = zk.getData("/master", false, stat);
				 isLeader=new String(data).equals(serverId);
			} catch (KeeperException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 }
	 }
	 
	
	public static void main(String[] args) throws Exception {
		Master m=new Master("127.0.0.1:2181");
		m.startZk();
		m.runForMaster();
		
		if(isLeader){
			System.out.println("I am the leader");
			//wait for a bit
			Thread.sleep(60000);
		}else{
			System.out.println("someone else is the leader");
		}
		m.stopZk();
	}
}
