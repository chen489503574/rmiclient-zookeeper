package rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Chenjf on 2018/11/30.
 * 这个类是专门对 Socket 的管理，Socket的创建及request的发送
 */
public class TCPTransport {

    private String serviceAddress;

    public TCPTransport(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    private Socket newSocket(){
        System.out.println("创建一个新的socket连接。。。");
        Socket socket;
        try {
            String[] arrs = serviceAddress.split(":");
            socket = new Socket(arrs[0],Integer.parseInt(arrs[1]) );
            return socket;
        } catch (Exception e) {
            throw new RuntimeException("建立连接失败！");
        }finally {

        }
    }

    //发送操作
    public Object send(RpcRequest request){
        Socket socket = null;
        try {
            socket = newSocket();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            outputStream.flush();

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object result = inputStream.readObject();

            inputStream.close();
            outputStream.close();

            return result;

        }catch (Exception e){
            throw new RuntimeException("发起远程调用异常",e);
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
