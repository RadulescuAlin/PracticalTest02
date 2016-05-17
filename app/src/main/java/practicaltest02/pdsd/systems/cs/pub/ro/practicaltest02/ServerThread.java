package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {

    private int port = 0;
    private ServerSocket serverSocket = null;

    private HashMap<String, String> data = null;

    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e("ServerThread", "An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }
        this.data = new HashMap<String, String>();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized void putData(String url, String body) {
        this.data.put(url, body);
    }

    public synchronized HashMap<String, String> getData() {
        return data;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i("ServerThread", "Waiting for a connection...");
                Socket socket = serverSocket.accept();
                Log.i("ServerThread", "A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (Exception e) {
            Log.e("ServerThread", "An exception has occurred: " + e);
        }
    }

    public void stopThread() {
        if (serverSocket != null) {
            interrupt();
            try {
                serverSocket.close();
            } catch (Exception e) {
                Log.e("ServerThread", "An exception has occurred: " + e);
            }
        }
    }

}