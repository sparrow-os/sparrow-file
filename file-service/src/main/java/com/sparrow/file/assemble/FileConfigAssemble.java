package com.sparrow.file.assemble;

import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.enums.UploadDealType;
import com.sparrow.protocol.Size;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import javax.inject.Named;

/**
 * #pathKey=path(路径)|length(长度单位byte)|type|big_size(-1表示不限制)|middle_size|small_size
 */
@Named
public class FileConfigAssemble {
    public FileConfig assemble(String key) {
        FileConfig fileConfig = new FileConfig();
        fileConfig.setKey(key);
        String config = ConfigUtility.getValue("attach_" + key);
        if (StringUtility.isNullOrEmpty(config)) {
            throw new RuntimeException("key attach_" + key + " not found");
        }
        String[] configArray = config.split("\\|");
        fileConfig.setPath(configArray[0]);
        fileConfig.setLength(Integer.parseInt(configArray[1]));

        if (configArray.length <= 2) {
            return fileConfig;
        }
        fileConfig.setDeal(true);
        String uploadType = configArray[2];
        if ("3p".equals(uploadType)) {
            uploadType = "_3p";
        }
        fileConfig.setType(UploadDealType.valueOf(uploadType.toUpperCase()));
        switch (fileConfig.getType()) {
            case _3P:
                fileConfig.setSize(new Size(configArray[3]));
                fileConfig.setBigSize(new Size(configArray[3]));
                fileConfig.setMiddleSize(new Size(configArray[4]));
                fileConfig.setSmallSize(new Size(configArray[5]));
                break;
            case BREAK_UP:
            case THUMBNAIL:
                fileConfig.setSize(new Size(configArray[3]));
                fileConfig.setBigSize(new Size(configArray[3]));
            default:
        }
        return fileConfig;
    }
}
