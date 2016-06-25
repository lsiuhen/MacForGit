package com.nepo.nepotransfered.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.model.MessageModel;
import com.nepo.nepotransfered.model.TransferProgressModel;
import com.nepo.nepotransfered.net.download.ObserverManage;
import com.nepo.nepotransfered.utils.SPUtils;
import com.nepo.nepotransfered.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by shen on 16/6/8.
 */
public class SocketServerService extends Service implements Observer {

    // 【abandon】private LocalBroadcastManager mLocalBroadcastManager;
    public static final int PORT = 9527;
    static boolean isConnection = true;
    static boolean startTrans = true;
    private static final String SOCKET_MESSAGE_USUALLY = "USUALLY";
    // 当前发送的文件 下标
    static int fileCount = 0;

    // 未传输过的数据
    private List<TransferProgressModel> transferModelList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ObserverManage.getObserver().addObserver(this);
        Utils.Sout("service", "oncreate");
        new InitSocketThread().start();
        //mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.Sout("service", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化Socket线程
     */
    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    private void initSocket() {//初始化Socket

        try {
            Socket so;
            DataInputStream dis;
            // 0.建立服务器端的server的socket
            ServerSocket ss = new ServerSocket(PORT);
            while (true) {

                // 1.打开socket连接
                // 等待客户端的请求
                so = ss.accept();
                if (so != null && isConnection) {
                    // 通知界面 连接成功
                    isConnection = false;
                    //[TODO 可用]
                    MessageModel model = new MessageModel();
                    model.setMsgType(Constant.transConnectionMsg);
                    ObserverManage.getObserver().setMessage(model);
                }


                System.out.println("---:" + so.toString());
                DataInputStream in = new DataInputStream(new BufferedInputStream(
                        so.getInputStream()));
                DataOutputStream out = new DataOutputStream(
                        new BufferedOutputStream(so.getOutputStream()));
                String comm = in.readUTF();
                //TODO [待改动，车机增加是否接受文件操作，等车机发送确定信息后[yes/NO]，再开始发送文件]
                // 心跳
                if (comm.equals("1")) {
                    new HeartThread(in, out).start();
                }
                // 发送文件
                else if (comm.equals("2")) {
                    if (startTrans) {
                        //[TODO 可用]
                        transferModelList = TransferDB.getTransferDB(SocketServerService.this).queryAllTransferProgress();
                        if (transferModelList.size() > 0) {
                            //TODO 代表开始开始/正在传输、设置flag标示[媒体不可点击、队列不可添加、传输队列不可删除]
                            SPUtils.put(this,Constant.SP_TRANSPROGRESSSTATUS,Constant.SP_TRANSPROGRESSSTATUS);
                            new FileThread(so, in, out, fileCount).start();
                        }
                        if (transferModelList.size() <= 0) {
                            startTrans = false;
                        }
                    }


                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 心跳线程，维持链接，定时发送心跳包
     *
     * @param
     * @param
     */
    class HeartThread extends Thread {
        DataInputStream dis;
        DataOutputStream dos;

        public HeartThread(DataInputStream in, DataOutputStream out) {
            this.dis = in;
            this.dos = out;
        }

        @Override
        public void run() {
            super.run();
            int count = 1;
            try {
                while (true) {
                    int size = 0;
                    byte[] bytes = new byte[1000];
                    while ((size = dis.read(bytes)) != -1) {
                        /**
                         * 如果传输进程中有数据就发送 文件列表过去
                         * TODO [待改进操作，有队列先发送心跳确认信息给车机，待车机确定接受，再发送json列表过去]
                         */
                        if (Constant.TRANSPROGRESSINGJSONLIST.size() > 0) {
                            startTrans = true;
                            dos.write(Constant.getTransJsonArrayByList().getBytes());
                            dos.flush();
                            Constant.TRANSPROGRESSINGJSONLIST.clear();
                        } else {
                            dos.write(SOCKET_MESSAGE_USUALLY.getBytes());
                            dos.flush();
                        }
                        count++;
                    }
                    // dis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送文件 线程
     *
     * @param
     * @param
     */
    class FileThread extends Thread {
        DataInputStream dis;
        DataOutputStream dos;
        Socket mSocket;
        int fCount = 0;

        public FileThread(Socket mSocket, DataInputStream in, DataOutputStream out,
                          int fCount) {
            this.dis = in;
            this.dos = out;
            this.mSocket = mSocket;
            this.fCount = fCount;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronized (this) {
                     /* start **/
                    MessageModel model = new MessageModel();
                    model.setMsgType(Constant.transStartTransMsg);
                    model.setMsgModel(transferModelList.get(0));
                    ObserverManage.getObserver().setMessage(model);

                    int length = 0;
                    double sumL = 0;
                    byte[] sendBytes = new byte[8192];
                    File file = new File(transferModelList.get(0).getTransPath());

                    Utils.Sout("path", transferModelList.get(0).getTransPath());

                    long l = file.length();
                    FileInputStream fis = new FileInputStream(file);
                    while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                        sumL += length;
                        dos.write(sendBytes, 0, length);
                        dos.flush();
                        int pro = (int) ((sumL / l) * 100);
                        // Utils.Sout("pro", pro);
                        transferModelList.get(0).setTransProgress(pro);
                        transferModelList.get(0).setCurrentSize(String.valueOf(sumL));


                        //每10秒更新一下 ，降低更新频率
                        MessageModel model2 = new MessageModel();
                        model2.setMsgType(Constant.transFragmentMsg);
                        model2.setMsgModel(transferModelList.get(0));
                        ObserverManage.getObserver().setMessage(model2);
                    }

                    /**End **/
                    //[TODO 可用]
                    MessageModel model3 = new MessageModel();
                    model3.setMsgType(Constant.transComplateMsg);
                    model3.setMsgModel(transferModelList.get(0));
                    ObserverManage.getObserver().setMessage(model3);
//                    //更新数据库标识 ，当前文件为传输完成
                    //TODO 改为传输完成 修改传输队列中是否传输的表示并删除当前传输进程队列中的数据【修改、删除操作】
                    TransferDB.getTransferDB(SocketServerService.this).updateIsTransed(transferModelList.get(0));
                    TransferDB.getTransferDB(SocketServerService.this).deletTransProgressModel(transferModelList.get(0));
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    dis.close();
                    dos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    mSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void update(Observable observable, Object data) {

    }
}
