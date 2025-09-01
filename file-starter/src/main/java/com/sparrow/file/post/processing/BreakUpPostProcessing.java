package com.sparrow.file.post.processing;

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.utils.ImageUtility;
import com.sparrow.support.web.WebConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Named
public class BreakUpPostProcessing implements UploadPostProcessing {

    private static Logger logger= LoggerFactory.getLogger(BreakUpPostProcessing.class);
    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {

        WebConfigReader webConfigReader= ApplicationContext.getContainer().getBean(WebConfigReader.class);

        String logoWaterFile = webConfigReader.getPhysicalResource()
                +webConfigReader.getWaterMark();
        String breakup = physicalFullPath.replace(
            FileConstant.SIZE.ORIGIN, FileConstant.SIZE.BIG);
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
