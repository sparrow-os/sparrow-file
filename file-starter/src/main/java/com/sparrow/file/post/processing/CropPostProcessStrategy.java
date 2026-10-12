/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.sparrow.file.post.processing;

import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.utils.ImageUtility;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.utility.FileUtility;
import jakarta.inject.Named;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Named
public class CropPostProcessStrategy implements UploadPostProcessing {
    @Override
    public void uploadPostProcessing(String physicalFullPath, AttachUploadParam imageCropperParam, FileConfig fileConfig) throws IOException {
        Rectangle rectangle = new Rectangle(imageCropperParam.getX(), imageCropperParam.getY(),
                imageCropperParam.getWidth(), imageCropperParam.getHeight());
        String smallPhysicalFilePath = physicalFullPath.replace(FileConstant.SIZE.ORIGIN, FileConstant.SIZE.SMALL);
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(smallPhysicalFilePath);
        File directory = new File(fileNameProperty.getDirectory());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        ImageUtility.saveSubImage(physicalFullPath, rectangle, smallPhysicalFilePath);
    }
}
