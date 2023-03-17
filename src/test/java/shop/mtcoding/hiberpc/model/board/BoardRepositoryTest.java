package shop.mtcoding.hiberpc.model.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.hiberpc.config.dummy.MyDummyEntity;
import shop.mtcoding.hiberpc.model.user.User;
import shop.mtcoding.hiberpc.model.user.UserRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({UserRepository.class, BoardRepository.class})
@DataJpaTest // DB관련된 것들 전부 다 띄운다. extends JpaRepository가 붙은 것들만. 그래서 정작 UserController 은 뜨지 않는다.
public class BoardRepositoryTest extends MyDummyEntity {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void save_test() {
        // given 1
        User user = newUser("ssar");
        User userPS = userRepository.save(user);

        // given 2
        Board board = newBoard("제목1", userPS);

        // when
        Board boardPS = boardRepository.save(board);
        System.out.println("테스트 : " + boardPS);
        // then
        assertThat(boardPS.getId()).isEqualTo(1);
        assertThat(boardPS.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void update_test() {
        // given 1 -- DB에 영속함
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

        // given 2 -- request 데이터
        String title = "제목수정";
        String content = "내용수정";

        // when
        boardPS.update(title, content);
        em.flush();

        Board findBoardPS = boardRepository.findById(1);

        // then
        assertThat(findBoardPS.getId()).isEqualTo(1);
        assertThat(findBoardPS.getTitle()).isEqualTo("제목수정");
        assertThat(findBoardPS.getContent()).isEqualTo("내용수정");

    }

    @Test
    public void delete_test() {
        // given 1 -- DB에 영속함
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        Board boardPS = boardRepository.save(board);

        // given 2 -- request 데이터
        int id = 1;
        Board findBoardPS = boardRepository.findById(id);


        // when
        boardRepository.delete(findBoardPS);

        // then
        Board deleteBoardPS = boardRepository.findById(id);
        Assertions.assertThat(deleteBoardPS).isNull();

    }

    @Test
    public void findById_Test() {
        // given 1 -- DB에 영속함
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        Board board = newBoard("제목1", userPS);
        boardRepository.save(board);

        // given 2
        int id = 1;

        // when
        Board findBoardPS = boardRepository.findById(id);

        // then
        assertThat(findBoardPS.getUser().getUsername()).isEqualTo("ssar");
        assertThat(findBoardPS.getTitle()).isEqualTo("제목1");
    }

    @Test
    public void findAll_Test() {
        // given
        User user = newUser("ssar");
        User userPS = userRepository.save(user);
        List<Board> boardList = Arrays.asList(newBoard("제목1", userPS), newBoard("제목2", userPS));
        boardList.stream().forEach((board)->{
            boardRepository.save(board);
        });

        // when
        List<Board> boardListPS = boardRepository.findAll();
//        System.out.println("테스트 : " + boardListPS);
        assertThat(boardListPS.size()).isEqualTo(2);
    }
}
