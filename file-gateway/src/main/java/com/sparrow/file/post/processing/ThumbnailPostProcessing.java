package com.sparrow.file.post.processing;

import com.sparrow.constant.File;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.utility.ImageUtility;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class ThumbnailPostProcessing implements UploadPostProcessing {
    private static Logger logger = LoggerFactory.getLogger(ThumbnailPostProcessing.class);

    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {
        String bigPath = physicalFullPath.replace(
            File.SIZE.ORIGIN, File.SIZE.BIG);
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
