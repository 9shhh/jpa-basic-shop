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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setName("park");

            em.persist(member);

            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.ORDER);

            em.persist(order);

            System.out.println(member.getId());
            System.out.println(order.getId());

            Member member1 = em.find(Member.class, member.getId());
            Order order1 = em.find(Order.class, order.getId());

            System.out.println(member1);
            System.out.println(order1);

            em.flush();
            System.out.println("==========flush==========");
            em.clear();
            System.out.println("==========clear==========");

            System.out.println(member.getId());
            System.out.println(order.getId());

            Order findOrder = em.find(Order.class, order.getId());

            System.out.println(findOrder);

            Long memberId = findOrder.getMemberId(); // 회원 식별자를 조회

            System.out.println(memberId);

            Member findMember = em.find(Member.class, memberId);// 객체 그래프 탐색 방식이 아닌 order 객체에 저장된 회원 식별자로 맴버를 조회함 -> bad.

            System.out.println(findMember);
            System.out.println(findMember.getId());
            System.out.println(findMember.getName());

            // 영속성화 되어 있는지 확인
            em.find(Order.class, order.getId());
            em.find(Member.class, memberId);
            System.out.println("==========select query run?==========");

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}