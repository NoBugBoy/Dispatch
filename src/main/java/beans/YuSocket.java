package beans;

import java.io.Serializable;
import java.util.Arrays;

public class YuSocket implements Serializable{
    private int head=2020;
    private int contentLength;
    private byte[] content;

    @Override
    public String toString() {
        return "YuSocket{" +
                "head=" + head +
                ", contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        byte [] temp = this.content;
        return temp;
    }

    public void setContent(byte[] content) {
        byte[] temp = content;
        this.content = temp.clone();
    }
}
