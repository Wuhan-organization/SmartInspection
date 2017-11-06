package tw.com.a_i_t.IPCamViewer.IPCam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class IPCamWhistlerReceiver extends Thread{

	String TAG="IPCamWhistlerReceiver";
	static String mMessage = "doorbell ring"; //get ring
	static String ipMessage = "doorbell IP";  //get IP
	static String pirMessage = "PIR Event";	//get PIR event
	
	private static DatagramSocket socket = null;
	private static DatagramPacket packet;
	private static byte[] buffer = new byte[4096];
	private final int		port     = 14926;
	private final String	listenIP = "0.0.0.0";
	public static boolean alive = true;

	private static String eid="0000";

	private Handler mHandler;
	String s;
	public IPCamWhistlerReceiver(Handler handler) {
		mHandler = handler;
		if (socket != null) return;
		try {
			packet = new DatagramPacket(buffer, buffer.length);
			socket = new DatagramSocket(port, InetAddress.getByName(listenIP));
			socket.setBroadcast(true);
			socket.setSoTimeout(1000);
			Log.i("CAMERASNIFFER", "===Create DatagramSocket Successfully!");
			alive = true;
		} catch (IOException ex) {
			alive = false;
			Log.i("CAMERASNIFFER", "===Create DatagramSocket Failed!");
		}
	}

	/**
	 * Converts a given datagram packet's contents to a String.
	 */
	static String stringFromPacket(DatagramPacket packet) {
		return new String(packet.getData(), 0, packet.getLength());
	}
	/*
    <Camera>
    	<Event id="35532932203" count="2">
    		<Message>doorbell ring</Message>
    		<URL>http://192.168.1.101/media/mmcblk0/DCIM/VIDEO_01/MOV_0001.mo4</URL>
    	</Event>
    </Camera>
	 */


	@Override
	public void run() {
		try {
			Log.i("IPCamWhistlerReceiver", "####IPCamWhistlerReceiver Start alive="+ alive);
			while (alive) {
				try {
					packet.setLength(buffer.length);
					socket.receive(packet);
					s = stringFromPacket(packet);
					Log.i(TAG, "== GET DATA=" + s);
					InputStream stream = new ByteArrayInputStream(s.getBytes("UTF-8"));
					String url = DomParseXML(stream);
					if (url != null) {
						if (mHandler != null) {
							Message message;
							//String obj = "OK";
							message = mHandler.obtainMessage();
							message.obj = url;
							if (url.contains("ip=")) // get camera broadcast IP address
								message.arg1 = IPCamWhistlerEvent.MSG_RECEIVE_IP;
							else if (url.contains("ring=")) {
								message.arg1 = IPCamWhistlerEvent.MSG_RECEIVE_RING;
							}else if (url.contains("pir=")) {
								message.arg1 = IPCamWhistlerEvent.MSG_RECEIVE_PIR;
							}
							else
								message.arg1 = IPCamWhistlerEvent.MSG_UPDATE_EVENT;

							mHandler.sendMessage(message);
						}
					} 
				} catch (java.io.InterruptedIOException e) {
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} finally {
			Log.i("IPCamWhistlerReceiver", "####IPCamWhistlerReceiver Stop");
			if (socket != null)
				socket.close();
			socket = null;
		}
	}

	private static String DomParseXML(InputStream inStream/*,String s*/) throws Exception {
		String s; // for check version with eid;
		String cnt;
		String sMsg;
		String sURL;

		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		Document document=builder.parse(inStream);
		Element root=document.getDocumentElement();
		NodeList nodes=root.getElementsByTagName("Event");
		Log.e("nodes.getLength", "nodes.getLength="+nodes.getLength());

		Element MenuElement=(Element)nodes.item(0);
		s= MenuElement.getAttribute("id");
		cnt = MenuElement.getAttribute("count");
		if (s!=null) {
			/////check version
							if ((!eid.equals(s))||(cnt.equals("0")))
								eid = s;
							else
								return null;
								
			/////
			NodeList msg=root.getElementsByTagName("Message");
			Element Msg=(Element)msg.item(0);
			sMsg = Msg.getTextContent();
			Log.e("msg.getNodeName", "msg.getNodeName="+ sMsg);//doorbell ring
			if (sMsg != null) {
				if (mMessage.equals(Msg.getTextContent())) {
					NodeList url = root.getElementsByTagName("URL");
					if (url.item(0) != null) {
						Element Url = (Element) url.item(0);
						sURL = Url.getTextContent();
						if (sURL != null) {
							Log.e("Url.getNodeName", "Url.getNodeName=" + Url.getTextContent());//http or rtsp
							return sURL;
						}
					}else {
						NodeList rip = root.getElementsByTagName("IP");
						Element Url = (Element) rip.item(0);
						sURL = Url.getTextContent();
						if (sURL != null) {
							Log.e("Url.getNodeName", "Url.getNodeName=" + Url.getTextContent());//http or rtsp
							return ("ring="+sURL);
						}
					}
				}else if (ipMessage.equals(Msg.getTextContent()))
				{
					NodeList ip=root.getElementsByTagName("IP");
					Element Url=(Element)ip.item(0);
					sURL = Url.getTextContent();
					if (sURL != null) {
						Log.e("Url.getNodeName", "Url.getNodeName="+Url.getTextContent());//get IP
						return ("ip="+sURL);
					}
					
				}else if (pirMessage.equals(Msg.getTextContent())) {
					NodeList ip=root.getElementsByTagName("IP");
					Element Url=(Element)ip.item(0);
					sURL = Url.getTextContent();
					if (sURL != null) {
						Log.e("Url.getNodeName", "Url.getNodeName="+Url.getTextContent());//get IP
						return ("pir="+sURL);
					}
				}
				
			}
		}
		return null;
	}
	
	public static void resetEid()
	{
		eid = "0000";
	}
}
