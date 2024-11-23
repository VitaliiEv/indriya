/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2024, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
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

import static javax.measure.BinaryPrefix.*;
import static javax.measure.MetricPrefix.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.units.indriya.NumberAssertions.assertNumberEquals;
import static tech.units.indriya.unit.Units.GRAM;
import static tech.units.indriya.unit.Units.KILOGRAM;
import static tech.units.indriya.unit.Units.LITRE;
import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.MINUTE;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.UnitFormat;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class EBNFPrefixTest {
	private static UnitFormat format;
	
	@BeforeAll
	static void init() {
		format = EBNFUnitFormat.getInstance();
	}
	
	// Metric Prefixes
	
	@Test
	public void testKilo() {
		assertEquals(format.format(KILOGRAM), format.format(KILO(GRAM)));
	}

	@Test
	public void testMega() {
		Quantity<Mass> m1 = Quantities.getQuantity(1.0, MEGA(Units.GRAM));
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("Mg", format.format(m1.getUnit()));
	}

	@Test
	public void testDeci() {
		Quantity<Volume> m1 = Quantities.getQuantity(1.0, LITRE);
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("l", format.format(m1.getUnit()));

		Quantity<Volume> m2 = m1.to(DECI(LITRE));
		assertNumberEquals(10, m2.getValue(), 1E-12);
		assertEquals("dl", format.format(m2.getUnit()));
	}

	@Test
	public void testMilli() {
		Quantity<Mass> m1 = Quantities.getQuantity(1.0, MILLI(Units.GRAM));
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("mg", format.format(m1.getUnit()));
	}

	@Test
	public void testMilli2() {
		Quantity<Volume> m1 = Quantities.getQuantity(10, MILLI(LITRE));
		assertNumberEquals(10, m1.getValue(), 1E-12);
		assertEquals("ml", format.format(m1.getUnit()));
	}

	@Test
	public void testMilli3() {
		Quantity<Volume> m1 = Quantities.getQuantity(1.0, LITRE);
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("l", format.format(m1.getUnit()));

		Quantity<Volume> m2 = m1.to(MILLI(LITRE));
		assertNumberEquals(1000L, m2.getValue(), 1E-12);
		assertEquals("ml", format.format(m2.getUnit()));
	}

	@Test
	public void testMilli4() {
		Quantity<Volume> m1 = Quantities.getQuantity(1.0, MILLI(LITRE));
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("ml", format.format(m1.getUnit()));

		Quantity<Volume> m2 = m1.to(LITRE);
		assertNumberEquals(0.001d, m2.getValue(), 1E-12);
		assertEquals("l", format.format(m2.getUnit()));
	}

	@Test
	public void testMicro() {
		Quantity<Length> m1 = Quantities.getQuantity(1.0, Units.METRE);
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("m", format.format(m1.getUnit()));

		final Quantity<Length> m2 = m1.to(MICRO(Units.METRE));
		assertNumberEquals(1000_000L, m2.getValue(), 1E-12);
		assertEquals("µm", format.format(m2.getUnit()));
	}

	@Test
	public void testNano() {
		Quantity<Mass> m1 = Quantities.getQuantity(1.0, Units.GRAM);
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("g", format.format(m1.getUnit()));

		final Quantity<Mass> m2 = m1.to(NANO(Units.GRAM));
		assertNumberEquals(1000_000_000L, m2.getValue(), 1E-12);
		assertEquals("ng", format.format(m2.getUnit()));
	}

	@Test
	public void testNano2() {
		Quantity<Length> m1 = Quantities.getQuantity(1.0, Units.METRE);
		assertNumberEquals(1d, m1.getValue(), 1E-12);
		assertEquals("m", format.format(m1.getUnit()));

		final Quantity<Length> m2 = m1.to(NANO(Units.METRE));
		assertNumberEquals(1000000000L, m2.getValue(), 1E-12);
		assertEquals("nm", format.format(m2.getUnit()));
	}

	@Test
	public void testQuetta() {
		assertEquals("Ql", format.format(QUETTA(LITRE)));	
	}
	
	@Test
	public void testRonna() {
		assertEquals("Rm", format.format(RONNA(METRE)));	
	}
	
	@Test
	public void testQuecto() {
		assertEquals("ql", format.format(QUECTO(LITRE)));	
	}
	
	@Test
	public void testRonto() {
		assertEquals("rg", format.format(RONTO(GRAM)));	
	}
	
	@Test
	public void testPArseMilli() {
		final Unit<?> m = format.parse("mg");
		assertEquals(m, MILLI(Units.GRAM));
	}
	
	@Test
	public void testParseRonna() {
		final Unit<?> l = format.parse("Rm");
		assertEquals(l, RONNA(METRE));	
	}
	
	@Test
	public void testParseQuecto() {
		final Unit<?> v = format.parse("ql");
		assertEquals(v, QUECTO(LITRE));	
	}
	
	
	@Test
	public void testQuettaMin() {
		assertEquals("Qmin", format.format(QUETTA(MINUTE)));	
	}
	
	@Test
	public void testHashMapAccessingMap() {
		assertThat(LITRE.toString(), is("l"));
		assertThat(MILLI(LITRE).toString(), is("ml"));
		assertThat(MILLI(GRAM).toString(), is("mg"));
	}

	// Binary Prefixes
	
	@Test
	public void testKibi() {
		final String s = format.format(KIBI(METRE));
		assertEquals("Kim", s);
	}
	
	@Test
	public void testKibiL() {
		final String s = format.format(KIBI(LITRE));
		assertEquals("Kil", s);
	}
	
	@Test
	public void testKibiG() {
		final String s = format.format(KIBI(GRAM));
		assertEquals("Kig", s);
	}

	@Test
	public void testMebi() {
		assertEquals("Mim", format.format(MEBI(METRE)));
	}

	@Test
	public void testGibi() {
		assertEquals("Gim", format.format(GIBI(METRE)));
	}

	@Test
	public void testTebi() {
		assertEquals("Til", format.format(TEBI(LITRE)));
	}

	@Test
	public void testPebi() {
		assertEquals("Pil", format.format(PEBI(LITRE)));
	}

	@Test
	public void testExbi() {
		assertEquals("Eig", format.format(EXBI(GRAM)));
	}

	@Test
	public void testZebi() {
		assertEquals("Zig", format.format(ZEBI(GRAM)));	
	}

	@Test
	public void testYobi() {
		assertEquals("Yig", format.format(YOBI(GRAM)));	
	}
	
	@Test
	public void testParseKibiL() {
		final Unit<?> v = format.parse("Kil");
		assertEquals(v, KIBI(LITRE));
	}
	
	@Test
	public void testParseMebi() {
		final Unit<?> l = format.parse("Mim");
		assertEquals(l, MEBI(METRE));
	}
}
