package com.springstudy.blogportfolio.restcontroller;

import com.springstudy.blogportfolio.dto.*;
import com.springstudy.blogportfolio.entity.Category;
import com.springstudy.blogportfolio.entity.UserComment;
import com.springstudy.blogportfolio.repository.UserCommentRepository;
import com.springstudy.blogportfolio.service.BlogSettingService;
import com.springstudy.blogportfolio.service.FileService;
import com.springstudy.blogportfolio.service.UserBoardService;
import com.springstudy.blogportfolio.service.User_Info_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BlogRestController {
    private final UserBoardService userBoardService;


    private final User_Info_Service user_info_service;
    private final BlogSettingService blogSettingService;

    private final FileService fileService;

    @Value("${itemImgLocation}")  //application-properties속성의 변수 값 가져오기
    private String itemImgLocation;  //blogNo 넘겨서 폴더만들기 후 경로 지정

    @Value("${uploadPath}")  //application-properties속성의 변수 값 가져오기
    private String uploadPath;  //blogNo 넘겨서 폴더만들기 후 경로 지정



    @DeleteMapping(value="/BoardDelete")
    public Object BoardDelete(Long articleNo,Long blogNo,String categoryName){

        //블로그 주인인지 검사
        //블로그 주인 일떄만 수정 삭제 버튼 보이게 변경
        log.info("articleNo:"  + articleNo);
        log.info("blogNo : " + blogNo);
        log.info("categorynAME : " + categoryName);


        log.info("삭제시 전달된 카테고리 이름" + categoryName);
        blogSettingService.update_CategoryCount(blogNo,categoryName,false);


        //boardimage 테이블의 데이터 삭제

        log.info("블로그 글 삭제 접근" + articleNo);

        userBoardService.BoardDelete(articleNo);


        //해당 글 삭제시 해당 글의 폴더 삭제
        String delpath = itemImgLocation + "/blogBoardImage/" + blogNo + "/"  + (articleNo);  // + 글 번호 추가 필요
        log.info("삭제할 폴더 경로 : " + delpath);

        fileService.deleteFolder(delpath);


        //블로그 글 다시 읽어오기

        return null;
    }




    @PostMapping(value = "/CommentInsert")
    public Object CommentInsert(Principal principal,Long blogNo,
                                Long articleNo,String comment,Long parentCommentNo , PageRequestDTO pageRequestDTO){

        log.info("댓글 작성 restcontroller");
        String userEmail = principal.getName();
        String userNickName = user_info_service.Get_UserNickame(userEmail);
        BlogSettingDTO blogSettingDTO = blogSettingService.GetBlog_SettingEntity(blogNo);
        UserBoardDTO userBoardDTO = userBoardService.GetBlog_BoardOne(articleNo);

        UserCommentDTO parentCommentDTO;


        if(parentCommentNo != 0){  // 해당 댓글이 자식 댓글일 때
            //부모 댓글 가져오기
             parentCommentDTO = userBoardService.GetBlog_ParentComment(parentCommentNo);
        }else{
            parentCommentDTO = null;
        }



        Map<String,Object> map = new HashMap<>();
        map.put("userNickName",userNickName);
        map.put("comment",comment);
        map.put("blogSettingDTO",blogSettingDTO);
        map.put("userBoardDTO",userBoardDTO);
        map.put("parentCommentDTO", parentCommentDTO);

        userBoardService.Insert_UserComment(map);





        pageRequestDTO.setSize(5); //가져올 댓글 수

        //다시 댓글 목록 가져오기
        PageResponseDTO<UserCommentDTO> pageResponseDTO = userBoardService.GetBlog_Comment(userBoardDTO.getArticleNo(),pageRequestDTO);
        log.info("댓글 페이지 정보 : " + pageResponseDTO);

        return pageResponseDTO;

    }

    @PostMapping(value = "/BoardLikePlus")
    public int BoardLikePlus(Long articleNo){

        //해당 게시글의 공감 수 얻기

        int boardLike = userBoardService.UpdateBoard_Like(articleNo);


        return boardLike;
    }





    @GetMapping("/pagingBoard")
    public Object pagingBoard(PageRequestDTO pageRequestDTO, Long blogNo){


        log.info("blog page 페이징 접근 Go...");

        pageRequestDTO.setSize(2);

        PageResponseDTO<UserBoardDTO> userBoardDTO = userBoardService.GetBlog_Board(blogNo,pageRequestDTO);
        log.info("페이징테스트: "+userBoardDTO);


        return userBoardDTO;


    }

    @GetMapping("/pagingComment")
    public Object pagingComment(PageRequestDTO pageRequestDTO, Long articleNo) {
        pageRequestDTO.setSize(5);

        //해당 블로그 글들의 댓글 가져오기
        PageResponseDTO<UserCommentDTO> pageCommentDTO = userBoardService.GetBlog_Comment(articleNo, pageRequestDTO);

        log.info("haha");
        log.info(pageCommentDTO);

        return pageCommentDTO;
    }

    @DeleteMapping("/deleteComment")
    public void deleteComment(Long commentNo){


        userBoardService.Delete_Comment(commentNo);



    }


}
