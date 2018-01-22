package kaist.aguno.melona;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketEventHandler {

    private Socket socket;
    private Activity mActivity;

    public SocketEventHandler(Activity activity) {
        mActivity = activity;
    }

    public void connectSocket() {
        try {
            socket = IO.socket("http://143.248.132.156:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on("confirmConnection", onConfirmConnection);
        socket.on("questAccepted", onQuestAccepted);
        socket.on("questGaveup", onQuestGaveup);
        socket.on("questWithdrawn", onQuestWithdrawn);
        socket.on("questCompleted", onQuestCompleted);

        socket.connect();
    }

    public void disconnectSocket() throws SocketNotInitializedException {
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

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Ignore
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Toast.makeText(mActivity, "Disconnected from server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConfirmConnection = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        Toast.makeText(mActivity, data.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject response = new JSONObject();
                    // TODO : send user's kakaoId to server
                    // response.put("kakaoId", <kakaoId> );
                    socket.emit("verifyKakaoId", response);
                }
            });
        }
    };

    private Emitter.Listener onQuestAccepted = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String questId = data.getString("questId");
                        String roomURL = data.getString("roomURL");

                        // TODO : further implementation
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onQuestGaveup = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String questId = data.getString("questId");

                        // TODO : further implementation
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onQuestWithdrawn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String questId = data.getString("questId");

                        // TODO : further implementation
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onQuestCompleted = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String questId = data.getString("questId");

                        // TODO : further implementation
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private class SocketNotInitializedException extends Exception {
        public SocketNotInitializedException(String msg) {
            super(msg);
        }
    }

}
