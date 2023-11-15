package lotto.domain.lotto;

import lotto.constants.Winning;
import lotto.domain.lotto.strategy.GenerateStrategy;
import lotto.dto.Summary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lottos {

    private static final int PRICE_PER_TICKET = 1000;

    private static final String PURCHASE_ERROR_MESSAGE = String.format("로또를 구매할 금액이 부족합니다. 로또 한장의 가격은 %s원 입니다.", PRICE_PER_TICKET);


    private final List<Lotto> lottos;
    private final long purchasePrice;

    public Lottos(List<Lotto> lottos, long purchasePrice) {
        this.lottos = new ArrayList<>(lottos);
        this.purchasePrice = purchasePrice;
    }

    public static Lottos of(int purchasePrice, GenerateStrategy strategy) {
        int lottoCount = lottoCount(purchasePrice);

        List<Lotto> lottoList = new ArrayList<>();
        for (int index = 0; index < lottoCount; index++) {
            lottoList.add(Lotto.of(LottoNumbers.of(strategy.generate())));
        }

        return new Lottos(lottoList, purchasePrice);
    }

    private static int lottoCount(int purchasePrice) {
        int lottoCount = purchasePrice / PRICE_PER_TICKET;
        validation(lottoCount);
        return lottoCount;
    }

    private static void validation(int lottoCount) {
        if (noPurchase(lottoCount)) {
            throw new IllegalArgumentException(PURCHASE_ERROR_MESSAGE);
        }
    }

    private static boolean noPurchase(int lottoCount) {
        return lottoCount == 0;
    }

    public Summary match(Lotto jackpot) {
        List<Winning> winnings = new ArrayList<>();

        for (Lotto lotto : lottos) {
            Winning winning = jackpot.match(lotto);
            winnings.add(winning);
        }

        return winningResult(winnings);
    }

    public Summary winningResult(List<Winning> winnings) {

        return new Summary(
                winningCount(winnings, Winning.FIRST),
                winningCount(winnings, Winning.SECOND),
                winningCount(winnings, Winning.THIRD),
                winningCount(winnings, Winning.FOURTH),
                profitRate(winnings));
    }

    private float profitRate(List<Winning> winnings) {
        return (float) prizeTotal(winnings) / (float) purchasePrice;
    }

    private long winningCount(List<Winning> winnings, Winning winning) {
        return winnings.stream()
                .filter(winning::equals)
                .count();
    }

    private long prizeTotal(List<Winning> winnings) {
        return winnings.stream()
                .mapToLong(Winning::prize)
                .sum();
    }

    public int size() {
        return lottos.size();
    }

    public List<Lotto> lottos() {
        return Collections.unmodifiableList(lottos);
    }
}
