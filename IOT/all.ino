#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include "DHT.h"

// WiFi credentials
const char* ssid = "UPH@Event";
const char* password = "transformationalleadership";

// Sensor and endpoint configurations
#define TdsSensorPin 34
#define VREF 3.3
#define SCOUNT 30

#define DHTPIN 5
#define DHTTYPE DHT11


// Token for authentication
const String authToken = "12345678";

// Api Url
const char* URL = "http://34.66.155.44:8080/iot/insertdata";
const char* relayStatusUrl = "http://34.66.155.44:8080/iot/relaystatus";
const char* configUrl = "http://34.66.155.44:8080/iot/getconfig";
const char* updateRelayUrl = "http://34.66.155.44:8080/iot/updaterelay";

// Pins for relays
const int relayPins[] = { 14, 27, 26, 25, 33, 32 };

// TDS variables
int analogBuffer[SCOUNT];
int analogBufferTemp[SCOUNT];
int analogBufferIndex = 0;
float averageVoltage = 0;
float tdsValue = 0;
float temperature = 25;

// pH variables
const int ph_Pin = 35;
int phBuffer[SCOUNT];
int phBufferTemp[SCOUNT];
int phBufferIndex = 0;
float Po = 0;
float PH_step;
int nilai_analog_ph;
double TeganganPh;
float PH4 = 3.1;
float PH7 = 2.4;

// DHT sensor
DHT dht(DHTPIN, DHTTYPE);
float t = 0;
float h = 0;

// Relay control variables
unsigned long lastOnTime[6] = { 0, 0, 0, 0, 0, 0 };
int timeouts[6] = { 0, 0, 0, 0, 0, 0 };
bool relayStatus[6] = { false, false, false, false, false, false };
int isManual[6] = { 0, 0, 0, 0, 0, 0 };  // Array to store is_manual for each relay
unsigned long lastSensorData =  millis();
unsigned long lastControlRelay = millis();

void setup() {
  Serial.begin(115200);
  connectWiFi();
  pinMode(ph_Pin, INPUT);
  pinMode(TdsSensorPin, INPUT);
  dht.begin();

  for (int i = 0; i < 6; i++) {
    pinMode(relayPins[i], OUTPUT);
    digitalWrite(relayPins[i], LOW);
  }

  fetchConfig();
}

void loop() {

  unsigned long currentTime = millis();
  if (currentTime - lastSensorData >= 5000) {
    readTDS();
    readPH();
    readSuhu();
    sendSensorData();
  }

  if (currentTime - lastControlRelay >= 1000) {
      if (WiFi.status() != WL_CONNECTED) {
        turnOffAllRelays();
        connectWiFi();
      }
    controlRelays();
  }
    

  delay(100);
}

void connectWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("Connecting to WiFi");

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);

    Serial.print(".");
  }

  Serial.print("Connected to: ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

int getMedianNum(int bArray[], int iFilterLen) {
  int bTab[iFilterLen];
  for (byte i = 0; i < iFilterLen; i++) {
    bTab[i] = bArray[i];
  }

  for (int j = 0; j < iFilterLen - 1; j++) {
    for (int i = 0; i < iFilterLen - j - 1; i++) {
      if (bTab[i] > bTab[i + 1]) {
        int bTemp = bTab[i];
        bTab[i] = bTab[i + 1];
        bTab[i + 1] = bTemp;
      }
    }
  }

  if ((iFilterLen & 1) > 0) {
    return bTab[(iFilterLen - 1) / 2];
  } else {
    return (bTab[iFilterLen / 2] + bTab[iFilterLen / 2 - 1]) / 2;
  }
}

void readTDS() {
  static unsigned long analogSampleTimepoint = millis();
  if (millis() - analogSampleTimepoint > 40U) {
    analogSampleTimepoint = millis();
    analogBuffer[analogBufferIndex] = analogRead(TdsSensorPin);
    analogBufferIndex = (analogBufferIndex + 1) % SCOUNT;
  }

  static unsigned long printTimepoint = millis();
  if (millis() - printTimepoint > 800U) {
    printTimepoint = millis();
    memcpy(analogBufferTemp, analogBuffer, sizeof(analogBuffer));
    averageVoltage = getMedianNum(analogBufferTemp, SCOUNT) * 3.3 / 4096.0;
    float compensationCoefficient = 1.0 + 0.02 * (temperature - 25.0);
    float compensationVoltage = averageVoltage / compensationCoefficient;
    tdsValue=(133.42*compensationVoltage*compensationVoltage*compensationVoltage - 255.86*compensationVoltage*compensationVoltage + 857.39*compensationVoltage)*0.70;
  }
}

void readPH() {
  static unsigned long phSampleTimepoint = millis();
  if (millis() - phSampleTimepoint > 40U) {
    phSampleTimepoint = millis();
    phBuffer[phBufferIndex] = analogRead(ph_Pin);
    phBufferIndex = (phBufferIndex + 1) % SCOUNT;
  }

  static unsigned long phPrintTimepoint = millis();
  if (millis() - phPrintTimepoint > 800U) {
    phPrintTimepoint = millis();
    memcpy(phBufferTemp, phBuffer, sizeof(phBuffer));
    nilai_analog_ph = getMedianNum(phBufferTemp, SCOUNT);
    TeganganPh = 3.3 / 4096 * nilai_analog_ph;
    PH_step = (PH4 - PH7) / 3;
    Po = 7.00 + ((PH7 - TeganganPh) / PH_step);
  }
}

void readSuhu() {
  t = dht.readTemperature();
  h = dht.readHumidity();
  if (isnan(h) || isnan(t)) {
    Serial.println("Sensor tidak terbaca!");
  }
}

void sendSensorData() {
  if (phBufferIndex == 0 && analogBufferIndex == 0) {
    String postData = "ph=" + String(Po) + "&tds=" + String(tdsValue) + "&temperature=" + String(t) + "&humidity=" + String(h);
    HTTPClient http;
    http.begin(URL);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("API-KEY", authToken);
    int httpCode = http.POST(postData);
    Serial.println(httpCode);
    lastSensorData = millis();
    // delay(900000);  // Delay for 10 seconds before sending the next data
  }
}

void fetchConfig() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(configUrl);
    http.addHeader("API-KEY", authToken);
    int httpCode = http.GET();

    if (httpCode == HTTP_CODE_OK) {
      String payload = http.getString();
      Serial.println(payload);

      const size_t capacity = JSON_OBJECT_SIZE(8) + 200;
      DynamicJsonDocument doc(capacity);
      DeserializationError error = deserializeJson(doc, payload);
      if (error) {
        Serial.print(F("deserializeJson() failed: "));
        Serial.println(error.f_str());
        return;
      }

      timeouts[0] = doc["ph_up"];
      timeouts[1] = doc["ph_down"];
      timeouts[2] = doc["nut_a"];
      timeouts[3] = doc["nut_b"];
      timeouts[4] = doc["fan"];
      timeouts[5] = doc["light"];
    } else {
      Serial.printf("GET request failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }
}

void controlRelays() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(relayStatusUrl);
    http.addHeader("API-KEY", authToken);
    int httpCode = http.GET();

    if (httpCode == HTTP_CODE_OK) {
      String payload = http.getString();
      Serial.println(payload);

      const size_t capacity = JSON_OBJECT_SIZE(14) + 400;  // Increased size for is_manual array
      DynamicJsonDocument doc(capacity);
      DeserializationError error = deserializeJson(doc, payload);
      if (error) {
        Serial.print(F("deserializeJson() failed: "));
        Serial.println(error.f_str());
        return;
      }

      // Check if synchronization is required
      if (doc["is_sync"].as<int>() == 0) {
        fetchConfig();
        return;
      }

      // Read the relay statuses and manual control flags
      for (int i = 0; i < 6; i++) {
        String relayKey = "Relay" + String(i + 1) + "_is";
        String manualKey = "Relay" + String(i + 1) + "_manual";
        const char* relayStatusValue = doc[relayKey];
        int manualStatus = doc[manualKey].as<int>();

        isManual[i] = manualStatus;
        updateRelay(relayPins[i], relayStatusValue, lastOnTime[i], timeouts[i], relayStatus[i], isManual[i]);
      }
      lastControlRelay = millis();
    } else {
      Serial.printf("GET request failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }
}


void updateRelay(int relayPin, const char* status, unsigned long& lastOnTime, int timeout, bool& relayStatus, int isManual) {

  if (strcmp(status, "on") == 0) {  // Assuming the status is in lowercase
    if (!relayStatus) {
      lastOnTime = millis();
      relayStatus = true;
      digitalWrite(relayPin, HIGH);
      Serial.printf("Relay %d turned ON\n", relayPin);
    }
  } else if (strcmp(status, "off") == 0) {  // Assuming the status is in lowercase
    if (relayStatus) {
      relayStatus = false;
      digitalWrite(relayPin, LOW);
      Serial.printf("Relay %d turned OFF\n", relayPin);
    }
  }
  if (isManual == 0) {  // Check if the relay is not in manual mode
    // Check if the relay has been on longer than the timeout
    if (relayStatus && millis() - lastOnTime >= timeout * 1000UL) {
      relayStatus = false;
      digitalWrite(relayPin, LOW);
      Serial.printf("Relay %d turned OFF due to timeout\n", relayPin);
      updateRelayStatus(relayPin, relayStatus);
    }
    // }else if(isManual == 1){

    // }
  }
}

void updateRelayStatus(int relayPin, bool status) {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(updateRelayUrl);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addHeader("API-KEY", authToken);

    // Adjust the relay ID if necessary (e.g., relayPin to relayID mapping)
    int relayID = getRelayIDFromPin(relayPin);
    String postData = "relay_id=" + String(relayID) + "&status=" + (status ? "on" : "off");

    int httpCode = http.POST(postData);
    Serial.println("--------------------------------------------------");
    Serial.print("Update Relay URL: ");
    Serial.println(updateRelayUrl);
    Serial.print("Update Relay Data: ");
    Serial.println(postData);
    Serial.print("HTTP Code: ");
    Serial.println(httpCode);
    String payload = http.getString();
    Serial.print("Payload: ");
    Serial.println(payload);
    Serial.println("--------------------------------------------------");

    http.end();
  }
}

int getRelayIDFromPin(int relayPin) {
  // Map the relay pin to relay ID as expected by the server
  switch (relayPin) {
    case 14: return 1;
    case 27: return 2;
    case 26: return 3;
    case 25: return 4;
    case 33: return 5;
    case 32: return 6;
    default: return -1;  // Invalid pin
  }
}

void turnOffAllRelays() {
  for (int i = 0; i < 6; i++) {
    digitalWrite(relayPins[i], LOW);
    relayStatus[i] = false;
    Serial.printf("Relay %d turned OFF due to WiFi disconnection\n", relayPins[i]);
  }
}
