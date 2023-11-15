package lotto.domain.lotto;

import lotto.dto.Summary;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class LottosTest {
    @Test
    @DisplayName("입력한 금액만큼의 로또를 생성한다. 금액이 부족한 경우 예외가 발생한다")
    public void create() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);

        Assertions.assertThat(
                Lottos.of(10000, () -> list).size()
        ).isEqualTo(10);

        Assertions.assertThatThrownBy(
                () -> Lottos.of(900, () -> list).size()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("당첨 통계를 조회한다. 각 등수별 당첨수와 수익률을 반환한다")
    public void match() {
        Lottos lottos = Lottos.of(10000, () -> Arrays.asList(1, 2, 3, 4, 5, 6));
        Lotto jackpot = Lotto.of(LottoNumbers.of(Arrays.asList(1, 2, 3, 20, 30, 40)));
        Summary match = lottos.match(jackpot);

        Assertions.assertThat(match).isEqualTo(new Summary(0, 0, 0, 10, 5f));
    }
}