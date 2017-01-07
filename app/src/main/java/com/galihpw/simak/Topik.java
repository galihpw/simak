package com.galihpw.simak;

/**
 * Created by GalihPW on 07/11/2016.
 */

public class Topik {
    private String judulForum;
    private String isiForum;

    Topik(String judulForum, String isiForum){
        this.judulForum = judulForum;
        this.isiForum = isiForum;
    }

    String getJudulForum() {
        return judulForum;
    }

    public void setJudulForum(String judulForum) {
        this.judulForum = judulForum;
    }

    String getIsiForum() {
        return isiForum;
    }

    public void setIsiForum(String isiForum) {
        this.isiForum = isiForum;
    }
}
