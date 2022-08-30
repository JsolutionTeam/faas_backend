package zinsoft.web.common.controller;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.util.AppPropertyUtil;
import zinsoft.util.Constants;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.FileInfoDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.FileInfoService;
import zinsoft.web.common.view.DownloadView;
import zinsoft.web.exception.CodeMessageException;

@RestController
@RequestMapping("${api.prefix}")
public class FileController {

    @Resource
    FileInfoService fileInfoService;

    @PostMapping("/file")
    public Result insert(MultipartFile file) throws IllegalStateException, IOException {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        Long fileSeq = fileInfoService.insert(userInfo.getUserId(), file);

        return new Result(true, Result.OK, (Object) fileSeq);
    }

    @GetMapping("/file/{fileSeq}")
    public ModelAndView download(@PathVariable Long fileSeq) {
        if (fileSeq == null || fileSeq.longValue() <= 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        FileInfoDto dto = fileInfoService.get(fileSeq);

        if (dto == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        // TODO: 파일 권한 체크

        File file = new File(AppPropertyUtil.get(Constants.UPLOAD_DIR) + dto.getSavedNm());

        if (!file.exists()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        ModelAndView mv = new ModelAndView(new DownloadView());

        mv.addObject("file", file);
        mv.addObject("fileName", dto.getFileNm());
        mv.addObject("contentType", dto.getContentType());

        return mv;
    }

}
