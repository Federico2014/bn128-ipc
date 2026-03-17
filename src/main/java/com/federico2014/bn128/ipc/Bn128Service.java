package com.federico2014.bn128.ipc;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Common interface for BN128 elliptic curve operations via IPC.
 */
public interface Bn128Service {

  /**
   * Performs BN128 point addition.
   *
   * @param input 128 bytes (two compressed G1 points)
   * @return Pair of (success, result bytes)
   */
  Pair<Boolean, byte[]> add(byte[] input) throws Exception;

  /**
   * Performs BN128 scalar multiplication.
   *
   * @param input 96 bytes (one compressed G1 point + scalar)
   * @return Pair of (success, result bytes)
   */
  Pair<Boolean, byte[]> mul(byte[] input) throws Exception;

  /**
   * Performs BN128 pairing check.
   *
   * @param input 192 bytes (pairing inputs)
   * @return Pair of (success, result bytes) - result is {1} if true, {0} if false
   */
  Pair<Boolean, byte[]> pairing(byte[] input) throws Exception;

  /**
   * Closes the service and releases resources.
   */
  void close() throws Exception;
}
