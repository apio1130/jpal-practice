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

            // TypeQuery: 반환 타입 명확할 때 사용
            TypedQuery<Member> quert1 = em.createQuery("select m from Member m", Member.class);
            // TypedQuery<String> quert2 = em.createQuery("select m.username from Member m", String.class);
            List<Member> resultList = quert1.getResultList();

            // Query: 반환 타입이 명확하지 않을 때 사용
            // Query query3 = em.createQuery("select m.username, m.age from Member m");

            // 파라미터 바인딩
            // 1) 이름 기준
            Member result1 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
            System.out.println("result =  " + result1.getUsername());
            System.out.println("=====================================");
            // 2) 위치 기준(사용 잘 안함: 쿼리 변경 시 순서에 대해서도 전부 변경해줘야 할 가능성이 있음)
            Member result2 = em.createQuery("select m from Member m where m.username = ?1", Member.class)
                .setParameter(1, "member1")
                .getSingleResult();
            System.out.println("result =  " + result2.getUsername());

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
