package com.sparrow.file.post.processing;

import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.enums.UploadDealType;
import com.sparrow.file.param.AttachUploadParam;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class UploadPostProcessStrategy implements  UploadPostProcessing{
    private static Logger logger= LoggerFactory.getLogger(UploadPostProcessStrategy.class);
    @Inject
    private BreakUpPostProcessing breakUpPostProcessing;
    @Inject
    private P3PostProcessing p3PostProcessing;
    @Inject
    private ThumbnailPostProcessing thumbnailPostProcessing;
    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam attachUpload, FileConfig fileConfig) {
        UploadDealType uploadDealType = fileConfig.getType();
        logger.info("uploading post processing dealType {}",fileConfig.getType());
        switch (uploadDealType){
            case _3P:
                this.p3PostProcessing.uploadPostProcessing(physicalFullPath,attachUpload,fileConfig);
                break;
            case THUMBNAIL:
                this.thumbnailPostProcessing.uploadPostProcessing(physicalFullPath,attachUpload,fileConfig);
                break;
            case BREAK_UP:
                this.breakUpPostProcessing.uploadPostProcessing(physicalFullPath,attachUpload,fileConfig);
                break;
        }
    }
}
