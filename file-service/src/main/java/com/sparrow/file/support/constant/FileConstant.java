/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.file.support.constant;

public class FileConstant {
    /**
     * 正确文件类型的配置前缀字符.使用举例:right_file_type(_userhead) userhead为pathKey
     */
    public static final String RIGHT_TYPE = "right_file_type";
    /**
     * 错误文件类型的错误提示信息前缀. 使用举例:errorType|文件类型不正确
     */
    public static final String ERROR_TYPE = "error-type";
    /**
     * 是否允许文件下载的策略编码
     */
    public static final String DOWNLOAD_URL = "download_url";
  /**
     * 图片
     */
    public static final String TAG_IMAGE_FORMAT = "<img src=\"%1$s\"/>";

    public class SIZE {
        public static final String ATTACH = "";
        public static final String ORIGIN = "origin";
        public static final String BIG = "big";
        public static final String MIDDLE = "middle";
        public static final String SMALL = "small";
    }
}
