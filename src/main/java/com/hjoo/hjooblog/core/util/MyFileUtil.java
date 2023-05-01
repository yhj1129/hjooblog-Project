package com.hjoo.hjooblog.core.util;

import com.hjoo.hjooblog.core.exception.ssr.Exception500;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

public class MyFileUtil {

    public static String write(String uploadFolder, MultipartFile file) {
        // 업로드된 파일을 upload 폴더에 저장
        // 이때 이름이 같으면 충돌이 발생할 수 있기 때문에 롤링 기법을 사용한다
        // 롤링 기법 (예 : 사진명 앞에 날짜를 붙인다 20230430220011_random한 값_person.png)
        // 날짜를 붙여도 동시에 업로드 되는 경우 충돌 발생 ->방법 1. 랜덤한 값을 만들어서 넣어준다
        // 방법 2. UUID 사용 : 충돌날 확률이 절대 없다
        UUID uuid = UUID.randomUUID();
        String originalFilename = file.getOriginalFilename();
        String uuidFilename = uuid + "_" + originalFilename;
        try {
            //파일 사이즈 줄이기
            Path filePath = Paths.get(uploadFolder + uuidFilename);
            Files.write(filePath, file.getBytes());
        } catch (Exception e) {
            throw new Exception500("파일 업로드 실패 : "+e.getMessage());
        }
        return uuidFilename;
    }
}
