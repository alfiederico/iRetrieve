/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.domain;

/**
 *
 * @author Alfie
 */
public class Message {

    private final long id;
    private final String content;
    private final int radius;

    public Message(long id, String content, int radius) {
        this.id = id;
        this.content = content;
        this.radius = radius;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getRadius() {
        return radius;
    }

}
