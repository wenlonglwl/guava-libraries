/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.hash;

import static com.google.common.hash.Hashing.ConcatenatedHashFunction;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table.Cell;
import com.google.common.testing.NullPointerTester;
import com.google.common.util.concurrent.AtomicLongMap;

import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Unit tests for {@link Hashing}.
 *
 * @author Dimitris Andreou
 * @author Kurt Alfred Kluever
 */
public class HashingTest extends TestCase {
  public void testMd5() {
    HashTestUtils.checkAvalanche(Hashing.md5(), 100, 0.4);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.md5());
    HashTestUtils.checkNoFunnels(Hashing.md5());
    HashTestUtils.assertInvariants(Hashing.md5());
    assertEquals("Hashing.md5()", Hashing.md5().toString());
  }

  public void testSha1() {
    HashTestUtils.checkAvalanche(Hashing.sha1(), 100, 0.4);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.sha1());
    HashTestUtils.checkNoFunnels(Hashing.sha1());
    HashTestUtils.assertInvariants(Hashing.sha1());
    assertEquals("Hashing.sha1()", Hashing.sha1().toString());
  }

  public void testSha256() {
    HashTestUtils.checkAvalanche(Hashing.sha256(), 100, 0.4);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.sha256());
    HashTestUtils.checkNoFunnels(Hashing.sha256());
    HashTestUtils.assertInvariants(Hashing.sha256());
    assertEquals("Hashing.sha256()", Hashing.sha256().toString());
  }

  public void testSha512() {
    HashTestUtils.checkAvalanche(Hashing.sha512(), 100, 0.4);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.sha512());
    HashTestUtils.checkNoFunnels(Hashing.sha512());
    HashTestUtils.assertInvariants(Hashing.sha512());
    assertEquals("Hashing.sha512()", Hashing.sha512().toString());
  }

  public void testCrc32() {
    HashTestUtils.assertInvariants(Hashing.crc32());
    assertEquals("Hashing.crc32()", Hashing.crc32().toString());
  }

  public void testAdler32() {
    HashTestUtils.assertInvariants(Hashing.adler32());
    assertEquals("Hashing.adler32()", Hashing.adler32().toString());
  }

  public void testMurmur3_128() {
    HashTestUtils.check2BitAvalanche(Hashing.murmur3_128(), 250, 0.20);
    HashTestUtils.checkAvalanche(Hashing.murmur3_128(), 250, 0.17);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.murmur3_128());
    HashTestUtils.checkNoFunnels(Hashing.murmur3_128());
    HashTestUtils.assertInvariants(Hashing.murmur3_128());
    assertEquals("Hashing.murmur3_128(0)", Hashing.murmur3_128().toString());
  }

  public void testMurmur3_32() {
    HashTestUtils.check2BitAvalanche(Hashing.murmur3_32(), 250, 0.20);
    HashTestUtils.checkAvalanche(Hashing.murmur3_32(), 250, 0.17);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.murmur3_32());
    HashTestUtils.checkNoFunnels(Hashing.murmur3_32());
    HashTestUtils.assertInvariants(Hashing.murmur3_32());
    assertEquals("Hashing.murmur3_32(0)", Hashing.murmur3_32().toString());
  }

  public void testGoodFastHash() {
    for (int i = 1; i < 200; i += 17) {
      HashFunction hasher = Hashing.goodFastHash(i);
      assertTrue(hasher.bits() >= i);
      HashTestUtils.assertInvariants(hasher);
    }
  }

  // goodFastHash(32) uses Murmur3_32. Use the same epsilon bounds.
  public void testGoodFastHash32() {
    HashTestUtils.check2BitAvalanche(Hashing.goodFastHash(32), 250, 0.20);
    HashTestUtils.checkAvalanche(Hashing.goodFastHash(32), 250, 0.17);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.goodFastHash(32));
    HashTestUtils.checkNoFunnels(Hashing.goodFastHash(32));
    HashTestUtils.assertInvariants(Hashing.goodFastHash(32));
  }

  // goodFastHash(128) uses Murmur3_128. Use the same epsilon bounds.
  public void testGoodFastHash128() {
    HashTestUtils.check2BitAvalanche(Hashing.goodFastHash(128), 250, 0.20);
    HashTestUtils.checkAvalanche(Hashing.goodFastHash(128), 250, 0.17);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.goodFastHash(128));
    HashTestUtils.checkNoFunnels(Hashing.goodFastHash(128));
    HashTestUtils.assertInvariants(Hashing.goodFastHash(128));
  }

  // goodFastHash(256) uses Murmur3_128. Use the same epsilon bounds.
  public void testGoodFastHash256() {
    HashTestUtils.check2BitAvalanche(Hashing.goodFastHash(256), 250, 0.20);
    HashTestUtils.checkAvalanche(Hashing.goodFastHash(256), 250, 0.17);
    HashTestUtils.checkNo2BitCharacteristics(Hashing.goodFastHash(256));
    HashTestUtils.checkNoFunnels(Hashing.goodFastHash(256));
    HashTestUtils.assertInvariants(Hashing.goodFastHash(256));
  }

  public void testPadToLong() {
    assertEquals(0x1111111111111111L, Hashing.padToLong(HashCodes.fromLong(0x1111111111111111L)));
    assertEquals(0x9999999999999999L, Hashing.padToLong(HashCodes.fromLong(0x9999999999999999L)));
    assertEquals(0x0000000011111111L, Hashing.padToLong(HashCodes.fromInt(0x11111111)));
    assertEquals(0x0000000099999999L, Hashing.padToLong(HashCodes.fromInt(0x99999999)));
  }

  public void testConsistentHash_correctness() {
    long[] interestingValues = { -1, 0, 1, 2, Long.MAX_VALUE, Long.MIN_VALUE };
    for (long h : interestingValues) {
      checkConsistentHashCorrectness(h);
    }
    Random r = new Random(7);
    for (int i = 0; i < 20; i++) {
      checkConsistentHashCorrectness(r.nextLong());
    }
  }

  private void checkConsistentHashCorrectness(long hashCode) {
    int last = 0;
    for (int shards = 1; shards <= 100000; shards++) {
      int b = Hashing.consistentHash(hashCode, shards);
      if (b != last) {
        assertEquals(shards - 1, b);
        last = b;
      }
    }
  }

  public void testConsistentHash_probabilities() {
    AtomicLongMap<Integer> map = AtomicLongMap.create();
    Random r = new Random(9);
    for (int i = 0; i < ITERS; i++) {
      countRemaps(r.nextLong(), map);
    }
    for (int shard = 2; shard <= MAX_SHARDS; shard++) {
      // Rough: don't exceed 1.2x the expected number of remaps by more than 20
      assertTrue(map.get(shard) <= 1.2 * ITERS / shard + 20);
    }
  }

  private void countRemaps(long h, AtomicLongMap<Integer> map) {
    int last = 0;
    for (int shards = 2; shards <= MAX_SHARDS; shards++) {
      int chosen = Hashing.consistentHash(h, shards);
      if (chosen != last) {
        map.incrementAndGet(shards);
        last = chosen;
      }
    }
  }

  private static final int ITERS = 10000;
  private static final int MAX_SHARDS = 500;

  public void testConsistentHash_outOfRange() {
    try {
      Hashing.consistentHash(5L, 0);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testConsistentHash_ofHashCode() {
    checkSameResult(HashCodes.fromLong(1), 1);
    checkSameResult(HashCodes.fromLong(0x9999999999999999L), 0x9999999999999999L);
    checkSameResult(HashCodes.fromInt(0x99999999), 0x0000000099999999L);
  }

  public void checkSameResult(HashCode hashCode, long equivLong) {
    assertEquals(Hashing.consistentHash(equivLong, 5555), Hashing.consistentHash(hashCode, 5555));
  }

  /**
   * Tests that the linear congruential generator is actually compatible with the c++
   * implementation.
   *
   * This test was added to help refactoring, it is not a strict requirement, it can be removed if
   * functionality changes in the future.
   */
  public void testConsistentHash_linearCongruentialGeneratorCompatibility() {
    assertEquals(55, Hashing.consistentHash(1, 100));
    assertEquals(62, Hashing.consistentHash(2, 100));
    assertEquals(8, Hashing.consistentHash(3, 100));
    assertEquals(45, Hashing.consistentHash(4, 100));
    assertEquals(59, Hashing.consistentHash(5, 100));
  }

  private static final double MAX_PERCENT_SPREAD = 0.5;
  private static final long RANDOM_SEED = 177L;

  public void testCombineOrdered_empty() {
    try {
      Hashing.combineOrdered(Collections.<HashCode>emptySet());
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testCombineOrdered_differentBitLengths() {
    try {
      Hashing.combineOrdered(ImmutableList.of(HashCodes.fromInt(32), HashCodes.fromLong(32L)));
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testCombineOrdered() {
    HashCode hash31 = HashCodes.fromInt(31);
    HashCode hash32 = HashCodes.fromInt(32);
    assertEquals(hash32, Hashing.combineOrdered(ImmutableList.of(hash32)));
    assertEquals(HashCodes.fromBytes(new byte[] { (byte) 0x80, 0, 0, 0 }),
        Hashing.combineOrdered(ImmutableList.of(hash32, hash32)));
    assertEquals(HashCodes.fromBytes(new byte[] { (byte) 0xa0, 0, 0, 0 }),
        Hashing.combineOrdered(ImmutableList.of(hash32, hash32, hash32)));
    assertFalse(
        Hashing.combineOrdered(ImmutableList.of(hash31, hash32)).equals(
        Hashing.combineOrdered(ImmutableList.of(hash32, hash31))));
  }

  public void testCombineOrdered_randomHashCodes() {
    Random random = new Random(7);
    List<HashCode> hashCodes = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      hashCodes.add(HashCodes.fromLong(random.nextLong()));
    }
    HashCode hashCode1 = Hashing.combineOrdered(hashCodes);
    Collections.shuffle(hashCodes, random);
    HashCode hashCode2 = Hashing.combineOrdered(hashCodes);

    assertFalse(hashCode1.equals(hashCode2));
  }

  public void testCombineUnordered_empty() {
    try {
      Hashing.combineUnordered(Collections.<HashCode>emptySet());
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testCombineUnordered_differentBitLengths() {
    try {
      Hashing.combineUnordered(ImmutableList.of(HashCodes.fromInt(32), HashCodes.fromLong(32L)));
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testCombineUnordered() {
    HashCode hash31 = HashCodes.fromInt(31);
    HashCode hash32 = HashCodes.fromInt(32);
    assertEquals(hash32, Hashing.combineUnordered(ImmutableList.of(hash32)));
    assertEquals(HashCodes.fromInt(64), Hashing.combineUnordered(ImmutableList.of(hash32, hash32)));
    assertEquals(HashCodes.fromInt(96),
        Hashing.combineUnordered(ImmutableList.of(hash32, hash32, hash32)));
    assertEquals(
        Hashing.combineUnordered(ImmutableList.of(hash31, hash32)),
        Hashing.combineUnordered(ImmutableList.of(hash32, hash31)));
  }

  public void testCombineUnordered_randomHashCodes() {
    Random random = new Random();
    List<HashCode> hashCodes = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      hashCodes.add(HashCodes.fromLong(random.nextLong()));
    }
    HashCode hashCode1 = Hashing.combineUnordered(hashCodes);
    Collections.shuffle(hashCodes);
    HashCode hashCode2 = Hashing.combineUnordered(hashCodes);

    assertEquals(hashCode1, hashCode2);
  }

  public void testConcatenatedHashFunction_bits() {
    assertEquals(Hashing.md5().bits(),
        new ConcatenatedHashFunction(Hashing.md5()).bits());
    assertEquals(Hashing.md5().bits() + Hashing.murmur3_32().bits(),
        new ConcatenatedHashFunction(Hashing.md5(), Hashing.murmur3_32()).bits());
    assertEquals(Hashing.md5().bits() + Hashing.murmur3_32().bits() + Hashing.murmur3_128().bits(),
        new ConcatenatedHashFunction(
            Hashing.md5(), Hashing.murmur3_32(), Hashing.murmur3_128()).bits());
  }

  public void testConcatenatedHashFunction_makeHash() {
    byte[] md5Hash = Hashing.md5().hashLong(42L).asBytes();
    byte[] murmur3Hash = Hashing.murmur3_32().hashLong(42L).asBytes();
    byte[] combined = new byte[md5Hash.length + murmur3Hash.length];
    ByteBuffer buffer = ByteBuffer.wrap(combined);
    buffer.put(md5Hash);
    buffer.put(murmur3Hash);

    assertEquals(HashCodes.fromBytes(combined),
        new ConcatenatedHashFunction(Hashing.md5(), Hashing.murmur3_32()).hashLong(42L));
  }

  private static final ImmutableTable<HashFunction, String, String> KNOWN_HASHES =
      ImmutableTable.<HashFunction, String, String>builder()
          .put(Hashing.adler32(),
               "",
               "01000000")
          .put(Hashing.adler32(),
               "The quick brown fox jumps over the lazy dog",
               "da0fdc5b")
          .put(Hashing.adler32(),
               "The quick brown fox jumps over the lazy dog.",
               "0810e46b")
          .put(Hashing.md5(),
               "",
               "d41d8cd98f00b204e9800998ecf8427e")
          .put(Hashing.md5(),
               "The quick brown fox jumps over the lazy dog",
               "9e107d9d372bb6826bd81d3542a419d6")
          .put(Hashing.md5(),
               "The quick brown fox jumps over the lazy dog.",
               "e4d909c290d0fb1ca068ffaddf22cbd0")
          .put(Hashing.murmur3_128(),
               "",
               "00000000000000000000000000000000")
          .put(Hashing.murmur3_128(),
               "The quick brown fox jumps over the lazy dog",
               "6c1b07bc7bbc4be347939ac4a93c437a")
          .put(Hashing.murmur3_128(),
               "The quick brown fox jumps over the lazy dog.",
               "c902e99e1f4899cde7b68789a3a15d69")
          .put(Hashing.murmur3_32(),
               "",
               "00000000")
          .put(Hashing.murmur3_32(),
               "The quick brown fox jumps over the lazy dog",
               "23f74f2e")
          .put(Hashing.murmur3_32(),
               "The quick brown fox jumps over the lazy dog.",
               "fc8bc4d5")
          .put(Hashing.sha1(),
               "",
               "da39a3ee5e6b4b0d3255bfef95601890afd80709")
          .put(Hashing.sha1(),
               "The quick brown fox jumps over the lazy dog",
               "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12")
          .put(Hashing.sha1(),
               "The quick brown fox jumps over the lazy dog.",
               "408d94384216f890ff7a0c3528e8bed1e0b01621")
          .put(Hashing.sha256(),
               "",
               "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
          .put(Hashing.sha256(),
               "The quick brown fox jumps over the lazy dog",
               "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592")
          .put(Hashing.sha256(),
               "The quick brown fox jumps over the lazy dog.",
               "ef537f25c895bfa782526529a9b63d97aa631564d5d789c2b765448c8635fb6c")
          .put(Hashing.sha512(),
               "",
               "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce" +
               "47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e")
          .put(Hashing.sha512(),
               "The quick brown fox jumps over the lazy dog",
               "07e547d9586f6a73f73fbac0435ed76951218fb7d0c8d788a309d785436bbb64" +
               "2e93a252a954f23912547d1e8a3b5ed6e1bfd7097821233fa0538f3db854fee6")
          .put(Hashing.sha512(),
               "The quick brown fox jumps over the lazy dog.",
               "91ea1245f20d46ae9a037a989f54f1f790f0a47607eeb8a14d12890cea77a1bb" +
               "c6c7ed9cf205e67b7f2b8fd4c7dfd3a7a8617e45f3c463d481c7e586c39ac1ed")
          .build();

  public void testKnownUtf8Hashing() {
    for (Cell<HashFunction, String, String> cell : KNOWN_HASHES.cellSet()) {
      HashFunction func = cell.getRowKey();
      String input = cell.getColumnKey();
      String expected = cell.getValue();
      assertEquals(
          String.format("Known hash for hash(%s, UTF_8) failed", input),
          expected,
          func.hashString(input, Charsets.UTF_8).toString());
    }
  }

  public void testNullPointers() {
    NullPointerTester tester = new NullPointerTester()
        .setDefault(HashCode.class, HashCodes.fromInt(0));
    tester.testAllPublicStaticMethods(Hashing.class);
  }
}
