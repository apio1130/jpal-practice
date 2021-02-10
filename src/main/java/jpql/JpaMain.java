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
            List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
            Member findMember = result.get(0);
            findMember.setAge(20);  // 20으로 변경되어 저장됨. 조회 결과가 영속성 컨텐스트에 전부 관리됨.

            // 2) 인티티 프로젝션
            // 3) 임베디드 타입 프로젝션
            // 4) 스칼라 타입 프로젝션

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
