package jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            // 프로젝션
            // 1) 엔티티 프로젝션
            // List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
            // Member findMember = result.get(0);
            // findMember.setAge(20);  // 20으로 변경되어 저장됨. 조회 결과가 영속성 컨텐스트에 전부 관리됨.

            // 2) 인티티 프로젝션
            // List<Team> result = em.createQuery("select m.team from Member m", Team.class).getResultList();
            //  -> inner join Query로 조회함(묵시적 조인). 조인의 사용은 성능에 영향을 많이 받기에 아래와 같이 명시적 조인으로 작성
            // List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();
            // inner join Query로 조회함(명시적 조인). jpql이 join을 사용한다고 직관적으로 알 수 있음.

            // 3) 임베디드 타입 프로젝션
            // em.createQuery("select o.address from Order o", Address.class).getResultList();
            // 임베디드 타입을 조회하기 위해서는 소속된 Entity에서 조회를 시작해줘야한다.
            // from Address로 실행 불가능

            // 4) 스칼라 타입 프로젝션
            // 4-1) Query 타입 조회
            // List resultList = em.createQuery("select m.username, m.age from Member m").getResultList();
            // Object o = resultList.get(0);
            // Object[] result = (Object[])o;
            // System.out.println("username = " + result[0]);
            // System.out.println("age = " + result[1]);

            // 4-2) Object[] 타입으로 조회
            // List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m").getResultList();
            // Object[] result = resultList.get(0);
            // System.out.println("username = " + result[0]);
            // System.out.println("age = " + result[1]);

            // 4-3) new 명령어로 조회
            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();
            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());
            // 단순 값을 조회하기에는 new DTO 명령어로 조회하는 방법을 추천.
            // 패키지 명을 포함한 전체 클래스명을 입력
            // 순서와 타입이 일치하는 생성자 필요

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
