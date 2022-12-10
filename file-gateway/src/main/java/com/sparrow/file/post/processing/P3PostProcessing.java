package com.sparrow.file.post.processing;

import com.sparrow.constant.Config;
import com.sparrow.constant.File;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.utils.ImageUtility;
import com.sparrow.utility.ConfigUtility;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class P3PostProcessing implements UploadPostProcessing {
    private static Logger logger = LoggerFactory.getLogger(P3PostProcessing.class);

    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {
        String bigPath = physicalFullPath.replace(
            File.SIZE.ORIGIN, File.SIZE.BIG);

        String logoWaterFile = ConfigUtility
            .getValue(Config.RESOURCE_PHYSICAL_PATH)
            + ConfigUtility.getValue(Config.WATER_MARK);
        String middlePath = physicalFullPath.replace(
            File.SIZE.ORIGIN, File.SIZE.MIDDLE);
        String smallPath = physicalFullPath.replace(
            File.SIZE.ORIGIN, File.SIZE.SMALL);
        try {
            ImageUtility.makeThumbnail(physicalFullPath,
                bigPath, fileConfig
                    .getBigSize().getWidth(),
                fileConfig.getBigSize()
                    .getHeight(),
                logoWaterFile, false);

            ImageUtility
                .makeThumbnail(physicalFullPath,
                    middlePath, fileConfig
                        .getMiddleSize()
                        .getWidth(), fileConfig
                        .getMiddleSize()
                        .getHeight(), null,
                    false);

            ImageUtility
                .makeThumbnail(physicalFullPath,
                    smallPath, fileConfig
                        .getSmallSize()
                        .getWidth(), fileConfig
                        .getSmallSize()
                        .getHeight(), null,
                    true);
        } catch (Exception e) {
            logger.error("make thumbnail", e);
        }
    }
}
