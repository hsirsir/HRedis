package cn.clo.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.clo.data.Hnode;
import cn.clo.data.Znode;

public class RedisClient {
	private String host;
	private int port;
	private Socket server;
	private OutputStream out;
	private InputStream in;
	public RedisClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		try {
			server = new Socket(host,port);
			in = server.getInputStream();
			out = server.getOutputStream();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("请检查地址后服务器端口,连接失败或是否开启服务器");
			e.printStackTrace();
		}
	}
	public void releaseResource()
	{
		try {
			in.close();
			out.close();
			server.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	public Socket getServer() {
		return server;
	}
	public void setServer(Socket server) {
		this.server = server;
	}
	public boolean auth(String password)
	{
		String auth = "auth "+password+"\r\n";
		try {
			out.write(auth.getBytes());
			byte[] buffer = new byte[1024];
			int len = in.read(buffer);
			String res = new String(buffer,0,len);
			boolean b = res.startsWith("-");
			if(b)
			{
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean set(String key,String val)
	{
		Integer keylen = key.length();
		Integer vallen = val.length();
		String str ="*3\r\n$3\r\nset\r\n$"+keylen+"\r\n"+key+"\r\n$"+vallen+"\r\n"+val+"\r\n";
		try {
			out.write(str.getBytes());
			byte[] buf = new byte[1024];
			int len = in.read(buf);
			String res = new String(buf,0,len);
			if(res.startsWith("-"))
			{
				return false;
			}
 		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public String get(String key)
	{
		Integer keyLen = key.length();
		String res = null;
		try {
			String command = "*2\r\n$3\r\nget\r\n$"+keyLen+"\r\n"+key+"\r\n";
			out.write(command.getBytes());
			int count = 0;
			while (count == 0) {
			   count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			res = new String(buf,0,count);
			
			if(res != null && res.startsWith("-"))
			{
				return null;
			}
			else
			{
				String[] strs = res.split("\r\n");
				if(strs.length > 1)
				{
					res = strs[1];
				}
				else
				{
					return null;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
		
	}
	public void del(String key)
	{
		Integer keyLen = key.length();
		String str6 = "*2\r\n$3\r\ndel\r\n$"+keyLen+"\r\n"+key+"\r\n";
		try {
			out.write(str6.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String res = new String(buf,0,count);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public int lpush(String key,List<Object> ls)
	{
		int res = 0;
		int lsLen = ls.size()+2;
		int keyLen = key.length();
		String command = "*"+lsLen+"\r\n$5\r\nlpush\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for (Object object : ls) {
			int tmpLen = object.toString().length();
			String tmpVal = object.toString();
			String tmp = "$"+tmpLen+"\r\n"+tmpVal+"r\n";
			command += tmp;
		}
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpRes = new String(buf,0,count).trim();
			if(tmpRes != null && tmpRes.startsWith("-"))
			{
				return res;
			}
			//System.out.println(tmpRes.split(":")[1]);
			res = Integer.parseInt(tmpRes.split(":")[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	public int rpush(String key,List<Object> ls)
	{
		int res = 0;
		int lsLen = ls.size()+2;
		int keyLen = key.length();
		String command = "*"+lsLen+"\r\n$5\r\nrpush\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for (Object object : ls) {
			int tmpLen = object.toString().length();
			String tmpVal = object.toString();
			String tmp = "$"+tmpLen+"\r\n"+tmpVal+"r\n";
			command += tmp;
		}
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpRes = new String(buf,0,count).trim();
			if(tmpRes != null && tmpRes.startsWith("-"))
			{
				return res;
			}
			//System.out.println(tmpRes.split(":")[1]);
			res = Integer.parseInt(tmpRes.split(":")[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public int llen(String key)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*2\r\n$4\r\nLLEN\r\n$"+keyLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public String lindex(String key,Integer index)
	{
		String res = null;
		int kenLen = key.length();
		String command = "*3\r\n$6\r\nlindex\r\n$"+kenLen+"\r\n"+key+"\r\n$"+index.toString().length()+"\r\n"+index+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return null;
			}
			else
			{
				String[] tmps = tmpres.split("\r\n");
				if(tmps.length>1)
				{
					res = tmps[1];
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public String lpop(String key)
	{
		String res = null;
		int kenLen = key.length();
		String command = "*2\r\n$4\r\nlpop\r\n$"+kenLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return null;
			}
			else
			{
				String[] tmps = tmpres.split("\r\n");
				if(tmps.length>1)
				{
					res = tmps[1];
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public String rpop(String key)
	{
		String res = null;
		int kenLen = key.length();
		String command = "*2\r\n$4\r\nrpop\r\n$"+kenLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return null;
			}
			else
			{
				String[] tmps = tmpres.split("\r\n");
				if(tmps.length>1)
				{
					res = tmps[1];
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int lrem(String key,Integer count1,String val)
	{
		int res = 0;
		int keyLen = key.length();
		String command = "*4\r\n$4\r\nLREM\r\n$"+keyLen+"\r\n"+key+"\r\n$"+count1.toString().length()+
				"\r\n"+count1+"\r\n$"+val.length()+"\r\n"+val+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			else
			{
				res = Integer.parseInt(tmpres.split(":")[1]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int linsert(String key,String destval,String val,boolean b)
	{
		String BorA = null;
		if(b)
		{
			BorA = "before";
		}
		else
		{
			BorA = "after";
		}
		int res = 0;
		int keyLen = key.length();
		String command = "*5\r\n$7\r\nlinsert\r\n$"+keyLen+"\r\n"+key+"\r\n$"+BorA.length()+"\r\n"+BorA+
				"\r\n$"+destval.length()+
				"\r\n"+destval+"\r\n$"+val.length()+"\r\n"+val+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			else
			{
				res = Integer.parseInt(tmpres.split(":")[1]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public boolean lset(String key,Integer index,String val)
	{
		int keyLen = key.length();
		String command = "*4\r\n$4\r\nlset\r\n$"+keyLen+"\r\n"+key+"\r\n$"+index.toString().length()+
				"\r\n"+index+"\r\n$"+val.length()+"\r\n"+val+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return false;
			}
			else
			{
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	
	public List lrange(String key,Integer start,Integer stop)
	{
		List res = new ArrayList<>();
		int keyLen = key.length();
		String command = "*4\r\n$6\r\nlrange\r\n$"+keyLen+"\r\n"+key+"\r\n$"+start.toString().length()+
				"\r\n"+start+"\r\n$"+stop.toString().length()+"\r\n"+stop+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=2)
				{
					res.add(tmp1[i]);
				}
				return res;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	//集合操作
	public int sadd(String key,Set<Object> sets)
	{
		int res = 0;
		int keyLen = key.length();
		String command = "\r\n$4\r\nSADD\r\n$"+keyLen+"\r\n"+key+"\r\n";
	    Iterator<Object> it = sets.iterator();
	    int tmpLen = 0;
	    while(it.hasNext())
	    {
	    	Object tmp = it.next();
	    	tmpLen++;
	    	String tmpStr = "$"+tmp.toString().length()+"\r\n"+tmp.toString()+"\r\n";
	    	command+=tmpStr;
	    }
	    tmpLen = tmpLen+2;
	    command = "*"+tmpLen+command;
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String tmp1 = tmpres.trim().split(":")[1];
				res = Integer.parseInt(tmp1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	//集合操作
	public int sadd(String key,String ...vals)
	{
		int res = 0;
		int keyLen = key.length();
		String command = "*"+(vals.length+2)+"\r\n$4\r\nSADD\r\n$"+keyLen+"\r\n"+key+"\r\n";
	    for(int i = 0;i<vals.length;i++)
	    {
	    	String tmpStr = "$"+vals[i].length()+"\r\n"+vals[i]+"\r\n";
	    	command+=tmpStr;
	    }
	  
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String tmp1 = tmpres.trim().split(":")[1];
				res = Integer.parseInt(tmp1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	public boolean sismember(String key,String member)
	{
		boolean res = false;
		int keyLen = key.length();
		String command = "*3\r\n$9\r\nSISMEMBER\r\n$"+keyLen+"\r\n"+key+"\r\n$"+member.length()+"\r\n"+member+
				"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return false;
			}
			else
			{
				int tmp = Integer.parseInt(tmpres.split(":")[1]);
				if(tmp == 1) res = true;
					
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int srem(String key,String ...members)
	{
		int res = 0;
		int keyLen = key.length();
		int allLen = 2+members.length;
		String command = "*"+allLen+"\r\n$4\r\nLREM\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for(int i = 0;i<members.length;i++)
		{
			String tmp = "$"+members[i].length()+"\r\n"+members[i]+"\r\n";
			command += tmp;
		}
		
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public List smembers(String key)
	{
		List res = new ArrayList<>();
		int keyLen = key.length();
		String command = "*2\r\n$8\r\nsmembers\r\n$"+keyLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=2)
				{
					res.add(tmp1[i]);
				}
				return res;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public List sinter(String ...keys)
	{
		List res = new ArrayList<>();
		int keysLen = keys.length+1;
		String command = "*"+keysLen+"\r\n$6\r\nsinter\r\n";
		for(int i = 0;i<keys.length;i++)
		{
			String tmp = "$"+keys[i].length()+"\r\n"+keys[i]+"\r\n";
			command += tmp;
		}
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=2)
				{
					res.add(tmp1[i]);
				}
				return res;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int zadd(String key,Znode ...members)
	{
		int res = 0;
		int keyLen = key.length();
		int allen = members.length*2+2;
		String command = "*"+allen+"\r\n$4\r\nzadd\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for(int i = 0;i<members.length;i++)
		{
			Znode znode = members[i];
			String tmp = "$"+znode.getScore().toString().length()+"\r\n"+
			znode.getScore()+"\r\n$"+znode.getMember().length()+"\r\n"+
			znode.getMember()+"\r\n";
			command += tmp;
		}
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int zrem(String key,String ...members)
	{
		int res = 0;
		int keyLen = key.length();
		int allLen = members.length+2;
		String command = "*"+allLen+"\r\n$4\r\nzrem\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for(int i = 0;i<members.length;i++)
		{
			String member = members[i];
			String tmp = "$"+member.length()+"\r\n"+member+"\r\n";
			command += tmp;
		}
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int zremrangebyrank(String key,Integer start,Integer stop)
	{
		if(key == null || key.equals("") || start == null || stop == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*4\r\n$15\r\nzremrangebyrank\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		start.toString().length()+"\r\n"+start+"\r\n$"+stop.toString().length()+"\r\n"+
				stop+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int zremrangebyscore(String key,Double min,Double max)
	{
		if(key == null || key.equals("") || min == null || max == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*4\r\n$16\r\nzremrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		min.toString().length()+"\r\n"+min+"\r\n$"+max.toString().length()+"\r\n"+
				max+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	public List zrange(String key,Integer start,Integer stop)
	{
		List res = new ArrayList<>();
		String scoretip = "withscores";
		int keyLen = key.length();
		String command = "*5\r\n$6\r\nzrange\r\n$"+keyLen+"\r\n"+key+"\r\n$"+start.toString().length()+
				"\r\n"+start+"\r\n$"+stop.toString().length()+"\r\n"+stop+"\r\n$"+scoretip.length()+
				"\r\n"+scoretip+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=4)
				{
					//res.add(tmp1[i]);
					String score = tmp1[i+2];
					String member = tmp1[i];
					Znode znode = new Znode(score, member);
					res.add(znode);
				}
				return res;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public List zrevrange(String key,Integer start,Integer stop)
	{
		List res = new ArrayList<>();
		String scoretip = "withscores";
		int keyLen = key.length();
		String command = "*5\r\n$8\r\nzrevrange\r\n$"+keyLen+"\r\n"+key+"\r\n$"+start.toString().length()+
				"\r\n"+start+"\r\n$"+stop.toString().length()+"\r\n"+stop+"\r\n$"+scoretip.length()+
				"\r\n"+scoretip+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=4)
				{
					//res.add(tmp1[i]);
					String score = tmp1[i+2];
					String member = tmp1[i];
					Znode znode = new Znode(score, member);
					res.add(znode);
				}
				return res;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	public List zrangebyscore(String key,Double min,Double max,Integer offset,Integer len)
	{
		List res = new ArrayList<>();
		String scoretip = "withscores";
		int keyLen = key.length();
		String mininf = "-inf";
		String maxinf = "+inf";
		int allLen = 5;
		String command = null;
		if(min == null && max == null)
		{
			command = "\r\n$13\r\nzrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+mininf.length()+
					"\r\n"+mininf+"\r\n$"+maxinf.length()+"\r\n"+maxinf+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		else if(min == null)
		{
			command = "\r\n$13\r\nzrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+mininf.length()+
					"\r\n"+mininf+"\r\n$"+max.toString().length()+"\r\n"+max+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		else if(max == null)
		{
			command = "\r\n$13\r\nzrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+min.toString().length()+
					"\r\n"+min+"\r\n$"+maxinf.length()+"\r\n"+maxinf+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		else
		{
			command = "\r\n$13\r\nzrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+min.toString().length()+
					"\r\n"+min.toString()+"\r\n$"+max.toString().length()+"\r\n"+max.toString()+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		if(offset != null && len != null)
		{
			String limitTip = "limit";
			String limitStr = "$"+limitTip.length()+"\r\n"+limitTip+
					"\r\n$"+offset.toString().length()+"\r\n"+offset+
					"\r\n$"+len.toString().length()+"\r\n"+len.toString()+
					"\r\n";
			command+=limitStr;
			allLen +=3;
		}
		String comLen = "*"+allLen;
		command = comLen+command;
		//System.out.println(command);
		try {
			out.write(command.getBytes());
			int count = 0;
			//Thread.sleep(1000);
			while(count == 0)
			{
				count = in.available();
			}
			//System.out.println("数据大小="+count);
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				//System.out.println("---------");
				String[] tmp1 = tmpres.split("\r\n");
				int tmpCount = 0;
				for(int i = 2;i<tmp1.length;i+=4)
				{
					//res.add(tmp1[i]);
					String score = tmp1[i+2];
					String member = tmp1[i];
					Znode znode = new Znode(score, member);
					tmpCount++;
					res.add(znode);
				}
				//System.out.println(tmpCount);
				//System.out.println(res);
				return res;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public List zrevrangebyscore(String key,Double max,Double min,Integer offset,Integer len)
	{
		List res = new ArrayList<>();
		String scoretip = "withscores";
		int keyLen = key.length();
		String mininf = "-inf";
		String maxinf = "+inf";
		int allLen = 5;
		String command = null;
		if(min == null && max == null)
		{
			command = "\r\n$16\r\nzrevrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+maxinf.length()+"\r\n"+maxinf+"\r\n$"+mininf.length()+
					"\r\n"+mininf+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		else if(min == null)
		{
			command = "\r\n$16\r\nzrevrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+max.toString().length()+"\r\n"+max+"\r\n$"+mininf.length()+
					"\r\n"+mininf+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		else if(max == null)
		{
			command = "\r\n$16\r\nzrevrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+maxinf.length()+"\r\n"+maxinf+"\r\n$"+min.toString().length()+
					"\r\n"+min+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		else
		{
			command = "\r\n$16\r\nzrevrangebyscore\r\n$"+keyLen+"\r\n"+key+"\r\n$"+max.toString().length()+"\r\n"+max+"\r\n$"+min.toString().length()+
					"\r\n"+min+"\r\n$"+scoretip.length()+
					"\r\n"+scoretip+"\r\n";
		}
		if(offset != null && len != null)
		{
			String limitTip = "limit";
			String limitStr = "$"+limitTip.length()+"\r\n"+limitTip+
					"\r\n$"+offset.toString().length()+"\r\n"+offset+
					"\r\n$"+len.toString().length()+"\r\n"+len.toString()+
					"\r\n";
			command+=limitStr;
			allLen +=3;
		}
		String comLen = "*"+allLen;
		command = comLen+command;
		try {
			out.write(command.getBytes());
			Thread.sleep(500);
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=4)
				{
					//res.add(tmp1[i]);
					String score = tmp1[i+2];
					String member = tmp1[i];
					Znode znode = new Znode(score, member);
					res.add(znode);
				}
				return res;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int zrank(String key,String member)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*3\r\n$5\r\nzrank\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		member.length()+"\r\n"+member+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return -1;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int zrevrank(String key,String member)
	{
		if(key == null || key.equals("")) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*3\r\n$8\r\nzrevrank\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		member.length()+"\r\n"+member+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return -1;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public Double zincrby(String key,Double increment,String member)
	{
		if(key == null || key.equals("") || increment == null || 
				increment.equals("") || member == null || member.equals("")) return -1.0;
		Double res = 0.0;
		int keyLen = key.length();
		String command = "*4\r\n$7\r\nzincrby\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
				increment.toString().length()+"\r\n"+increment.toString()+"\r\n$"+member.length()+"\r\n"+
				member+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return -1.0;
			}
			else
			{
				String[] strs = tmpres.split("\r\n");
				if(strs.length > 1)
				{
					res = Double.parseDouble(strs[1]);
				}
				else
				{
					return -1.0;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int zcount(String key,Integer start,Integer stop)
	{
		if(key == null || key.equals("") || start == null || stop == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*4\r\n$6\r\nzcount\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		start.toString().length()+"\r\n"+start+"\r\n$"+stop.toString().length()+"\r\n"+
				stop+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int hset(String key,String field,String val)
	{
		if(key == null || key.equals("") || field == null || val == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*4\r\n$4\r\nhset\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
				field.length()+"\r\n"+field+"\r\n$"+val.length()+"\r\n"+
				val+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public boolean hmset(String key,Hnode ...hnodes)
	{
		int keyLen = key.length();
		int allen = hnodes.length*2+2;
		String command = "*"+allen+"\r\n$5\r\nhmset\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for(int i = 0;i<hnodes.length;i++)
		{
			Hnode hnode = hnodes[i];
			String tmp = "$"+hnode.getField().length()+"\r\n"+
					hnode.getField()+"\r\n$"+hnode.getVal().length()+"\r\n"+
					hnode.getVal()+"\r\n";
			command += tmp;
		}
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return false;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	
	public void hdel(String key,String ...fields)
	{
		if(key == null || key.equals("") || fields == null || fields.length == 0) return;
		int keyLen = key.length();
		int allen = fields.length+2;
		String command = "*"+allen+"\r\n$4\r\nhdel\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for(int i = 0;i<fields.length;i++)
		{
			String field = fields[i];
			String tmp = "$"+field.length()+"\r\n"+
					field+"\r\n";
			command += tmp;
		}
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String hget(String key,String field)
	{
		Integer keyLen = key.length();
		String res = null;
		try {
			String command = "*3\r\n$4\r\nhget\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
					field.length()+"\r\n"+field+"\r\n";
			out.write(command.getBytes());
			int count = 0;
			while (count == 0) {
			   count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			res = new String(buf,0,count);
			
			if(res != null && res.startsWith("-"))
			{
				return null;
			}
			else
			{
				String[] strs = res.split("\r\n");
				if(strs.length > 1)
				{
					res = strs[1];
				}
				else
				{
					return null;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
		
	}
	
	public List hmget(String key,String ...fields)
	{
		Integer keyLen = key.length();
		int allLen = fields.length+2;
		List res = new ArrayList<>();
		String tmpres = null;
		String command = "*"+allLen+"\r\n$5\r\nhmget\r\n$"+keyLen+"\r\n"+key+"\r\n";
		for(int i = 0;i<fields.length;i++)
		{
			String field = fields[i];
			String tmpStr = "$"+field.length()+"\r\n"+field+"\r\n";
			command += tmpStr;
		}
		try {
			
			out.write(command.getBytes());
			int count = 0;
			while (count == 0) {
			   count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			tmpres = new String(buf,0,count);
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=2)
				{
					res.add(tmp1[i]);
				}
				return res;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
		
	}
	
	public List hgetall(String key)
	{
		Integer keyLen = key.length();
		List res = new ArrayList<>();
		String tmpres = null;
		String command = "*2\r\n$7\r\nhgetall\r\n$"+keyLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while (count == 0) {
			   count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			tmpres = new String(buf,0,count);
			//System.out.println(tmpres);
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return res;
			}
			else
			{
				String[] tmp1 = tmpres.split("\r\n");
				for(int i = 2;i<tmp1.length;i+=4)
				{
					String field = tmp1[i];
					String val = tmp1[i+2];
					Hnode hnode = new Hnode(field, val);
					res.add(hnode);
				}
				return res;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public int hexists(String key,String field)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*3\r\n$7\r\nhexists\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		field.length()+"\r\n"+field+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int exists(String key)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*2\r\n$6\r\nexists\r\n$"+keyLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int expire(String key,Integer seconds)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*3\r\n$6\r\nexpire\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		seconds.toString().length()+"\r\n"+seconds+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int persist(String key)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*2\r\n$7\r\npersist\r\n$"+keyLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public boolean renamenx(String key,String newkey)
	{
		if(key == null) return false;
		boolean res = false;
		int keyLen = key.length();
		String command = "*3\r\n$8\r\nrenamenx\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
		newkey.length()+"\r\n"+newkey+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return false;
			}
			res = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	public String type(String key)
	{
		Integer keyLen = key.length();
		String res = null;
		try {
			String command = "*2\r\n$4\r\ntype\r\n$"+keyLen+"\r\n"+key+"\r\n";
			out.write(command.getBytes());
			int count = 0;
			while (count == 0) {
			   count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			res = new String(buf,0,count);
			
			if(res != null && res.startsWith("-"))
			{
				return null;
			}
			else
			{
				String[] strs = res.split("\r\n");
				if(strs.length > 1)
				{
					res = strs[1];
				}
				else
				{
					return null;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public int ttl(String key)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*2\r\n$3\r\nttl\r\n$"+keyLen+"\r\n"+key+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int expireat(String key,Integer timestamp)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*3\r\n$8\r\nexpireat\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
				timestamp.toString().length()+"\r\n"+timestamp+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int pexpire(String key,Integer milliseconds)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*3\r\n$6\r\nexpire\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
				milliseconds.toString().length()+"\r\n"+milliseconds+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public int pexpireat(String key,Integer millisecondstimestamp)
	{
		if(key == null) return -1;
		int res = 0;
		int keyLen = key.length();
		String command = "*3\r\n$9\r\npexpireat\r\n$"+keyLen+"\r\n"+key+"\r\n$"+
				millisecondstimestamp.toString().length()+"\r\n"+millisecondstimestamp+"\r\n";
		try {
			out.write(command.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			String tmpres = new String(buf,0,count).trim();
			if(tmpres != null && tmpres.startsWith("-"))
			{
				return 0;
			}
			res = Integer.parseInt(tmpres.split(":")[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
	
	public String clusterInfo()
	{
		String res = null;
		String str6 = "*2\r\n$7\r\ncluster\r\n$5\r\nnodes\r\n";
		try {
			out.write(str6.getBytes());
			int count = 0;
			while(count == 0)
			{
				count = in.available();
			}
			byte[] buf = new byte[count];
			in.read(buf);
			res = new String(buf,0,count);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}
}


