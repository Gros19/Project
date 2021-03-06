#include "WiFiEsp.h"

//Emulate Serial1 on pins 6/7 if not present
#ifndef HAVE_HWSERIAL1
#include "SoftwareSerial.h"
SoftwareSerial Serial1(6, 7); //RX, TX
#endif

char ssid[] = "204 3G";            // your network SSID (name)
char pass[] = "71044129";          // your network password
int status = WL_IDLE_STATUS;       // the Wifi radio's status
int reqCount = 0;                   // number of requests received

WiFiEspServer server(80);           //port set

void setup() {
  //initialize serial for debugging
  Serial.begin(115200);
  //initialize serial for ESP module
  Serial1.begin(9600);
  //initialize ESP module
  WiFi.init(&Serial1);

  //check for the presence of the shield
  if(WiFi.status() == WL_NO_SHIELD){
    Serial.println("WiFi shield not present");
    //don't continue
    while(true);
  }

  //attempt to connect to WiFi network
  while (status != WL_CONNECTED){
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    //Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pass);
  }

  Serial.println("You're connected to the network");
  printWifiStatus();

  //start the web server on port 80
  server.begin();

}

void loop() {
 
  //listen for incoming clients
  WiFiEspClient client = server.available();
  if(client){
    Serial.println("New Client");
    //an http request ends with a blank line
    boolean currentLineIsBlank = true;
    while (client.connected()){
      if (client.available()){
        char c = client.read();
        Serial.write(c);
        //if you've gotten to the end of the line (received a new line
        //character) and the line is blank, the http request has ended,
        // so you can send a reply
        if ( c == '\n' && currentLineIsBlank){
          Serial.println("Sending response");

          //send a standard http response header
          //use \r\n instead of many println statments to speedup data send
          client.print(
            "HTTP/1.1 200 OK\r\n"
            "Content-Type: text/html\r\n"
            "Connection: close\r\n"
            "Refresh: 20\r\n"   //refresh the page automatically every 20sec
            "\r\n");
           client.print("<!DOCTYPE HTML\r\n");
           client.print("<html>\r\n");
           client.print("<h1>Hello World!</h1>\r\n");
           client.print("Requests receive: ");
           client.print(++reqCount);
           client.print("<br>\r\n");
           client.print("Analog input A0: ");
           client.print(analogRead(0));
           client.print("<br>\r\n");
           client.print("</html>\r\n");
           break;
        }
        if ( c == '\n'){
          //you're starting a new line
          currentLineIsBlank = true;
        }
        else if ( c != '\r'){
          //you've gotten a character on the current line
          currentLineIsBlank = false;
        }
      }
    }
    // give the web browser time to receive the data
    delay(10);

    //close the connection;
    client.stop();
    Serial.println("Client disconnected");
  }
}

void printWifiStatus(){
  //print the SSID of the network you're attached to
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  //print your WiFi shield's IP address
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  //print where to go in the browser
  Serial.println();
  Serial.print("To see this page in action, open a browser to http://");
  Serial.println(ip);
  Serial.println();
 }
