package com.federico2014.bn128.socket;

import com.federico2014.bn128.ipc.Bn128Service;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Socket-based implementation of Bn128Service. Connects to a remote BN128 worker server via TCP
 * socket.
 */
public class SocketBn128Client implements Bn128Service {

  private ArrayBlockingQueue<SocketChannel> pool;
  private final int port;

  public SocketBn128Client(String host, int port, int poolSize) throws Exception {
    this.port = port;
    pool = new ArrayBlockingQueue<>(poolSize);
    for (int i = 0; i < poolSize; i++) {
      SocketChannel ch = SocketChannel.open();
      ch.connect(new InetSocketAddress(host, port));
      pool.add(ch);
    }
  }

  public SocketBn128Client(int port, int poolSize) throws Exception {
    this("127.0.0.1", port, poolSize);
  }

  public SocketBn128Client(int poolSize) throws Exception {
    this("127.0.0.1", 9001, poolSize);
  }

  private Pair<Boolean, byte[]> call(byte opcode, byte[] input) throws Exception {
    SocketChannel ch = pool.take();
    try {
      ByteBuffer req = ByteBuffer.allocate(5 + input.length);
      req.put(opcode);
      req.putInt(input.length);
      req.put(input);
      req.flip();
      ch.write(req);

      ByteBuffer header = ByteBuffer.allocate(5);
      ch.read(header);
      header.flip();
      boolean success = header.get() == 0; // status byte: 0=success
      int len = header.getInt();

      ByteBuffer body = ByteBuffer.allocate(len);
      ch.read(body);
      body.flip();
      byte[] result = new byte[len];
      body.get(result);
      return Pair.of(success, result);
    } finally {
      pool.put(ch);
    }
  }

  @Override
  public Pair<Boolean, byte[]> add(byte[] input) throws Exception {
    return call((byte) 1, input);
  }

  @Override
  public Pair<Boolean, byte[]> mul(byte[] input) throws Exception {
    return call((byte) 2, input);
  }

  @Override
  public Pair<Boolean, byte[]> pairing(byte[] input) throws Exception {
    return call((byte) 3, input);
  }

  @Override
  public void close() throws Exception {
    for (SocketChannel ch : pool) {
      try {
        ch.close();
      } catch (Exception e) {
        // Ignore
      }
    }
  }
}
