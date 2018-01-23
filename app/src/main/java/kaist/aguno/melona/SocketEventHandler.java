package kaist.aguno.melona;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketEventHandler {

    private static Socket socket;
    private static String mKakaoId;

    public static void connectSocket(String kakaoId) {
        try {
            socket = IO.socket("http://143.248.36.249:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mKakaoId = kakaoId;

        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on("confirmConnection", onConfirmConnection);
        socket.on("questAccepted", onQuestAccepted);
        socket.on("questGaveup", onQuestGaveup);
        socket.on("questWithdrawn", onQuestWithdrawn);
        socket.on("questCompleted", onQuestCompleted);

        socket.connect();
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
            // ignore
        }
    };

    private static Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // ignore
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

                // TODO : further implementation
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static Emitter.Listener onQuestGaveup = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String questId = data.getString("questId");

                // TODO : further implementation
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

                // TODO : further implementation
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

                // TODO : further implementation
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static class SocketNotInitializedException extends Exception {
        public SocketNotInitializedException(String msg) {
            super(msg);
        }
    }

}
