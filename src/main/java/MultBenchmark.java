/*
 * Copyright (c) 2013, Timothy Buktu
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;

import java.math.BigInteger;

/**
 * Benchmark for {@link BigInteger#multiply(BigInteger)} using different input sizes.
 */
public class MultBenchmark {
    private static int POW10_MIN = 1;   // start with 10^1-digit numbers
    private static int POW10_MAX = 7;   // go up to 10^7 digits
    private static long MIN_BENCH_DURATION = 2000000000;   // in nanoseconds

    /**
     * @param args ignored
     */
    public static void main(String[] args) {
        for (int i=POW10_MIN; i<=POW10_MAX; i++) {
            doBench(10, i);
            doBench(25, i);
            doBench(50, i);
            doBench(75, i);
        }
    }

    /**
     * Multiplies numbers of length <code>mag/10 * 10<sup>pow10</sup></code>.
     * @param mag 25 for <code>2.5*10<sup>pow10</sup></code>, 50 for <code>5*10<sup>pow10</sup></code>, etc.
     * @param pow10
     */
    private static void doBench(int mag, int pow10) {
        int numDecimalDigits = TEN.pow(pow10).intValue() * mag / 10;
        BigInteger a = BigInteger.valueOf(5).pow(numDecimalDigits-1).shiftLeft(numDecimalDigits-1);   // 10^(numDecimalDigits-1)
        BigInteger b = a.add(ONE);

        System.out.print("Warming up... ");
        int numIterations = 0;
        long tStart = System.nanoTime();
        do {
            a.multiply(b);
            numIterations++;
        } while (System.nanoTime()-tStart < MIN_BENCH_DURATION);

        System.out.print("Benchmarking " + mag/10.0 + "E" + pow10 + " digits... ");
        a = new BigInteger(a.toByteArray());
        b = new BigInteger(b.toByteArray());
        tStart = System.nanoTime();
        for (int i=0; i<numIterations; i++)
            a.multiply(b);
        long tEnd = System.nanoTime();
        long tNano = (tEnd-tStart+(numIterations+1)/2) / numIterations;   // in nanoseconds
        double tMilli = tNano / 1000000.0;   // in milliseconds
        System.out.printf("Time per mult: %12.5fms", tMilli);
        System.out.println();
    }
}