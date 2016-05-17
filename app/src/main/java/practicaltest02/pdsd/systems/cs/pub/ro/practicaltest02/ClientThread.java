package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private String address;
    private int port;
    private String url;
    private TextView etUrlBody;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String url,
            TextView etUrlBody) {
        this.address = address;
        this.port = port;
        this.url = url;
        this.etUrlBody = etUrlBody;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e("[CLIENT THREAD]", "Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(url);
                printWriter.flush();
                String body;
                while ((body = bufferedReader.readLine()) != null) {
                    final String finalizedBody = body;
                    etUrlBody.post(new Runnable() {
                        @Override
                        public void run() {
                            etUrlBody.append(finalizedBody + "\n");
                        }
                    });
                }
            }
            socket.close();
        } catch (Exception e) {
            Log.e("[CLIENT THREAD]", "An exception has occurred: " + e);
        }
    }

}
