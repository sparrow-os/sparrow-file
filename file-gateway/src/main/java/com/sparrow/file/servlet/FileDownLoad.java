package com.sparrow.file.servlet;

import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.api.AttachService;
import com.sparrow.file.dto.AttachDTO;
import com.sparrow.protocol.LoginUser;
import com.sparrow.support.Authenticator;
import com.sparrow.support.AuthenticatorConfigReader;
import com.sparrow.support.Authorizer;
import com.sparrow.support.web.CookieUtility;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
                Authenticator.class);

        Authorizer authorizer = ApplicationContext.getContainer().getBean(Authorizer.class);
        boolean accessible = false;
        try {
            AuthenticatorConfigReader configReader = ApplicationContext.getContainer().getBean(AuthenticatorConfigReader.class);
            String permission = this.cookieUtility.getPermission(request,configReader.getTokenKey());
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
            attach = attachService.getAttach(Long.getLong(fileId));
            //todo
            String physicalPath = null;
            String clientFileName = attach
                    .getClientFileName();
            String downLoadFileName = new String(clientFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.reset();
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
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
