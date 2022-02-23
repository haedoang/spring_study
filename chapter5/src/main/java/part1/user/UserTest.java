package part1.user;

import org.junit.Before;
import org.junit.Test;
import part1.user.domain.Level;
import part1.user.domain.User;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * author : haedoang
 * date : 2022/02/24
 * description :
 */
public class UserTest {
    User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        // given
        Level[] levels = Level.values();

        // when
        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();

            // then
            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradedLevel() {
        // given
        Level[] levels = Level.values();

        // when
        for (Level level : levels) {
            if (level.nextLevel() != null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
