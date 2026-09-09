package com.sparrow.file.config;

import com.sparrow.file.support.enums.FileError;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.spring.mvc.ControllerReturnAdvice;
import com.sparrow.utility.FileUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局返回结构 / 统一异常兜底。
 *
 * <p>注意：此处<strong>不能再用 basePackages 限定</strong>。容器 multipart 超限时，异常在
 * DispatcherServlet#checkMultipart()（controller handler 匹配之前、handler 为 null）抛出
 * {@link MaxUploadSizeExceededException}，带 basePackages 的 advice 无法命中
 * （HandlerTypePredicate.test(null)=false）。故必须为全局无选择器 advice。</p>
 *
 * <p>约定：无论"容器 multipart 超限"还是"业务按 pathKey 校验超限"，统一抛 Spring 自带的
 * {@link MaxUploadSizeExceededException}（业务层构造时把 <code>fileConfig.getLength()</code> 作为
 * maxUploadSize 传入），由下方唯一的 handler 兜底：返回 <code>Result</code>
 * （code=FileError.UPLOAD_OUT_OF_SIZE=12202），data 携带<strong>可读的限制大小</strong>。
 * 本类<strong>不覆写</strong>父类对 {@link BusinessException} 的处理，其它业务异常保持 sparrow
 * 原始统一逻辑，不被文件大小逻辑污染。</p>
 */
@Slf4j
@RestControllerAdvice
public class FileControllerAdvice extends ControllerReturnAdvice {

    /**
     * 文件大小超过上限统一兜底。两个来源都汇到这里：
     * <ul>
     *   <li>容器 multipart 超限（handler=null 阶段抛出，maxUploadSize=spring.servlet.multipart 上限）；</li>
     *   <li>业务按 pathKey 校验超限（FileController 内构造并抛出，maxUploadSize=该 pathKey 允许长度）。</li>
     * </ul>
     * 用 e.getMaxUploadSize() 换算成可读文本放入 Result.data，供前端按 code 识别后展示。
     */
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Result handleMaxUploadSizeExceeded(HttpServletRequest request, MaxUploadSizeExceededException e) {
        log.error("upload file too large, server max size = {}", e.getMaxUploadSize(), e);
        Result result = Result.fail(new BusinessException(FileError.UPLOAD_OUT_OF_SIZE));
        if (e.getMaxUploadSize() >= 0) {
            result.setData(FileUtility.getInstance().getHumanReadableFileLength(e.getMaxUploadSize()));
        }
        return result;
    }
}
