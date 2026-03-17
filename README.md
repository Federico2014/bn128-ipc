# BN128 IPC Worker

Java NIO-based BN128 elliptic curve cryptographic worker server using TCP socket communication.

## Architecture

Socket-based IPC implementation for BN128 operations.

### Components

- **Bn128Service**: Common interface (`com.federico2014.bn128.ipc`)
- **SocketBn128Client/Server**: TCP socket-based IPC (`com.federico2014.bn128.socket`)

## Protocol

Request format: `[opcode: 1 byte][length: 4 bytes][payload: N bytes]`

Response format: `[status: 1 byte][length: 4 bytes][result: N bytes]`

Opcodes:
- `1` - BN128Add (128 bytes input, 64 bytes output)
- `2` - BN128Mul (96 bytes input, 64 bytes output)
- `3` - BN128Pairing (192 bytes input, 1 byte boolean output)

## Build and Run

```bash
./gradlew build              # Build project
./gradlew test               # Run tests
```

## Start Server

```bash
./gradlew runSocketServer    # Start socket server on port 9001
./gradlew run                # Same as above (default)
```

Server listens on `127.0.0.1:9001`

## Run Client Test

After starting the server, run in another terminal:

```bash
./gradlew runTest
```

## Usage Examples

```java
Bn128Service client = new SocketBn128Client(8); // pool size
byte[] result = client.add(input);
client.close();
```

## Configuration

| Parameter | Value |
|-----------|-------|
| Port | 9001 |
| Thread pool | CPU cores |
| Queue size | 1000 |
| Buffer size | 4KB |
