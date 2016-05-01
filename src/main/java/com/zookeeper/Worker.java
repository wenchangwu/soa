package com.zookeeper;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker implements Watcher{

	ZooKeeper zk;
	String hostport;
	String serverId=Integer.toHexString(new java.util.Random().nextInt());
	String status="";
	
	private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
	
	Worker(String hostport){
		this.hostport=hostport;
	}
	
	
	void startZK() throws Exception{
		zk=new ZooKeeper(hostport, 15000, this);
	}
	
	void register(){
		zk.create("/workers/worker-"+serverId, "Idle".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,createWorkerCallback,null);
	}
	
	StringCallback createWorkerCallback=new StringCallback(){

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				register();
				break;
			case OK:
				LOG.info("Registered successfully: " + serverId);
				break;
			case NODEEXISTS:
				LOG.warn("Already registered: " + serverId);
				break;
			default:
				LOG.error("Something went wrong: " + KeeperException.create(Code.get(rc), path));
			}
		}
		
	};
	
	
	StatCallback statusUpdateCallback=new StatCallback(){

		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			switch(Code.get(rc)){
			case CONNECTIONLOSS:
			updateStatus((String)ctx);
			return;
			}
			
		}
	};

		private synchronized void updateStatus(String status) {
			if(status==this.status){
					zk.setData("/workers/" + serverId, status.getBytes(), -1,
					statusUpdateCallback, status);
			}
		};
	
	public void setStatus(String status){
		this.status=status;
		updateStatus(status);
	}
	
	@Override
	public void process(WatchedEvent event) {
		LOG.info(event.toString()+","+hostport);
	}

	public static void main(String[] args) throws Exception {
		Worker w = new Worker("127.0.0.1:2181");
		w.startZK();
		w.register();
		Thread.sleep(30000);
	}
}
