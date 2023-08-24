package com.springstudy.blogportfolio.service;


import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springstudy.blogportfolio.dto.*;
import com.springstudy.blogportfolio.entity.*;
import com.springstudy.blogportfolio.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional   //카테고리 삭제 시
public class BlogSettingServiceImpl implements BlogSettingService {

    private final BlogSettingRepository blogSettingRepository;
    private final VisitRepository visitRepository;
    private final UserBoardRepository userBoardRepository;
    private final BlogSettingImageRepository blogSettingImageRepository;
    private final CategoryItemRepository categoryItemRepository;

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @PersistenceContext
    EntityManager em;

    @Override
    public BlogSettingDTO create_blog(UserInfoDTO user_info_dto) {
        UserInfo user_info = modelMapper.map(user_info_dto,UserInfo.class);

        BlogSetting blog_setting = BlogSetting.builder()
                .userInfo(user_info)
                .blogTopic("기본값")
                .headerImagePath("../imgs/whitebg.png")
                .bodyImagePath("../imgs/whitebg.png")
                .privateChk(true)
                .writePermission(false)
                .profileImagePath("../imgs/pngegg.png")
                .profileInfo("소개 없음")
                .build();

        blogSettingRepository.save(blog_setting);

        BlogSettingDTO blogSettingDTO = modelMapper.map(blog_setting,BlogSettingDTO.class);




        return blogSettingDTO;
    }

    @Override
    public void create_visit(BlogSettingDTO blogSettingDTO) {

        BlogSetting blogSetting = modelMapper.map(blogSettingDTO,BlogSetting.class);

        LocalDateTime date = LocalDateTime.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekNumber = dayOfWeek.getValue();

        Visit visit = Visit.builder()
                .blogsetting(blogSetting)
                .today(0)
                .total(0)
                .week(dayOfWeekNumber)
                .build();

        visitRepository.save(visit);

    }












    @Override
    public void create_category(BlogSettingDTO blogSettingDTO) {

        BlogSetting blogSetting = modelMapper.map(blogSettingDTO,BlogSetting.class);
        List<CategoryItem> list = new ArrayList<>();

        Category category = new Category();

        CategoryItem categoryItem = new CategoryItem();
        categoryItem.setCategoryName("기본 카테고리");
        categoryItem.setCategory(category);
        categoryItem.setCategoryCount(0);


        list.add(categoryItem);

        category.setBlogSetting(blogSetting);
        category.setCategoryItem(list);

        categoryRepository.save(category);

    }

    @Override
    public void create_Image(BlogSettingDTO blogSettingDTO) {

        BlogSetting blogSetting = modelMapper.map(blogSettingDTO,BlogSetting.class);

        BlogSettingImage blogSettingImage = BlogSettingImage.builder()
                .blogSetting(blogSetting)
                .PastHeaderImagePath("/images/blogImage/default/whitebg.png")  //수정 전 이미지
                .PastBodyImagePath("/images/blogImage/default/whitebg.png")
                .PastProfileImagePath("/images/blogImage/default/whitebg.png")
                .build();

        blogSettingImageRepository.save(blogSettingImage);

        

    }


    @Override
    public RequestBlogSettingDTO GetBlog_Setting(String userNickName) { //접속하려는 블로그의 설정 정보 가져오기


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBlogSetting qBlogSetting = QBlogSetting.blogSetting;
        QUserInfo qUserInfo = QUserInfo.userInfo;
        QVisit qVisit = QVisit.visit;

        JPAQuery<RequestBlogSettingDTO> query =  queryFactory.select(Projections.bean(RequestBlogSettingDTO.class,
                qBlogSetting,
                qUserInfo.userEmail,
                qUserInfo.userNickName,
                qUserInfo.joinDate,
                qVisit.today,
                qVisit.total
                ))
                .from(qBlogSetting)
                .join(qUserInfo)
                .on(qUserInfo.userEmail.eq(qBlogSetting.userInfo.userEmail))
                .join(qVisit)
                .on(qBlogSetting.blogNo.eq(qVisit.blogsetting.blogNo))
                .where(qUserInfo.userNickName.eq(userNickName));



        RequestBlogSettingDTO requestBlogSettingDTO = query.fetchOne();

        log.info(requestBlogSettingDTO);



        return requestBlogSettingDTO;
    }

    @Override
    public BlogSettingDTO GetBlog_SettingEntity(Long blogNo) {
        BlogSetting blogSetting = blogSettingRepository.findById(blogNo).orElseThrow();

        BlogSettingDTO blogSettingDTO = modelMapper.map(blogSetting,BlogSettingDTO.class);



        return blogSettingDTO;
    }




    @Override
    public PageResponseDTO<RequestPopularBlogDTO> GetAll_Blog(PageRequestDTO pageRequestDTO) {  //메인페이지에 뿌려줄 블로그 정보 가져오기

        Pageable pageable =pageRequestDTO.getPageable();



        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBlogSetting qBlogSetting = QBlogSetting.blogSetting;
        QUserInfo qUserInfo = QUserInfo.userInfo;
        QVisit qVisit = QVisit.visit;

        List<RequestPopularBlogDTO> list =  queryFactory.select(Projections.bean(RequestPopularBlogDTO.class,
                        qUserInfo.userEmail,
                        qUserInfo.userNickName,
                        qBlogSetting.profileImagePath,
                        qVisit.total
                ))
                .from(qBlogSetting)
                .join(qUserInfo)
                .on(qUserInfo.userEmail.eq(qBlogSetting.userInfo.userEmail))
                .join(qVisit)
                .on(qBlogSetting.blogNo.eq(qVisit.blogsetting.blogNo))
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .orderBy(qVisit.total.desc())
                .fetch();


        Long count = queryFactory
                .select(qUserInfo.count())
                .from(qUserInfo)
                .fetchOne();



        return PageResponseDTO.<RequestPopularBlogDTO>widthAll()
                .pageRequestDTO(pageRequestDTO)
                .list(list)
                .total(Integer.parseInt(count.toString()))
                .build();
    }


    @Override
    public CategoryDTO GetBlog_Category(Long blogNo) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCategory qCategory = QCategory.category;

        JPAQuery<Category> query =  queryFactory.selectFrom(qCategory)
                .where(qCategory.blogSetting.blogNo.eq(blogNo));

        Category category = query.fetchOne();


        CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);
        log.info("가져온 카테고리 테스트 : " + categoryDTO);



        return categoryDTO;
    }



    @Override
    public BlogSettingImageDTO GetBlog_SettingImage(Long blogNo) {






        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBlogSettingImage qBlogSettingImage = QBlogSettingImage.blogSettingImage;


        JPAQuery<BlogSettingImage> query = queryFactory.selectFrom(qBlogSettingImage)
                .where(qBlogSettingImage.blogSetting.blogNo.eq(blogNo));


        BlogSettingImageDTO blogSettingImageDTO = modelMapper.map(query.fetch().get(0),BlogSettingImageDTO.class);



        return blogSettingImageDTO;
    }

    @Override
    public void update_BlogImage(Long blogNo,BlogSettingDTO blogSettingDTO) {

        BlogSetting blogSetting = blogSettingRepository.findById(blogNo).orElseThrow(EntityNotFoundException::new);

        blogSetting.update_profile(blogSettingDTO);

        blogSettingRepository.save(blogSetting);

    }


    @Override
    public void update_PastBlogImage(Long blogNo, BlogSettingImageDTO blogSettingImageDTO) {
        BlogSettingImage blogSettingImage = blogSettingImageRepository.findByBlogSetting_BlogNo(blogNo);

        blogSettingImage.update_Image(blogSettingImageDTO);  //수정 전 이미지 업데이트
        blogSettingImageRepository.save(blogSettingImage);

    }


    @Override
    public void update_Category(Long blogNo, String[] parentCategory) { //카테고리 업데이트

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCategory qCategory = QCategory.category;

        JPAQuery<Category> query =  queryFactory.selectFrom(qCategory)
                .where(qCategory.blogSetting.blogNo.eq(blogNo));

        Category category = query.fetchOne();



        //기존의 카테고리 아이템 값 전체 삭제
        log.info("수정 접근");

        //새 카테고리 값 추가
        List<CategoryItem> list = new ArrayList<>();

        for(int i = 0; i < parentCategory.length;i++){
            log.info(" :  "+parentCategory[i]);
        }


        for(int i =0; i< parentCategory.length; i+= 2){   //인덱스 + 2     /   인덱스 i  i + 1  사용


            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setCategory(category);
            categoryItem.setCategoryName(parentCategory[i]);
            categoryItem.setCategoryCount(Integer.parseInt(parentCategory[i + 1]));

            list.add(categoryItem);
        }

        categoryItemRepository.saveAll(list);

    }

    @Override
    public void delete_Category(Long blogNo) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCategory qCategory = QCategory.category;
        QCategoryItem qCategoryItem = QCategoryItem.categoryItem;

        JPAQuery<Category> query =  queryFactory.selectFrom(qCategory)
                .where(qCategory.blogSetting.blogNo.eq(blogNo));

        Category category = query.fetchOne();

        //기존의 카테고리 아이템 값 전체 삭제
        log.info("카테고리 삭제 접근");

         queryFactory.delete(qCategoryItem)
                        .where(qCategoryItem.category.categoryNo.eq(category.getCategoryNo()))
                        .execute();

        categoryRepository.save(category);

    }

    @Override
    public void update_CategoryCount(Long blogNo,String categoryName,boolean chk) {

//        Category category = categoryRepository.findByCategory(blogNo);

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCategory qCategory = QCategory.category;

        JPAQuery<Category> query =  queryFactory.selectFrom(qCategory)
                .where(qCategory.blogSetting.blogNo.eq(blogNo));

        Category category = query.fetchOne();

        log.info("qdsl 테스트 : " + category.getCategoryNo());



        //해당 카테고리 찾기

        for(int i = 0; i < category.getCategoryItem().size(); i++){

            if(category.getCategoryItem().get(i).getCategoryName().equals(categoryName)){

                if(chk) {  //증감
                    category.getCategoryItem().get(i).setCategoryCount(category.getCategoryItem().get(i).getCategoryCount() + 1);
                }else{
                    category.getCategoryItem().get(i).setCategoryCount(category.getCategoryItem().get(i).getCategoryCount() - 1);
                }

            }
        }

        categoryRepository.save(category);

    }




}
