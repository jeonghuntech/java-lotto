package lotto.domain;

import lotto.domain.lotto.Lottos;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class LottosTest {
    @Test
    @DisplayName("금액 입력 받아서 로또를 생성한다. 가격만큼의 로또가 생성된다. 로또 구매금액이 부족할 시 에러가 발생한다")
    public void create() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);

        Assertions.assertThat(
                Lottos.from(10000, () -> list).size()
        ).isEqualTo(10);

        Assertions.assertThatThrownBy(
                () -> Lottos.from(900, () -> list).size()
        ).isInstanceOf(IllegalArgumentException.class);
    }

}