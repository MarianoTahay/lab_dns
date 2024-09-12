import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) throws IOException {
    DatagramSocket serverSocket = new DatagramSocket(53);
    ExecutorService threads = Executors.newFixedThreadPool(10);

    System.out.println("Server is UP!");

    while (true) {
      byte[] buffer = new byte[256];
      DatagramPacket receivedDatagramPacket = new DatagramPacket(buffer, buffer.length);
      serverSocket.receive(receivedDatagramPacket);

      // Manejamos cada petición en un hilo separado
      threads.submit(() -> dnsRequest(serverSocket, receivedDatagramPacket));
    }
  }

  private static void dnsRequest(DatagramSocket serverSocket, DatagramPacket clientDatagramPacket) {
    try {
      // Enviar la petición al DNS de Google
      byte[] requestData = clientDatagramPacket.getData();
      DatagramSocket forwardSocket = new DatagramSocket();
      InetAddress forwardDnsAddress = InetAddress.getByName("8.8.8.8");

      DatagramPacket forwardPacket = new DatagramPacket(requestData, requestData.length, forwardDnsAddress,
          53);
      forwardSocket.send(forwardPacket);

      // Recibir la respuesta del servidor DNS de Google
      byte[] responseBuffer = new byte[512];
      DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
      forwardSocket.receive(responsePacket);

      // Enviar la respuesta de vuelta al cliente
      DatagramPacket clientResponsePacket = new DatagramPacket(responsePacket.getData(),
          responsePacket.getLength(),
          clientDatagramPacket.getAddress(), clientDatagramPacket.getPort());
      serverSocket.send(clientResponsePacket);

      forwardSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}