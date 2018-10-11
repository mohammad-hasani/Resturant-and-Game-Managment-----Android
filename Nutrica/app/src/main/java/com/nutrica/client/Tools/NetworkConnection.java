package com.nutrica.client.Tools;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by root on 6/10/2017.
 */

public class NetworkConnection {

    private Socket socket;
    private Scanner in;
    private PrintStream out;

    private String host = "192.168.1.99";
    private int port = 9000;

    public NetworkConnection() {
        try {
            this.socket = new Socket(this.host, this.port);
            in = new Scanner(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintStream(socket.getOutputStream());
            in.useDelimiter("\r\n");
        } catch (IOException e) {
            Log.d("AAAAAAAA", e.getMessage());
        }
    }

    public String sendQuery(String query) {
        try {
            out.print(query);
            out.flush();
            StringBuilder data = new StringBuilder();
            while (in.hasNext()) {
                String response = in.nextLine();
                data.append(response);
                return data.toString();
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void close() {
        try {
            if (this.socket != null)
                this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}