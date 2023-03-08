//package nyang.cat.comment.entity;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import java.util.Date;
//
//@Entity
//public class Comment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long cno;
//
//    private Long pno;
//    private Long target;
//    private String comment;
//    private String commenter;
//    private Date regDate;
//    private Date upDate;
//
//    public Comment(Long pno, Long target, String comment, String commenter) {
//        this.pno = pno;
//        this.target = target;
//        this.comment = comment;
//        this.commenter = commenter;
//    }
//}
