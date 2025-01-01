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

import java.util.function.Supplier;
import java.util.stream.Collector;

import javax.measure.MeasurementError;
import javax.measure.Quantity;
import javax.measure.Unit;

/**
 * Utility methods for operating on quantities using streams.
 * 
 * @author Otavio
 * @author Werner
 * @version 1.1
 * @since 2.0
 *
 */
public final class QuantityStreams {

  private QuantityStreams() {
	  throw new MeasurementError("no instances");
  }

  /**
   * Summary of Quantity
   * 
   * @return the QuantitySummaryStatistics
   */
  public static <Q extends Quantity<Q>> Collector<Quantity<Q>, QuantitySummaryStatistics<Q>, QuantitySummaryStatistics<Q>> summarizeQuantity(
			Unit<Q> unit) {
		Supplier<QuantitySummaryStatistics<Q>> supplier = () -> new QuantitySummaryStatistics<>(unit);
		return Collector.of(supplier, QuantitySummaryStatistics<Q>::accept, QuantitySummaryStatistics<Q>::combine);
	}
}
