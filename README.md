# portfolioBlog
블로그 관리 웹 사이트 개인 프로젝트 포트폴리오


# 소개
개개인의 블로그를 생성해 포스팅 작성, 댓글 작성, 블로그 방문, 좋아요 기능을 지원하는 사이트 입니다.
<BR>

# 제작기간 & 참여 인원
<UL>
  <LI>2023.07.27 ~ 2023.08.16</LI>
  <LI>개인 프로젝트</LI>
</UL>


# 사용기술
![js](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white)
![js](https://img.shields.io/badge/Java-FF0000?style=for-the-badge&logo=Java&logoColor=white)
![js](https://img.shields.io/badge/IntelliJ-004088?style=for-the-badge&logo=IntelliJ&logoColor=white)
![js](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white)
![js](https://img.shields.io/badge/security-6DB33F?style=for-the-badge&logo=security&logoColor=white)

![js](https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white)
![js](https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![js](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)

# E-R 다이어그램


![개인 프로젝트 BLOG ERD 페이지](https://github.com/oals/portfolioBlog/assets/136543676/6300be94-d45e-4641-aba2-6fdf95022ca3)



# 핵심 기능 및 페이지 소개

<details>
 <summary> UserInfo Entity
 
 </summary> 





          
    @Log4j2
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "userinfo")
    @Builder
    @Entity
    public class UserInfo {

    @Id
    @Column(nullable = false,length = 200)
    private String userEmail;  //이메일

    @Column(nullable = false)
    private String userPw; //비밀번호

    @Column(nullable = false,length = 50)
    private String userName; //이름

    @Column(nullable = true,length = 50)
    private String userNickName; //닉네임

    @Column(nullable = false,length = 300)
    private String address; //주소

    @Column(nullable = false,length = 50)
    private String phone; //전화번호

    @Column(nullable = false)
    private String joinDate; //가입날짜

    @Column(nullable = true)
    private int followCount; //팔로워 수

    @Column(nullable = true)
    private int followingCount; //팔로잉 수
    @Enumerated(EnumType.STRING)
    private Role role;  //권한 설정


    public static UserInfo createMember(UserInfoDTO userInfoDTO, PasswordEncoder passwordEncoder){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(userInfoDTO.getUserEmail());
        userInfo.setUserName(userInfoDTO.getUserName());
        userInfo.setUserNickName(userInfoDTO.getUserNickName());
        userInfo.setAddress(userInfoDTO.getAddress());
        userInfo.setPhone(userInfoDTO.getPhone());
        userInfo.setJoinDate(userInfoDTO.getJoinDate());
        userInfo.setFollowCount(userInfoDTO.getFollowCount());
        userInfo.setFollowingCount(userInfoDTO.getFollowingCount());
        userInfo.setRole(Role.USER);


        // 암호화
        String password = passwordEncoder.encode(userInfoDTO.getUserPw());
        userInfo.setUserPw(password);

        return userInfo;
    }

    public void Update_NickName(String userNickName){

        this.userNickName = userNickName;
    }



    }
          








</details>

<details>
 <summary> UserBoard Entity
 
 </summary> 




    @Entity
    @Getter
    @Setter
    @Table(name="userboard")
    public class UserBoard {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long articleNo; 

    @ManyToOne(fetch = FetchType.LAZY)    //확인 필요
    @JoinColumn(name="blogsetting_blog_no")
    private BlogSetting blogSetting; //블로그 정보


    @OneToMany(mappedBy = "userBoard",cascade = CascadeType.ALL)
    private List<UserComment> userComment = new ArrayList<UserComment>(); //댓글 


    @OneToOne(cascade = CascadeType.ALL)
    private BoardImage Thumbnail;   //썸네일 이미지 



    @Column(nullable = false)
    private String category;    //해당 포스트의 카테고리 명

    @Column(nullable = false)
    private String title;   //제목

    @Column(nullable = false,length = 5000)
    private String content; //내용

    @Min(value=0)
    @Column(nullable = false)
    private int view;   //조회수

    @Min(value=0)
    @Column(nullable = false,name = "board_Like")
    private int like;   //좋아요 수

    @Column(nullable = false)
    private LocalDate writeDate;    //작성일


    public void updateLike(){
        this.like += 1;

    }




    }




  





</details>


<details>
 <summary> UserComment Entity
 
 </summary> 





    @Entity
    @Getter
    @Setter
    @Log4j2
    public class UserComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentNo;  


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userboard_articleNo")
    private UserBoard userBoard;    //댓글이 달린 포스트

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blogsetting_blogNo")
    private BlogSetting blogSetting;  //블로그 설정
    

    @Column(nullable = false,name="user_nick_name")
    private String userNickName;    //댓글 작성자


    @Column(nullable = false,name="comment")
    private String comment; //댓글 내용


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserComment_commentNo")
    private UserComment parentComment;       //부모 댓글


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="UserComment_commentNo")
    private List<UserComment> childComment = new ArrayList<UserComment>();  //자식 댓글  



    @Column(nullable = false)
    private LocalDate writeDate;    //댓글 작성일



    public void addChildComment(UserComment userComment){

        this.childComment.add(userComment);

    }



    }









</details>


<details>
 <summary> Category Entity
 
 </summary> 





    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name="category")
    @Log4j2
    @Entity
    public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blogsetting_blog_no")
    private BlogSetting blogSetting;    //블로그 정보


    @OneToMany(mappedBy = "category" ,cascade = CascadeType.PERSIST)
    private List<CategoryItem> categoryItem = new ArrayList<CategoryItem>();    //카테고리 정보



    public void addCategory(List<CategoryItem> categoryItem){

        for(int i = 0; i< categoryItem.size(); i++) {
            this.categoryItem.add(categoryItem.get(i));
        }
    }


    }









</details>


<details>
 <summary> CategoryItem Entity
 
 </summary> 





    @Entity
    @Getter
    @Setter
    @Table(name="categoryItem")
    public class CategoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category_name")
    private String CategoryName; // 카테고리 명

    @Min(value = 0)
    @Column(name="category_count")
    private int categoryCount;  //카테고리의 글 개수

    @ManyToOne
    private Category category;


     }









</details>


<details>
 <summary> BlogSetting Entity
 
 </summary> 




     @Entity
     @Getter
     @AllArgsConstructor
     @NoArgsConstructor
     @Table(name="blogsetting")
     @Builder
     public class BlogSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogNo;


    @OneToOne(fetch = FetchType.LAZY)    
    @JoinColumn(name = "userinfo_userEmail")
    private UserInfo userInfo;  //블로그 주인

    @Column(nullable = true)
    private String blogTopic;  //블로그 주제

    @Column(nullable = true)
    private String headerImagePath; //헤더 이미지

    @Column(nullable = true)
    private String bodyImagePath; //바디 이미지

    @Column(nullable = true)
    private String profileInfo; //프로필 소개글

    @Column(nullable = true)
    private String profileImagePath; //프로필 이미지

    @Column(nullable = true)
    private boolean privateChk; //비공개 여부

    @Column(nullable = true)
    private boolean writePermission; //댓글 작성 여부


    public void update_profile(BlogSettingDTO blogSettingDTO){


        this.profileInfo = blogSettingDTO.getProfileInfo();
        this.blogTopic = blogSettingDTO.getBlogTopic();
        this.privateChk = blogSettingDTO.isPrivateChk();
        this.headerImagePath = blogSettingDTO.getHeaderImagePath();
        this.bodyImagePath =  blogSettingDTO.getBodyImagePath();
        this.profileImagePath = blogSettingDTO.getProfileImagepath();
    }






    }








</details>


<details>
 <summary> BlogSettingImage Entity
 
 </summary> 





    @Entity
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class BlogSettingImage {    


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long BlogImgNo;

    @OneToOne(fetch = FetchType.LAZY)    
    @JoinColumn(name="blogsetting_blog_no")
    BlogSetting blogSetting; //블로그 정보

    String PastHeaderImagePath;  //수정 전 헤더이미지 
    String PastBodyImagePath; //수정 전 바디 이미지
    String PastProfileImagePath; //수정 전 프로필 이미지

    public void update_Image(BlogSettingImageDTO blogSettingImageDTO){
        this.PastHeaderImagePath = blogSettingImageDTO.getPastHeaderImagePath();
        this.PastBodyImagePath = blogSettingImageDTO.getPastBodyImagePath();
        this.PastProfileImagePath = blogSettingImageDTO.getPastProfileImagePath();

    }



    }    








</details>



<details>
 <summary> Visit Entity
 
 </summary> 





    @Getter
    @ToString
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity
    public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blogsetting_blog_no")
    private BlogSetting blogsetting; //블로그 정보

    @Column(nullable = false)
    private int today; //오늘의 방문자 수

    @Column(nullable = false)
    private int total; //총 방문자 수

    @Column(nullable = false)
    private int week; //오늘의 요일



    public void updateVisit(int today,int total){

        this.today = today + 1;
        this.total = total + 1;



    }


    }









</details>





<hr>
<H3>메인 페이지</H3>
<BR>


![메인페이지](https://github.com/oals/portfolioLibrary/assets/136543676/c759c2c8-de7a-480a-85a7-2892e1893529)


<BR>
<BR>
<details>
 <summary> 메인페이지 플로우 차트
 
 </summary> 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/b2af51d4-3574-403d-acd2-42df754a7476'>
</details>

<details>
 <summary> 메인 페이지 카테고리 Service 코드
 
 </summary> 


         public PageResponseDTO<TopicBoardDTO> GetTopic_Board(PageRequestDTO pageRequestDTO,String topic) {

        Pageable pageable = pageRequestDTO.getPageable();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUserBoard qUserBoard = QUserBoard.userBoard;
        QBlogSetting qBlogSetting = QBlogSetting.blogSetting;
        QUserInfo qUserInfo = QUserInfo.userInfo;
        QBoardImage qBoardImage = QBoardImage.boardImage;

        BooleanBuilder builder = new BooleanBuilder();

        if(topic.equals("all")){  //전체 카테고리 선택 시
            builder.and(qUserBoard.blogSetting.blogTopic.ne("default"));
        }else{ //특정 카테고리 선택 시
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


        for(int i = 0; i< list.size(); i++){   //글 db 구조 변경시 삭제

            String[] strArr = list.get(i).getContent().split(",");
            //텍스트와 이미지 변환 작업
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

            //글 미리보기 텍스트 변환 작업
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




 
</details>



<br>
<br>






<H3>개개인의 블로그 디자인 설정 </H3>
<BR>
<BR>


![개인 프로젝트 BLOG 개인블로그 페이지1](https://github.com/oals/portfolioBlog/assets/136543676/9330a11d-6988-42fc-81f6-fd85867c99f9)

![개인 프로젝트 BLOG 개인블로그 페이지2](https://github.com/oals/portfolioBlog/assets/136543676/8b75032e-2f5e-4222-98a2-25545a72f975)



<BR>




<BR>
<H3>포스팅 </H3>
<BR>

![포스팅-작성](https://github.com/oals/portfolioBlog/assets/136543676/841cde84-7922-4ed5-a92d-16dabc93c227)


<BR>
<BR>
<BR>
<details>
 <summary> 블로그 포스팅 플로우 차트
 
 </summary> 
  <img src='https://github.com/oals/portfolioLibrary/assets/136543676/c74a46d8-b260-403f-a9f0-9dcefa48fc71'>
</details>


<details>
 <summary> 포스팅 Controller 코드
 
 </summary> 



          public ModelAndView myBlogInsert(Model model,Long blogNo,String userNickName,
                                     UserBoardDTO userBoardDTO,
                                     @RequestPart List<MultipartFile> multipartFileList) throws Exception {

        String Content = userBoardDTO.getContent();
        BoardImageDTO boardImageDTO = null;

        //테이블 먼저 생성 후 articleNo 가져오기
        UserBoardDTO createUserBoardDTO = userBoardService.create_UserBoard(userBoardDTO);
        Long articleNo = createUserBoardDTO.getArticleNo();

        List<String> imagePathList = new ArrayList<>();  //이미지 저장소
        

        if(multipartFileList != null) {   //해당 글 내용에 이미지가 있으면

            //이미지 저장 폴더 경로
            String path = itemImgLocation + "/blogBoardImage/" + blogNo + "/" + articleNo;  // + 글 번호 추가 필요

            //글 번호 폴더만들기
            createDirectoryService.createBlogContentDirectory(blogNo, articleNo);    

            //이미지 파일 업로드
            for(int i = 0; i <  multipartFileList.size(); i++) {

                //이미지 파일 업로드
                if(!multipartFileList.get(i).getOriginalFilename().isEmpty()){
                    String fileName = fileService.uploadFile(path, multipartFileList.get(i).getOriginalFilename(), multipartFileList.get(i).getBytes());

                    if(Content.contains(multipartFileList.get(i).getOriginalFilename())){
                        String imagePath = "/images/blogBoardImage/" + blogNo + "/" + articleNo + "/" + fileName;

                        //List에 이미지 경로들 모음
                        imagePathList.add(imagePath);

                        //반복문 종료 후 서비스 호출 -> 반복문으로 엔티티 여러개 생성

                        Content =  Content.replace(multipartFileList.get(i).getOriginalFilename(), imagePath);
                        //여기 데이터를 boardImage 테이블에 저장

                    }
                }
            }
        }


        //content내용 알고리즘 생성 / img 문자열 -> 이미지 경로
        BlogSettingDTO blogSettingDTO = blogSettingService.GetBlog_SettingEntity(blogNo);


        //알고리즘을 거친 블로그 컨텐츠 문자열로 다시 저장
        createUserBoardDTO.setContent(Content);



        if(!imagePathList.isEmpty()) {
            //작성 게시글의 이미지 저장 테이블에 이미지 저장
            boardImageDTO = userBoardService.UpdateBoard_Image(createUserBoardDTO, imagePathList);
        }

        //db에 글 저장
        userBoardService.Insert_UserBoard(blogSettingDTO,createUserBoardDTO,boardImageDTO);



        //카테고리의 count 값 + 1
        blogSettingService.update_CategoryCount(blogNo,userBoardDTO.getCategory(),true);

        
        
        //컨트롤러 -> 컨트롤러 이동 코드

        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/Blog");
        MAV.addObject("userNickName",userNickName);

        return MAV;
    }






 
</details>


<details>
 <summary> 포스팅 Service 코드
 
 </summary> 


            public void Insert_UserBoard(BlogSettingDTO blogSettingDTO, UserBoardDTO userBoardDTO,BoardImageDTO boardImageDTO) {

        BlogSetting blogSetting = modelMapper.map(blogSettingDTO,BlogSetting.class);
        UserBoard userBoard = modelMapper.map(userBoardDTO,UserBoard.class);
        BoardImage boardImage = null;

        if(boardImageDTO != null) {
             boardImage = modelMapper.map(boardImageDTO, BoardImage.class);
        }


        userBoard.setBlogSetting(blogSetting);       
        userBoard.setTitle(userBoardDTO.getTitle());
        userBoard.setContent(userBoardDTO.getContent());
        userBoard.setThumbnail(boardImage == null ? null : boardImage);
        userBoard.setCategory(userBoardDTO.getCategory());
        userBoard.getLike();
        userBoard.getView();
        userBoard.setWriteDate(LocalDateTime.now().toLocalDate());

        userBoardRepository.save(userBoard);

    }




 
</details>


<BR>
<BR>






<H3>댓글 및 무한 대댓글 구현 </H3>
<BR>


![댓글-작성_1](https://github.com/oals/portfolioLibrary/assets/136543676/da14d892-92e2-459e-b4ef-92dff9ea566a)

<BR>
<BR>

<details>
 <summary> 댓글 플로우 차트
 
 </summary> 
  <img src='https://github.com/oals/portfolioLibrary/assets/136543676/77b9eb63-b935-4f29-9f07-1b31c95e0cf6'>
</details>


<details>
 <summary> 댓글 & 대댓글 재귀 함수 자바 스크립트 코드
 
 </summary> 



              function createChild(comments, padding = 1) {
                const commentString = [];
                let str = ''
                for(let i = 0; i < comments.length; i++) {

                          str = "<div class='d-flex align-items-start flex-column mb-1' style='padding-left :" + (padding * 40)  + "px'>"
                          + "<div class='col-3 mt-1'>"
                                + "ㄴ " + "<a href='/Blog?userNickName=" + comments[i].userNickName + "' >"
                                    + comments[i].userNickName + "님"
                                + "</a>"
                            +"</div>"

                            +"<div class='col-6'>"
                               +  comments[i].comment
                            if(comments[i].comment != '삭제된 댓글입니다.'){
                               str += "<button class='badge bg-secondary m-1' data-bs-toggle='collapse'" + "data-bs-target='#collapseOne" + comments[i].commentNo  + "'aria-expanded='true'" +  "aria-controls='collapseOne" + comments[i].commentNo  + "'>답글</button>"

                                if(userNickName == comments[i].userNickName){
                                 str += "<button class='badge bg-secondary m-1 delComment'  data-value='"+ comments[i].commentNo   + "' >삭제</button>"
                                    }
                            }

                            str += "</div>"

                            +"<div class='col-3'>"
                                + comments[i].writeDate
                            +"</div>"
                       + "</div>"

                            +"<div id='collapseOne"+ comments[i].commentNo   + "'class='accordion-collapse collapse' aria-labelledby='headingOne' data-bs-      parent='#accordionExample'>"
                               +"<div class='accordion-body'>"
                              + "ㄴ <input type='text' class='w-75' style='border-left-width:0;border-right-width:0;border-top-width:0;border-bottom-width:0.5;'>"


                              +"<button type='button' class='replyBtn badge bg-secondary m-1' value ='" +  comments[i].commentNo + "'> 작성 </button>"

                                +"</div>"



                               +"</div>"

                       +"</div>"

                    commentString.push(str);


                  if (comments[i].childComment.length > 0) { //재귀 함수 호출
                    commentString.push(createChild(comments[i].childComment, padding + 1));
                  }

                }

                console.log(commentString)
                return commentString.join('')
              }

        





</details>



<details>
 <summary> 댓글 작성 Service 코드
 
 </summary> 



          public void Insert_UserComment(Map<String,Object> map) {

        BlogSetting blogSetting = modelMapper.map(map.get("blogSettingDTO"),BlogSetting.class);
        UserBoard userBoard = modelMapper.map(map.get("userBoardDTO"),UserBoard.class);

        UserComment parentComment;

        if(map.get("parentCommentDTO") == null){ //부모 댓글이 없을 때
            parentComment = null;

            UserComment userComment = new UserComment();
            userComment.setBlogSetting(blogSetting);
            userComment.setUserBoard(userBoard);
            userComment.setUserNickName(map.get("userNickName").toString());
            userComment.setComment(map.get("comment").toString());
            userComment.setParentComment(parentComment); //부모 댓글 null 처리
            userComment.setWriteDate(LocalDateTime.now().toLocalDate());

            userCommentRepository.save(userComment);

        }else{                                  //부모 댓글이 있을 때
            parentComment = modelMapper.map(map.get("parentCommentDTO"),UserComment.class);

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





</details>




<BR>
<BR>





<H3>블로그 설정</H3>
<BR>


![블로그-설정_1](https://github.com/oals/portfolioLibrary/assets/136543676/aa81ee04-9830-4d73-91ba-0393b39ae6ca)


<BR>
<BR>

<details>
 <summary> 블로그 설정 플로우 차트
 
 </summary> 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/e545d4fc-feb1-43f8-9031-9f9a526a2694'>
</details>
<BR>
<BR>




<H3>게시글 좋아요 기능</H3>
<BR>

![개인 프로젝트 BLOG 좋아요 페이지1](https://github.com/oals/portfolioBlog/assets/136543676/0d8b3be7-1faf-42d0-9adb-04a59333786d)

<BR>
<BR>
<UL>
    <LI> 블로그를 방문해 해당 포스팅에 좋아요를 기록 할 수 있도록 구현 했습니다.</LI>
    <LI> 해당 블로그 상단의 좋아요 순으로 포스팅이 나열됩니다.</LI>
</UL>
<BR>




<H3>블로그 방문자 수 집계 및 블로그 순위</H3>
<BR>

![개인 프로젝트 BLOG 방문자수 페이지1](https://github.com/oals/portfolioBlog/assets/136543676/5120a4dd-b910-4257-b9d9-f662eaab48b0)
![개인 프로젝트 BLOG 방문자수 페이지2](https://github.com/oals/portfolioBlog/assets/136543676/f5f54c1f-a375-42ed-8aa8-01417c895253)


<BR>
<details>
 <summary> 블로그 방문자수 집계 플로우 차트
 
 </summary> 
<img src='https://github.com/oals/portfolioLibrary/assets/136543676/5077b6ed-1c32-484f-b174-2df49b26cf68'>
</details>
<BR>



<details>
 <summary> 블로그 방문자수 집계 Service 코드
 
 </summary> 



            public void GetVisit_Info(Long blogNo) {

        Visit visit = visitRepository.findByBlogsetting_BlogNo(blogNo);

        //블로그의 투데이 / 토탈  + 1
        visit.updateVisit(visit.getToday(), visit.getTotal());


         LocalDateTime date = LocalDateTime.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekNumber = dayOfWeek.getValue();

            //하루가 지났을 경우 초기화
        if (dayOfWeekNumber != visit.getWeek()) {
            visit.updateVisit(0, visit.getTotal());
            visit.setWeek(dayOfWeekNumber);
        }


        visitRepository.save(visit);

    }




 
</details>




# 프로젝트를 통해 느낀 점과 소감

처음으로 만들어보는 개인 프로젝트였다.<BR>
이번 프로젝트를 통해 엔티티간의 매핑 관계와 스프링 부트의 기본적인 개념들에 대해서 어느정도 익숙해 진 것 같다. <BR>
기억나는 점은 설계 단계를 꼼꼼히 할 것 <BR>
어떤 식으로 기능을 구현할 지 중간중간 바뀌다 보니 코드가 꼬여버려서 시간을 낭비하기도 했다 <BR>
그런 부분이 아쉽지만 배워가는 과정에서 어쩔 수 없는 부분이라고 생각한다<BR>








