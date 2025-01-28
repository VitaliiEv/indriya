/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2025, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-385, Indriya nor the names of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tech.units.indriya.format;

import static javax.measure.BinaryPrefix.KIBI;
import static javax.measure.BinaryPrefix.TEBI;
import static javax.measure.MetricPrefix.CENTI;
import static javax.measure.MetricPrefix.GIGA;
import static javax.measure.MetricPrefix.KILO;
import static javax.measure.MetricPrefix.MEGA;
import static javax.measure.MetricPrefix.MICRO;
import static javax.measure.MetricPrefix.MILLI;
import static javax.measure.MetricPrefix.NANO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static tech.units.indriya.format.SimpleUnitFormat.Flavor.ASCII;
import static tech.units.indriya.unit.Units.CANDELA;
import static tech.units.indriya.unit.Units.GRAM;
import static tech.units.indriya.unit.Units.HERTZ;
import static tech.units.indriya.unit.Units.KILOGRAM;
import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.METRE_PER_SECOND;
import static tech.units.indriya.unit.Units.NEWTON;
import static tech.units.indriya.unit.Units.OHM;
import static tech.units.indriya.unit.Units.DAY;
import static tech.units.indriya.unit.Units.WEEK;
import static tech.units.indriya.unit.Units.MONTH;
import static tech.units.indriya.unit.Units.YEAR;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;
import javax.measure.format.UnitFormat;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Length;
import javax.measure.quantity.LuminousIntensity;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Speed;
import javax.measure.spi.ServiceProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import tech.units.indriya.function.ExpConverter;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.function.RationalNumber;
import tech.units.indriya.unit.AlternateUnit;
import tech.units.indriya.unit.ProductUnit;
import tech.units.indriya.unit.TransformedUnit;
import tech.units.indriya.unit.Units;

/**
 * @author <a href="mailto:werner@units.tech">Werner Keil</a>
 *
 */
public class SimpleUnitFormatTest {
    private static final Logger logger = Logger.getLogger(SimpleUnitFormatTest.class.getName());
    private static final Level LOG_LEVEL = Level.FINER;
    private UnitFormat format;

    @BeforeEach
    public void init() {
        format = SimpleUnitFormat.getInstance();
    }

    @Test
    public void testFormat2() {
        Unit<Speed> kph = Units.KILOMETRE_PER_HOUR;
        String s = format.format(kph);
        assertEquals("km/h", s);
    }

    @Test
    public void testKilo() {
        Unit<Mass> m = KILOGRAM;
        String s = format.format(m);
        assertEquals("kg", s);
    }

    @Test
    public void testKilo2() {
        Unit<Mass> m = KILO(GRAM);
        final String s = format.format(m);
        assertEquals("kg", s);
    }

    @Test
    public void testFormatMicro() {
      final String s = format.format(MICRO(METRE));
      assertEquals("μm", s);
    }
    
    @Test
    public void testFormatMicroGram() {
      final String s = format.format(MICRO(GRAM));
      assertEquals("µg", s);
    }
    
    @Test
    public void testFormatMicroNewton() {
        final String s = format.format(MICRO(NEWTON));
        assertEquals("μN", s);
    }
    
    @Test
    public void testMilli() {
        Unit<Mass> m = MILLI(GRAM);
        String s = format.format(m);
        assertEquals("mg", s);
    }

    @Test
    public void testFormatNano() {
        Unit<Mass> m = NANO(GRAM);
        String s = format.format(m);
        assertEquals("ng", s);
    }
    
    @Test
    public void testFormatKibi() {
        Unit<Mass> m = KIBI(GRAM);
        String s = format.format(m);
        assertEquals("Kig", s);
    }

    @Test
    public void testFormatHz2() {
        Unit<Frequency> hz = MEGA(HERTZ);
        String s = format.format(hz);
        assertEquals("MHz", s);
    }

    @Test
    public void testFormatHz3() {
        Unit<Frequency> hz = KILO(HERTZ);
        String s = format.format(hz);
        assertEquals("kHz", s);
    }
    
    @Test
    public void testFormatHz4() {
        Unit<Frequency> hz = TEBI(HERTZ);
        String s = format.format(hz);
        assertEquals("TiHz", s);
    }

    @Test
    public void testFormatOhm() {
        final String s = format.format(OHM);
        assertEquals("Ω", s);
    }
    
    @Test
    public void testFormatMicroOhm() {
        final String s = format.format(MICRO(OHM));
        assertEquals("μΩ", s);
    }
    
    @Test
    public void testFormatMilliOhm() {
        final String s = format.format(MILLI(OHM));
        assertEquals("mΩ", s);
    }
    
    @Test
    public void testFormatTransformed() {
        final String ANGSTROEM_SYM = "\u212B";
        final Unit<Length> ANGSTROEM = new TransformedUnit<Length>(ANGSTROEM_SYM, METRE, METRE,
                MultiplyConverter.ofRational(RationalNumber.of(BigInteger.ONE, BigInteger.TEN.pow(10))));
        final String s = format.format(ANGSTROEM);
        assertEquals(ANGSTROEM_SYM, s);
    }

    @Test
    public void testParseHertz() {
        assertThrows(MeasurementParseException.class, () -> {
            ServiceProvider.current().getFormatService().getUnitFormat().parse("1/s");
        });

        Unit<?> onePerSecond = ServiceProvider.current().getFormatService().getUnitFormat().parse("one/s");
        logger.log(LOG_LEVEL, onePerSecond.toString());
    }
    
    @Test
    public void testParsePowerAndRoot() {
      assertEquals("1/m^19:31", format.format(format.parse("m^12:31").divide(METRE)));
    }    

    
    
    @Test
    public void testFormatNewLabeledUnits() {
        logger.log(LOG_LEVEL, "== Use case 1: playing with base units ==");
        final AlternateUnit<?> aCd = new AlternateUnit<>(CANDELA, "altCD");

        logger.log(LOG_LEVEL, "Candela: " + format.format(CANDELA));
        logger.log(LOG_LEVEL, "Candela times 2: " + format.format(CANDELA.multiply(2)));
        logger.log(LOG_LEVEL, "Square Candela: " + format.format(CANDELA.pow(2)));
        logger.log(LOG_LEVEL, "Alt. Candela: " + format.format(aCd));
        logger.log(LOG_LEVEL, "Square alt. Candela: " + format.format(aCd.pow(2)));

        logger.log(LOG_LEVEL, "=> The Candela shall now be known as \"CD\"");
        format.label(CANDELA, "CD");

        logger.log(LOG_LEVEL, "Candela: " + format.format(CANDELA));
        logger.log(LOG_LEVEL, "Candela times 2: " + format.format(CANDELA.multiply(2)));
        logger.log(LOG_LEVEL, "Square Candela: " + format.format(CANDELA.pow(2)));
        logger.log(LOG_LEVEL, "Alt. Candela: " + format.format(aCd));
        logger.log(LOG_LEVEL, "Square alt. Candela: " + format.format(aCd.pow(2)));

        logger.log(LOG_LEVEL, "== Use case 2: playing with product units ==");
        final ProductUnit<?> cdK = new ProductUnit<>(CANDELA.multiply(Units.KELVIN));
        final AlternateUnit<?> aCdK = new AlternateUnit<>(cdK, "altCDK");

        logger.log(LOG_LEVEL, "Candela-Kelvin: " + format.format(cdK));
        logger.log(LOG_LEVEL, "Candela-Kelvin times 2: " + format.format(cdK.multiply(2)));
        logger.log(LOG_LEVEL, "Square Candela-Kelvin: " + format.format(cdK.pow(2)));
        logger.log(LOG_LEVEL, "Alt. Candela-Kelvin: " + format.format(aCdK));
        logger.log(LOG_LEVEL, "Square alt. Candela-Kelvin: " + format.format(aCdK.pow(2)));

        logger.log(LOG_LEVEL, "=> The Candela-Kelvin shall now be known as \"CDK\"");
        format.label(cdK, "CDK");

        logger.log(LOG_LEVEL, "Candela-Kelvin: " + format.format(cdK));
        logger.log(LOG_LEVEL, "Candela-Kelvin times 2: " + format.format(cdK.multiply(2)));
        logger.log(LOG_LEVEL, "Square Candela-Kelvin: " + format.format(cdK.pow(2)));
        logger.log(LOG_LEVEL, "Alt. Candela-Kelvin: " + format.format(aCdK));
        logger.log(LOG_LEVEL, "Square alt. Candela-Kelvin: " + format.format(aCdK.pow(2)));

        final UnitFormat formatter2 = EBNFUnitFormat.getInstance();
        final Unit<LuminousIntensity> cdX = CANDELA.transform(new ExpConverter(10));
        logger.log(LOG_LEVEL, "Candela-Exp: " + format.format(cdX));
        logger.log(LOG_LEVEL, "Candela-Exp E: " + formatter2.format(cdX));
    
        format.label(CANDELA, "cd"); // cleanup, UnitFormat.label() applies for the entire VM, and the order of JUnit tests is not guaranteed
    }

    @Test
    @Disabled("SimpleUnitFormat cannot deal with expressions that start with 1 at this point")
    public void testParseInverseL() {
        Unit<?> u = format.parse("1/l");
        assertEquals("1/l", u.toString());
    }
    
    @Test
    public void testParseM3() {
        Unit<?> u = SimpleUnitFormat.getInstance(ASCII).parse("m3");
        assertEquals(Units.CUBIC_METRE, u);
    }
    
    @Test
    public void testParseM3Alias() {
        Unit<?> u = SimpleUnitFormat.getInstance().parse("m3");
        assertEquals(Units.CUBIC_METRE, u);
    }
    
    @Test
    public void testParseMicro() {
      Unit<?> u = format.parse("μm");
      assertEquals(MICRO(METRE), u);
    }
    
    @Test
    public void testParseMicroAlias() {
      Unit<?> u = format.parse("\u03bcm");
      assertEquals(MICRO(METRE), u);
    }
    
    @Test
    public void testParseMicro2() {
      Unit<?> u = format.parse("μg");
      assertEquals(MICRO(GRAM), u);
    }
    
    @Test
    public void testParseMicro3() {
      Unit<?> u = format.parse("μN");
      assertEquals(MICRO(NEWTON), u);
    }
    
    @Test
    public void testParseNano() {
      Unit<?> u = format.parse("ng");
      assertEquals(NANO(GRAM), u);
    }
    
	@Test
	public void testPrefix() {
		logger.log(LOG_LEVEL, format.format(GIGA(METRE_PER_SECOND))); 
		assertEquals("N", format.format(NEWTON));
		assertEquals("mN", format.format(MILLI(NEWTON)));
	}
	
	@Test
	public void testFormatDay() {
		logger.log(LOG_LEVEL, format.format(DAY)); 
		assertEquals("d", format.format(DAY));
		
		assertEquals("cd", format.format(CENTI(DAY)));
	}
	
	@Test
	public void testFormatWeek() {
		logger.log(LOG_LEVEL, format.format(WEEK)); 
		assertEquals("wk", format.format(WEEK));
	}
	
	@Test
	public void testFormatYear() {
		logger.log(LOG_LEVEL, format.format(YEAR)); 
		assertEquals("yr", format.format(YEAR));
	}
	
	@Test
	public void testFormatMonth() {
		logger.log(LOG_LEVEL, format.format(MONTH)); 
		assertEquals("mo", format.format(MONTH));
	}
	
	@Test
	public void testParseDay() {
		logger.log(LOG_LEVEL, format.format(DAY));
		assertEquals(DAY, format.parse("d"));
		assertEquals(DAY, format.parse("day"));
		// CENTI(DAY) is NOT symmetric, because "cd" overlaps with CANDELA, hence it prints "cd" but won't parse, 
		// because we cannot parse with more than one outcome, see https://github.com/unitsofmeasurement/indriya/issues/433
		assertNotEquals(CENTI(DAY), format.parse("cd"));
		// Instead we have to use the alias "cday" for parsing.
		assertEquals(CENTI(DAY), format.parse("cday"));
	}
	
	@Test
	public void testParseWeek() {
		logger.log(LOG_LEVEL, format.format(WEEK)); 
		assertEquals(WEEK, format.parse("week"));
		assertEquals(WEEK, format.parse("wk"));
	}
	
	@Test
	public void testParseYear() {
		logger.log(LOG_LEVEL, format.format(YEAR)); 
		assertEquals(YEAR, format.parse("yr"));
		assertEquals(YEAR, format.parse("y"));
		assertEquals(YEAR, format.parse("year"));
	}
	
	@Test
	public void testParseMonth() {
		logger.log(LOG_LEVEL, format.format(MONTH)); 
		assertEquals(MONTH, format.parse("month"));
		assertEquals(MONTH, format.parse("mo"));
	}
	
	@Test
	public void testParseCandela() {
		//assertEquals(CANDELA, format.parse("cd"));
		final UnitFormat newFormat = SimpleUnitFormat.getNewInstance();
		assertEquals(CANDELA, newFormat.parse("cd"));
	}
	
	@Test
	public void testFormatNewInstance() {
		logger.log(LOG_LEVEL, "Candela (shared format): " + format.format(CANDELA)); 
		assertEquals("cd", format.format(CANDELA));
		
		logger.log(LOG_LEVEL, "Now we retrieve a new format instance");
		final UnitFormat newFormat = SimpleUnitFormat.getNewInstance();
		newFormat.label(CANDELA, "kd");
		// The shared format and toString() based on it are unaffected
		assertEquals("cd", format.format(CANDELA));		
		assertEquals("cd", CANDELA.toString());
		// Only the new format got the label
		assertEquals("kd", newFormat.format(CANDELA));
	}
	
	@Test
	public void testAddAndRemoveLabel() {		
		logger.log(LOG_LEVEL, "We retrieve a new format instance");
		final SimpleUnitFormat newFormat = SimpleUnitFormat.getNewInstance();
		newFormat.label(CANDELA, "kd");
		newFormat.alias(CANDELA, "can");
		// Other instances are unaffected
		assertEquals("cd", format.format(CANDELA));		
		assertEquals("cd", CANDELA.toString());
		// Only the new format got the label
		assertEquals("kd", newFormat.format(CANDELA));
		assertNotNull(newFormat.parse("kd"));
		assertNotNull(newFormat.parse("can"));
				
		logger.log(LOG_LEVEL, "Now we remove the label");
		newFormat.removeLabel(CANDELA);
		assertEquals(CANDELA.toString(), newFormat.format(CANDELA));
		assertThrows(MeasurementParseException.class, () -> {
			assertNull(newFormat.parse("kd"));
		});
		assertThrows(MeasurementParseException.class, () -> {
			assertNull(newFormat.parse("can"));
		});
	}
	
	@Test
	public void testRepeatingLabels() {
		logger.log(LOG_LEVEL, "We set a label");
		format.label(CANDELA, "kd");		
		assertEquals("kd", format.format(CANDELA));		
		assertEquals("kd", CANDELA.toString());
		assertEquals("kd", format.format(CANDELA));
		assertNotNull(format.parse("kd"));
				
		logger.log(LOG_LEVEL, "Now we set another label");
		format.label(CANDELA, "can");
		assertEquals("can", format.format(CANDELA));
		assertEquals("can", CANDELA.toString());
		assertNotNull(format.parse("can"));
		// The old one still remains like an alias
		assertNotNull(format.parse("kd"));
	}
	
	@Test
	public void testAddAndRemoveAlias() {		
		logger.log(LOG_LEVEL, "We retrieve a new format instance");
		final SimpleUnitFormat newFormat = SimpleUnitFormat.getNewInstance();
		newFormat.alias(CANDELA, "can");
		newFormat.alias(CANDELA, "cn");
		assertNotNull(newFormat.parse("can"));
		assertNotNull(newFormat.parse("cn"));
		
		logger.log(LOG_LEVEL, "Now we remove one alias");
		newFormat.removeAlias(CANDELA, "can");
		assertThrows(MeasurementParseException.class, () -> {
			assertNull(newFormat.parse("can"));
		});
		assertNotNull(newFormat.parse("cn"));		
	}
	
	@Test
	public void testAddAndRemoveAliases() {		
		logger.log(LOG_LEVEL, "We retrieve a new format instance");
		final SimpleUnitFormat newFormat = SimpleUnitFormat.getNewInstance();
		newFormat.alias(CANDELA, "can");
		newFormat.alias(CANDELA, "cn");
		assertNotNull(newFormat.parse("can"));
		assertNotNull(newFormat.parse("cn"));
		
		logger.log(LOG_LEVEL, "Now we remove the aliases");
		newFormat.removeAliases(CANDELA);
		assertThrows(MeasurementParseException.class, () -> {
			assertNull(newFormat.parse("can"));
		});
		newFormat.removeAliases(CANDELA);
		assertThrows(MeasurementParseException.class, () -> {
			assertNull(newFormat.parse("cn"));
		});
	}
}
