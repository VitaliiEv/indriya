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
package tech.units.indriya.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Random;

import javax.measure.UnitConverter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tech.units.indriya.NumberAssertions;
import tech.units.indriya.function.ConverterTypeUtil.ConverterType;

@DisplayName("Testing Composition of UnitConverters")
class CompositionEquivalenceTest {

  private Random random = new Random(0); // seed = 0, to make tests reproducible
  private final static int RANDOM_VALUES_REPEAT_COUNT = 100;

  /**
   * Just to ensure ConverterType enum entry count and ConverterType.typeCount are consistent. 
   * This is a precondition to all the other tests within this class.
   */
  @Test
  @DisplayName("Setup for tests should include all converter types.")
  public void setupForTestShouldIncludeAllTypes() throws Exception {
    assertEquals(ConverterType.values().length, ConverterType.typeCount);
  }

  @Nested
  @DisplayName("Any converter type should ...")
  @ExtendWith(ConverterTypeUtil.ConverterTypesForTests.class)
  public class ConverterTypeTests {


    /**
     * We cycle through all {@code ConverterType}s and for each such type test, whether 
     * the identity transformation (as expressed by the concrete sub-class implementing 
     * AbstractConverter) does actually behave like an identity transformation.
     */
    @RepeatedTest(
        value = ConverterType.typeCount, 
        name = "{currentRepetition} of {totalRepetitions} candidates")
    @DisplayName("(if has identity) provide identity")
    public void testIdentityByConstruction(ConverterType c0) {

      String msg = String.format("testing %s", c0);

      if(c0.hasIdentity()) {

        final UnitConverter _I;

        try {
          _I = c0.getIdentity(); // get identity by construction
        } catch (Exception e) {
          fail(msg+": "+e.getMessage());
          return;
        }

        assertTrue(_I.isIdentity(), msg);
        assertTrue(_I.isLinear(), msg);  // identity must always be linear
        assertTrue(_I.concatenate(_I).isIdentity(), msg);

        assertIdentityCalculus(_I, RANDOM_VALUES_REPEAT_COUNT);
      }

    }		

  }

  @Nested
  @DisplayName("Any converter should ...")
  @ExtendWith(ConverterTypeUtil.UnitConverterForCompositionTests.class)
  public class CompositionTests {

    /**
     * let
     * 
     * <ul>
     * <li>o ... transformation composition</li>
     * <li>f^-1 ... inverse transformation of f</li>
     * <li>𝟙  ... identity</li>
     * <li>=== ... equivalence relation</li>
     * </ul>
     * 
     * We cycle through all {@code ConverterType}s and take their concrete transformation
     * examples. For each such example transformation we test, whether
     * 
     * <ul>
     * <li>a o a^-1 === 𝟙 </li>
     * <li>a o b === b o a , given a==b (commute with itself)</li>
     * <li>a o 𝟙 === 𝟙 o a </li>
     * </ul>
     */
    @RepeatedTest(
        value = ConverterType.candidateCount, 
        name = "{currentRepetition} of {totalRepetitions} candidates")
    @DisplayName("compose with inverse to identity, commute with itself and with identity")
    public void testIdentityByComposition(UnitConverter u0) {

      String msg = String.format("testing %s", u0);

      UnitConverter _I = identityOf(u0); // get identity by composition

      assertTrue(_I.isIdentity(), msg);
      assertTrue(_I.isLinear(), msg);  // identity must always be linear
      assertTrue(_I.concatenate(_I).isIdentity(), msg);

      assertTrue(commutes(u0, u0), msg);
      assertTrue(commutes(u0, _I), msg);
      assertTrue(commutes(_I, u0), msg);
    }

    /**
     * We cycle through all {@code ConverterType}s and take their concrete transformation
     * examples. For each such example transformation we test, whether the composition
     * a o a^-1 is equivalent to the identity transformation with respect to calculus. 
     */
    @RepeatedTest(
        value = ConverterType.candidateCount, 
        name = "{currentRepetition} of {totalRepetitions} candidates")
    @DisplayName("(if identity) calculate like identity")
    public void testIdentityCalculus(UnitConverter u0) {
      UnitConverter _I = identityOf(u0);
      assertIdentityCalculus(_I, RANDOM_VALUES_REPEAT_COUNT); 
    }

    /**
     * Given all concrete transformation examples as defined by the {@code ConverterType}, 
     * we cycle through all possible pairs {a, b} and test, whether 
     * a o b === b o a, for cases when both (a and b) are scaling (a.isLinear and b.isLinear).
     */
    @RepeatedTest(value = ConverterType.candidateCount * ConverterType.candidateCount)
    @DisplayName("(if scaling) commute with any other that is scaling")
    public void commuteWithScaling(UnitConverter u1, UnitConverter u2) {
      if(u1.isLinear() && u2.isLinear()) {
        assertTrue(commutes(u1, u2), String.format("testing %s %s", u1, u2));
        assertCommutingCalculus(u1, u2, RANDOM_VALUES_REPEAT_COUNT); 
      }
    }

  }

  @Test @DisplayName("(a o b) o (b^-1 o a) === a o a")
  public void equivalenceHappyCase() {

    AbstractConverter a = new AddConverter(3);
    AbstractConverter b = DoubleMultiplyConverter.of(2);

    AbstractConverter ab = (AbstractConverter) a.concatenate(b);
    AbstractConverter Ba = (AbstractConverter) b.inverse().concatenate(a);

    {
      // this demonstrates that (a o b) o (b^-1 o a) === a o a

      AbstractConverter left = (AbstractConverter) a.concatenate(a); 
      AbstractConverter right = (AbstractConverter) ab.concatenate(Ba);
      assertEquals(left, right);
    }

  }

  @Test @DisplayName("Add(3) ○ Mul(2) ○ Add(-7) === Mul(2) ○ Add(-1)") @Disabled
  public void equivalenceUnhappyCase() {

    AbstractConverter a = new AddConverter(3);
    AbstractConverter b = DoubleMultiplyConverter.of(2);
    AbstractConverter c = new AddConverter(-7);

    {        
      // this demonstrates, the limitations of the current implementation:
      // even though Add(3) ○ Mul(2) ○ Add(-7) === Mul(2) ○ Add(-1)
      // the test for equivalence fails
      AbstractConverter left = (AbstractConverter) a.concatenate(b).concatenate(c); 
      AbstractConverter right = (AbstractConverter) b.concatenate(new AddConverter(-1));
      assertEquals(left, right); // fails
    }

  }


  // -- HELPER

  private UnitConverter identityOf(UnitConverter a) {
    return a.concatenate(a.inverse()); // a.(a^-1) == identity
  }

  private boolean commutes(UnitConverter a, UnitConverter b) {
    // a.b == (b^-1).(a^-1), must always hold
    // a.b == (a^-1).(b^-1), only holds if a and b commute (a.b == b.a)
    UnitConverter ab = a.concatenate(b);
    UnitConverter ba = b.concatenate(a);

    boolean commutes = ab.concatenate(ba.inverse()).isIdentity();

    if(!commutes) {
      System.out.println("Does not resolve to identity, but should!");
      System.out.println("ab: "+ab);
      System.out.println("ba: "+ba);
      System.out.println("id: "+ab.concatenate(ba.inverse()));
      System.out.println();
    }

    return commutes;
  }

  private double nextRandomValue() {
    double randomRange = Math.pow(10., random.nextInt(65)-32); // [10^-32..10^32]
    double randomFactor = 2.*random.nextDouble()-1.; // [-1..1]
    double randomValue = randomFactor * randomRange;
    return randomValue;
  }

  private void assertIdentityCalculus(UnitConverter a, int repeating) {
    for(int i=0; i<repeating; ++i) {
      double randomValue = nextRandomValue();
      // double calculus
      assertEquals(randomValue, a.convert(randomValue), 1E-12, 
          String.format("testing %s: identity calculus failed for double value %f", 
              a, randomValue));
      // BigDecimal calculus
      BigDecimal bdRandomValue = BigDecimal.valueOf(randomValue);
      // we assume a.convert(BigDecimal) returns BigDecimal, but this is not a strict requirement
      assertEquals(0, bdRandomValue.compareTo((BigDecimal) a.convert(bdRandomValue)), 
          String.format("testing %s: identity calculus failed for double value %f "
              + "using BigDecimal", 
              a, randomValue));
    }
  }

  private void assertCommutingCalculus(UnitConverter a, UnitConverter b, int repeating) {

    UnitConverter ab = a.concatenate(b);
    UnitConverter ba = b.concatenate(a);

    for(int i=0; i<repeating; ++i) {
      double randomValue = nextRandomValue();

      // double calculus
      assertEquals(ab.convert(randomValue), ba.convert(randomValue), 1E-12, 
          String.format("testing %s: commuting calculus failed for double value %f", 
              a, randomValue));
      // BigDecimal calculus
      BigDecimal bdRandomValue = BigDecimal.valueOf(randomValue);

      // we assume AbstractConverter.convert(BigDecimal) returns BigDecimal, 
      // but this is not a strict requirement
      Number abValue = ab.convert(bdRandomValue);
      Number baValue = ba.convert(bdRandomValue);

      NumberAssertions.assertNumberEquals(abValue, baValue, 1E-12);

    }
  }



}
