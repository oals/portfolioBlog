package com.springstudy.blogportfolio.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springstudy.blogportfolio.dto.*;
import com.springstudy.blogportfolio.entity.*;
import com.springstudy.blogportfolio.repository.CategoryRepository;
import com.springstudy.blogportfolio.repository.UserBoardImageRepository;
import com.springstudy.blogportfolio.repository.UserBoardRepository;
import com.springstudy.blogportfolio.repository.UserCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserBoardServiceImpl implements UserBoardService{


    private final ModelMapper modelMapper;
    private final UserBoardRepository userBoardRepository;

    private final UserBoardImageRepository userBoardImageRepository;
    private final UserCommentRepository userCommentRepository;

    private final CategoryRepository categoryRepository;


    @PersistenceContext
    EntityManager em;

    @Override
    public void Insert_UserBoard(BlogSettingDTO blogSettingDTO, UserBoardDTO userBoardDTO,BoardImageDTO boardImageDTO) {

        BlogSetting blogSetting = modelMapper.map(blogSettingDTO,BlogSetting.class);
        UserBoard userBoard = modelMapper.map(userBoardDTO,UserBoard.class);
        BoardImage boardImage = null;

        if(boardImageDTO != null) {
             boardImage = modelMapper.map(boardImageDTO, BoardImage.class);
        }

        log.info("boardImage테스트 : "+ boardImage);


        userBoard.setBlogSetting(blogSetting);        //몇가지 지우기
        userBoard.setTitle(userBoardDTO.getTitle());
        userBoard.setContent(userBoardDTO.getContent());
        userBoard.setThumbnail(boardImage == null ? null : boardImage);
        userBoard.setCategory(userBoardDTO.getCategory());
        userBoard.getLike();
        userBoard.getView();
        userBoard.setWriteDate(LocalDateTime.now().toLocalDate());

        userBoardRepository.save(userBoard);


        log.info("테스트 :" + userBoard);


    }

    @Override
    public void Insert_UserComment(Map<String,Object> map) {

        BlogSetting blogSetting = modelMapper.map(map.get("blogSettingDTO"),BlogSetting.class);
        UserBoard userBoard = modelMapper.map(map.get("userBoardDTO"),UserBoard.class);

        UserComment parentComment;

        if(map.get("parentCommentDTO") == null){
            parentComment = null;


            UserComment userComment = new UserComment();
            userComment.setBlogSetting(blogSetting);
            userComment.setUserBoard(userBoard);
            userComment.setUserNickName(map.get("userNickName").toString());
            userComment.setComment(map.get("comment").toString());
            userComment.setParentComment(parentComment);
            userComment.setWriteDate(LocalDateTime.now().toLocalDate());

            userCommentRepository.save(userComment);

        }else{
            parentComment = modelMapper.map(map.get("parentCommentDTO"),UserComment.class);


            log.info("엔티티 체크 : " + parentComment.getCommentNo());

            UserComment userComment = new UserComment();
            userComment.setBlogSetting(blogSetting);
            userComment.setUserBoard(userBoard);
            userComment.setUserNickName(map.get("userNickName").toString());
            userComment.setComment(map.get("comment").toString());
            userComment.setParentComment(parentComment);
            userComment.setWriteDate(LocalDateTime.now().toLocalDate());

            userCommentRepository.save(userComment);


            parentComment.addChildComment(userComment);

            userCommentRepository.save(parentComment);


        }





    }

    @Override
    public UserBoardDTO create_UserBoard(UserBoardDTO userBoardDTO) {



        UserBoard userBoard = modelMapper.map(userBoardDTO,UserBoard.class);
        userBoard.setWriteDate(LocalDateTime.now().toLocalDate());

        userBoardRepository.save(userBoard);

        UserBoardDTO userBoardDTO1 = modelMapper.map(userBoard,UserBoardDTO.class);

        return userBoardDTO1;
    }


    @Override
    public int Get_ArticleNo(Long blogNo) {  //테스트

        //개시글 개수 반환하는 레포지토리 생성
        String result = userBoardRepository.findByArticleCount();



        return 1;
    }

    @Override
    public List<UserBoardDTO> Get_PopularBoard(Long blogNo) {

        List<UserBoard> result = userBoardRepository.findByPopularBoard(blogNo,PageRequest.of(0,5));

        List<UserBoardDTO> list = result.stream().map(x-> modelMapper.map(x,UserBoardDTO.class)).collect(Collectors.toList());

        return list;
    }

    @Override
    public int UpdateBoard_Like(Long articleNo) {

        UserBoard userBoard = userBoardRepository.findById(articleNo).orElseThrow();

        userBoard.updateLike();

       userBoardRepository.save(userBoard);

       int boardLike = userBoard.getLike();

        return boardLike;
    }

    @Override
    public BoardImageDTO UpdateBoard_Image(UserBoardDTO createUserBoardDTO,List<String> imagePathList) {

        UserBoard userBoard = modelMapper.map(createUserBoardDTO,UserBoard.class);

        List<BoardImage> list = new ArrayList<>();

        for(int i =0; i< imagePathList.size(); i++){
            BoardImage boardImage = new BoardImage();
            boardImage.setArticleNo(userBoard.getArticleNo());
            boardImage.setImagePath(imagePathList.get(i));

            list.add(boardImage);
        }

        userBoardImageRepository.saveAll(list);

        BoardImageDTO boardImageDTO = modelMapper.map(list.get(0),BoardImageDTO.class);


        return boardImageDTO;


    }

    @Override
    public void Delete_Comment(Long commentNo) {

        UserComment userComment = userCommentRepository.findById(commentNo).orElseThrow();

        userComment.setComment("삭제된 댓글입니다.");

        userCommentRepository.save(userComment);

    }


    @Override
    public void BoardDelete(Long articleNo) {
        log.info("art : " +articleNo);


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBoardImage qBoardImage = QBoardImage.boardImage;


        userBoardRepository.deleteById(articleNo);


        queryFactory.delete(qBoardImage)
                .where(qBoardImage.articleNo.eq(articleNo))
                .execute();



    }

    @Override
    public void CommentAllDelete(Long articleNo) {

        log.info("댓글 삭제 접근 < 사용안되는걸로예상");

//        userCommentRepository.deleteByBoardComment(articleNo);


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUserComment qUserComment = QUserComment.userComment;

        queryFactory.delete(qUserComment)
                .where(qUserComment.userBoard.articleNo.eq(articleNo));





    }




    @Override
    public PageResponseDTO<UserBoardDTO> GetBlog_Board(Long blogNo, PageRequestDTO pageRequestDTO) {    //해당 블로그의 글 가져오기

        Pageable pageable =pageRequestDTO.getPageable();

        Page<Map<String,Object>> list =  userBoardRepository.findByBlogBoard(blogNo,pageable);

        for(int i = 0; i < list.get().toList().size(); i++){
            list.get().toList().get(i).put("rownum",list.getTotalElements() - i);
        }



        log.info("GetBlog_Board : "+list.get().toList());

        return PageResponseDTO.<UserBoardDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .data(list)
                .total((int)list.getTotalElements())
                .build();

    }

    @Override
    public UserBoardDTO GetBlog_BoardOne(Long articleNo) {  //댓글 쓰기 시 해당 글의 엔티티 얻기


        UserBoard userBoard = userBoardRepository.findById(articleNo).orElseThrow();

        UserBoardDTO userBoardDTO = modelMapper.map(userBoard,UserBoardDTO.class);


        return userBoardDTO;
    }

    @Override
    public PageResponseDTO<UserCommentDTO> GetBlog_Comment(Long articleNo , PageRequestDTO pageRequestDTO) { //댓글 목록 가져오기


        Pageable pageable =pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUserComment qUserComment = QUserComment.userComment;


        List<UserComment> query = queryFactory.selectFrom(qUserComment)
                .where(qUserComment.userBoard.articleNo.eq(articleNo).and(qUserComment.parentComment.isNull()))
                .orderBy(qUserComment.commentNo.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();



        List<UserCommentDTO> list = query.stream().map(x -> modelMapper.map(x,UserCommentDTO.class)).collect(Collectors.toList());


        Long count = queryFactory
                .select(qUserComment.count())
                .from(qUserComment)
                .where(qUserComment.parentComment.isNull().and(qUserComment.userBoard.articleNo.eq(articleNo)))
                .fetchOne();



        return PageResponseDTO.<UserCommentDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .list(list)
                .total(Integer.parseInt(count.toString()))
                .build();


    }
    @Override
    public PageResponseDTO<TopicBoardDTO> GetTopic_Board(PageRequestDTO pageRequestDTO,String topic) {

        //메인페이지에서 호출했을 때 topic 값은 null
        Pageable pageable = pageRequestDTO.getPageable();



        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUserBoard qUserBoard = QUserBoard.userBoard;
        QBlogSetting qBlogSetting = QBlogSetting.blogSetting;
        QUserInfo qUserInfo = QUserInfo.userInfo;
        QBoardImage qBoardImage = QBoardImage.boardImage;

        BooleanBuilder builder = new BooleanBuilder();

        if(topic.equals("all")){
            builder.and(qUserBoard.blogSetting.blogTopic.ne("default"));
        }else{
            builder.and(qUserBoard.blogSetting.blogTopic.eq(topic));
        }


        List<TopicBoardDTO> list = queryFactory.select(Projections.bean(TopicBoardDTO.class,
                        qUserInfo.userEmail,
                        qUserInfo.userNickName,
                        qBlogSetting.profileImagePath,
                        qUserBoard.Thumbnail.imagePath,
                        qUserBoard.title,
                        qUserBoard.content,
                        qUserBoard.writeDate))
                .from(qUserBoard)
                .where(builder)
                .join(qBlogSetting)
                .on(qBlogSetting.blogNo.eq(qUserBoard.blogSetting.blogNo))
                .join(qUserInfo)
                .on(qUserInfo.userEmail.eq(qBlogSetting.userInfo.userEmail))
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .orderBy(qUserBoard.writeDate.desc()).fetch();





//        List<UserBoardDTO> list = query.fetch().stream().map(x -> modelMapper.map(x,UserBoardDTO.class)).collect(Collectors.toList());

        for(int i = 0; i< list.size(); i++){   //글 db 구조 변경시 삭제

            String[] strArr = list.get(i).getContent().split(",");

            for(int j =0; j < (strArr.length); j++){
                if(strArr[j].contains("image")){
                    strArr[j] = " ";
                }
                if(strArr[j].contains("<br>")){
                    strArr[j] = " ";
                }
                if(strArr[j].contains(",")){
                    strArr[j] = " ";
                }
            }

            String str = String.join(" ",strArr);
            if(str.length() < 100){
                list.get(i).setContent(str);

            }else{
                list.get(i).setContent(str.substring(0,100) + ".....");
            }


        }


        Long count = queryFactory
                .select(qUserBoard.count())
                .from(qUserBoard)
                .where(builder.and(qUserBoard.Thumbnail.isNotNull()))
                .fetchOne();


        return PageResponseDTO.<TopicBoardDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .list(list)
                .total(Integer.parseInt(count.toString()))
                .build();


    }

    @Override
    public UserCommentDTO GetBlog_ParentComment(Long parentCommentNo) {

        UserComment userComment = userCommentRepository.findByParent_CommentNo(parentCommentNo);
        log.info("우선 체크 : " +userComment.getCommentNo());

        UserCommentDTO userCommentDTO = modelMapper.map(userComment,UserCommentDTO.class);


        return userCommentDTO;
    }
}
