package top.r2ys.meshx.admin.controller;

import cn.hutool.core.io.FileUtil;
import io.minio.ObjectStat;
import org.apache.commons.io.IOUtils;
import top.r2ys.meshx.admin.vo.R;
import top.r2ys.meshx.common.minio.service.MinioTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author r2ys
 * @date 2018-12-30
 * <p>
 * 文件管理
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {
	private final MinioTemplate minioTemplate;

	/**
	 * 上传文件
	 * 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
	 *
	 * @param file 资源
	 * @return R(bucketName, filename)
	 */
	@PostMapping("/upload/{bucketName}")
	public R upload(@PathVariable("bucketName") String bucketName, @RequestParam("file") MultipartFile file) {
		String fileName = UUID.randomUUID().toString().replace("-", "") + "." + FileUtil.extName(file.getOriginalFilename());
		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put("bucketName", bucketName);
		resultMap.put("fileName", fileName);

		try {
			minioTemplate.putObject(bucketName, fileName, file.getInputStream());
		} catch (Exception e) {
			log.error("上传失败", e);
			return R.builder().code("100001")
					.msg(e.getLocalizedMessage()).build();
		}
		return R.builder().data(resultMap).build();
	}

	/**
	 * 获取文件
	 *
	 * @param fileName 文件空间/名称
	 * @param response
	 * @return
	 */
	@GetMapping("/{bucketName}/{fileName}")
	public void file(@PathVariable("bucketName") String bucketName,
                     @PathVariable("fileName") String fileName,
                     HttpServletResponse response) {
		try (InputStream inputStream = minioTemplate.getObject(bucketName, fileName)) {
			response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.copy(inputStream, response.getOutputStream());
		} catch (Exception e) {
			log.error("文件读取异常", e);
		}
	}

    @GetMapping("/info/{bucketName}/{fileName}")
    public R fileInfo(@PathVariable("bucketName") String bucketName,
                      @PathVariable("fileName") String fileName,
                      HttpServletResponse response) {
        ObjectStat objectInfo = null;
        try {
            objectInfo = minioTemplate.getObjectInfo(bucketName, fileName);
        } catch (Exception e) {
            log.error("获取失败", e);
            return R.builder().code("100002")
                    .msg(e.getLocalizedMessage()).build();
        }
        log.info(objectInfo.toString());
        return R.builder().data(objectInfo.toString()).build();
    }

    @GetMapping("/shorturl/{bucketName}/{fileName}")
    public R shorturl(@PathVariable("bucketName") String bucketName,
                      @PathVariable("fileName") String fileName,
                      HttpServletResponse response) {
        String result = null;
        try {
            result = minioTemplate.getObjectURL(bucketName, fileName, 6);
        } catch (Exception e) {
            log.error("获取失败", e);
            return R.builder().code("100003")
                    .msg(e.getLocalizedMessage()).build();
        }
        log.info(result);
        return R.builder().data(result).build();
    }

}
