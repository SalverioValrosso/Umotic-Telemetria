package org.altervista.umotic.AdapterEvents;

public class EventItem {
    private String title;
    private String data;
    private String time;

    public EventItem(String title, String data, String time) {
        this.title = title;
        this.data = data;
        this.time = time;
    }

    public String getTitle() {
        return this.title;
    }

    public String getData() {
        return this.data;
    }

    public String getTime() {
        return this.time;
    }
}