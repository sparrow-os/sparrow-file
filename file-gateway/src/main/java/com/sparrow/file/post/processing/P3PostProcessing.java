package com.sparrow.file.post.processing;

import com.sparrow.constant.Config;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.utils.ImageUtility;

import javax.inject.Named;

import com.sparrow.support.web.WebConfigReader;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class P3PostProcessing implements UploadPostProcessing {
    private static Logger logger = LoggerFactory.getLogger(P3PostProcessing.class);

    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUploadBo, FileConfig fileConfig) {
        String bigPath = physicalFullPath.replace(
                FileConstant.SIZE.ORIGIN, FileConstant.SIZE.BIG);

        WebConfigReader webConfigReader= ApplicationContext.getContainer().getBean(WebConfigReader.class);

        String logoWaterFile = webConfigReader.getPhysicalResource()
                +webConfigReader.getWaterMark();
        String middlePath = physicalFullPath.replace(
                FileConstant.SIZE.ORIGIN, FileConstant.SIZE.MIDDLE);
        String smallPath = physicalFullPath.replace(
                FileConstant.SIZE.ORIGIN, FileConstant.SIZE.SMALL);
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
