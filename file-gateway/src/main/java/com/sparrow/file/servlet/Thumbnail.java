package com.sparrow.file.servlet;

import com.sparrow.constant.Config;
import com.sparrow.constant.Regex;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.ImageUtility;
import com.sparrow.utility.RegexUtility;
import com.sparrow.utility.StringUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http://thumbnail.sparrowzoo.com/thumbnail?domain=img1_sparrowzoo_net&url=/ios_big_thread/6/361/6361.png
 */
public class Thumbnail extends HttpServlet{
    private Map<String, String> pathConfig = new ConcurrentHashMap<String, String>();
    private Map<String, Map<String, String>> thumbnailSize = new ConcurrentHashMap<String, Map<String, String>>();

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String rootPath = config.getInitParameter("root_path");
        String thumbnailSizePath = config.getInitParameter("thumbnail_size_path");
        if (StringUtility.isNullOrEmpty(rootPath)) {
            rootPath = "/root_path.properties";
        }
        if (StringUtility.isNullOrEmpty(thumbnailSizePath)) {
            thumbnailSizePath = "/thumbnail";
        }
        pathConfig = ConfigUtility.loadFromClassesPath(rootPath);
        try {
            File file = new File(Thumbnail.class.getResource(thumbnailSizePath).toURI());
            if (!file.isDirectory()) {
                return;
            }
            File[] fileLists = file.listFiles();
            if (fileLists == null) {
                return;
            }
            for (File configFile : fileLists) {
                try {
                    Map<String, String> configMap = ConfigUtility.load(new FileInputStream(configFile));
                    thumbnailSize.put(FileUtility.getInstance().getFileNameProperty(configFile.getName()).getName(), configMap);
                } catch (FileNotFoundException ignore) {
                }
            }
        } catch (URISyntaxException ignore) {
        }
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String domain = req.getParameter("domain");
        String url = req.getParameter("url");
        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        List<String> config = RegexUtility.groups(url, Regex.IMG_URL_RULE);
        String rootPath = pathConfig.get(domain);
        String waterMark = ConfigUtility
                .getValue(Config.RESOURCE_PHYSICAL_PATH)
                + ConfigUtility.getValue(Config.WATER_MARK);

        Map<String, String> thumbnailSizeConfig = thumbnailSize.get(domain);
        if (thumbnailSizeConfig != null) {
            String sizeKey = config.get(0);
            int urlStartIndex=sizeKey.length()+1;
            String sizeKeyArray[] = sizeKey.split("_");
            if (sizeKeyArray.length > 1) {
                if(sizeKeyArray.length==2){
                    sizeKey+="_"+"thread";
                }
                String size = thumbnailSizeConfig.get(sizeKey);
                if (!StringUtility.isNullOrEmpty(size)) {
                    String[] configArray = size.split("\\*");
                    String currentWatermark = null;
                    if (configArray.length == 3 && configArray[1].equalsIgnoreCase("true")) {
                        currentWatermark = waterMark;
                    }
                    int width = Integer.valueOf(configArray[0]);
                    int height = Integer.valueOf(configArray[1]);
                    String filePath = url.substring(urlStartIndex);
                    String sourceFile = rootPath + filePath;
                    File f = new File(sourceFile);
                    if (!f.exists()) {
                        sourceFile = rootPath + com.sparrow.constant.File.SIZE.ORIGIN + filePath;
                    }
                    ImageUtility.makeThumbnail(sourceFile, resp.getOutputStream(), width, height, currentWatermark, true);
                    return;
                }
            }
        }
        String sourceFile = rootPath + url;
        FileUtility.getInstance().copy(new FileInputStream(sourceFile), resp.getOutputStream());
    }
}
