/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoconferencing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
/**
 *
 * @author Satya
 */


class ClientFrame{
    JFrame clientconnect;
    JFrame chat;
    String username;
    InetAddress ipaddress;
    Socket socket;
    Socket socket_media;
    public ClientFrame() {
        clientconnect = new JFrame("Connect to the Server");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(43, 135, 214));
        
        JLabel label = new JLabel("Video Conferencing Application");
        label.setFont(new Font("Papyrus", Font.BOLD, 44));
        label.setForeground(new Color(255, 255, 255));
        

        label.setSize(new Dimension(1000, 60));
        label.setLocation(150, 63);
        
        
        //Information Form
        JLabel ipadd = new JLabel("IP Address:");
        ipadd.setFont(new Font("Baskerville Old Face", Font.PLAIN, 32));
        ipadd.setForeground(new Color(255, 255, 255));
       
        ipadd.setSize(new Dimension(300, 40));
        ipadd.setLocation(302, 222);

        JTextField ipadd_txt = new JTextField("");
        ipadd_txt.setSize(220, 50);
        ipadd_txt.setLocation(564, 216);
        ipadd_txt.setFont(new Font("Verdana", Font.PLAIN, 32));
        ipadd_txt.setHorizontalAlignment(JTextField.CENTER);
        
        
        
        
        JLabel port = new JLabel("Port Number:");
        port.setFont(new Font("Baskerville Old Face", Font.PLAIN, 32));
        port.setForeground(new Color(255, 255, 255));
       
        port.setSize(new Dimension(300, 40));
        port.setLocation(285, 332);
        


        JTextField port_txt = new JTextField("2585");
        port_txt.setSize(220, 50);
        port_txt.setLocation(564, 325);
        port_txt.setFont(new Font("Verdana", Font.PLAIN, 32));
        port_txt.setHorizontalAlignment(JTextField.CENTER);
        
        

        
        
        
        JLabel name = new JLabel("User Name:");
        name.setFont(new Font("Baskerville Old Face", Font.PLAIN, 32));
        name.setForeground(new Color(255, 255, 255));
       
        name.setSize(new Dimension(300, 40));
        name.setLocation(294, 439);
        


        JTextField name_txt = new JTextField("");
        name_txt.setSize(220, 50);
        name_txt.setLocation(564, 434);
        name_txt.setFont(new Font("Verdana", Font.PLAIN, 32));
        name_txt.setHorizontalAlignment(JTextField.CENTER);
        

        
        
        //Submit Button 
        JButton submit = new JButton("LogIn");
        submit.setBackground(new Color(198,200,16));
        submit.setSize(new Dimension(140, 65));
        submit.setLocation(431, 543);

        
        
        
        
        panel.add(label);
        panel.add(submit);
        panel.add(ipadd);
        panel.add(ipadd_txt);
        panel.add(port);
        panel.add(port_txt);
        panel.add(name);
        panel.add(name_txt);
        
        
        
        clientconnect.setContentPane(panel);
        clientconnect.setSize(1000,700);
        clientconnect.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientconnect.setVisible(true);
        clientconnect.setResizable(false);
        
        
        
        //Chat Frame
        /*
        
        
        
        
        */
        chat = new JFrame("Chat:"+username);
        
        JLabel label_chat = new JLabel("Video Conferencing Application");
        label_chat.setFont(new Font("Papyrus", Font.BOLD, 27));
        label_chat.setForeground(new Color(255, 255, 255));
        

        label_chat.setSize(new Dimension(500, 60));
        label_chat.setLocation(35, 42);
        
        
        JLabel label_user = new JLabel("Welcome User.");
        label_user.setFont(new Font("Papyrus", Font.BOLD, 25));
        label_user.setForeground(new Color(255, 255, 255));
        

        label_user.setSize(new Dimension(500, 60));
        label_user.setLocation(75, 890);
        
        
        
        JTabbedPane tabpane = new JTabbedPane();
        
        tabpane.setBounds(0, 120, 500, 760);
        tabpane.setFont(new Font("Papyrus", Font.PLAIN, 22));
        tabpane.setBackground(new Color(255, 242, 0));
        

        

        
        
        chat.getContentPane().setBackground(new Color(43, 135, 214));
        chat.add(label_chat);
        chat.add(label_user);
        chat.add(tabpane);
        chat.setLayout(null);
        chat.setSize(500,1000);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setVisible(false);
        chat.setResizable(false);
        
        
        
        
        //Action Listener
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientconnect.dispose();
                username = name_txt.getText();
                String ipaddString = ipadd_txt.getText();
                
                
                try {
                    ipaddress = InetAddress.getByName(ipaddString);
                    socket = new Socket(ipaddress, 2585);
                    socket_media = new Socket(ipaddress,2586);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
                ChatPanel chatPanel = new ChatPanel(username,socket);
                Media media = new Media(username,socket_media);
                chat.setTitle("Chat: "+username);
                label_user.setText("Welcome User: "+ username);
                tabpane.addTab("  Chat  ",chatPanel );
                tabpane.addTab("  Group Members  ", new Group(username));
                tabpane.addTab("  Media  ", media);
                tabpane.addTab("  Info  ", new Info(username));
                
                MediaThread mt = new MediaThread(socket_media,media);
                mt.start();
                ChatPanelThread chatThread = new ChatPanelThread(socket, chatPanel);
                chatThread.start();
                chat.setVisible(true);
               
               
               /**/
            }
        });
        
        
    }
    
}


class ChatPanel extends JPanel{
    Socket s;
    JTextArea chat_disp;
    String username;
    String send_msg;
    JTextArea chat_msg;
    
    ChatPanel(String username,Socket s){
        this.s = s;
        this.username = username;
        //(new GridLayout(4, 4));
        setLayout(null);
        setBackground(new Color(43, 135, 214));
        
        
        
        //Chat Display TextArea
        chat_disp = new JTextArea("     "+new Date().toString());
        chat_disp.setFont(new Font("Verdana",Font.PLAIN,24));
        chat_disp.setEditable(false);

        chat_disp.setLineWrap(true);
        JScrollPane chat_scroll = new JScrollPane(chat_disp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //
        chat_scroll.setBounds(30, 30, 430, 450);
        
        
        ///Msg Box
        chat_msg = new JTextArea();
        chat_msg.setFont(new Font("Verdana",Font.PLAIN,24));
        chat_msg.setEditable(true);

        chat_msg.setLineWrap(true);
        JScrollPane chatmsg_scroll = new JScrollPane(chat_msg,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        chatmsg_scroll.setBounds(30, 530, 300, 100);
        
        
        //Submit Button 
        
        //Submit Button 
        JButton send = new JButton("Send");
        send.setBackground(new Color(198,200,16));
        send.setSize(new Dimension(100, 65));
        send.setLocation(350, 550);
        
        
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send_msg = chat_msg.getText();
                DataInputStream in;
                DataOutputStream out;
                try {
                    in = new DataInputStream(s.getInputStream());
                    out = new DataOutputStream(s.getOutputStream());
                    out.writeUTF("\n"+username+": "+send_msg);
                    chat_msg.setText("");
                } catch (IOException ex) {
                    Logger.getLogger(ChatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
        });
        
        
        add(chat_scroll);
        add(chatmsg_scroll);
        add(send);
        
    }
}


class ChatPanelThread extends Thread{
    String msg = "";
    String newUser;
    DataInputStream in;
    DataOutputStream out;
    Socket s;
    ChatPanel chatPanel;
    public ChatPanelThread(Socket s, ChatPanel chatPanel) {
        this.s = s;
        this.chatPanel = chatPanel;
        try {
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ChatPanelThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void run(){
        try {
           while(true){
                //out.writeUTF("User: "+chatPanel.username);
                newUser = in.readUTF();
                msg = chatPanel.chat_disp.getText() + newUser;
                chatPanel.chat_disp.setText(msg);
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatPanelThread.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}




class Group extends JPanel{
    Group(String username){
        
    }
}

class Media extends JPanel{
    Socket s;
    String username;
    JTextField filelocation_txt ;
    Media(String username, Socket s){
        this.s = s;
        this.username = username;
        
        setLayout(null);
        setBackground(new Color(43, 135, 214));
        
        filelocation_txt = new JTextField("kjdds");
        filelocation_txt.setFont(new Font("Verdana",Font.PLAIN,24));
        filelocation_txt.setEditable(true);
        filelocation_txt.setBounds(19, 343,500,50);
        
        //Button 
        
        JButton send = new JButton("Send File");
        send.setBackground(new Color(198,200,16));
        send.setSize(new Dimension(100, 65));
        send.setLocation(350, 550);
        
        
        
        add(send);
        add(filelocation_txt);
        
        
        
        send.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
            OutputStream os;
            File videofile;
            byte [] bytearray;
            FileInputStream fis;
            BufferedInputStream bis;
            DataInputStream din;
            DataOutputStream dout;
            int filesize;
                try {
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    String filename = filelocation_txt.getText();
                    videofile = new File(filename);
                    filesize = (int)videofile.length();
                    //out.write(filesize);
                    //out.flush();
                    //dout.write(filesize);
                    fis = new FileInputStream(videofile);
                    bis = new BufferedInputStream(fis);
                    //bis.read(bytearray);
                    bytearray = IOUtils.toByteArray(bis);
                    os = s.getOutputStream();
                    os.write(bytearray);
                    os.flush();
                    os.close();
                    fis.close();
                    bis.close();
                    in.close();
                    out.close();
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                }
            }
        });
        
    }
    
    
}

class MediaThread extends Thread{
    InputStream is;
    FileOutputStream fos;
    BufferedOutputStream bos;
    Socket s;
    Media m;
    MediaThread(Socket s, Media m){
        this.s = s;
        this.m = m;
    }
    
    @Override
    public void run(){
        try{
            /*File f = new File("C:/Users/Satya/Downloads/2.avi");
            byte [] mybytearray  = new byte [(int)f.length()];
            is = s.getInputStream();
            fos = new FileOutputStream("C:/Users/Satya/Desktop/Done.avi");
            bos = new BufferedOutputStream(fos);
                    //is.read(mybytearray);
            IOUtils.read(is, mybytearray);
            bos.write(mybytearray);
            bos.flush();
            fos.close();
            bos.close();
            is.close();*/
        }catch(Exception ex){
                
        }
    }
    
}

class Info extends JPanel{
    Info(String username){
        JPanel panel_chat = new JPanel();
        panel_chat.setLayout(null);
        panel_chat.setBackground(new Color(43, 135, 214));
    }
}






public class ClientLogin {
    public static void main(String args[]){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientFrame();
            }
        });
    }
}
