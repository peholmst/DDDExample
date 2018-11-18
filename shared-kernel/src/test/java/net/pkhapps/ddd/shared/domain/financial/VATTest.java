package net.pkhapps.ddd.shared.domain.financial;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link VAT}.
 */
public class VATTest {

    @Test
    public void valueOf_nullPercentage_nullReturned() {
        assertThat(VAT.valueOf(null)).isNull();
    }

    @Test
    public void valueOf_nonNullPercentage_vatCreated() {
        assertThat(VAT.valueOf(24)).isNotNull();
    }

    @Test
    public void addTax_noDecimals() {
        var withTax = new VAT(24).addTax(new Money(Currency.EUR, 100.00));
        assertThat(withTax).isEqualTo(new Money(Currency.EUR, 124.00));
    }

    @Test
    public void addTax_withDecimals() {
        var withTax = new VAT(24).addTax(new Money(Currency.EUR, 1.00));
        assertThat(withTax).isEqualTo(new Money(Currency.EUR, 1.24));
    }

    @Test
    public void subtractTax_noDecimals() {
        var withoutTax = new VAT(24).subtractTax(new Money(Currency.EUR, 124.00));
        assertThat(withoutTax).isEqualTo(new Money(Currency.EUR, 100.00));
    }

    @Test
    public void subtractTax_withDecimals() {
        var withoutTax = new VAT(24).subtractTax(new Money(Currency.EUR, 1.24));
        assertThat(withoutTax).isEqualTo(new Money(Currency.EUR, 1.00));
    }

    @Test
    public void toDouble_returnedAsFraction() {
        assertThat(new VAT(24).toDouble()).isEqualTo(0.24);
    }

    @Test
    public void toInteger_returnedAsPercent() {
        assertThat(new VAT(24).toInteger()).isEqualTo(24);
    }

    @Test
    public void toString_returnedAsFormattedString() {
        assertThat(new VAT(24).toString()).isEqualTo("24 %");
    }
}
