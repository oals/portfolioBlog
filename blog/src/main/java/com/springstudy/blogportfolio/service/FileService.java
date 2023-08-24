package com.springstudy.blogportfolio.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    public String uploadFile(String uploadPath,String originalFileName,byte[] fileDate) throws  Exception{

        UUID uuid = UUID.randomUUID();

        // .파일확장자 찾기
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String savedFileName = uuid.toString() + extension; // 난수에 확장자 붙이기

        String fileUploadFullUrl = uploadPath + "/"+savedFileName;

        // 결과 : 업로드할 경로 + / + uuid값 + .파일확장자

        
        //업로드 완료
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileDate);
        fos.close();
        

        return savedFileName;
    }





    public void deleteFile(List<String> UuidList,String FolderPath) throws Exception{

        //해당 폴더의 모든 아이디 읽어오기
        File dir = new File(FolderPath);

        String[] filenames = dir.list();



        for (int i = 0; i < filenames.length; i++) {
                                            //인덱스 문제  -> or 연산자 사용 ,
            if(!(filenames[i].contains(UuidList.get(0)) || filenames[i].contains(UuidList.get(1)) || filenames[i].contains(UuidList.get(2)))){ //포함되어 있지 않으면 삭제
                File deleteFile = new File( FolderPath+ "/" +filenames[i]);
                deleteFile.delete();
            }
        }
    }


    public void deleteFolder(String delpath){

        File delFolder = new File(delpath);
        String[] filenames = delFolder.list();



        if(filenames.length != 0) {
            for (int i = 0; i < filenames.length; i++) {
                //인덱스 문제  -> or 연산자 사용 ,
                File deleteFile = new File(delFolder + "/" + filenames[i]);
                log.info("삭제할폴더의 하위 파일 : " + deleteFile.toString());
                deleteFile.delete();
            }
        }

        delFolder.delete();

//        File reloadFolder = new File(reloadpath);
//        String[] foldernames = reloadFolder.list();

//        log.info("저장된폴더 길이 : " + foldernames.length );  //오류
//        //저장된 폴더의 번호 갱신
//        if(foldernames.length > 0){
//
//            for(int i =0; i< foldernames.length; i++) {
//                log.info("변경할 폴더 명 : " + foldernames[i]);
//                File reloadFolderName = new File(reloadFolder + "/" + foldernames[i]);
//                log.info("변경할 폴더 경로 : " + reloadFolderName.getPath());
//                File newName = new File(reloadFolder + "/" + (i + 1));
//                log.info("변경 할 값 : " + newName.getName());
//               boolean chk = reloadFolderName.renameTo(newName);
//                log.info("변경 테스트 결과 : " + chk);
//            }
//
//        }



    }




}
