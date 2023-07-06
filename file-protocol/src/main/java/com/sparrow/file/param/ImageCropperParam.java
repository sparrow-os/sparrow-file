package com.sparrow.file.param;

import com.sparrow.protocol.Param;

public class ImageCropperParam implements Param {
    public ImageCropperParam() {
    }

    /**
     * 原图路径
     */
    private String imageUrl;
    /**
     * x the specified X coordinate
     */
    private Integer x;
    /**
     * y the specified Y coordinate
     */
    private Integer y;
    /**
     * width    the width of the <code>Rectangle</code>
     */
    private Integer width;
    /**
     * height   the height of the <code>Rectangle</code>
     */
    private Integer height;

    public ImageCropperParam(String imageUrl, Integer x, Integer y, Integer width, Integer height) {
        this.imageUrl = imageUrl;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "ImageCropperParam{" +
                ", imageUrl='" + imageUrl + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
