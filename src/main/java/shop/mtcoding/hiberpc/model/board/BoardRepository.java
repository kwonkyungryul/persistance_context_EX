package shop.mtcoding.hiberpc.model.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public Board findById(int id) {
        return em.find(Board.class, id);
    }

    public List<Board> findAll() {
        return em.createQuery("SELECT b FROM Board b", Board.class).getResultList();
    }

    public Board save(Board board) {
        if (board.getId() == null) {
            em.persist(board);
        } else { // 더티 체킹을 할 것이기 때문에 쓸 일이 없다.
            Board boardPS = em.find(Board.class, board.getId());
            if (boardPS != null) {
                em.merge(board);
            } else {
                System.out.println("잘못된 머지를 하셨습니다.");
            }
        }
        return board;
    }

    public void delete(Board board) {

        em.remove(board);
    }

}
