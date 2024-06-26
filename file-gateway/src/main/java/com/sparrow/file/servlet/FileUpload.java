package com.sparrow.file.servlet;

import com.sparrow.constant.Config;
import com.sparrow.constant.ConfigKeyLanguage;
import com.sparrow.constant.User;
import com.sparrow.container.Container;
import com.sparrow.core.cache.ExpirableCache;
import com.sparrow.core.cache.SoftExpirableCache;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.enums.LoginType;
import com.sparrow.file.UploadingProgress;
import com.sparrow.file.api.AttachService;
import com.sparrow.file.assemble.FileConfigAssemble;
import com.sparrow.file.bo.FileConfig;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.file.param.AttachUploadParam;
import com.sparrow.file.post.processing.UploadPostProcessStrategy;
import com.sparrow.file.support.constant.FileConstant;
import com.sparrow.file.support.enums.FileError;
import com.sparrow.file.support.utils.AttachUrlUtility;
import com.sparrow.file.support.utils.path.url.PathUrlConverter;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class FileUpload extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(FileUpload.class);
    private ExpirableCache<String, UploadingProgress> expirableStatusCache = new SoftExpirableCache<>("uploading-progress", 10);
    private FileConfigAssemble configAssemble;
    private AttachService attachService;

    private PathUrlConverter pathUrlConverter;

    public FileUpload() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        Container container = ApplicationContext.getContainer();
        this.configAssemble = container.getBean("fileConfigAssemble");
        this.attachService = container.getBean("attachService");
        this.pathUrlConverter = container.getBean("pathUrlConverter");
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //设置服务器端编码
        //https://articles.zsxq.com/id_sfemnfjowaaw.html
        response.setCharacterEncoding("UTF-8");
        String fileSerialNumber = request.getParameter("file-serial-number");
        if (fileSerialNumber != null) {
            response.setHeader("Content-Type", Constant.CONTENT_TYPE_JAVASCRIPT);
            writeState(response.getWriter(), fileSerialNumber);
            return;
        }

        String pathKey = request.getParameter("path-key");
        if (pathKey == null) {
            return;
        }

        PrintWriter out = response.getWriter();
        String editor = request.getParameter("editor");
        LoginUser loginToken = ThreadContext.getLoginToken();
        if (loginToken == null || LoginUser.VISITOR_ID.equals(loginToken.getUserId())) {
            initVisitorUploadHtml(out, pathKey, editor);
            return;
        }
        initUploadHtml(out, pathKey, new UploadingProgress(), editor);
    }

    private void initVisitorUploadHtml(PrintWriter out, String pathKey, String editor) {
        String dialogLoginUrl = ConfigUtility.getValue(Config.LOGIN_TYPE_KEY
                .get(LoginType.DIALOG_LOGIN))
                + "?callback-ns=file&parameter=" + pathKey + "&editor=" + editor;

        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"utf-8\">");
        out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">");
        out.println("</head>");
        out.println("<body>");

        out.write("<script type=\"text/javascript\">document.domain='" + ConfigUtility.getValue(Config.ROOT_DOMAIN) + "'</script>");
        out.write("<a href=\"javascript:parent.$.window({showHead:false,url:'"
                + dialogLoginUrl + "&shortRegister=false'});\">" + ConfigUtility.getLanguageValue(ConfigKeyLanguage.CONTROL_TEXT_LOGIN) + "</a>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginToken = ThreadContext.getLoginToken();
        if (LoginUser.VISITOR_ID.equals(loginToken.getUserId())) {
            logger.error("current user is not login");
            return;
        }
        logger.info("---------file uploading-----------");
        byte[] buffer = new byte[1024];
        int readLength;
        int fileNameIndex;
        String readString;
        String fileName = Symbol.EMPTY;
        String fileEndFlag = Symbol.EMPTY;
        String physicalFullPath = Symbol.EMPTY;

        UploadingProgress status;
        AttachUploadParam attachUploadParam;
        try {
            attachUploadParam = this.assembleFileInfo(request);
            status = this.expirableStatusCache.get(attachUploadParam.getSerialNumber());
        } catch (IOException e) {
            logger.error("fetch status error", e);
            return;
        }

        String pathKey = attachUploadParam.getPathKey();
        String editor = attachUploadParam.getEditor();
        FileConfig fileConfig = this.configAssemble.assemble(pathKey);
        int fileConfigLength = fileConfig
                .getLength();
        if (request.getContentLength() > fileConfigLength) {
            status.setError(FileError.UPLOAD_OUT_OF_SIZE.getMessage());
            this.uploadEnd(status, pathKey, editor, response);
            return;
        }

        attachUploadParam.setCreateUserId(loginToken.getUserId());
        FileOutputStream fileOutputStream = null;
        ServletInputStream servletInputStream = null;
        try {
            servletInputStream = request.getInputStream();
            while ((readLength = servletInputStream.readLine(buffer,
                    0, buffer.length)) != -1) {
                status.setReadLength(status.getReadLength()
                        + readLength);
                readString = new String(buffer, 0, readLength,
                        StandardCharsets.UTF_8);

                if (StringUtility.isNullOrEmpty(fileEndFlag)) {
                    // -----------------------------7da3992a803bc--为文件头结尾标记
                    fileEndFlag = readString.substring(0,
                            readLength - 2)
                            + "--"
                            + readString.substring(readLength - 2);
                    continue;
                }
                fileNameIndex = readString
                        .indexOf("\"; filename=\"");
                if (fileNameIndex != -1
                        && StringUtility.isNullOrEmpty(fileName)) {
                    // Content-Disposition: form-data;
                    // name="file_upload"
                    // filename="上传的文件名.扩展名"
                    fileName = readString.substring(
                            fileNameIndex + 13,
                            readString.length() - 3);
                    if (StringUtility.isNullOrEmpty(fileName)) {
                        status.setError(FileError.UPLOAD_FILE_NAME_NULL.getMessage());
                        this.uploadEnd(status, pathKey, editor, response);
                        return;
                    }
                    attachUploadParam.setClientFileName(fileName
                            .substring(fileName.lastIndexOf('\\') + 1));
                    status.setClientFileName(attachUploadParam.getClientFileName());

                    String rightFileType = ConfigUtility
                            .getValue(FileConstant.RIGHT_TYPE
                                    + "_" + pathKey.toLowerCase());
                    if (rightFileType != null
                            && !rightFileType.toLowerCase().contains(FileUtility.getInstance()
                            .getFileNameProperty(fileName).getExtension()
                            .toLowerCase())) {
                        status.setContentType(FileConstant.ERROR_TYPE + "|"
                                + rightFileType);
                        status.setError(FileError.UPLOAD_FILE_TYPE_ERROR.getMessage());
                        this.uploadEnd(status, pathKey, editor, response);
                        return;
                    }
                    continue;
                }

                if (readString.contains("Content-Type")
                        && StringUtility.isNullOrEmpty(status.getContentType())) {
                    // Content-Type: application/msword
                    readLength = servletInputStream.readLine(buffer,
                            0, buffer.length);
                    status.setContentType(readString.split(":")[1]
                            .trim());
                    status.setReadLength(status
                            .getReadLength() + readLength);

                    attachUploadParam.setContentType(status.getContentType());


                    UploadingProgress uploadingProgress = this.expirableStatusCache.get(attachUploadParam.getSerialNumber());

                    if (fileConfig.getPath().equals("$ShuffleFileId")) {
                        AttachDTO attach = this.attachService.generateFileId(attachUploadParam);
                        physicalFullPath = AttachUrlUtility.getShuffleImagePhysicalPath(attach, FileConstant.SIZE.ORIGIN);
                    } else {
                        physicalFullPath = AttachUrlUtility.getPhysicalFilePath(attachUploadParam, fileConfig, loginToken);
                    }
                    uploadingProgress.setFileUrl(this.pathUrlConverter.getWebUrlByPhysicalFileName(physicalFullPath));
                    FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(physicalFullPath);
                    File file = new File(fileNameProperty.getDirectory());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(
                            physicalFullPath);
                    continue;
                }
                // 如果是文件的结尾
                if (fileEndFlag.equals(readString)) {
                    continue;
                }
                fileOutputStream.write(buffer, 0, readLength);
            }
        } catch (IOException e) {
            logger.error("make thumbnail", e);
            status.setError(FileError.UPLOAD_SERVICE_ERROR.getMessage());
            this.uploadEnd(status, pathKey, editor, response);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    servletInputStream.close();
                } catch (IOException e) {
                    status.setError(FileError.UPLOAD_SERVICE_ERROR.getMessage());
                    this.uploadEnd(status, pathKey, editor, response);
                }
            }
        }
        if (!FileUtility.getInstance().isImageByContentType(attachUploadParam.getContentType()) || !fileConfig.isDeal()) {
            this.uploadEnd(status, pathKey, editor, response);
            return;
        }
        UploadPostProcessStrategy uploadPostProcessStrategy = ApplicationContext.getContainer().getBean("uploadPostProcessStrategy");
        uploadPostProcessStrategy.uploadPostProcessing(physicalFullPath, attachUploadParam, fileConfig);
        this.uploadEnd(status, pathKey, editor, response);
    }

    private void uploadEnd(UploadingProgress status, String pathKey, String editor, HttpServletResponse response) {
        logger.info("upload end, reading length {}", status.getReadLength());
        status.setReadLength(status
                .getContentLength());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            PrintWriter out = response.getWriter();
            initUploadHtml(out, pathKey, status, editor);
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error("upload end io error", e);
        }
    }

    private void writeState(PrintWriter out, String serialNumber) {
        UploadingProgress status = this.expirableStatusCache.get(serialNumber);
        if (status == null) {
            out.print(String.format("parent.$.file.progressCallback(%s)", "{status:'loading'}"));
            return;
        }
        if (status.getContentLength() == null || status.getReadLength() == null) {
            return;
        }
        this.configAssemble.assemble(status);
        this.expirableStatusCache.continueKey(serialNumber);
        out.print(String.format("$.file.progressCallback(%s)", JsonFactory.getProvider().toString(status)));
    }

    private void initUploadHtml(PrintWriter out, String pathKey, UploadingProgress progress, String editor) {
        FileConfig fileConfig = this.configAssemble.assemble(pathKey);
        Json json = JsonFactory.getProvider();
        // 这里的newUUID必须 但是文件上传成功后一定要重新加载控件
        String serialNumber = StringUtility.newUuid();
        //editor.pathkey.serialNumber
        String fileInfo = editor + "." + pathKey + "." + serialNumber;
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<html>");
        out.println("<head><title>文件上传 -"
                + ConfigUtility.getLanguageValue(ConfigKeyLanguage.WEBSITE_NAME,
                ConfigUtility.getValue(Config.LANGUAGE)) + "</title>");
        out.println("<script type=\"text/javascript\">");
        //跨域必须两端都加
        out.println(String.format("window.onload=function(){document.domain=window.location.host.substr(window.location.host.indexOf('.')+1);if(!parent.$){return;}if(parent.$.file.uploadCallBack){parent.$.file.uploadCallBack(%s,%s,%s);}}",
                json.toString(progress),
                ("null".equals(editor) || editor == null) ? "null" : "parent." + editor,
                json.toString(fileConfig.getSize())));
        out.println("</script>");
        out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
        out.println("</head>");
        out.println("<body style='margin:0px;padding:0px;height:25px;'>");
        out.println("<form action=\"file-upload\" method=\"post\" enctype=\"multipart/form-data\" target=\"_self\">");
        out.println("<input type=\"hidden\" id=\"fileInfo\" name=\"fileInfo\" value=\""
                + fileInfo + "\" />");
        out.println("<input style=\"cursor:pointer;width:200px;\" type=\"file\" id=\"file_upload\" name=\"file_upload\"");
        out.println(String.format(" onchange=\"parent.$.file.validateUploadFile(this,'%s',%s);\" />", pathKey, ("null".equals(editor) ? "" : "parent." + editor)));
        out.println("</form>");
        out.println("  </body>");
        out.println("</html>");
    }

    /**
     * 获取form表单中的name=fileInfo内容
     * <p/>
     * 并初始化 UploadingProgress 对象
     * <p/>
     * 设置配置信息 设置已读大小
     *
     * @param request
     * @return
     * @throws IOException
     */
    public AttachUploadParam assembleFileInfo(HttpServletRequest request) throws IOException {
        UploadingProgress status = new UploadingProgress();
        ServletInputStream servletInputStream = request.getInputStream();
        byte[] buffer = new byte[1024];
        int readLength = 0;
        for (int i = 0; i < 3; i++) {
            servletInputStream
                    .readLine(buffer, 0, buffer.length);
        }
        readLength = servletInputStream.readLine(buffer, 0, buffer.length);
        String fileInfo = new String(buffer, 0, readLength - 2, StandardCharsets.UTF_8);
        String[] fileInfoArray = fileInfo.split("\\.");
        String editor = fileInfoArray[0];
        String pathKey = fileInfoArray[1];
        String fileSerialNumber = fileInfoArray[2];
        status.setReadLength((long) readLength);
        status.setContentLength((long) request.getContentLength());
        this.expirableStatusCache.put(fileSerialNumber, status);
        AttachUploadParam attachUploadParam = new AttachUploadParam();
        attachUploadParam.setSerialNumber(fileSerialNumber);
        attachUploadParam.setPathKey(pathKey);
        attachUploadParam.setEditor(editor);
        attachUploadParam.setContentType(status.getContentType());
        attachUploadParam.setContentLength(status.getContentLength());
        return attachUploadParam;
    }
}
