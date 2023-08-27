package com.springstudy.blogportfolio.controller;

import com.springstudy.blogportfolio.dto.*;
import com.springstudy.blogportfolio.entity.*;
import com.springstudy.blogportfolio.global.FindCookie;
import com.springstudy.blogportfolio.repository.BlogSettingRepository;
import com.springstudy.blogportfolio.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class BlogController {

    private final BlogSettingService blogSettingService;
    private final User_Info_Service user_info_service;
    private final VisitService visitService;
    private final UserBoardService userBoardService;

    private final HttpServletRequest httpServletRequest;
    private final FileService fileService;
    private final FindCookie findMyCookie;
    private final CreateDirectoryService createDirectoryService;


    @Value("${itemImgLocation}")  //application-properties속성의 변수 값 가져오기
    private String itemImgLocation;  //blogNo 넘겨서 폴더만들기 후 경로 지정

    @Value("${uploadPath}")  //application-properties속성의 변수 값 가져오기
    private String uploadPath;  //blogNo 넘겨서 폴더만들기 후 경로 지정




    @GetMapping(value = "/Blog")
    public String myBlog(String userNickName, Model model,
                         Principal principal,PageRequestDTO pageRequestDTO){


        //접속하려는 블로그의 설정 정보 받기
        RequestBlogSettingDTO blog_setting = blogSettingService.GetBlog_Setting(userNickName);


        //접속하려는 블로그의 투데이 및 토탈 + 1
        visitService.GetVisit_Info(blog_setting.getBlogSetting().getBlogNo());


        //해당 블로그의 글 가져오기
        pageRequestDTO.setSize(2);
        PageResponseDTO<UserBoardDTO> pageBoardDTO = userBoardService.GetBlog_Board(blog_setting.getBlogSetting().getBlogNo(),pageRequestDTO);



        //공감 순으로 블로그 글 제목 5개 가져오기
        List<UserBoardDTO> popularBoardDTO = userBoardService.Get_PopularBoard(blog_setting.getBlogSetting().getBlogNo());


        pageRequestDTO.setSize(5);  //블로그에서 보여줄 댓글 5개씩

        //해당 블로그의 카테고리 목록 가져오기
        CategoryDTO categoryDTO = blogSettingService.GetBlog_Category(blog_setting.getBlogSetting().getBlogNo());




        //블로그의 관리 권한
       boolean myBlogChk = false;

        //비로그인 시 처리
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){

            myBlogChk=false;

            model.addAttribute("blog_setting",blog_setting);
            model.addAttribute("responseDTO",pageBoardDTO);
            model.addAttribute("popularBoardDTO",popularBoardDTO);
            model.addAttribute("myBlogChk", myBlogChk);
            model.addAttribute("category",categoryDTO.getCategoryItem());
            return "blog/freeblog";
        }


        //로그인 상태에서 다른 사람의 블로그 접근 했을 때 권한 설정

        if(principal.getName().equals(blog_setting.getUserEmail())){
            myBlogChk=true;
        }


        model.addAttribute("blog_setting",blog_setting);
        model.addAttribute("responseDTO",pageBoardDTO);
        model.addAttribute("popularBoardDTO",popularBoardDTO);
        model.addAttribute("myBlogChk", myBlogChk);
        model.addAttribute("category",categoryDTO.getCategoryItem());

        return "blog/freeblog";
    }


    @PreAuthorize("principal.userNickName == #userNickName")
    @GetMapping(value="/myBlogWrite")           //글쓰기 페이지 보여주기
    public String myBlogWrite(Long blogNo,String userNickName,Model model){

        //해당 블로그가 가지고 있는 카테고리 가지고 가기
        CategoryDTO categoryDTO = blogSettingService.GetBlog_Category(blogNo);



        model.addAttribute("category",categoryDTO.getCategoryItem());
        model.addAttribute("userNickName",userNickName);
        model.addAttribute("blogNo",blogNo);


        return "blog/blog_Write_Page";
    }


    @GetMapping(value="findEmailPage")
    public String findEmailPage(){


        return "login/find_id";
    }


    @GetMapping(value="findPwPage")
    public String findPwPage(){


        return "login/find_pw";
    }



    @PostMapping(value="/myBlogInsert")
    public ModelAndView myBlogInsert(Model model,Long blogNo,String userNickName,
                                     UserBoardDTO userBoardDTO,
                                     @RequestPart List<MultipartFile> multipartFileList) throws Exception {

        String Content = userBoardDTO.getContent();
        BoardImageDTO boardImageDTO = null;

        //테이블 먼저 생성 후 articleNo 가져오기
        UserBoardDTO createUserBoardDTO = userBoardService.create_UserBoard(userBoardDTO);
        Long articleNo = createUserBoardDTO.getArticleNo();

        List<String> imagePathList = new ArrayList<>();  //이미지 저장소
        
        log.info("해당 게시물의 사이즈 " +multipartFileList.get(0).getOriginalFilename());

        if(multipartFileList != null) {   //해당 글 내용에 이미지가 있으면

            //이미지 저장 폴더 경로
            String path = itemImgLocation + "/blogBoardImage/" + blogNo + "/" + articleNo;  // + 글 번호 추가 필요

            //글 번호 폴더만들기
            createDirectoryService.createBlogContentDirectory(blogNo, articleNo);    //이 아티클 번호가 실제 아티클 번호여야 함

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

                        // saveall


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

        //db에 글 저장]
        userBoardService.Insert_UserBoard(blogSettingDTO,createUserBoardDTO,boardImageDTO);



        //카테고리의 count 값 + 1
        blogSettingService.update_CategoryCount(blogNo,userBoardDTO.getCategory(),true);

        
        
        //컨트롤러 -> 컨트롤러 이동 코드

        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/Blog");
        MAV.addObject("userNickName",userNickName);

        return MAV;
    }





    @PreAuthorize("principal.userNickName == #userNickName")
    @GetMapping(value="/myBlogSetting")  //세팅 페이지 보여주기
    public String myBlogSetting(String userNickName,Model model,Principal principal){


        RequestBlogSettingDTO blog_setting = blogSettingService.GetBlog_Setting(userNickName);;


        //현재 카테고리 정보 가져와야함
        CategoryDTO categoryDTO = blogSettingService.GetBlog_Category(blog_setting.getBlogSetting().getBlogNo());


        model.addAttribute("blog_setting",blog_setting);
        model.addAttribute("category",categoryDTO.getCategoryItem());
        return "blog/blog_Setting";
    }





    @PostMapping(value="/myBlogSetting")
    public ModelAndView UpdateMyBlogSetting(@Valid @RequestParam("headerImagePath") MultipartFile headerImagePath,
                                      @Valid @RequestParam("bodyImagePath") MultipartFile bodyImagePath,
                                      @Valid @RequestParam("profileImagePath") MultipartFile profileImagePath,
                                            ResponseBlogSettingDTO responseBlogSettingDTO  )throws Exception{


        Map<String,String> ImageMap = new HashMap<>();

        Long blogNo = responseBlogSettingDTO.getBlogNo();

        //닉네임 업데이트  -> 동일 닉네임 검사 필요 -> 타임리프에서 ajax로 중복검사 ->
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        user_info_service.Update_UserNickName(((UserDetails) principal).getUsername(),responseBlogSettingDTO.getUserNickName());

        blogSettingService.delete_Category(blogNo);

        //카테고리 추가, 수정, 삭제
        blogSettingService.update_Category(blogNo,responseBlogSettingDTO.getParentCategory());


        //블로그 정보 업데이트
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(headerImagePath);
        multipartFileList.add(bodyImagePath);
        multipartFileList.add(profileImagePath);


        //이미지 업로드
        String path  = itemImgLocation + "/blogImage/" + blogNo;


        for(int i = 0; i < multipartFileList.size(); i++) {

            if(!multipartFileList.get(i).getOriginalFilename().isEmpty()){
                String fileName = fileService.uploadFile(path, multipartFileList.get(i).getOriginalFilename(), multipartFileList.get(i).getBytes());

                ImageMap.put(multipartFileList.get(i).getName(), "/images/blogImage/" + blogNo + "/" + fileName);
            }else{
                ImageMap.put(multipartFileList.get(i).getName(), null);
            }
        }


        //blogNo 로 블로그 수정전 이미지 엔티티 혹은 dto 얻기
        BlogSettingImageDTO blogSettingImageDTO =  blogSettingService.GetBlog_SettingImage(blogNo);

        log.info(blogSettingImageDTO);

        BlogSettingDTO blogSettingDTO = BlogSettingDTO.builder()
                .blogNo(blogNo)
                .profileInfo(responseBlogSettingDTO.getProfileInfo())
                .privateChk(responseBlogSettingDTO.isPrivateChk())
                .blogTopic(responseBlogSettingDTO.getBlogTopic())
                .headerImagePath(ImageMap.get("headerImagePath") == null ? blogSettingImageDTO.getPastHeaderImagePath() : ImageMap.get("headerImagePath"))
                .bodyImagePath(ImageMap.get("bodyImagePath") == null ? blogSettingImageDTO.getPastBodyImagePath() : ImageMap.get("bodyImagePath"))
                .profileImagepath(ImageMap.get("profileImagePath") == null ? blogSettingImageDTO.getPastProfileImagePath() : ImageMap.get("profileImagePath"))
                .build();


        //blogSettingImage 테이블에 수정전 이미지의 image 경로 저장    -> 삼항 연산자에서 사용
        blogSettingImageDTO.setPastHeaderImagePath(blogSettingDTO.getHeaderImagePath());
        blogSettingImageDTO.setPastBodyImagePath(blogSettingDTO.getBodyImagePath());
        blogSettingImageDTO.setPastProfileImagePath(blogSettingDTO.getProfileImagepath());

        //blogSettingImage 테이블 업데이트 및 저장
        blogSettingService.update_PastBlogImage(blogNo,blogSettingImageDTO);


        //블로그 정보 저장
        blogSettingService.update_BlogImage(blogNo,blogSettingDTO);


        // /image/uuid 경로에서 uuid 가 포함되지 않은 파일 삭제
        String PastHeaderImagePath = blogSettingDTO.getHeaderImagePath();
        String PastBodyImagePath = blogSettingDTO.getBodyImagePath();
        String PastProfileImagePath = blogSettingDTO.getProfileImagepath();


        //삭제 하지 않을 (현재 이미지 파일) 이미지의 uuid 값 얻기
        List<String> UuidList = new ArrayList<>();
        UuidList.add(PastHeaderImagePath.substring(PastHeaderImagePath.lastIndexOf("/") + 1));
        UuidList.add(PastBodyImagePath.substring(PastBodyImagePath.lastIndexOf("/") + 1));
        UuidList.add(PastProfileImagePath.substring(PastProfileImagePath.lastIndexOf("/") + 1));


        // 리스트에 있는 uuid 값을 제외한 모든 파일 삭제
        fileService.deleteFile(UuidList,path);


        //블로그 정보 받는 곳 매핑
        //컨트롤러 -> 컨트롤러 이동 코드
        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/Blog");
        MAV.addObject("userNickName",responseBlogSettingDTO.getUserNickName());

        return MAV;


    }







}
