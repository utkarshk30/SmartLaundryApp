package com.example.imad;


public class Orders {

    private String date,uid;
    private int upper,lower,small,orderid;
    private double price;
    boolean ready,paid,delivered;

    public Orders() {

    }

    public Orders(String date, int upper, int lower, int small, int orderid, double price, boolean ready, boolean paid, boolean delivered,String uid) {
        this.date = date;
        this.uid = uid;
        this.upper = upper;
        this.lower = lower;
        this.small = small;
        this.orderid = orderid;
        this.price = price;
        this.ready = ready;
        this.paid = paid;
        this.delivered = delivered;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

