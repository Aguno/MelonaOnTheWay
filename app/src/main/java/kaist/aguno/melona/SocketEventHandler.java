package kaist.aguno.melona;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketEventHandler {

    private static Socket socket;
    private static String mKakaoId;
    private static Activity mActivity;

    public static MessageQueue msgQueue;

    public static void connectSocket(String kakaoId, Activity activity) {
        try {
            socket = IO.socket("http://143.248.36.249:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mKakaoId = kakaoId;
        mActivity = activity;

        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on("confirmConnection", onConfirmConnection);
        socket.on("questAccepted", onQuestAccepted);
        socket.on("questGaveup", onQuestGaveup);
        socket.on("questWithdrawn", onQuestWithdrawn);
        socket.on("questCompleted", onQuestCompleted);

        socket.connect();

        msgQueue = new MessageQueue();
    }

    public static void disconnectSocket() throws SocketNotInitializedException {
        if(socket != null) {
            socket.disconnect();

            socket.off(Socket.EVENT_CONNECT, onConnect);
            socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.off("confirmConnection", onConfirmConnection);
            socket.off("questAccepted", onQuestAccepted);
            socket.off("questGaveup", onQuestGaveup);
            socket.off("questWithdrawn", onQuestWithdrawn);
            socket.off("questCompleted", onQuestCompleted);
        } else {
            throw new SocketNotInitializedException("Socket is not initialized. Did you call connect() method before?");
        }
    }

    private static Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity.getApplicationContext(), "connected to server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private static Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity.getApplicationContext(), "disconnected from server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private static Emitter.Listener onConfirmConnection = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject response = new JSONObject();
            try {
                response.put("kakaoId", mKakaoId);
                socket.emit("verifyKakaoId", response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static Emitter.Listener onQuestAccepted = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String questId = data.getString("questId");
                String roomURL = data.getString("roomURL");

                Log.e("questAccepted", questId);

                if(MainActivity.isActivityVisible()) createAcceptPopUp(mActivity, questId, roomURL);
                else {
                    Log.e("visible", MainActivity.isActivityVisible() ? "true" : "false");
                    msgQueue.pushMessage(new MessageQueue.Message("questAccepted", questId, roomURL));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public static void createAcceptPopUp(Activity activity, String questId, final String roomURL) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder;
                final AlertDialog popup;
                builder = new AlertDialog.Builder(mActivity);
                View mView = mActivity.getLayoutInflater().inflate(R.layout.chatting,null);
                Button yesButton = mView.findViewById(R.id.yes);
                Button noButton = mView.findViewById(R.id.no);
                builder.setView(mView);
                popup = builder.create();
                popup.show();
                yesButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        // new MyPageDetail.putQuest().execute("http://143.248.36.249:8080/api/withdraw");
                        //Toast.makeText(mActivity.getApplicationContext(), roomURL, Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(roomURL); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mActivity.startActivity(intent);
                        popup.cancel();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        popup.cancel();
                    }
                });
            }
        });
    }

    private static Emitter.Listener onQuestGaveup = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String questId = data.getString("questId");

                Log.e("questAccepted", questId);

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(mActivity.getApplicationContext(), "상대가 취소했습니다", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static Emitter.Listener onQuestWithdrawn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String questId = data.getString("questId");

                Log.e("questAccepted", questId);

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(mActivity.getApplicationContext(), "상대가 취소했습니다", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static Emitter.Listener onQuestCompleted = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String questId = data.getString("questId");

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(mActivity.getApplicationContext(), "상대가 퀘스트 완료를 확인했습니다.", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public static class SocketNotInitializedException extends Exception {
        public SocketNotInitializedException(String msg) {
            super(msg);
        }
    }
    public static class MyOtherAlertDialog {

        public static AlertDialog create(Context context) {
            final TextView message = new TextView(context);
            // i.e.: R.string.dialog_message =>
            // "Test this dialog following the link to dtmilano.blogspot.com"
            final SpannableString s =
                    new SpannableString(context.getText(R.string.dialog_message));
            Linkify.addLinks(s, Linkify.WEB_URLS);
            message.setText(s);
            message.setMovementMethod(LinkMovementMethod.getInstance());

            return new AlertDialog.Builder(context)
                    .setTitle(R.string.dialog_title)
                    .setCancelable(true)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(R.string.dialog_action_dismiss, null)
                    .setView(message)
                    .create();
        }
    }

}
