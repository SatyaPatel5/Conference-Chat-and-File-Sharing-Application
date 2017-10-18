/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoconferencing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import static videoconferencing.ServerThread.socket;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Satya
 */

class ServerGUI{
    static int server_files_count = 1;
    static ServerSocket ss ;
    static ServerSocket ss1 ;
    static ServerThread st ; 
    static ServerThread1 st1;
    JFrame server;
    static ServerFileReader sfr;
    ServerGUI(){
        server = new JFrame("Server");
        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(43, 135, 214));

        //Add label
        
        JLabel label = new JLabel("Video Conferencing Application");
        label.setFont(new Font("Papyrus", Font.BOLD, 44));
        label.setForeground(new Color(255, 255, 255));
        

        label.setSize(new Dimension(1000, 60));
        label.setLocation(150, 63);

        
        //Button 
        JButton btn_start = new JButton("Start Server");
        JButton btn_stop = new JButton("Stop Server");
        
        


        btn_start.setBackground(new Color(198, 200, 16));
        btn_start.setSize(new Dimension(140, 65));
        btn_start.setLocation(135, 488);
        
        btn_stop.setBackground(new Color(198, 200, 16));
        btn_stop.setSize(new Dimension(140, 65));
        btn_stop.setLocation(726, 488);
        btn_stop.setEnabled(false);
        
        // Labels
        
        JLabel label1 = new JLabel("Server Status: OFF");
        label1.setFont(new Font("Baskerville Old Face", Font.PLAIN, 32));
        label1.setForeground(new Color(255, 255, 255));
       
        label1.setSize(new Dimension(1000, 40));
        label1.setLocation(379, 291);
        
        JLabel label2 = new JLabel("Server listens at Port Number : 2585");
        label2.setFont(new Font("Baskerville Old Face", Font.PLAIN, 32));
        label2.setForeground(new Color(255, 255, 255));
       
        label2.setSize(new Dimension(1000, 40));
        label2.setLocation(269, 331);
        
        JLabel label3 = new JLabel("Please Start the Server.");
        label3.setFont(new Font("Baskerville Old Face", Font.PLAIN, 32));
        label3.setForeground(new Color(255, 255, 255));
       
        label3.setSize(new Dimension(1000, 40));
        label3.setLocation(269, 400);

        
        
        ///Action Listeners
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label1.setText("Server Status: ON");
                btn_stop.setEnabled(true);
                btn_start.setEnabled(false);
                
                try {
                    //Start the Server
                    ss = new ServerSocket(2585);
                    ss1 = new ServerSocket(2586);
                    st = new ServerThread(ss,server,label3);
                    st1 = new ServerThread1(ss1,server,label3);
                    st.start();
                    st1.start();
                    
                } catch (IOException ex) {
                    Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btn_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label1.setText("Server Status: OFF");
                btn_stop.setEnabled(false);
                btn_start.setEnabled(true);
                st.stop();
                label3.setText("Please Start the Server.");
                server.repaint();
                try {
                    ss.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        panel.add(btn_start);
        panel.add(btn_stop);
        panel.add(label);
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        
        server.setSize(1000, 700);
        server.setContentPane(panel);
        server.setLocationByPlatform(true);
        server.setVisible(true);
        server.setResizable(false);
    }
}



class ServerThread extends Thread{
    static Socket socket[] = new Socket[50];
    String msg = "";
    ServerSocket ss;
    JFrame server;
    JLabel label3;
    static int i = 0;
    public ServerThread(ServerSocket ss, JFrame server,JLabel label3) {
        this.ss = ss;
        this.server = server;
        this.label3 = label3;
    }
    
    @Override
    public void run(){
        try {
            label3.setText("Waiting for Clients:");
            server.repaint();
            
            
            Socket s;
            while(true){ 
                socket[i] = ss.accept();
                new ServerMsgReader(socket[i], server).start();
                i++;
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}


class ServerThread1 extends Thread{
    static Socket socket[] = new Socket[50];
    String msg = "";
    ServerSocket ss1;
    JFrame server;
    JLabel label3;
    static int i = 0;
    public ServerThread1(ServerSocket ss, JFrame server,JLabel label3) {
        this.ss1 = ss;
        this.server = server;
        this.label3 = label3;
    }
    
    @Override
    public void run(){
        try {
            Socket s;
            while(true){ 
                socket[i] = ss1.accept();
                new ServerFileReader(socket[i]).start();
                i++;
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}

class ServerMsgReader extends Thread{
    Socket s1;
    DataInputStream din;
    JFrame server;
    DataOutputStream dout;
    public ServerMsgReader(Socket ss,JFrame server) {
        this.s1 = ss;
        this.server = server;
    } 
    @Override
    public void run(){
        Socket s;
        while(true){ 
            try {
                //for(int i =0; i<ServerThread.i;i++){
                    if(true){
                    DataInputStream in = new DataInputStream(s1.getInputStream());
                    DataOutputStream out = new DataOutputStream(s1.getOutputStream());
                    
                    
                    String chat_msg = in.readUTF();
                    for(int j = 0; j<=(ServerThread.i-1);j++){
                            if(j!=ServerThread.i){
                                s = socket[j];
                                din = new DataInputStream(s.getInputStream());
                                dout = new DataOutputStream(s.getOutputStream());
                                dout.writeUTF(chat_msg);
                            }
                    }
                    }
                //}    
            } catch (IOException ex) {
                Logger.getLogger(ServerMsgReader.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        }
    }
}



class ServerFileReader extends Thread{
    
    Socket s;
    OutputStream os;
    File videofile;
    byte [] bytearray;
    FileInputStream fis;
    BufferedInputStream bis;
    DataInputStream din;
    DataOutputStream dout;
    InputStream is;
    FileOutputStream fos;
    BufferedOutputStream bos;
    
    public ServerFileReader(Socket s) {
        this.s = s;
    }
    
    public void run(){
        while(true){
            try{    
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
                //File f = new File("C:/Users/Satya/Downloads/2.avi");  
                //f str = in.read();
                
                
                
                
                //bytearray  = new byte [(int)f.length()];
                bytearray  = new byte [50000000];
                is = s.getInputStream();
                fos = new FileOutputStream("C:/Users/Satya/Desktop/Done.avi");
                bos = new BufferedOutputStream(fos);
                IOUtils.read(is, bytearray);
                bos.write(bytearray);
                bos.flush();
                fos.close();
                bos.close();
                is.close();
                in.close();
                out.close();
                
                
                
                /*fis = new FileInputStream(videofile);
                bis = new BufferedInputStream(fis);
                // bis.read(bytearray);
                bytearray = IOUtils.toByteArray(bis);
                for(int j = 0; j<=(ServerThread.i-1);j++){
                    if(j!=ServerThread.i){
                        s1 = socket[j];
                        os = s1.getOutputStream();
                        os.write(bytearray);
                        os.flush();
                    }
                }*/
                
                
        }catch(Exception e){
            
        }
        }
    }
}
public class VideoConferencing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
    }
    
}
