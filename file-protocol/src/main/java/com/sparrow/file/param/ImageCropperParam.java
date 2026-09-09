package com.sparrow.file.param;

import com.sparrow.protocol.Param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageCropperParam implements Param {

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
