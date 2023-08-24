package com.springstudy.blogportfolio.service;

import com.springstudy.blogportfolio.dto.*;

import java.util.List;
import java.util.Map;

public interface UserBoardService {

    public void Insert_UserBoard(BlogSettingDTO blogSettingDTO, UserBoardDTO userBoardDTO,BoardImageDTO boardImageDTO);
    //db에 게시글 저장
    
    public void Insert_UserComment(Map<String,Object> map); //댓글 작성

    public UserBoardDTO create_UserBoard(UserBoardDTO userBoardDTO);
    




    public int Get_ArticleNo(Long blogNo);

    public List<UserBoardDTO> Get_PopularBoard(Long blogNo);


    public  PageResponseDTO<UserBoardDTO> GetBlog_Board(Long blogNo,PageRequestDTO pageRequestDTO);   //블로그의 글 전체 가져오기


    public UserBoardDTO GetBlog_BoardOne(Long articleNo);

    public PageResponseDTO<UserCommentDTO> GetBlog_Comment(Long articleNo, PageRequestDTO pageRequestDTO);

    public PageResponseDTO<TopicBoardDTO> GetTopic_Board(PageRequestDTO pageRequestDTO,String topic);

    public UserCommentDTO GetBlog_ParentComment(Long parentCommentNo);




    public int UpdateBoard_Like(Long articleNo);


    public BoardImageDTO UpdateBoard_Image(UserBoardDTO createUserBoardDTO,List<String> imagePathList);


    public void Delete_Comment(Long commentNo);




    public void BoardDelete(Long articleNo);

    public void CommentAllDelete(Long articleNo);  //글 삭제 시 해당 글의 댓글들 삭제




}
