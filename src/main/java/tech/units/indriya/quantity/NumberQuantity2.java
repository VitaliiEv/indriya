package tech.units.indriya.quantity;

import static javax.measure.Quantity.Scale.ABSOLUTE;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnitConverter;

import tech.units.indriya.AbstractQuantity;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.internal.calc.Calculator;

public class NumberQuantity2<Q extends Quantity<Q>> extends AbstractQuantity<Q> {

    private static final long serialVersionUID = 1L;

    private final Number value;

    /**
     * @since 2.0
     */
    protected NumberQuantity2(Number number, Unit<Q> unit, Scale sc) {
      super(unit, sc);
      value = number;
    }
    
    protected NumberQuantity2(Number number, Unit<Q> unit) {
        this(number, unit, ABSOLUTE); 
    }

    @Override
    public ComparableQuantity<Q> add(Quantity<Q> that) {
        return addition(that, (thisValueInSystemUnit, thatValueInSystemUnit) -> 
            Calculator
                .loadDefault(thisValueInSystemUnit)
                .add(thatValueInSystemUnit)
                .peek());
    }

    @Override
    public ComparableQuantity<Q> subtract(Quantity<Q> that) {
        return addition(that, (thisValueInSystemUnit, thatValueInSystemUnit) -> 
        Calculator
            .loadDefault(thisValueInSystemUnit)
            .subtract(thatValueInSystemUnit)
            .peek());
    }

    @Override
    public ComparableQuantity<?> divide(Quantity<?> that) {
        final Number resultValueInThisUnit = Calculator
                .loadDefault(getValue())
                .divide(that.getValue())
                .peek();
        return Quantities.getQuantity(resultValueInThisUnit, getUnit().multiply(that.getUnit()));
    }

    @Override
    public ComparableQuantity<Q> divide(Number divisor) {
        final Number resultValueInThisUnit = Calculator
                .loadDefault(getValue())
                .divide(divisor)
                .peek();
        return Quantities.getQuantity(resultValueInThisUnit, getUnit());
    }

    @Override
    public ComparableQuantity<?> multiply(Quantity<?> that) {
        final Number resultValueInThisUnit = Calculator
                .loadDefault(getValue())
                .multiply(that.getValue())
                .peek();
        return Quantities.getQuantity(resultValueInThisUnit, getUnit().multiply(that.getUnit()));
    }

    @Override
    public ComparableQuantity<Q> multiply(Number multiplier) {
        final Number resultValueInThisUnit = Calculator
                .loadDefault(getValue())
                .multiply(multiplier)
                .peek();
        return Quantities.getQuantity(resultValueInThisUnit, getUnit());
    }

    @Override
    public ComparableQuantity<?> inverse() {
        final Number resultValueInThisUnit = Calculator
                .loadDefault(getValue())
                .reciprocal()
                .peek();
        return Quantities.getQuantity(resultValueInThisUnit, getUnit().inverse());
    }

    @Override
    public Quantity<Q> negate() {
        final Number resultValueInThisUnit = Calculator
                .loadDefault(getValue())
                .negate()
                .peek();
        return Quantities.getQuantity(resultValueInThisUnit, getUnit());
    }

    @Override
    public Number getValue() {
        return value;
    }
    
    // -- HELPER
    
    private ComparableQuantity<Q> addition(Quantity<Q> that, BinaryOperator<Number> operator) {
        
        final Unit<Q> systemUnit = getUnit().getSystemUnit();
        final UnitConverter c1 = this.getUnit().getConverterTo(systemUnit);
        final UnitConverter c2 = that.getUnit().getConverterTo(systemUnit);
        
        final Number thisValueInSystemUnit = c1.convert(this.getValue()); 
        final Number thatValueInSystemUnit = c2.convert(that.getValue()); 
        
        final Number resultValueInSystemUnit = 
                operator.apply(thisValueInSystemUnit, thatValueInSystemUnit);

        final Number resultValueInThisUnit = c1.inverse().convert(resultValueInSystemUnit);

        //TODO[220] scale not handled at all !!!
        return Quantities.getQuantity(resultValueInThisUnit, getUnit());
    }
    
    // -- DEPRECATIONS

    @Override
    public boolean isBig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigDecimal decimalValue(Unit<Q> aUnit) throws ArithmeticException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double doubleValue(Unit<Q> aUnit) throws ArithmeticException {
        throw new UnsupportedOperationException();
    }

}
