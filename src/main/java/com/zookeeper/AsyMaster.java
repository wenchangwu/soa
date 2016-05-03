package com.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class AsyMaster implements Watcher {

	static ZooKeeper zk;
	String hostport;
	static Random random = new Random();
	static boolean isLeader = false;
	static String serverId = Integer.toHexString(random.nextInt());
	enum MasterStates {RUNNING, ELECTED, NOTELECTED};
	private volatile MasterStates state = MasterStates.RUNNING;

	@Override
	public void process(WatchedEvent arg0) {
		System.out.println(arg0);
	}

	AsyMaster(String port) {
		this.hostport = port;
	}

	void startZk() {
		try {
			zk = new ZooKeeper(hostport, 1500, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void stopZk() {
		try {
			zk.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void runForMaster() {
				zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,asynCallback,null);
		}

	
	StringCallback asynCallback=new StringCallback() {
		
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch(Code.get(rc)){
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case OK:
				isLeader=true;
				break;
				default:
				isLeader=false;
			}
		System.out.println("I'm " + (isLeader ? "" : "not ") +
		"the leader");
		}
	};

	
	void masterExists(){
		zk.exists("/master", masterExistsWatch, masterExistsCallback, null);
	}
	
	Watcher masterExistsWatch=new Watcher(){

		@Override
		public void process(WatchedEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	StatCallback masterExistsCallback=new StatCallback(){

		@Override
		public void processResult(int rc, String arg1, Object arg2, Stat stat) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
			masterExists();
			break;
			case OK:
			if(stat == null) {
			state= MasterStates.RUNNING;
			runForMaster();
			}
			break;
			default:
			checkMaster();
			break;
			}
			
		}
		
	};
	
	static void  checkMaster() {
		zk.getData("/master", false, (DataCallback) masterCreateCallback, null);
	}
	
	DataCallback masterCheckCallback=new DataCallback(){
		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			switch(Code.get(rc)){
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case NONODE:
				runForMaster();
				return;
			}
			
		}
		
	};
	
	
	byte[] getData(String path, boolean watch, Stat stat) {
		return new byte[2];
	}

	void create(String path, byte[] data, List<ACL> acl, CreateMode createMode, StringCallback cb, Object ctx) {

	}

	static StringCallback masterCreateCallback = new StringCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case OK:
				isLeader = true;
				break;
			default:
				isLeader = false;
			}
			System.out.println("I'm " + (isLeader ? "" : "not ") + "the leader");
		}

	};


	void processResult(int rc, String path, Object ctx, String name) {

	}

	public void bootStrap() {
		createParent("/workers", new byte[0]);
		createParent("/assign", new byte[0]);
		createParent("/tasks", new byte[0]);
		createParent("/status", new byte[0]);
	}

	void createParent(String path, byte[] data) {
		zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createParentCallback, data);
	}

	StringCallback createParentCallback = new StringCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				createParent(path, (byte[]) ctx);
				break;
			case OK:
				System.out.println("parent is created");
				break;
			default:
				System.out.println("something went wrong");
			}

		}

	};

	public static void main(String[] args) throws Exception {
		AsyMaster m = new AsyMaster("127.0.0.1:2181");
		m.startZk();
		m.runForMaster();

		if (isLeader) {
			System.out.println("I am the leader");
			// wait for a bit
			Thread.sleep(60000);
		} else {
			System.out.println("someone else is the leader");
		}
		m.stopZk();
	}
}
