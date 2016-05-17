package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i("[COMMUNICATION THREAD]", "Waiting for URL from client");
                    String url = bufferedReader.readLine();
                    HashMap<String, String> data = serverThread.getData();
                    String body = null;
                    if (data != null && !data.isEmpty() && url != null && !url.isEmpty()) {
                        if (data.containsKey(url)) {
                            Log.i("[COMMUNICATION THREAD]", "Getting url body from cache...");
                            body = data.get(url);
                        } else {
                            Log.i("[COMMUNICATION THREAD]", "Making request for body");
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpGet request = new HttpGet();
                            URI website = new URI(url);
                            request.setURI(website);
                            HttpResponse response = httpclient.execute(request);
                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    response.getEntity().getContent()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while((line = in.readLine()) != null){
                                sb.append(line);
                            }
                            body = sb.toString();
                            data.put(url, body);
                            serverThread.putData(url, body);
                        }
                    } else {
                        Log.e("[COMMUNICATION THREAD]", "Error receiving url from client");
                    }
                    printWriter.println(body);
                    printWriter.flush();
                } else {
                    Log.e("[COMMUNICATION THREAD]", "BufferedReader / PrintWriter are null!");
                }

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("[COMMUNICATION THREAD]", "Socket is null!");
        }
    }

}
