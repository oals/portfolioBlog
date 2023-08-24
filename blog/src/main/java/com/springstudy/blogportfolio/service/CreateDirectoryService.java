package com.springstudy.blogportfolio.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Log4j2
public class CreateDirectoryService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    public void createBlogDirectory(Long blogNo){   //블로그의 이미지 헤더 바디 이미지 저장소



        String blogPath = "c:/blog";
        String UserPath = "c:/blog/User";
        String blogImagePath = "c:/blog/User/blogImage";
        String blogBoardImagePath = "c:/blog/User/blogBoardImage";


        File blogFolder = new File(blogPath);
        File UserFolder = new File(UserPath);
        File blogImagePathFolder = new File(blogImagePath);
        File blogBoardImagePathFolder = new File(blogBoardImagePath);


        blogFolder.mkdir(); //폴더 생성합니다.
        UserFolder.mkdir();
        blogImagePathFolder.mkdir(); //폴더 생성합니다.
        blogBoardImagePathFolder.mkdir();



        String ImagePath = itemImgLocation + "/"+ "blogImage"+"/" + blogNo;
        String BoardPath = itemImgLocation + "/"+ "blogBoardImage"+"/" + blogNo;

        log.info("생성된 폴더의 주소 : " + blogFolder);
        log.info("생성된 폴더의 주소 : " + UserFolder);
        log.info("생성된 폴더의 주소 : " + ImagePath);
        log.info("생성된 폴더의 주소 : " + BoardPath);
        File ImageFolder = new File(ImagePath);
        File BoardFolder = new File(BoardPath);

        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!ImageFolder.exists()) {
            try{
                ImageFolder.mkdir(); //폴더 생성합니다.
                BoardFolder.mkdir();
                log.info("폴더가 생성되었습니다.");
            }
            catch(Exception e){
                e.getStackTrace();
            }

        }else {
            log.info("이미 폴더가 생성되어 있습니다.");
        }


    }




    //회읜의 글 번호 폴더 생성
    public void createBlogContentDirectory(Long blogNo,Long articleNo){

        String AarticlePath = itemImgLocation + "/"+ "blogBoardImage"+"/" + blogNo + "/" + articleNo;
        
        //유저 폴더 만들기
        String blogFolderPath = itemImgLocation + "/"+ "blogBoardImage"+"/" + blogNo;

        log.info("유저폴더경로 : "+ blogFolderPath);
        File ArticleFolder = new File(AarticlePath);
        File blogFolder = new File(blogFolderPath);

        if(!blogFolder.exists()){
            log.info("폴더 만들기");
            boolean chk = blogFolder.mkdir();
            log.info("결과 : " + chk);
        }



        if (!ArticleFolder.exists()) {
            try{
                ArticleFolder.mkdir(); //폴더 생성합니다.
                log.info("폴더가 생성되었습니다.");
            }
            catch(Exception e){
                e.getStackTrace();
            }

        }else {
            log.info("이미 폴더가 생성되어 있습니다.");
        }


    }










}
