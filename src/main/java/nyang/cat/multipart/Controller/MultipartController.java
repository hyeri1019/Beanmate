//package nyang.cat.multipart.Controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.util.UUID;
//
//@RestController
//public class MultipartController {
//
//    /* 클라이언트가 파일을 업로드 */
//    @PostMapping("/files")
//    public ResponseEntity<String> uploadFiles(@RequestParam("file") MultipartFile file) {
//        System.out.println(" file "+file);
//
//        try {
//            /* 저장할 파일 이름 */
//            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//            /* 저장할 경로 */
//            String filePath = "C:/uploads/" + fileName;
//
//            /* 파일 저장 */
//            file.transferTo(new File(filePath));
//            return ResponseEntity.ok().body("파일 업로드 성공");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
//        }
//    }
//}