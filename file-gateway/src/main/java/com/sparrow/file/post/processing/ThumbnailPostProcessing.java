package com.sparrow.file.post.processing;

import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.utils.ImageUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named
public class ThumbnailPostProcessing implements UploadPostProcessing {
    private static Logger logger = LoggerFactory.getLogger(ThumbnailPostProcessing.class);

    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {
        String bigPath = physicalFullPath.replace(
                FileConstant.SIZE.ORIGIN, FileConstant.SIZE.BIG);
        try {
            // 缩放并保存至大图 使其在前台能够正常显示
            ImageUtility.makeThumbnail(physicalFullPath,
                bigPath, fileConfig.getSize()
                    .getWidth(),
                fileConfig.getSize()
                    .getHeight(), null, true);
        } catch (Exception e) {
            logger.error("make thumbnail", e);
        }
    }
}
