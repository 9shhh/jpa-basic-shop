package jpabook;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderStatus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // 애플리케이션 로딩 시점에 딱 1개만 생성되어야 함.

        EntityManager em = emf.createEntityManager(); // 트랜잭션 단위 마다 생성 되어야함. 웹 기준, 고객의 요청이 올떄 마다 사용되고 close() 됨.

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            System.out.println("아니 왜 안돼");
            Member member = new Member();
            member.setId(1L);
            member.setName("park");

            em.persist(member);

            Order order = new Order();
            order.setId(1L);
            order.setMemberId(member.getId());
            order.setOrderDate(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.ORDER);

            em.persist(order);

            Order findOrder = em.find(Order.class, 1L);
            Long memberId = findOrder.getMemberId(); // 회원 식별자를 조회

            Member findMember = em.find(Member.class, memberId);// 객체 그래프 탐색 방식이 아닌 order 객체에 저장된 회원 식별자로 맴버를 조회함 -> bad.
            System.out.println(findMember.getId());
            System.out.println(findMember.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}