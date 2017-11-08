package tw.com.a_i_t.IPCamViewer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import tw.com.a_i_t.IPCamViewer.IPCam.IPCamWhistlerEvent;

import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

public class CameraSocket {
	private Socket clientSocket;
	private BufferedWriter bw;   
	private Thread thread;
	private String targetIp;
	private int targetPort;
	private int tcpTimeout=5000;
	private int tcpLinkcnt=0;
	private long tcpSendtime;
	private long tcpRecvtime;
	
	private Handler mHandler;
	
	char data[] = {'W','a','k','e','u','p','\n'};
	
	public CameraSocket(Handler handler) {
		mHandler = handler;
	}
	
	public void SendTcpWakeupData(String ip) {
		targetIp = ip;
		thread=new Thread(SendWakeup);
		thread.start(); 
	}


	private Runnable SendWakeup=new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				InetAddress serverIp = InetAddress.getByName(targetIp);
				int serverPort = 8000;
				
				clientSocket = new Socket();
				clientSocket.setSoTimeout(tcpTimeout);
				
				tcpSendtime = System.currentTimeMillis();
				
				clientSocket.connect(new InetSocketAddress(targetIp, serverPort), 3000);
				
				bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()),7);

				while (clientSocket.isConnected()) {
						bw.write(data);  
						bw.flush();
						bw.close();
						clientSocket.close();
				}
			}catch(Exception e){
				e.printStackTrace();
				//Log.e("CameraSocket","Err ="+e.toString());
				tcpRecvtime = System.currentTimeMillis();
				Log.e("CameraSocket","timeout ="+ (tcpRecvtime-tcpSendtime));
				
				try {
					if ((tcpRecvtime-tcpSendtime) < 3000)
						Thread.sleep(3000 - (tcpRecvtime-tcpSendtime));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Message message;
				message = mHandler.obtainMessage();
				message.arg1 = IPCamWhistlerEvent.MSG_WAKEUP_SENT;
				mHandler.sendMessage(message);

				return;
			}
			try {
				Thread.sleep(3000);
				Message message;
				message = mHandler.obtainMessage();
				message.arg1 = IPCamWhistlerEvent.MSG_WAKEUP_SENT;
				mHandler.sendMessage(message);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	////only do socket connection
	public void TcpConnectPort(String ip,int port) {
		tcpLinkcnt = 0;
		targetIp = ip;
		targetPort = port;
		thread=new Thread(ConnectPort);
		thread.start(); 
	}
	
	private Runnable ConnectPort=new Runnable(){
		@Override
		public void run() {
			//Message message;
			//message = mHandler.obtainMessage();
			//message.arg1 = IPCamWhistlerEvent.MSG_RUNRTSP_EVENT;
			//mHandler.sendMessage(message);

			// TODO Auto-generated method stub
			try{

				clientSocket = new Socket();
				clientSocket.setSoTimeout(tcpTimeout);
				clientSocket.connect(new InetSocketAddress(targetIp, targetPort), tcpTimeout);

				while (clientSocket.isConnected()) {
					clientSocket.close();
				}
				/*
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/
				Log.e("CameraSocket","#############################554 connect fail times =" + tcpLinkcnt);
				
				Message message;
				message = mHandler.obtainMessage();
				message.arg1 = IPCamWhistlerEvent.MSG_RUNRTSP_EVENT;
				mHandler.sendMessage(message);
				
			}catch(IOException e){
				e.printStackTrace();
				//Log.e("CameraSocket","Err ="+e.toString());
				tcpLinkcnt ++;
				if (tcpLinkcnt > 100) {
					Message message;
					message = mHandler.obtainMessage();
					message.arg1 = IPCamWhistlerEvent.MSG_CONNECTRTSP_FAILED;
					mHandler.sendMessage(message);	
				}else {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					TcpConnectPort(targetIp,targetPort);
				}
			}
		}
	};
	
}
