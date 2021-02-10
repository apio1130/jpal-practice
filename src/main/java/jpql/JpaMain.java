package jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 내부 조인 - inner 생략 가능
            String innerJoinQuery = "select m from Member m inner join m.team t";
            // 외부 조인 - outer 생략 가능
            String leftJoinQuery = "select m from Member m left outer join m.team t where t.name = :teamName";
            // 세타 조인
            String crossJoinQuery = "select m from Member m Team t where m.username = t.name";

            // 조인 - ON
            // 1) 조인 대상 필터링
            // JPQL
            // String jpqlJoinOnFiltering = "SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'A'";
            // String sqlJoinOnFiltering = "SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID = t.id AND t.name = 'A'";
            // 2) 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1 부터)
            // String jpqlJoinOnNotRefTable = "SELECT m, t FROM Member m LEFT JOIN Team t ON m.username = t.name";
            // String sqlJoinOnNotRefTable = "SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name";


            List<Member> result = em.createQuery(leftJoinQuery, Member.class)
                .setParameter("teamName", "teamA")
                .getResultList();

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
