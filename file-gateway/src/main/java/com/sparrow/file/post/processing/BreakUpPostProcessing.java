package com.sparrow.file.post.processing;

import com.sparrow.constant.Config;
import com.sparrow.constant.File;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.ImageUtility;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class BreakUpPostProcessing implements UploadPostProcessing {

    private static Logger logger= LoggerFactory.getLogger(BreakUpPostProcessing.class);
    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {
        String logoWaterFile = ConfigUtility
            .getValue(Config.RESOURCE_PHYSICAL_PATH)
            + ConfigUtility.getValue(Config.WATER_MARK);
        String breakup = physicalFullPath.replace(
            File.SIZE.ORIGIN, File.SIZE.BIG);
        try {
            ImageUtility.makeThumbnail(physicalFullPath,
                breakup, fileConfig
                    .getBigSize().getWidth(),
                fileConfig.getBigSize()
                    .getHeight(),
                logoWaterFile, false);
        } catch (Exception e) {
            logger.error("make thumbnail", e);
        }
    }
}
