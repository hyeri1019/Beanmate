package nyang.cat.repository;

import nyang.cat.entity.SecurityUsers;
import nyang.cat.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
                                            // <엔티티, 엔티티 ID 타입>
public interface UsersRepository extends JpaRepository<Users, Long>{
    /*  JpaRepository
         기본 메소드
             save()  :  insert/update
             findOne() : select(PK 이용)
             findAll() : select all/sort/pageable
             count()
             delete()
     */

    /* 사용자 정의 메소드 선언 규칙
      이름 :  findBy~ /  countBy~
      키워드 :
             And/Or/Between
             LessThan/GreaterThanEqual : 작은항목/크거나 같은항목 검색
             Like
             IsNull
             In : 여러 값 중 하나
             OrderBy

    */

    /*  Optional<T> : NullPointException 방지
                          orElse() : 값이 null 이면 인수로 전달된 값 반환
                          orElseThrow() : 값이 null 이면 인수로 전달된 예외를 발생시킴
     */
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}


