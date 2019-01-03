package org.shr.sockettesting;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, String> {

    String dstAddress="192.168.10.138";
    int dstPort=2010;
    String response = "";
    TextView textResponse;
    private Socket socket=null;
    private final Handler handler;

    Client(String addr, String port, TextView textResponse) {
        if (!addr.isEmpty())
        dstAddress = addr;
        if (!port.isEmpty())
        dstPort = Integer.parseInt(port);
        handler = new Handler();

        this.textResponse = textResponse;
    }


    @Override
    protected String doInBackground(Void... arg0) {

        try {
            socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                        1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                /*
                 * notice: inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response= byteArrayOutputStream.toString("UTF-8");

                    showResponse(response);
                    Log.d("ETGDFGR","  called : "+response);
                }

                inputStream.close();
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();

            }

        catch(UnknownHostException e){
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch(IOException e){
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally{
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        return response;
    }

    private void showResponse(final String response) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                                textResponse.setText(textResponse.getText().toString() + "\nFrom Server : " + response);
                        }
                    });
            }
        });

        thread.start();

    }

    @Override
    protected void onPostExecute(String result) {
        //textResponse.setText(response);
        //Log.d("BCVBDFGBR", "data : " + response + "");

        super.onPostExecute(result);
    }



    public void sendMessage(final String msg) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    OutputStream out = socket.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

                    output.println(msg);
                    output.flush();
                    final BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final String st = input.readLine();
                    /*
                     * notice: inputStream.read() will block if no data return
                     */


                 /*   final BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String s = textResponse.getText().toString();
                            if (st.trim().length() != 0)
                                textResponse.setText(s + "\nFrom Server : " + st);
                        }
                    });
*/
                    output.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

}

}