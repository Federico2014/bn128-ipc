package com.federico2014.bn128;

import com.federico2014.bn128.socket.SocketBn128Client;
import com.federico2014.bn128.ipc.Bn128Service;
import java.security.SecureRandom;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Test client for Socket-based BN128 IPC.
 * <p>
 * Before running, start the server: ./gradlew runSocketServer
 */
public class BN128Test {

  public static void main(String[] args) throws Exception {
    System.out.println("=== Testing Socket-based BN128 IPC ===");

    Bn128Service client = new SocketBn128Client(8);

    try {
      byte[] addInput = new byte[128];
      byte[] mulInput = new byte[96];
      byte[] pairingInput = new byte[192];

      new SecureRandom().nextBytes(addInput);
      new SecureRandom().nextBytes(mulInput);
      new SecureRandom().nextBytes(pairingInput);

      long start = System.currentTimeMillis();
      Pair<Boolean, byte[]> addResult = client.add(addInput);
      long addTime = System.currentTimeMillis() - start;
      System.out.println("BN128Add success=" + addResult.getLeft()
          + ", result length = " + addResult.getRight().length + " (" + addTime + "ms)");
      System.out.println("boolean: " + addResult.getLeft());
      System.out.println("result: " + bytesToHex(addResult.getRight()));

      start = System.currentTimeMillis();
      Pair<Boolean, byte[]> mulResult = client.mul(mulInput);
      long mulTime = System.currentTimeMillis() - start;
      System.out.println("BN128Mul success=" + mulResult.getLeft()
          + ", result length = " + mulResult.getRight().length + " (" + mulTime + "ms)");
      System.out.println("boolean: " + mulResult.getLeft());
      System.out.println("result: " + bytesToHex(mulResult.getRight()));

      start = System.currentTimeMillis();
      Pair<Boolean, byte[]> pairingResult = client.pairing(pairingInput);
      long pairingTime = System.currentTimeMillis() - start;
      System.out.println("BN128Pairing success=" + pairingResult.getLeft()
          + ", result=" + (pairingResult.getRight().length > 0
          && pairingResult.getRight()[0] == 1)
          + " (" + pairingTime + "ms)");
      System.out.println("boolean: " + pairingResult.getLeft());
      System.out.println("result: " + bytesToHex(pairingResult.getRight()));

      System.out.println("Socket test completed successfully.");
    } finally {

    }

    try {
      byte[] addInput = hexToBytes(
          "18b18acfb4c2c30276db5411368e7185b311dd124691610c5d3b74034e093dc9063c909c4720840cb5134"
              + "cb9f59fa749755796819658d32efc0d288198f3726607c2b7f58a84bd6145f00c9c2bc0bb1a187f"
              + "20ff2c92963a88019e7c6a014eed06614e20c147e940f2d70da3f74c9a17df361706a4485c742bd"
              + "6788478fa17d7");
      byte[] mulInput = hexToBytes(
          "2bd3e6d0f3b142924f5ca7b49ce5b9d54c4703d7ae5648e61d02268b1a0a9fb721611ce0a6af85915e2f1d"
              + "70300909ce2e49dfad4a4619c8390cae66cefdb20400000000000000000000000000000000000000"
              + "000000000011138ce750fa15c2");
      byte[] pairingInput = hexToBytes(
          "1c76476f4def4bb94541d57ebba1193381ffa7aa76ada664dd31c16024c43f593034dd2920f673e204fee2"
              + "811c678745fc819b55d3e9d294e45c9b03a76aef41209dd15ebff5d46c4bd888e51a93cf99a73296"
              + "36c63514396b4a452003a35bf704bf11ca01483bfa8b34b43561848d28905960114c8ac04049af4b"
              + "6315a416782bb8324af6cfc93537a2ad1a445cfd0ca2a71acd7ac41fadbf933c2a51be344d120a2a"
              + "4cf30c1bf9845f20c6fe39e07ea2cce61f0c9bb048165fe5e4de877550111e129f1cf1097710d41c"
              + "4ac70fcdfa5ba2023c6ff1cbeac322de49d1b6df7c2032c61a830e3c17286de9462bf242fca28835"
              + "85b93870a73853face6a6bf411198e9393920d483a7260bfb731fb5d25f1aa493335a9e71297e485"
              + "b7aef312c21800deef121f1e76426a00665e5c4479674322d4f75edadd46debd5cd992f6ed090689"
              + "d0585ff075ec9e99ad690c3395bc4b313370b38ef355acdadcd122975b12c85ea5db8c6deb4aab71"
              + "808dcb408fe3d1e7690c43d37b4ce6cc0166fa7daa");

      long start = System.currentTimeMillis();
      Pair<Boolean, byte[]> addResult = client.add(addInput);
      long addTime = System.currentTimeMillis() - start;
      System.out.println("BN128Add success=" + addResult.getLeft()
          + ", result length = " + addResult.getRight().length + " (" + addTime + "ms)");
      System.out.println("boolean: " + addResult.getLeft());
      System.out.println("result: " + bytesToHex(addResult.getRight()));

      if (!addResult.getLeft()) {
        throw new AssertionError("Add (known input) should return true");
      }
      if (!bytesToHex(addResult.getRight()).equals(
          "2243525c5efd4b9c3d3c45ac0ca3fe4dd85e830a4ce6b65fa1eeaee202839703301d1d33be6da8e509d"
              + "f21cc35964723180eed7532537db9ae5e7d48f195c915")) {
        throw new AssertionError("Add (known input) should return correct result");
      }

      start = System.currentTimeMillis();
      Pair<Boolean, byte[]> mulResult = client.mul(mulInput);
      long mulTime = System.currentTimeMillis() - start;
      System.out.println("BN128Mul success=" + mulResult.getLeft()
          + ", result length = " + mulResult.getRight().length + " (" + mulTime + "ms)");
      System.out.println("boolean: " + mulResult.getLeft());
      System.out.println("result: " + bytesToHex(mulResult.getRight()));

      if (!mulResult.getLeft()) {
        throw new AssertionError("Add (known input) should return true");
      }
      if (!bytesToHex(mulResult.getRight()).equals(
          "070a8d6a982153cae4be29d434e8faef8a47b274a053f5a4ee2a6c9c13c31e5c031b8ce914eba3a9ff"
              + "b989f9cdd5b0f01943074bf4f0f315690ec3cec6981afc")) {
        throw new AssertionError("Add (known input) should return correct result");
      }

      start = System.currentTimeMillis();
      Pair<Boolean, byte[]> pairingResult = client.pairing(pairingInput);
      long pairingTime = System.currentTimeMillis() - start;
      System.out.println("BN128Pairing success=" + pairingResult.getLeft()
          + ", result=" + (pairingResult.getRight().length > 0
          && pairingResult.getRight()[0] == 1)
          + " (" + pairingTime + "ms)");
      System.out.println("boolean: " + pairingResult.getLeft());
      System.out.println("result: " + bytesToHex(pairingResult.getRight()));

      if (!pairingResult.getLeft()) {
        throw new AssertionError("Add (known input) should return true");
      }
      if (!bytesToHex(pairingResult.getRight()).equals(
          "0000000000000000000000000000000000000000000000000000000000000001")) {
        throw new AssertionError("Add (known input) should return correct result");
      }

      System.out.println("Socket test completed successfully.");
    } finally {
      client.close();
    }
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private static byte[] hexToBytes(String hex) {
    if (hex == null || hex.isEmpty()) {
      return new byte[0];
    }
    if (hex.length() % 2 != 0) {
      hex = "0" + hex;
    }
    byte[] result = new byte[hex.length() / 2];
    for (int i = 0; i < hex.length(); i += 2) {
      result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
    }
    return result;
  }
}
