package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest02MainActivity extends ActionBarActivity {

    EditText etServerPort;
    EditText etClientPort;
    EditText etClientUrl;
    EditText etRequestBody;
    Button btnStartServer;
    Button btnGet;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private StartButtonClickListener startButtonClickListener = new StartButtonClickListener();
    private class StartButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = etServerPort.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e("[MAIN ACTIVITY]", "Could not create server thread!");
            }

        }
    }


    private GetUrlBodyButtonClickListener getUrlBodyButtonClickListener = new GetUrlBodyButtonClickListener();
    private class GetUrlBodyButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientPort    = etClientPort.getText().toString();
            if(clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e("[MAIN ACTIVITY]", "There is no server to connect to!");
                return;
            }

            String url = etClientUrl.getText().toString();
            if (url == null || url.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client (city / information type) should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            etRequestBody.setText("");

            clientThread = new ClientThread(
                    "127.0.0.1",
                    Integer.parseInt(clientPort),
                    url,
                    etRequestBody);
            clientThread.start();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        etServerPort = (EditText)findViewById(R.id.et_server_port);
        etClientPort = (EditText)findViewById(R.id.et_client_port);
        etClientUrl = (EditText)findViewById(R.id.et_client_url);
        etRequestBody = (EditText)findViewById(R.id.et_request_body);
        btnStartServer = (Button)findViewById(R.id.btn_start_server);
        btnGet = (Button)findViewById(R.id.btn_get);

        btnStartServer.setOnClickListener(new StartButtonClickListener());
        btnGet.setOnClickListener(new GetUrlBodyButtonClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test02_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
