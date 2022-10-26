package com.sparrow.file.bo;

import com.sparrow.file.enums.UploadDealType;
import com.sparrow.protocol.Size;

public class FileConfig {
    private String key;
    private String path;
    private int length;
    private UploadDealType type;
    private Size size;
    private Size bigSize;
    private Size middleSize;
    private Size smallSize;
    /**
     * 是否需要处理
     */
    private boolean isDeal = false;

    public boolean isDeal() {
        return isDeal;
    }

    public String getPath() {
        return path;
    }

    public int getLength() {
        return length;
    }

    public UploadDealType getType() {
        return type;
    }

    public Size getSize() {
        return size;
    }

    public Size getBigSize() {
        return bigSize;
    }

    public Size getMiddleSize() {
        return middleSize;
    }

    public Size getSmallSize() {
        return smallSize;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setType(UploadDealType type) {
        this.type = type;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setBigSize(Size bigSize) {
        this.bigSize = bigSize;
    }

    public void setMiddleSize(Size middleSize) {
        this.middleSize = middleSize;
    }

    public void setSmallSize(Size smallSize) {
        this.smallSize = smallSize;
    }

    public void setDeal(boolean deal) {
        isDeal = deal;
    }
}
