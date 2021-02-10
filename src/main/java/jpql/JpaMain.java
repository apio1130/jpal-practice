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
            // Member member = new Member();
            // member.setUsername("member1");
            // member.setAge(10);
            // em.persist(member);

            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + (i+1));
                member.setAge(i + 1);
                em.persist(member);
            }

            em.flush();
            em.clear();

            // 페이징
            List<Member> members = em.createQuery("select m from Member m order by m.age desc ", Member.class)
                // .setFirstResult(0)
                .setFirstResult(10) // offset 값
                .setMaxResults(10) // limit 값
                .getResultList();

            System.out.println("result.size = " + members.size());
            for(Member member1 : members) {
                System.out.println("member1 = " + member1);
            }


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
