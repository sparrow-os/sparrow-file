package com.sparrow.file.servlet;

import com.sparrow.constant.File.SIZE;
import com.sparrow.constant.SysObjectName;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.api.AttachService;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.Authenticator;
import com.sparrow.support.Authorizer;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownLoad extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public FileDownLoad() {
        super();
    }

    private CookieUtility cookieUtility;

    private AttachService attachService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        Container container = ApplicationContext.getContainer();
        this.attachService = container.getBean("attachService");
        this.cookieUtility = container.getBean(config.getInitParameter("cookieUtility"));
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        if (StringUtility.isNullOrEmpty(request.getParameter("fileId"))) {
            response.getWriter().write("文件未找到,可能已被删除");
            return;
        }

        String fileId = request.getParameter("fileId");

        Authenticator authenticator = ApplicationContext.getContainer().getBean(
            SysObjectName.AUTHENTICATOR_SERVICE);

        Authorizer authorizer=ApplicationContext.getContainer().getBean(SysObjectName.AUTHORIZER_SERVICE);
        boolean accessible = false;
        try {
            String permission = this.cookieUtility.getPermission(request);
            String deviceId = ServletUtility.getInstance().getDeviceId(request);
            LoginUser loginToken = authenticator.authenticate(permission, deviceId);
            String actionKey = ServletUtility.getInstance().getActionKey(request);
            accessible = authorizer.isPermitted(loginToken, actionKey);
        } catch (Exception e2) {
            response.getWriter().write("对不起！您无下载权限。");
            return;
        }
        if (!accessible) {
            response.getWriter().write("对不起！您无下载权限。");
            return;
        }

        AttachDTO attach;
        FileInputStream inStream = null;
        try {
            attach = attachService.getAttach(fileId);
            String physicalPath = FileUtility.getInstance()
                .getShufflePath(
                    fileId,
                    FileUtility.getInstance().getFileNameProperty(
                        attach.getClientFileName()).getExtension(), false,
                    SIZE.ATTACH);

            String downLoadFileName = new String(attach
                .getClientFileName().getBytes(Constant.CHARSET_UTF_8), Constant.CHARSET_ISO_8859_1);
            response.reset();
            response.setCharacterEncoding(Constant.CHARSET_UTF_8);
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition",
                "attachment;filename=\"" + downLoadFileName + "\"");
            File file = new File(physicalPath);
            if (file.exists() && file.isFile()) {
                inStream = new FileInputStream(file);
                FileUtility.getInstance().copy(inStream, response.getOutputStream());
            }
            attachService.addDownLoadTimes(fileId);
        } catch (Exception e) {
            response.getWriter().write("文件未找到,可能已被删除");
        }
    }
}
