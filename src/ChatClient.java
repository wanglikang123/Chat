import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ChatClient extends Frame {
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnected = false;

	TextField tfTxt = new TextField();
	
	TextArea taContent = new TextArea();
	
	Thread tRecv = new Thread(new RecvThead());

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatClient().lauchFrame();
	}

	public void lauchFrame() {
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				disconnet();
				System.exit(0);
			}
			
		});
		tfTxt.addActionListener(new TFListener());;
		setVisible(true);
		connect();
		tRecv.start();
	}
	
	public void connect() {
		try {
			s = new Socket("127.0.0.1",8866);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			System.out.println("connected!");
			bConnected = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void disconnet(){
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class TFListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String str=tfTxt.getText().trim();
			//taContent.setText(str);
			tfTxt.setText("");
			try {
				dos.writeUTF(str);
				dos.flush();
				//dos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

    private class RecvThead implements Runnable{

		public void run() {
			try{
			   while(bConnected){
				   String str = dis.readUTF();
				   //System.out.println(str);
				   taContent.setText(taContent.getText()+str+'\n');
			   } 
			}catch (SocketException e){
				System.out.println("退出");
			}catch (EOFException e){
				System.out.println("退出，再见");
			}catch (IOException e){
			   e.printStackTrace();
			}
		}
    	
    }
}
