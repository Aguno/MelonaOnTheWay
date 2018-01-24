package kaist.aguno.melona;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MessageQueue {

    private ArrayList<Message> queue;
    private int head, tail;

    public MessageQueue() {
        queue = new ArrayList<Message>();
        head = 0;
        tail = 0;
    }

    public void pushMessage(Message msg) {
        queue.add(msg);
        tail++;
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public Message popMessage() {
        if(isEmpty()) return null;
        Message ret = queue.get(head);
        head++;
        return ret;
    }

    public static class Message {
        String messageType;
        String questId;
        String roomURL;

        public Message(String messageType, String questId, String roomURL) {
            this.messageType = messageType;
            this.questId = questId;
            this.roomURL = roomURL;
        }

        public Message(String messageType, String questId) {
            this.messageType = messageType;
            this.questId = questId;
        }
    }

}