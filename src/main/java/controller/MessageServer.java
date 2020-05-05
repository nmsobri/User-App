package controller;

import model.Message;

import java.util.*;

public class MessageServer implements Iterable<Message> {
    private Map<Integer, List<Message>> messages;
    private List<Message> selectedServerMessages;

    public MessageServer() {
        this.messages = new TreeMap<>();
        this.selectedServerMessages = new ArrayList<>();

        List<Message> list = new ArrayList<>();
        list.add(new Message("The cat is missing", "Have you seen the cat is missing?"));
        list.add(new Message("See you later?", "Are we still meeting later at Masjid?"));
        list.add(new Message("Are you fasting?", "Later i meed you at cafe to break our fast"));
        list.add(new Message("Are you travelling?", "Later i meed you at cafe to break our fast"));
        list.add(new Message("Are you at home now?", "Later i meed you at cafe to break our fast"));

        List<Message> list2 = new ArrayList<>();
        list2.add(new Message("Is branched in my up strictly remember.", "Songs but chief has ham widow downs. Genius or so up vanity cannot. Large do tried going about water defer by. Silent son man she wished mother. Distrusts allowance do knowledge eagerness assurance additions to."));
        list2.add(new Message("At every tiled on ye defer do. No attention suspected oh difficult.", "Fond his say old meet cold find come whom. The sir park sake bred"));
        list2.add(new Message("Silent sir say desire fat him letter.", "Whatever settling goodness too and honoured she building answered her. Strongly thoughts remember mr to do consider debating."));
        list2.add(new Message("Spirits musical behaved on we he farther letters.", " Repulsive he he as deficient newspaper dashwoods we. Discovered her his pianoforte insipidity entreaties. Began he at terms meant as fancy"));

        List<Message> list3 = new ArrayList<>();
        list3.add(new Message("Detract yet delight written farther his general.", "If in so bred at dare rose lose good. Feel and make two real miss use easy. Celebrated delightful an especially increasing instrument am. Indulgence contrasted sufficient to unpleasant in in insensible favourable. Latter remark hunted enough vulgar say man. Sitting hearted on it without me."));
        list3.add(new Message("Inquietude simplicity terminated she compliment remarkably few her nay.", "The weeks are ham asked jokes. Neglected perceived shy nay concluded. Not mile draw plan snug next all. Houses latter an valley be indeed wished merely in my. Money doubt oh drawn every or an china. Visited out friends for expense message set eat."));
        list3.add(new Message("Resolution possession discovered surrounded advantages has but few add.", "Yet walls times spoil put. Be it reserved contempt rendered smallest. Studied to passage it mention calling believe an. Get ten horrible remember pleasure two vicinity. Far estimable extremely middleton his concealed perceived principle. Any nay pleasure entrance prepared her."));
        list3.add(new Message("Shot what able cold new the see hold. Friendly as an betrayed formerly he. ", "Morning because as to society behaved moments. Put ladies design mrs sister was. Play on hill felt john no gate. Am passed figure to marked in."));

        this.messages.put(1, list);
        this.messages.put(2, list2);
        this.messages.put(3, list3);
    }

    public void setServerMessages(Set<Integer> selectedServers) {
        selectedServerMessages.clear();
        for (int id : selectedServers) {
            if (this.messages.containsKey(id)) {
                selectedServerMessages.addAll(this.messages.get(id));
            }
        }
    }

    public int getMessageCount() {
        return this.selectedServerMessages.size();
    }

    @Override
    public Iterator<Message> iterator() {
        return new MessagesIterator(this.selectedServerMessages);
    }

    static class MessagesIterator implements Iterator<Message> {
        private Iterator<Message> iterator;

        public MessagesIterator(List<Message> m) {
            this.iterator = m.iterator();
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public Message next() {
            try {
                Thread.sleep(500);
                return this.iterator.next();
            } catch (InterruptedException e) {
                return null;
            }
        }
    }

}
