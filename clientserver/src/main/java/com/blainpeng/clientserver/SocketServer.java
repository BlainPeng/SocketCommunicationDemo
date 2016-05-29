package com.blainpeng.clientserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by BlainPeng on 16/5/29.
 */
public class SocketServer {


    /**
     * 用来保存不同的客户端
     */
    private static Map<String, Socket> mClients = new LinkedHashMap<>();

    public static void main(String[] args) {

        int port = 9999;
        try {
            //创建服务器
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                //获取客户端的连接
                final Socket socket = serverSocket.accept();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //读取从客户端发送过来的数据
                            InputStream inputStream = socket.getInputStream();
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = inputStream.read(buffer)) != -1) {
                                String data = new String(buffer, 0, len);

                                //先认证客户端
                                if (data.startsWith("#")) {
                                    mClients.put(data, socket);
                                } else {
                                    //将数据发送给指定的客户端
                                    String[] split = data.split("#");
                                    Socket c = mClients.get("#" + split[0]);
                                    OutputStream outputStream = c.getOutputStream();
                                    outputStream.write(split[1].getBytes());

                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
