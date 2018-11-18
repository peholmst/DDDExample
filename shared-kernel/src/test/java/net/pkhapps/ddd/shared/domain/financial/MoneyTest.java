package net.pkhapps.ddd.shared.domain.financial;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Money}.
 */
public class MoneyTest {

    @Test
    public void createFromInteger() {
        var money = new Money(Currency.EUR, 12500);
        assertThat(money.currency()).isEqualTo(Currency.EUR);
        assertThat(money.fixedPointAmount()).isEqualTo(12500);
        assertThat(money.doubleAmount()).isEqualTo(125.00);
    }

    @Test
    public void createFromDouble() {
        var money = new Money(Currency.SEK, 130.50);
        assertThat(money.currency()).isEqualTo(Currency.SEK);
        assertThat(money.fixedPointAmount()).isEqualTo(13050);
        assertThat(money.doubleAmount()).isEqualTo(130.50);
    }

    @Test
    public void valueOf_nullCurrency_nullReturned() {
        assertThat(Money.valueOf(null, 123)).isNull();
    }

    @Test
    public void valueOf_nullValue_nullReturned() {
        assertThat(Money.valueOf(Currency.SEK, null)).isNull();
    }

    @Test
    public void valueOf_nonNull_moneyCreated() {
        assertThat(Money.valueOf(Currency.SEK, 12300)).isEqualTo(new Money(Currency.SEK, 123.00));
    }

    @Test
    public void add() {
        var m1 = new Money(Currency.SEK, 150.00);
        var m2 = new Money(Currency.SEK, 225.00);
        assertThat(m1.add(m2)).isEqualTo(new Money(Currency.SEK, 375.00));
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_differentCurrencies_exceptionThrown() {
        var m1 = new Money(Currency.SEK, 150.00);
        var m2 = new Money(Currency.EUR, 225.00);
        m1.add(m2);
    }

    @Test
    public void subtract() {
        var m1 = new Money(Currency.SEK, 300.50);
        var m2 = new Money(Currency.SEK, 200.50);
        assertThat(m1.subtract(m2)).isEqualTo(new Money(Currency.SEK, 100.00));
    }

    @Test(expected = IllegalArgumentException.class)
    public void subtract_differentCurrencies_exceptionThrown() {
        var m1 = new Money(Currency.SEK, 300.50);
        var m2 = new Money(Currency.EUR, 200.50);
        m1.subtract(m2);
    }

    @Test
    public void toString_zero() {
        assertThat(new Money(Currency.SEK, 0).toString()).isEqualTo("SEK 0.00");
    }

    @Test
    public void toString_noZero() {
        assertThat(new Money(Currency.EUR, 212550).toString()).isEqualTo("EUR 2125.50");
    }
}
