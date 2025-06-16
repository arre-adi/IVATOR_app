# IVator: A Smart IV Drip and Urine Flow Monitoring System

<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/3/38/ESP32_chip_pinout.png" alt="ESP32" height="40"/>
  <img src="https://img.icons8.com/ios/40/000000/firebase.png" alt="Firebase"/>
  <img src="https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin_Icon.png" alt="Kotlin" height="40"/>
</p>

---

## Overview

**IVator** is an innovative Internet of Things (IoT) device designed to automate the monitoring of intravenous (IV) fluid administration and urine output in healthcare environments.

Manual monitoring by healthcare professionals is prone to human error and can increase workload—especially in high-pressure settings. IVator offers a real-time, cost-effective, and integrated solution that enhances patient safety and optimizes operational efficiency.

---

## About the Device

IVator is a dual-function system that monitors IV fluid delivery and urine output using compact, sensor-based hardware. It:

- Tracks IV fluid levels and urine volume in real time.
- Sends alerts when anomalies are detected in flow or volume.
- Logs data and enables remote access via a mobile application.

Data is transmitted wirelessly to cloud platforms for easy access by caregivers.

---

## Key Features

- **Continuous IV Fluid Monitoring**  
  Load cells measure IV bag weight, while IR sensors detect drip rates.

- **Automated Urine Output Monitoring**  
  Load cells provide precise measurements of urine volume and flow.

- **Real-time Alerts**  
  Audio-visual alerts via buzzer and LEDs, along with push notifications to the mobile app.

- **IoT Connectivity**  
  ESP32 Wi-Fi module transmits data to cloud platforms like Blynk and Firebase.

- **Enhanced Patient Safety**  
  Reduces risks such as air embolism and delayed diagnosis due to manual errors.

- **High Accuracy**  
  Consistent automated data eliminates human error in charting.

- **Affordable & Scalable**  
  Designed with low-cost components for use in both high-end and resource-limited healthcare settings.

---

## Technology Used

| Component              | Description                                                                 |
|------------------------|------------------------------------------------------------------------------|
| **Microcontroller** | ESP32 – controls all sensors and handles Wi-Fi transmission         |
| **Sensors**            | - Load Cells + HX711 Amplifier (IV and urine measurement)  <br> - IR Sensor (IV drip detection) |
| **Display**            | LCD screen for on-device status feedback                                     |
| **Alerts**             | Buzzer and LEDs for visual and sound alerts                                  |
| **Cloud Platform** | Firebase for data storage and push notifications                     |                      |
|  **Mobile App** | Android app developed using Kotlin                                  |
| **IDE & Language**     | Embedded C via Arduino IDE for ESP32 programming                             |

---

### Device Images

<p align="center">
  <img src="https://github.com/user-attachments/assets/158b7218-e830-40e9-8fb8-856b2a3a3a2d" style="width:30%; height:600px; object-fit: cover; margin: 0 1%;" alt="Device Image 1">
  <img src="https://github.com/user-attachments/assets/65379757-2afe-40dc-b890-d990e95beaac" style="width:30%; height:600px; object-fit: cover; margin: 0 1%;" alt="Device Image 2">
  <img src="https://github.com/user-attachments/assets/01b0bf35-7ca1-46be-9449-3a271337b299" style="width:30%; height:600px; object-fit: cover; margin: 0 1%;" alt="Device Image 3">
</p>


### Mobile Application Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/f77a3276-1dda-40b4-9d42-f83b461e7553" style="width:30%; margin: 0 1%;" alt="IVator Device Image 1">
  <img src="https://github.com/user-attachments/assets/7901e1be-01e7-4451-97b5-58e7e5be38d1" style="width:30%; margin: 0 1%;" alt="IVator Device Image 2">
  <img src="https://github.com/user-attachments/assets/757796b2-564e-4cc3-b945-2328210d7075" style="width:30%; margin: 0 1%;" alt="IVator Device Image 3">
</p>



---

## Future Scope

- Integration with hospital management systems (HMS)
- Predictive analytics for flow anomalies using machine learning
- Portable battery-powered version for field operations
- Auto-shutoff valve control based on alerts

---

## Contact

**Team:**  

- **Aditya Chaurasia**  
  [LinkedIn](https://www.linkedin.com/in/ad84a/)

- **Aryan Sharma**  
  [LinkedIn](https://www.linkedin.com/in/aryan-sharma-profile/)]

- **Aryan Mehra**  
  [LinkedIn](https://www.linkedin.com/in/aryan-mehra-profile/)]
- **Amber Gaur**  
  [LinkedIn](https://www.linkedin.com/in/amber-gaur-profile/) 
