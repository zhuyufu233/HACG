package com.shicheeng.hacg.util;

import org.jsoup.select.Elements;

import java.io.Serializable;

public class CommentSerializable implements Serializable {

    private Elements elements;

    public Elements getElements() {
        return elements;
    }

    public void setElements(Elements elements) {
        this.elements = elements;
    }
}
