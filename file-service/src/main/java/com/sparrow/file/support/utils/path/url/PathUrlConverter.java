package com.sparrow.file.support.utils.path.url;

import com.sparrow.constant.Config;
import com.sparrow.file.support.constant.PathConfig;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import java.util.HashMap;
import java.util.Map;

public class PathUrlConverter {
    static class PhysicalPathWebUrlPair {
        PhysicalPathWebUrlPair(String physicalPath, String webUrl) {
            this.physicalPath = physicalPath;
            this.webUrl = webUrl;
        }

        private String physicalPath;
        private String webUrl;

        public String getPhysicalPath() {
            return physicalPath;
        }

        public String getWebUrl() {
            return webUrl;
        }
    }

    private Map<String, String> physicalPathWebUrlMap = new HashMap<>();
    private Map<String, String> webUrlPhysicalPathMap = new HashMap<>();

    /**
     * resource=${resource}
     * upload=${upload}
     * root_domain=sparrowzoo.com
     * root_path=http://upload.sparrowzoo.com
     * physical_resource=${physical_resource}
     * physical_upload=${physical_upload}
     */
    public PathUrlConverter() {
        String resource = ConfigUtility.getValue(Config.RESOURCE);
        String physicalResource = ConfigUtility.getValue(Config.RESOURCE_PHYSICAL_PATH);

        String upload = ConfigUtility.getValue(Config.UPLOAD);
        String physicalUploadPath = ConfigUtility.getValue(Config.UPLOAD_PHYSICAL_PATH);

        int shuffleImageNum = ConfigUtility.getIntegerValue(PathConfig.IMG_SHUFFLER_NUM);
        this.physicalPathWebUrlMap.put(physicalResource, resource);
        this.physicalPathWebUrlMap.put(physicalUploadPath, upload);

        this.webUrlPhysicalPathMap.put(resource, physicalResource);
        this.webUrlPhysicalPathMap.put(upload, physicalUploadPath);
        String shuffleImageUrlPrefix = ConfigUtility.getValue(PathConfig.IMG_URL);
        if (StringUtility.isNullOrEmpty(shuffleImageUrlPrefix)) {
            return;
        }
        shuffleImageUrlPrefix = shuffleImageUrlPrefix.replace("$upload", upload);
        for (int i = 0; i < shuffleImageNum; i++) {
            String shuffleUrl = String.format(shuffleImageUrlPrefix, i);
            String shuffleUrlPhysicalPath = ConfigUtility.getValue(PathConfig.IMG_SHUFFLER_DIR + i);
            this.webUrlPhysicalPathMap.put(shuffleUrl, shuffleUrlPhysicalPath);
            this.physicalPathWebUrlMap.put(shuffleUrlPhysicalPath, shuffleUrlPhysicalPath);
        }
    }

    private PhysicalPathWebUrlPair getPhysicalPathByWebUrl(String webUrl) {
        for (String configWebUrl : this.webUrlPhysicalPathMap.keySet()) {
            if (webUrl.startsWith(configWebUrl)) {
                String matchedPhysicalPath = this.webUrlPhysicalPathMap.get(configWebUrl);
                return new PhysicalPathWebUrlPair(matchedPhysicalPath, configWebUrl);
            }
        }
        return null;
    }

    private PhysicalPathWebUrlPair getWebUrlByPhysicalPath(String physicalPath) {
        for (String configPhysicalPath : this.physicalPathWebUrlMap.keySet()) {
            if (physicalPath.startsWith(configPhysicalPath)) {
                String matchedWebUrl = this.physicalPathWebUrlMap.get(configPhysicalPath);
                return new PhysicalPathWebUrlPair(configPhysicalPath, matchedWebUrl);
            }
        }
        return null;
    }

    public String getPhysicalFileByWebUrl(String webUrl) {
        PhysicalPathWebUrlPair physicalPathWebUrlPair = this.getPhysicalPathByWebUrl(webUrl);
        return webUrl.replace(physicalPathWebUrlPair.webUrl, physicalPathWebUrlPair.physicalPath);
    }

    public String getWebUrlByPhysicalFileName(String physicalFileName) {
        PhysicalPathWebUrlPair physicalPathWebUrlPair = this.getWebUrlByPhysicalPath(physicalFileName);
        return physicalFileName.replace(physicalPathWebUrlPair.physicalPath, physicalPathWebUrlPair.webUrl);
    }
}
