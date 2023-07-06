package com.sparrow.file.support.utils;

import com.sparrow.exception.Asserts;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.constant.PathConfig;
import com.sparrow.file.support.enums.FileError;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.FileUtility;

public class AttachUrlUtility {
    public static String getPhysicalFilePath(AttachUploadParam attachUploadParam,
                                          FileConfig fileConfig,
                                          LoginUser loginUser) {
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(
                attachUploadParam.getClientFileName());
        String fileExtension = fileNameProperty.getExtension();

        // 文件web路径
        String physicalFullPath = fileConfig.getPath();
        String currentTime = System.currentTimeMillis() + "";
        physicalFullPath = physicalFullPath
                .replace("$datetime", currentTime)
                .replace(
                        "$userId",
                        loginUser.getUserId().toString())
                .replace(
                        "$fileName",
                        fileNameProperty.getFullFileName())
                .replace("$serialNumber", attachUploadParam.getSerialNumber())
                .replace("$extension", fileExtension);
        return physicalFullPath;
    }




    /**
     * 通过一数字ID获取文件打散路径
     *
     * @param attach
     * @param size
     * @return
     */
    public static String getShuffleImagePhysicalPath(AttachDTO attach,
                                                     String size) throws BusinessException {

        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(
                attach.getClientFileName());
        String fileExtension = fileNameProperty.getExtension();

        long id = attach.getId();
        boolean isImage = FileUtility.getInstance().isImage(fileExtension);
        long remaining = id % Digit.TWELVE;
        long remaining1 = id % Digit.THOUSAND;
        long div = id / Digit.THOUSAND;
        long remaining2 = div % Digit.THOUSAND;
        String path;
        Asserts.isTrue(!isImage, FileError.UPLOAD_FILE_TYPE_ERROR);

        //img_shuffle_dir_0=file://ip1:port/sparrow/img0 参数在key中定义
        String imgShufflerDir = ConfigUtility.getValue(PathConfig.IMG_SHUFFLER_DIR + "_" + remaining);
        path = imgShufflerDir
                + "/%1$s/%2$s/%3$s/%4$s%5$s";
        return String.format(path, size, remaining2, remaining1,
                attach.getId(), fileExtension);
    }
}
