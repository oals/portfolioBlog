package com.springstudy.blogportfolio.entity;

import lombok.*;

import javax.persistence.*;

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
    private BlogSetting blogsetting;

    @Column(nullable = false)
    private int today;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private int week;



    public void updateVisit(int today,int total){

        this.today = today + 1;
        this.total = total + 1;

        //스레드 사용해서 초기화?


    }


}
